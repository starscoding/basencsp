/**
 * 
 */
package com.eastcom_sw.core.web.filter;

import com.eastcom_sw.common.utils.CommonUtil;
import com.eastcom_sw.common.utils.Constants;
import com.eastcom_sw.common.utils.DateUtil;
import com.eastcom_sw.core.dao.security.OnlineUserRedisDao;
import com.eastcom_sw.core.dao.security.OnlineUserRedisDao.OnlineUserRedisKey;
import com.eastcom_sw.core.service.reqlog.ReqLogService;
import com.eastcom_sw.core.web.extension.RecordReqLogThread;
import com.eastcom_sw.core.web.extension.UserReqLog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;

/**
 * 请求过滤器，每次请求会记录请求日志
 * 
 * @author JJF
 * @date MAY 2014
 * 
 */
public class RequestLogFilter extends DelegatingFilterProxy implements Filter {
	@Autowired
	private ReqLogService reqLogService;

	@Override
	protected void initFilterBean() throws ServletException {
		ServletContext servletContext = this.getServletContext();
		OnlineUserRedisDao.webXMLPath = servletContext
				.getRealPath("WEB-INF/web.xml");
	}

	public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
		try {
			HttpServletRequest req = (HttpServletRequest) request;
			HttpSession session = req.getSession();
			String userinfo = (String) session
					.getAttribute(Constants.SYS_CURRENT_USER);
			String reqURL = "";
			try {
				reqURL = CommonUtil.getRequestURL(req);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			if (StringUtils.isNotBlank(userinfo)
					&& StringUtils.isNotBlank(reqURL)) {// 如果不存在userinfo则为未登录状态
				
				String username = userinfo.split(Constants.ONLINE_USER_SPLITER)[0];
				String clientIp = userinfo.split(Constants.ONLINE_USER_SPLITER)[1];
				if (reqLogService.filterUrl(reqURL)) {
					UserReqLog log = new UserReqLog();
					String serverIp = "";
					Date d = new Date();
					try {
						serverIp = InetAddress.getLocalHost().getHostAddress();
					} catch (Exception e) {
						e.printStackTrace();
					}

					log.setReqURL(reqURL);
					log.setUsername(username);
					log.setSessionId(session.getId());
					log.setClientIp(clientIp);
					log.setServerIp(serverIp);
					log.setReqTime(d.getTime());
					log.setReqTimeStr(DateUtil
							.getDate(d, "yyyy-MM-dd HH:mm:ss"));
					Thread t = new Thread(new RecordReqLogThread(reqLogService,
							log));
					t.start();
				} else {// 如果url为被过滤不需要记录请求日志的url，则单独刷新用户在线redis数据
					reqLogService.refreshOnlineUser(OnlineUserRedisKey
							.getInstance(username, clientIp));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		chain.doFilter(request, response);
	}

	public void destroy() {
	}
}
