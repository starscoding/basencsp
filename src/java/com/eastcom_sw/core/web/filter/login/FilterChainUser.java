package com.eastcom_sw.core.web.filter.login;

/**
 * 验证链中使用的USER
 * 
 * @author JJF
 * @date Apr 16 2014
 * 
 */
public class FilterChainUser {
	private String username;
	private String password;
	private boolean isEncodePsd = false;// 密码是否已加密，默认false

	public FilterChainUser() {
	}

	public FilterChainUser(String username, String password, boolean isEncodePsd) {
		this.username = username;
		this.password = password;
		this.isEncodePsd = isEncodePsd;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEncodePsd() {
		return isEncodePsd;
	}

	public void setEncodePsd(boolean isEncodePsd) {
		this.isEncodePsd = isEncodePsd;
	}
}
