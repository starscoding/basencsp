package com.eastcom_sw.core.web;

import com.eastcom_sw.common.utils.Constants;
import com.eastcom_sw.core.entity.security.User;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.util.Calendar;
import java.util.Map;

/**
 * 记录用户登陆日志线程
 * 
 * @author zhongrui
 * @since 2017-05-24
 * 
 */
public class LogLoginFailUserInfoThread implements Runnable {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private LoginController loginController;
	private User user;
	private HttpSession session;
	private Map<String, Object> extensionArgs;

	@SuppressWarnings("unused")
	private LogLoginFailUserInfoThread() {
	}

	public LogLoginFailUserInfoThread(LoginController loginController,
                                      HttpSession session, User user,
                                      Map<String, Object> extensionArgs) {
		this.loginController = loginController;
		this.user = user;
		this.session = session;
		this.extensionArgs = extensionArgs;
	}

	@Override
	public void run() {
		log.info("Start save onlineUserInfo...");
		try {
			sysLog();
		} catch (Exception e) {
			log.error("Save login log failed!");
			e.printStackTrace();
		}
		log.info("Save onlineUserInfo completed");
	}

	private void sysLog() {
		long lStart = Calendar.getInstance().getTimeInMillis();
		LoginBundle.setLc(loginController.getI18NLocale());
		String log_ = "用户:"+user.getUserName()+"登录失败!" + "SESSIONID:" + session.getId();
		String extSystemId = extensionArgs.get("extSystemId").toString();
		if (StringUtils.isBlank(extSystemId)) {
			extSystemId = "本系统";
		}
		log_ += (",登录入口:" + extSystemId);
		loginController.logLoginLog(extensionArgs.get("clientIP").toString(),
				user.getUserName(), false, Constants.SYSLOGSOPERATETYPE_OTHER,
				"login", log_,
				(int) (Calendar.getInstance().getTimeInMillis() - lStart));
	}

}
