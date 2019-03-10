package com.eastcom_sw.core.web;

import com.eastcom_sw.core.service.log.SysLogsAuditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.TimerTask;

public class LoginCheckTimerTask extends TimerTask {
	public Logger log = LoggerFactory.getLogger(getClass());

	private ApplicationContext sc;
	private int timeLimit;
	private int threshold;
	
	public LoginCheckTimerTask(ApplicationContext sc, int timeLimit, int threshold){
		this.sc = sc;
		this.timeLimit = timeLimit;
		this.threshold = threshold;
	}
	
	@Override
	public void run() {
		doCheckLogs();
	}

	private void doCheckLogs() {
		log.info("-----------审核登录日志开始------------");
		sc.getBean(SysLogsAuditService.class).checkLoginLogs(timeLimit,threshold);
		log.info("-----------审核登录日志结束------------");
	}
}
