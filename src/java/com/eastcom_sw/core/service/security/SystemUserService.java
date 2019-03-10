/**
 * 
 */
package com.eastcom_sw.core.service.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.support.collections.RedisZSet;
import org.springframework.stereotype.Component;

import com.eastcom_sw.common.service.BaseService;
import com.eastcom_sw.common.utils.Constants;
import com.eastcom_sw.common.utils.RedisKeyConstants;
import com.eastcom_sw.core.dao.security.OnlineUserRedisDao;
import com.eastcom_sw.core.dao.security.OnlineUserRedisDao.OnlineUserRedisKey;
import com.eastcom_sw.core.entity.security.OnlineUser;
import com.eastcom_sw.core.entity.security.User;
import com.eastcom_sw.security.extension.dao.ConfigurationExtensionRedisDao;

/**
 * @author Cason
 * 
 * @Date May 17, 2012
 */
@Component
public class SystemUserService extends BaseService {

	@Autowired
	private OnlineUserRedisDao onlineUserRedisDao;

	@Autowired
	private ConfigurationExtensionRedisDao configurationExtensionRedisDao;

	/**
	 * 将用户信息保存在session
	 * 
	 * @param session
	 * @param userinfo
	 */
	public void setUserToSession(HttpSession session, String userinfo, User u) {
		session.setAttribute(Constants.SYS_CURRENT_USER, userinfo);
		session.setAttribute(Constants.SYS_CURRENT_USER_DETAIL, u);
	}

	/**
	 * 将用户信息保存在session
	 * 
	 * @param session
	 * @param userinfo
	 */
	public void setUserToSession(HttpSession session, String userinfo) {
		session.setAttribute(Constants.SYS_CURRENT_USER, userinfo);
	}

	/**
	 * 从session中获取用户的信息
	 * 
	 * @param session
	 * @return
	 */
	public String getUserFromSession(HttpSession session) {
		return session.getAttribute(Constants.SYS_CURRENT_USER) == null ? ""
				: session.getAttribute(Constants.SYS_CURRENT_USER).toString();
	}

	/**
	 * 从redis中删除用户的信息，包括：用户基础信息（hash）、session状态信息、在线用户set中的用户名
	 * 
	 * @param userinfo
	 */
	public void removeUser(String userinfo) {
		String sessionid = getUserSession(userinfo);
		if (!"".equals(sessionid)) {
			onlineUserRedisDao.redisZSet(RedisKeyConstants.ONLINE_USER_ZSET).remove(userinfo);
			onlineUserRedisDao.deleteKey(String.format(RedisKeyConstants.SESSIONID_STATUS, sessionid));
			onlineUserRedisDao.deleteKey(String.format(RedisKeyConstants.ONLINE_USER_NAME, userinfo));
		}
	}

	/**
	 * 如果用户已登录返回用户的sessionid,未登录返回空
	 * 
	 * @param userinfo
	 *            用户名
	 * @return 用户的sessionid
	 */
	public String getUserSession(String userinfo) {
		String key = String.format(RedisKeyConstants.ONLINE_USER_NAME, userinfo);
		if (onlineUserRedisDao.hasKey(key)) {
			return onlineUserRedisDao.redisMap(key).get("sessionid");
		}
		return "";
	}

	/**
	 * 存储在线用户的基本信息(hash)
	 * 
	 * @param username
	 * @param onlineuser
	 */
	public void setUserSession(OnlineUserRedisKey k, OnlineUser onlineuser) {
		onlineUserRedisDao.putAll(k, onlineuser);
	}

	/**
	 * 获取在线用户基本信息
	 * 
	 * @param username
	 * @return
	 */
	public OnlineUser getUserSessionByUsername(OnlineUserRedisKey k) {
		return onlineUserRedisDao.getByUserKey(k);
	}

	/**
	 * 获取session的状态
	 * 
	 * @param sessionid
	 * @return
	 */
	public String getSessionStatus(String sessionid) {
		return onlineUserRedisDao.getValue(String.format(RedisKeyConstants.SESSIONID_STATUS, sessionid));
	}

