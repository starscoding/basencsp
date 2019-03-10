package com.eastcom_sw.core.web.filter.login;

/**
 * 登录链接口
 * 
 * @author JJF
 * @date JAN 15 2014
 * 
 */
public interface LoginFilter {
	/**
	 * 登录前自定义操作
	 * 
	 * @param loginReq
	 * @return
	 */
	public FilterResp beforeLogin(FilterReq loginReq);

	/**
	 * 登录失败后的操作
	 * 
	 * @param loginReq
	 * @param e
	 * @return
	 */
	public FilterResp failedLogin(FilterReq loginReq, Exception e);

	/**
	 * 登录成功后的操作
	 * 
	 * @param loginReq
	 * @return
	 */
	public FilterResp afterLogin(FilterReq loginReq);
}
