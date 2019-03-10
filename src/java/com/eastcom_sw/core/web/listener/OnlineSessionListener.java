/**
 * session监听器
 *
 * @author Scm
 * @Date Jan 16 , 2012
 */
package com.eastcom_sw.core.web.listener;

import com.eastcom_sw.common.dao.redis.BaseRedisDao;
import com.eastcom_sw.common.utils.Constants;
import com.eastcom_sw.common.utils.DateUtil;
import com.eastcom_sw.common.utils.RedisKeyConstants;
import com.eastcom_sw.core.entity.security.User;
import com.eastcom_sw.core.service.security.SystemUserService;
import com.eastcom_sw.core.web.AppContextLoader;
import com.eastcom_sw.core.web.LoginBundle;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.redis.support.collections.RedisZSet;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;

public class OnlineSessionListener implements HttpSessionListener {
	protected transient final Log log = LogFactory.getLog(getClass());

	private SystemUserService systemUserService = null;

	private BaseRedisDao baseRedisDao = null;
	
	public void sessionCreated(HttpSessionEvent arg0) {
		// log.info("session created id :" + arg0.getSession().getId());
	}

	@SuppressWarnings("static-access")
	public void sessionDestroyed(HttpSessionEvent arg0) {
		long lStart = Calendar.getInstance().getTimeInMillis();
		
		//单点登录
		if(arg0.getSession().getAttribute("NCSP_LINK_USERNAME") != null && 
				!"".equals(arg0.getSession().getAttribute("NCSP_LINK_USERNAME"))){
			arg0.getSession().removeAttribute("NCSP_LINK_USERNAME");
		}
		
		String sessionid = arg0.getSession().getId();
		if (systemUserService == null) {
			systemUserService = (SystemUserService) AppContextLoader
					.getInstance().getServiceByName("systemUserService");
		}
		String userinfo = systemUserService.getUserFromSession(arg0
				.getSession());

		if (systemUserService != null && StringUtils.isNotBlank(userinfo)) {
			String host = null;
			try {
				host = InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			}
			if (!RedisKeyConstants.STATUS_ENFORCEQUIT.equals(systemUserService
					.getSessionStatus(sessionid))) {

				Object ud = arg0.getSession().getAttribute(
						Constants.SYS_CURRENT_USER_DETAIL);
				User u = ud == null ? new User() : ((User) ud);
				log.info("Remove Online User:" + userinfo);
				String username = "";
				String clientIp = "";
				if (StringUtils.isNotBlank(userinfo)
						&& userinfo.indexOf(Constants.ONLINE_USER_SPLITER) != -1) {
					username = userinfo.split(Constants.ONLINE_USER_SPLITER)[0];
					clientIp = userinfo.split(Constants.ONLINE_USER_SPLITER)[1];
				} else {
					username = userinfo;
				}

				/** syslog */
				String locale = systemUserService.getCommonFieldValueByName(
						"current_local", "value");
				if (StringUtils.isBlank(locale)) {
					locale = "zh_CN";// 默认简体中文
				}
				LoginBundle.setLc(locale);
				String log = String.format(LoginBundle.getString("logoutmsg"),
						u.getFullName() + "(" + username + ")")
						+ "SESSIONID:"
						+ sessionid;
				systemUserService
						.saveSysLog(host, clientIp, username, "1",
								Constants.SYSLOGSOPERATETYPE_OTHER, "logout",
								log, (int) (Calendar.getInstance()
										.getTimeInMillis() - lStart), DateUtil
										.getCurrentDatetime());
				
				if (baseRedisDao == null) {
					baseRedisDao = (BaseRedisDao) AppContextLoader
							.getInstance().getServiceByName("baseRedisDao");
				}
				String key = String.format(RedisKeyConstants.CRSF_TOKEN, sessionid);
				RedisZSet<String> set = baseRedisDao.redisZSet(key);
				String[] t = set.toArray(new String[ ]{});
				if(t!=null && t.length >0){
					String token = t[0];
					baseRedisDao.redisZSet(key).remove(token);
					baseRedisDao.deleteKey(key);
				}
				systemUserService.removeUser(userinfo);
			}
		}
	}
}
