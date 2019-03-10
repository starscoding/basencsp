package com.eastcom_sw.core.web.extension;

import org.apache.commons.lang3.StringUtils;

/**
 * 用户请求日志vo
 * 
 * @author JJF
 * @date May 08 2014
 * 
 */
public class UserReqLog {
	private String sessionId;
	private String username;
	private String reqURL;
	private String clientIp;
	private String serverIp;
	private long reqTime;
	private String reqTimeStr;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getReqURL() {
		return reqURL;
	}

	public void setReqURL(String reqURL) {
		this.reqURL = reqURL;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public long getReqTime() {
		return reqTime;
	}

	public void setReqTime(long reqTime) {
		this.reqTime = reqTime;
	}

	public String getReqTimeStr() {
		return reqTimeStr;
	}

	public void setReqTimeStr(String reqTimeStr) {
		this.reqTimeStr = reqTimeStr;
	}

	public boolean checkFields() {
		if (StringUtils.isBlank(this.username)) {
			return false;
		}

		if (StringUtils.isBlank(this.sessionId)) {
			return false;
		}

		if (StringUtils.isBlank(this.reqURL)) {
			return false;
		}

		if (this.reqTime == 0) {
			return false;
		}

		return true;
	}
}
