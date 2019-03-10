package com.eastcom_sw.core.dao.security;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.springframework.data.redis.hash.DecoratingStringHashMapper;
import org.springframework.data.redis.hash.HashMapper;
import org.springframework.data.redis.hash.JacksonHashMapper;
import org.springframework.stereotype.Component;

import com.eastcom_sw.common.dao.redis.BaseRedisDao;
import com.eastcom_sw.common.utils.Constants;
import com.eastcom_sw.common.utils.RedisKeyConstants;
import com.eastcom_sw.common.utils.XMLFileUtil;
import com.eastcom_sw.core.entity.security.OnlineUser;

/**
 * 在线用户
 * 
 * @author SCM
 * @author JJF
 * 
 */
@Component
public class OnlineUserRedisDao extends BaseRedisDao {

	public static String webXMLPath;// RequestLogFilter中初始化该参数

	private final HashMapper<OnlineUser, String, String> onlineMapper = new DecoratingStringHashMapper<OnlineUser>(
			new JacksonHashMapper<OnlineUser>(OnlineUser.class));

	private static int SESSION_TIMEOUT = 0;

	/**
	 * 将用户基本信息存入
	 * 
	 * @param username
	 *            用户名（英文名）@ip组合
	 * @param onlineuser
	 */
	public void putAll(OnlineUserRedisKey k, OnlineUser onlineuser) {
		redisMap(k.getKey()).putAll(onlineMapper.toHash(onlineuser));
		refreshOnlineUser(k);
	}

	/**
	 * 刷新在线用户数据redis超时时间
	 * 
	 * @param username
	 *            用户名（英文名）@ip组合
	 */
	public void refreshOnlineUser(OnlineUserRedisKey k) {
		if (SESSION_TIMEOUT == 0) {
			initSessionTimeout();
		}

		if (SESSION_TIMEOUT > 0) {
			redisMap(k.getKey()).expire(SESSION_TIMEOUT, TimeUnit.MINUTES);
		}
	}