	/**
	 * 设置session状态
	 * 
	 * @param sessionid
	 * @param status
	 */
	public void setSessionStatus(String sessionid, String status) {
		onlineUserRedisDao.setValue(String.format(RedisKeyConstants.SESSIONID_STATUS, sessionid), status);
	}

	/**
	 * 统计在线用户人数
	 * 
	 * @return
	 */
	public Long countOnlineUser() {
		Long count = 0L;
		count = (long) onlineUserRedisDao.redisList(RedisKeyConstants.ONLINE_USER_ZSET).size();
		return count;
	}

	/**
	 * 在在线用户set中添加用户
	 * 
	 * @param username
	 */
	public void logOnlineUser(String username) {
		onlineUserRedisDao.redisZSet(RedisKeyConstants.ONLINE_USER_ZSET).add(username);
	}

	/**
	 * 获取所有在线用户
	 * 
	 * @return
	 */
	public List<OnlineUser> getOnlineUsers() {
		List<OnlineUser> users = new ArrayList<OnlineUser>();
		RedisZSet<String> set = onlineUserRedisDao.redisZSet(RedisKeyConstants.ONLINE_USER_ZSET);
		String[] usernames = set.toArray(new String[] {});
		for (String username : usernames) {
			OnlineUser onlineUser = null;
			if (username.indexOf(Constants.ONLINE_USER_SPLITER) != -1) {
				String[] usernameIp = username.split(Constants.ONLINE_USER_SPLITER);
				onlineUser = onlineUserRedisDao
						.getByUserKey(OnlineUserRedisKey.getInstance(usernameIp[0], usernameIp[1]));
			}
			if (onlineUser != null) {
				users.add(onlineUser);
			} else {// 如果redis不存在该用户名IP对应的信息，则在线信息已超时，从set中删除
				set.remove(username);
			}
		}
		return users;
	}

	/**
	 * 获取所有在线用户
	 * 
	 * @return
	 */
	public void clearOnlineUsers() {
		/* List<OnlineUser> users = new ArrayList<OnlineUser>(); */
		RedisZSet<String> set = onlineUserRedisDao.redisZSet(RedisKeyConstants.ONLINE_USER_ZSET);
		onlineUserRedisDao.deleteKeys(set);
	}

	/**
	 * 记录用户登录日志
	 * 
	 * @param userName
	 * @param loginTime
	 * @param clientIP
	 */
	public void recordLoginLog(String userName, String loginTime, String clientIP) {
		String key = String.format(RedisKeyConstants.USER_LOGIN_LOG, userName);
		StringBuffer info = new StringBuffer();
		info.append(loginTime).append("&").append(clientIP);
		onlineUserRedisDao.addListOne(key, info.toString());
	}

	/**
	 * 判断用户与上次登录的IP是否一致
	 * 
	 * @param username
	 * @param clientIP
	 * @return 第一次登录也返回false
	 */
	public boolean judgeIssameIpLastLogin(String username, String clientIP) {
		String lastLoginIp = onlineUserRedisDao.queryUserLastLoginIp(username);
		return lastLoginIp.isEmpty() || !clientIP.equals(lastLoginIp) ? false : true;
	}

	/**
	 * 获取用户上次登录的信息
	 * 
	 * @param username
	 * @return
	 */
	public Map<String, String> getLastLoginTimeAndIp(String username) {
		return onlineUserRedisDao.getUserLastLoginInfo(username);
	}

	/**
	 * 记录用户密码
	 * 
	 * @param username
	 * @param password
	 */
	public void logUserPassword(String username, String password) {
		String key = String.format(RedisKeyConstants.USER_LATE_PASSWORDS, username);
		onlineUserRedisDao.addListOne(key, password);
	}

	/**
	 * 判断用户修改的密码N次内是否重复
	 * 
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @param times
	 *            检测次数范围 ： 从0开始计算
	 * @return times次内密码是否重复
	 */
	public boolean judeIsSamePasswordIntimes(String username, String password, int times) {
		List<String> passwords = onlineUserRedisDao.getUserLatePassword(username, times);
		return passwords.contains(password) ? true : false;
	}

