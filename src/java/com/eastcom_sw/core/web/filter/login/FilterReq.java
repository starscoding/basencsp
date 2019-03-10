package com.eastcom_sw.core.web.filter.login;

import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * LoginFilter参数类
 * 
 * @author JJF
 * @date JAN 15 2014
 * 
 */
public class FilterReq {
	private HttpServletRequest request;
	private HttpSession session;
	private HttpServletResponse response;
	private Model model;
	private boolean extOpenFlag;

	public FilterReq(HttpServletRequest request, HttpSession session,
                     HttpServletResponse response, Model model, boolean extOpenFlag) {
		this.request = request;
		this.session = session;
		this.response = response;
		this.model = model;
		this.extOpenFlag = extOpenFlag;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpSession getSession() {
		return session;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public Model getModel() {
		return model;
	}

	public boolean isExtOpenFlag() {
		return extOpenFlag;
	}
}
