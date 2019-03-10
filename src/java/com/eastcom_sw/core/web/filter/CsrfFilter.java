package com.eastcom_sw.core.web.filter;

import com.eastcom_sw.common.dao.redis.BaseRedisDao;
import com.eastcom_sw.common.dao.redis.RedisTemplateUtil;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class CsrfFilter extends AuthorizationFilter {

	private Logger logger = LoggerFactory.getLogger(CsrfFilter.class);

	
	@Autowired
	private BaseRedisDao baseRedisDao;
	
	@Autowired
	private RedisTemplateUtil redisTemplateUtil;
	
	@Override
	protected boolean isAccessAllowed(ServletRequest request,
                                      ServletResponse response, Object mappedValue) throws Exception {
//		logger.info("验证csrf攻击！！！");
//		HttpServletRequest req = (HttpServletRequest) request;
//		
//		HttpSession session = req.getSession();
//		
//		String csrfToken = req.getParameter("csrfToken");
//		logger.info("token:"+session.getAttribute("CSRFToken"));
//		if(csrfToken != null && csrfToken.equals(session.getAttribute("CSRFToken"))){
//			logger.info("验证成功！！！");
//			return true;
//		}else{
//			logger.info("验证失败！！！");
//			return false;
//		}
		return true;
	}
}