	/**
	 * 初始化web.xml中配置的session超时时间
	 */
	private void initSessionTimeout() {
		if (SESSION_TIMEOUT == 0) {
			try {
				Document d = XMLFileUtil.LoadXmlFile(webXMLPath);
				String t = d.getRootElement().element("session-config").element("session-timeout").getText();
				SESSION_TIMEOUT = Integer.parseInt(t);
				logger.info("Set SESSION_TIMEOUT to:" + SESSION_TIMEOUT + " mins.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 通过用户名获取用户的基础信息
	 * 
	 * @param username
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public OnlineUser getByUserKey(OnlineUserRedisKey k) {
		String key = k.getKey();
		if (hasKey(key)) {
			Set<Entry<String, String>> entitys = redisMap(key).entrySet();
			Map map = new HashMap();
			for (Entry<String, String> entity : entitys) {
				map.put(entity.getKey(), entity.getValue());
			}
			return onlineMapper.fromHash(map);
		} else {
			return null;
		}
	}

	/**
	 * 获取用户session信息
	 * 
	 * @param username
	 * @return
	 */
	public String getUserSession(OnlineUserRedisKey k) {
		String key = k.getKey();
		String sessionid = "";
		try {
			sessionid = redisMap(key).get("sessionid");
		} catch (NullPointerException e) {

		}
		return sessionid;
	}

	/**
	 * 
	 * @param username
	 * @return 用户上一次登录的IP
	 */
	public String queryUserLastLoginIp(String username) {
		String key = String.format(RedisKeyConstants.USER_LOGIN_LOG, username);
		try {
			String logInfo = redisList(key).getFirst();
			String ip = logInfo.split("&")[1];
			return ip;
		} catch (NoSuchElementException e) {
			return "";
		}
	}

	/**
	 * 获取用户上次登录的信息
	 * 
	 * @param username
	 * @return
	 */
	public Map<String, String> getUserLastLoginInfo(String username) {
		Map<String, String> map = new HashMap<String, String>();
		String key = String.format(RedisKeyConstants.USER_LOGIN_LOG, username);
		try {
			String logInfo = redisList(key).getFirst();
			String[] info = logInfo.split("&");
			map.put("ip", info[1]);
			map.put("loginTime", info[0]);
		} catch (NoSuchElementException e) {

		}
		return map;
	}

	/**
	 * 
	 * @param username
	 *            :用户名
	 * @param times
	 *            :索引从0开始计算
	 * @return 返回用户最近 times 次的密码
	 */
	public List<String> getUserLatePassword(String username, int times) {
		String key = String.format(RedisKeyConstants.USER_LATE_PASSWORDS, username);
		return redisList(key).range(0, times);
	}

	/**
	 * 记录用户登录密码错误次数
	 * 
	 * @param username
	 * @return
	 */
	public long recordErrorPasswordTimes(String username, long interval) {
		String key = String.format(RedisKeyConstants.USER_PASSWORD_ERROR_TIMES, username);
		Long times = valueOps().increment(key, 1);
		// 判断，如果是第一次错误那么设置失效时间；之后间隔内，次数加 1；
		if (times == 1) {
			getRedisTemplate().expire(key, interval, TimeUnit.MINUTES);
			
			//锁定用户需要验证码登录（代码级别），到指定时间自动解锁
			String _key = String.format(RedisKeyConstants.USER_LOGIN_NEED_CODE, username);
			valueOps().set(_key, "0", interval, TimeUnit.MINUTES);
		}
		return times;
	}

	/**
	 * 锁定用户，到指定时间自动解锁
	 * 
	 * @param username
	 *            用户名
	 * @param time
	 *            锁定时间 （单位：分钟）
	 * 
	 */
	public void lockedUserInTime(String username, long time) {
		String key = String.format(RedisKeyConstants.USER_LOCKED_TIME, username);
		valueOps().set(key, "1", time, TimeUnit.MINUTES);
	}

	/**
	 * 解除用户账户锁定
	 * 
	 * @param username
	 */
	public void unLockedUser(String username) {
		String key = String.format(RedisKeyConstants.USER_LOCKED_TIME, username);
		deleteKey(key);
	}

	/**
	 * 判断用户是否锁定
	 * 
	 * @param username
	 * @return
	 */
	public boolean judeUserHaveLocked(String username) {
		String key = String.format(RedisKeyConstants.USER_LOCKED_TIME, username);
		return hasKey(key);
	}

	/**
	 * 判断用户登录是否需要验证码（代码级别的）(值为1)
	 * 
	 * @param username
	 * @return
	 */
	public boolean judeUserNeedCodeLogin(String username) {
		String key = String.format(RedisKeyConstants.USER_LOGIN_NEED_CODE, username);
		return "1".equals(getValue(key));
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
		String key = String.format(RedisKeyConstants.USER_LOGIN_NEED_CODE, username);
		long expire = this.getRedisTemplate().getExpire(key);
		if(expire <= 0){
			expire = 1;
		}
		//set方法会清除expire，故需要重新设置
		valueOps().set(key, "1", expire, TimeUnit.SECONDS);
	}

	/**
	 * KEY
	 * 
	 * @author JJF
	 * 
	 */
	public static class OnlineUserRedisKey {
		private String username;
		private String clientIp;

		private OnlineUserRedisKey(String username, String clientIp) {
			this.username = username;
			this.clientIp = clientIp;
		}

		public static OnlineUserRedisKey getInstance(String username, String clientIp) {
			return new OnlineUserRedisKey(username, clientIp);
		}

		public String getKey() {
			if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(clientIp)) {
				return String.format(RedisKeyConstants.ONLINE_USER_NAME,
						username + Constants.ONLINE_USER_SPLITER + clientIp);
			} else {
				return null;
			}
		}
	}
}
