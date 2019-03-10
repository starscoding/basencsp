package com.eastcom_sw.core.web.filter.login;

import java.util.HashMap;
import java.util.Map;

/**
 * LoginFilter结果类
 * 
 * @author JJF
 * @date JAN 15 2014
 * 
 */
public class FilterResp {
	private boolean result;// 处理结果，如果失败，则不再进行继续登录操作
	private FilterChainUser user;// 验证处理之后的新user
	private String redirectURL;// 跳转页面，只要定义了这个参数，登录成功之后，最终跳转为该URL地址
	private String errorURL;// 登录失败跳转页面，登录验证失败之后跳转至该页面（一般为每个项目的自定义登录页面）
	private Map<String, Object> extensionMap;// 扩展返回参数,备用

	public FilterResp(boolean result) {
		this.result = result;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getRedirectURL() {
		return redirectURL;
	}

	public void setRedirectURL(String redirectURL) {
		this.redirectURL = redirectURL;
	}

	public Map<String, Object> getExtensionMap() {
		return extensionMap;
	}

	public void setExtensionMap(Map<String, Object> extensionMap) {
		this.extensionMap = extensionMap;
	}

	public String getErrorURL() {
		return errorURL;
	}

	public void setErrorURL(String errorURL) {
		this.errorURL = errorURL;
	}

	public FilterChainUser getUser() {
		return user;
	}

	public void setUser(FilterChainUser user) {
		this.user = user;
	}

	public void putModelData(String key, Object o) {
		if (this.extensionMap == null) {
			this.extensionMap = new HashMap<String, Object>();
		}
		this.extensionMap.put(key, o);
	}
}
