package com.eastcom_sw.core.web;

import com.eastcom_sw.common.utils.Constants;
import com.eastcom_sw.common.utils.DateUtil;
import com.eastcom_sw.common.utils.RedisKeyConstants;
import com.eastcom_sw.core.dao.security.OnlineUserRedisDao.OnlineUserRedisKey;
import com.eastcom_sw.core.entity.security.OnlineUser;
import com.eastcom_sw.core.entity.security.User;
import com.eastcom_sw.core.service.security.SystemUserService;
import com.eastcom_sw.core.service.security.UserService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Calendar;
import java.util.Map;

/**
 * 记录用户登陆日志线程
 * 
 * @author JJF
 * @since DEC 16 2013
 * 
 */
public class LogLoginUserInfoThread implements Runnable {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private LoginController loginController;
	private User user;
	private SystemUserService systemUserService;
	private UserService userService;
	private HttpServletRequest request;
	private HttpSession session;
	private Map<String, Object> extensionArgs;

	@SuppressWarnings("unused")
	private LogLoginUserInfoThread() {
	}

	public LogLoginUserInfoThread(LoginController loginController,
                                  HttpServletRequest request, HttpSession session, User user,
                                  SystemUserService systemUserService, UserService userService,
                                  Map<String, Object> extensionArgs) {
		this.loginController = loginController;
		this.user = user;
		this.request = request;
		this.session = session;
		this.systemUserService = systemUserService;
		this.userService = userService;
		this.extensionArgs = extensionArgs;
	}

	@Override
	public void run() {
		log.info("Start save onlineUserInfo...");
		try {
			sysLog();
		} catch (Exception e) {
			log.error("Save login log failed!");
			e.printStackTrace();
		}
		try {
			updateUser();
		} catch (Exception e) {
			log.error("Update user login info failed!");
			e.printStackTrace();
		}
		try {
			redisOpts();
		} catch (Exception e) {
			log.error("Update redis online user failed!");
			e.printStackTrace();
		}
		log.info("Save onlineUserInfo completed");
	}

	private void sysLog() {
		long lStart = Calendar.getInstance().getTimeInMillis();
		LoginBundle.setLc(loginController.getI18NLocale());
		String usernames = user.getFullName() + "(" + user.getUserName() + ")";
		String log_ = String.format(LoginBundle.getString("loginmsg"),
				usernames) + "SESSIONID:" + session.getId();
		String extSystemId = extensionArgs.get("extSystemId").toString();
		if (StringUtils.isBlank(extSystemId)) {
			extSystemId = "本系统";
		}
		log_ += (",登录入口:" + extSystemId);
		loginController.logLoginLog(extensionArgs.get("clientIP").toString(),
				user.getUserName(), true, Constants.SYSLOGSOPERATETYPE_OTHER,
				"login", log_,
				(int) (Calendar.getInstance().getTimeInMillis() - lStart));
	}

	private void updateUser() {
		String lastLoginTime = DateUtil.getCurrentDatetime();
		String clientIP = extensionArgs.get("clientIP").toString();
		boolean updateTimes = true;
		if (extensionArgs.get("updateTimes") != null) {
			updateTimes = (Boolean) extensionArgs.get("updateTimes");
		}
		if (updateTimes) {
			user.setTimes(user.getTimes() + 1);
		}
		// 将用户的字段：上次登陆时间和ip设置成当前的时间和ip，下次登陆的时候先读取数据库内容再更新此字段
		user.setLastLoginTime(lastLoginTime);
		user.setLastLoginIp(clientIP);
		userService.updateUser(user);
	}

	private void redisOpts() {
		OnlineUser onlineUser = new OnlineUser();
		String sessionId = session.getId();
		String lastLoginTime = DateUtil.getCurrentDatetime();
		String clientIP = extensionArgs.get("clientIP").toString();
		String username = user.getUserName();
		JSONObject userInfo = userService.querySingleUser(user.getId(), "true");

		// 如果用户已经登录那么强制退出
		String loginedSession = systemUserService.getUserSession(username + Constants.ONLINE_USER_SPLITER
				+ clientIP);
		if (!"".equals(loginedSession)) {
			systemUserService.setSessionStatus(loginedSession,
					RedisKeyConstants.STATUS_ENFORCEQUIT);
		}

		// 记录在线用户的基本信息
		onlineUser.setSessionid(sessionId);
		onlineUser.setUserid(user.getId());
		onlineUser.setUserlevel(user.getUserLevel());
		onlineUser.setXtheme(user.getXtheme());
		onlineUser.setUsernameCn(user.getFullName());
		onlineUser.setUsername(user.getUserName());
		onlineUser.setLoginTime(lastLoginTime);
		onlineUser.setAuditTeam(user.getAuditTeam());
		onlineUser.setMobileNo(user.getMobileNo());
		onlineUser.setSex(user.getSex());
		onlineUser.setClientIp(clientIP);
		onlineUser.setDepartment(userInfo.getString("deptName"));
		systemUserService.setUserSession(
				OnlineUserRedisKey.getInstance(username, clientIP), onlineUser);
		systemUserService.setSessionStatus(sessionId,
				RedisKeyConstants.STATUS_LOGINED);
		systemUserService.logOnlineUser(username + Constants.ONLINE_USER_SPLITER + clientIP);
		systemUserService.recordLoginLog(username, lastLoginTime, clientIP);
	}

}
