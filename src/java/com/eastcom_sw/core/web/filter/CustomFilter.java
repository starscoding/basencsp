package com.eastcom_sw.core.web.filter;

import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @author wangzf
 * 
 */
public class CustomFilter extends AuthorizationFilter {
	private Logger logger = LoggerFactory.getLogger(XssFilter.class);

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
		logger.debug("......CustomFilter");
		
		return true;
	}
}
