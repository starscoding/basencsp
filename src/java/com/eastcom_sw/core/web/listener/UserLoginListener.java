package com.eastcom_sw.core.web.listener;

import com.eastcom_sw.common.service.BaseService;
import com.eastcom_sw.common.web.CommonDataController;
import com.eastcom_sw.core.web.AppContextLoader;
import com.eastcom_sw.core.web.LoginCheckTimerTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Timer;

public class UserLoginListener implements ServletContextListener {
	
	@Autowired
	private BaseService baseService;
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext servletContext = sce.getServletContext();
		ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);

		AppContextLoader.getInstance().setApplicationContext(ctx);
		int times = ctx.getBean(CommonDataController.class).getLogsAuditTimeLimit();
		int threshold = ctx.getBean(CommonDataController.class).getLogsAuditThreshold();
		
		try {
			LoginCheckTimerTask loginCheckTimerTask = new LoginCheckTimerTask(ctx,times,threshold);

			Timer timer = new Timer(true);
			timer.schedule(loginCheckTimerTask, 0, times * 60 * 1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
	}

}
