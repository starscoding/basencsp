package com.eastcom_sw.core.web.interceptor;

import com.eastcom_sw.common.dao.redis.BaseRedisDao;
import com.eastcom_sw.common.utils.AuthToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 
 * 类说明：用于Controller 拦截，防御CSRF攻击，验证token
 * @author: fangqian
 * @date: 2015年9月24日 上午10:46:05
 * @ClassName: CsrfInterceptor 
 * @项目： base
 * @包：com.eastcom_sw.core.web.interceptor
 */
public class CsrfInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private BaseRedisDao baseRedisDao;

	private static Logger log = LoggerFactory.getLogger(CsrfInterceptor.class);
	
	public CsrfInterceptor(){
		log.info("=====  CsrfInterceptor has inited ....  ======");
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        if(handler.getClass().isAssignableFrom(HandlerMethod.class)){
        	AuthToken authToken = ((HandlerMethod) handler).getMethodAnnotation(AuthToken.class);
        	
        	HttpSession session = request.getSession();
        	
        	String loginUrl = request.getContextPath() + "/login";
        	
        	//没有声明需要验证或者证明不验证
        	if(authToken == null || authToken.validate() == false){
        		return true;
        	}else{
        		String csrfToken = request.getParameter("loto");
        		if(csrfToken != null && csrfToken.equals(session.getAttribute("CSRFToken"))){
        			return true;
        		}else{
        			log.info("验证失败，可能为非法请求！");
        			
        			String str = "<script language='javascript'>"
                            + "window.top.location.href='"
                            + loginUrl
                            + "';</script>";
                    response.setContentType("text/html;charset=UTF-8");// 解决中文乱码
        			try {
        				PrintWriter out = response.getWriter();
        				out.print(str);
        				out.flush();
        				out.close();
        			} catch (IOException e) {
        				e.printStackTrace();
        			}
        			return false;
        		}
        	}
        		
        }else{
        	return true;
        }
	}
}
