package com.eastcom_sw.core.web.listener;

import com.eastcom_sw.common.dao.jpa.DataSourceHeartbeat;
import com.eastcom_sw.common.utils.CommonUtil;
import com.eastcom_sw.common.utils.Constants;
import com.eastcom_sw.common.utils.PropertiesUtil;
import com.eastcom_sw.core.web.AppContextLoader;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Properties;
import java.util.Timer;

public class ServletContextSettingListener implements ServletContextListener {

	public void contextDestroyed(ServletContextEvent arg0) {

	}

	public void contextInitialized(ServletContextEvent arg0) {
		ServletContext servletContext = arg0.getServletContext();
		ApplicationContext ctx = WebApplicationContextUtils
				.getRequiredWebApplicationContext(servletContext);

		AppContextLoader.getInstance().setApplicationContext(ctx);

		// 加载数据源心跳配置
		try {
			Properties p = new Properties();
			p.load(ServletContextSettingListener.class.getClassLoader()
					.getResourceAsStream("db.properties"));
			boolean enabled = false;
			String _enabled = p.getProperty("heartbeat.enabled");
			if (StringUtils.isNotBlank(_enabled)) {
				enabled = Boolean.parseBoolean(_enabled);
			}
			int inteval = 0;
			String _inteval = p.getProperty("heartbeat.inteval");
			if (StringUtils.isNotBlank(_inteval)) {
				inteval = Integer.parseInt(_inteval);
			}

			String sql = p.getProperty("heartbean.testsql");

			if (enabled && inteval > 0 && StringUtils.isNotBlank(sql)) {
				DataSourceHeartbeat dataSourceHeartbeat = new DataSourceHeartbeat(
						ctx, sql);

				Timer timer = new Timer(true);
				timer.schedule(dataSourceHeartbeat, 0, inteval * 60 * 1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 加载当前密码加密方式
		Constants.SYS_PASSWORD_ENCODE_MODE = CommonUtil
				.getCurrentSecureType(servletContext);

		// 加载是否对接ecip
		Constants.SYS_LINK_ECIP = CommonUtil.needLinkECIP(servletContext);

		// 是否使用cdn
		try {
			Constants.USING_CDN = "true".equals(PropertiesUtil.getPropertie(
					"cdn/cdn.properties", "enabled"));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
