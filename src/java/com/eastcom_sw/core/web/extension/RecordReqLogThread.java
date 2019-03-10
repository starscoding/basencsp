package com.eastcom_sw.core.web.extension;

import com.eastcom_sw.core.service.reqlog.ReqLogService;

/**
 * 记录用户记录请求日志线程
 * 
 * @author JJF
 * @date May 08 2014
 */
public class RecordReqLogThread implements Runnable {
	private ReqLogService reqLogService;

	private UserReqLog userReqLog;

	public RecordReqLogThread(ReqLogService reqLogService, UserReqLog l) {
		this.reqLogService = reqLogService;
		this.userReqLog = l;
	}

	@Override
	public void run() {
		reqLogService.recordLog(this.userReqLog);
	}
}
