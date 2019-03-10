/**
 * 
 */
package com.eastcom_sw.core.dao.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springside.modules.utils.Collections3;

import com.eastcom_sw.common.dao.redis.BaseRedisDao;
import com.eastcom_sw.common.utils.ParseJSONObject;
import com.eastcom_sw.common.utils.RedisKeyConstants;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * @author SCM 用户角色授权
 */
@Component
public class UserRoleRedisDao extends BaseRedisDao {

	@Autowired
	private RoleDao roleDao;

	/**
	 * @param userId
	 * @param newName
	 * @param deleteName
	 */
	public void userRoleAuthorization(String userId, String newName[],
			String deleteName[]) {
		// 用户新增角色
		String key = String.format(RedisKeyConstants.USER_ROLE, userId);
		List<String> newRoles = Arrays.asList(newName);
		if (!newRoles.isEmpty())
			redisSet(key).addAll(newRoles);

		// 用户删除角色
		List<String> delRoles = Arrays.asList(deleteName);
		if (!delRoles.isEmpty())
			redisSet(key).removeAll(delRoles);
	}

	/*
	 * 根据删除用户删除相关的用户角色redis数据
	 */
	public void removeRelationByUsers(List<String> userIds) {
		for (String userId : userIds) {
			String key = String.format(RedisKeyConstants.USER_ROLE, userId);
			deleteKey(key);
		}
	}

	/**
	 * 根据角色删除用户角色相关的redis数据
	 * 
	 * @param roleIds
	 * @param roleNames
	 */
	@SuppressWarnings("unchecked")
	public void removeRelationByRoles(List<String> roleIds,
			List<String> roleNames) {
		List<Object> daoRs = roleDao.queryUserInfoByRole(roleIds
				.toArray(new String[roleIds.size()]));
		List<String> userIds = new ArrayList<String>();
		for (Object o : daoRs) {
			JSONArray ja = JSONArray.fromObject(o);
			userIds.add(ja.getString(0));
		}
		for (String userId : userIds) {
			String key = String.format(RedisKeyConstants.USER_ROLE, userId);
			redisSet(key).removeAll(roleNames);
		}
	}

	/**
	 * 通过用户名获取角色
	 * 
	 * @param userId
	 * @return 用‘，’分隔的角色字符串
	 */
	public String getUserRoles(String userId) {
		String[] names = getUserRole(userId);
		return Collections3.convertToString(Arrays.asList(names), ",");
	}

	/**
	 * 通过用户名获取角色
	 * 
	 * @param userId
	 * @return 角色的List
	 */
	public List<String> getUserRoleNames(String userId) {
		String[] names = getUserRole(userId);
		return Arrays.asList(names);
	}

	/**
	 * 获取用户多个角色名称
	 * 
	 * @param userId
	 * @return
	 */
	private String[] getUserRole(String userId) {
		String[] roles = new String[] {};
		String key = String.format(RedisKeyConstants.USER_ROLE, userId);
		if (hasKey(key)) {
			roles = redisSet(key).toArray(new String[] {});
		}
		return roles;
	}

	/**
	 * 初始化用户角色关系redis关系
	 * 
	 * @param list
	 */
	@SuppressWarnings("rawtypes")
	public void initUserRoleRelation(List list) {
		String pattern = String.format(RedisKeyConstants.USER_ROLE, "*");
		// 如果redis中已经存在用户角色关系数据，那么先删除
		Set<String> keys = fuzzyGetKeys(pattern);
		if (!keys.isEmpty())
			deleteKeys(keys);

		Map<String, List<String>> uroleMap = convertData(list);
		for (Entry<String, List<String>> entry : uroleMap.entrySet()) {
			String key = String.format(RedisKeyConstants.USER_ROLE,
					entry.getKey());
			redisSet(key).addAll(entry.getValue());
		}
	}

	/**
	 * 转换用户角色关系
	 * 
	 * @param list
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Map<String, List<String>> convertData(List list) {
		Map<String, List<String>> map = Maps.newHashMap();
		for (Object o : list) {
			Object[] ur = (Object[]) o;
			String userid = (String) ur[0];
			String rolename = (String) ur[1];
			if (map.containsKey(userid)) {
				map.get(userid).add(rolename);
			} else {
				map.put(userid, Lists.newArrayList(rolename));
			}
		}
		return map;
	}
}