	/**
	 * 记录用户密码输入错误次数，超过最大次数将会被锁定约束的时间
	 * 
	 * @param username
	 *            用户名
	 * @return 剩余输入次数（返回0时不进行锁定操作,返回正数时为剩余尝试次数，负数时为被锁时间）
	 */
	@SuppressWarnings("unchecked")
	public int logUserPasswordErrorTimesAndlocked(String username) {

		Map<String, String> securityArgs = configurationExtensionRedisDao.getSysArguments("security");

		long interval = RedisKeyConstants.LOGIN_TIME_INTERVAL;
		int times = RedisKeyConstants.ALLOW_PASSWORD_TIMES;

		if (StringUtils.isNotBlank(securityArgs.get("loginArgs"))) {
			JSONObject jo = JSONObject.fromObject(securityArgs.get("loginArgs"));
			if (jo.containsKey("loginTimeInterval")) {
				interval = Integer.parseInt(jo.getString("loginTimeInterval"));
				RedisKeyConstants.LOGIN_TIME_INTERVAL = interval;
				RedisKeyConstants.LOGIN_ERROR_TIME_INTERVAL = interval;
			}

			if (jo.containsKey("allowPasswordErrorTimes")) {
				times = Integer.parseInt(jo.getString("allowPasswordErrorTimes"));
				RedisKeyConstants.ALLOW_PASSWORD_TIMES = times;
				RedisKeyConstants.ALLOW_PASSWORD_UNCODE_TIMES = times;
			}

			if (jo.containsKey("allowPasswordErrorUncodeTimes")) {
				RedisKeyConstants.ALLOW_PASSWORD_UNCODE_TIMES = Integer.parseInt(jo.getString("allowPasswordErrorUncodeTimes"));
			}
			
			if (jo.containsKey("loginErrorTimeInterval")) {
				RedisKeyConstants.LOGIN_ERROR_TIME_INTERVAL = Integer.parseInt(jo.getString("allowPasswordErrorUncodeTimes"));
			}
		}

		// times=0的时候未系统配置不进行帐号锁定操作的时候，直接返回-1标识不进行锁定操作
		if (times != 0) {
			int errorTimes = (int) onlineUserRedisDao.recordErrorPasswordTimes(username, RedisKeyConstants.LOGIN_ERROR_TIME_INTERVAL);

			int remainTimes = times - errorTimes;
			if (errorTimes >= times) {
				onlineUserRedisDao.lockedUserInTime(username, interval);
				String key = String.format(RedisKeyConstants.USER_PASSWORD_ERROR_TIMES, username);
				onlineUserRedisDao.deleteKey(key);
				
				//解除用户验证码登录校验（代码级别）
				key = String.format(RedisKeyConstants.USER_LOGIN_NEED_CODE, username);
				onlineUserRedisDao.deleteKey(key);
				
				return (int) (0 - interval);// 返回被锁时间
			} else {
				return remainTimes;// 返回剩余尝试次数
			}
		} else {
			return 0;// 返回不锁定标识
		}
	}

	/**
	 * 清除账户锁定状态
	 * 
	 * @param username
	 */
	public void unLockedUser(String username) {
		onlineUserRedisDao.unLockedUser(username);// 删除锁定状态数据

		// 删除锁定次数记录数据
		String key = String.format(RedisKeyConstants.USER_PASSWORD_ERROR_TIMES, username);
		onlineUserRedisDao.deleteKey(key);
	}

	/**
	 * 判断登录的用户是否被锁定
	 * 
	 * @param username
	 * @return
	 */
	public boolean haveLockUser(String username) {
		return onlineUserRedisDao.judeUserHaveLocked(username);
	}

	/**
	 * 判断用户是否需要验证码登录（非公共配置）
	 * 
	 * @param username
	 * @return
	 */
	public boolean checkNeedCodeLogin(String username) {
		return onlineUserRedisDao.judeUserNeedCodeLogin(username);
	}
	
	/**
	 * 设置用户登录是否需要验证码（代码级别的）(值为1)
	 * 
	 * @param username
	 *            用户名
	 * @param value
	 *            值
	 * 
	 */
	public void lockedUserNeedCodeLogin(String username, String value) {
		onlineUserRedisDao.lockedUserNeedCodeLogin(username, value);
	}
}
