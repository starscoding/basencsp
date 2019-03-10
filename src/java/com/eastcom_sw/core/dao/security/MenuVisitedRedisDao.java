/**
 * 
 */
package com.eastcom_sw.core.dao.security;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.eastcom_sw.common.dao.redis.BaseRedisDao;
import com.eastcom_sw.common.utils.RandomGUID;
import com.eastcom_sw.common.utils.RedisKeyConstants;

/**
 * 常用菜单redis存储
 * 
 * @author SCM 2012-11-5
 */
@Component
public class MenuVisitedRedisDao extends BaseRedisDao {

	/**
	 * 将用户访问菜单的次数存储
	 * 
	 * @param userId
	 * @param menuId
	 * @param score
	 */
	public boolean addUserCommonlyuserdMenu(String userId, String menuId) {

		String key = String
				.format(RedisKeyConstants.USER_MENUS_VISITED, userId);
		int time = 0;
		if (hasKey(key)) {
			// 获取该菜单当前的访问次数
			Double time_ = redisZSet(key).score(menuId);
			if (time_ != null) {
				time = time_.intValue();
			}
		}
		return redisZSet(key).add(menuId, time + 1);
	}

	/**
	 * 获取用户常用菜单的TOPN
	 * 
	 * @param userId
	 * @param topn
	 */
	public Set<String> getUserVisitedTopN(String userId, int topn) {
		String key = String
				.format(RedisKeyConstants.USER_MENUS_VISITED, userId);
		Set<String> set = redisZSet(key).reverseRange(0, topn * 2 - 1);// 返回双倍数量topN以避免某些无效id
		return set;
	}

	/**
	 * 从用户常用菜单中删除某菜单
	 * 
	 * @param menuid
	 */
	public void delMenuFromUsermenus(String userId, String menuId) {
		String key = String
				.format(RedisKeyConstants.USER_MENUS_VISITED, userId);
		redisZSet(key).remove(menuId);
	}

	/**
	 * 记录用户访问模块随机生成码
	 * 
	 * @param menuId
	 *            页面id
	 * @param userName
	 *            用户名
	 * @return 随机码
	 */
	public String logRandomUUIDForMenuVisited(String menuId, String userName) {
		String key = String.format(RedisKeyConstants.USER_VISITEDMENU_RANDOM,
				userName);
		RandomGUID random = new RandomGUID();
		String uuid32 = random.getUUID32();
		redisMap(key).put(menuId, uuid32);
		return uuid32;
	}

	/**
	 * 删除用户访问页面的随机码
	 * 
	 * @param username
	 * @param pageId
	 */
	public void deleteRandomByPageId(String username, String pageId) {
		String key = String.format(RedisKeyConstants.USER_VISITEDMENU_RANDOM,
				username);
		if (redisMap(key).containsKey(pageId)) {
			redisMap(key).remove(pageId);
		}
	}
}
