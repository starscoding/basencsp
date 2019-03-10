/**
 * 
 */
package com.eastcom_sw.core.dao.security;

import java.util.List;

import com.eastcom_sw.common.dao.jpa.Dao;
import com.eastcom_sw.core.entity.security.Role;
import com.eastcom_sw.core.entity.security.User;

import net.sf.json.JSONObject;

/**
 * 角色管理
 * 
 * @author SCM
 * 
 */
public interface RoleDao extends Dao<Role> {
	/** 根据角色id获取所有关联用户id */
	@SuppressWarnings("rawtypes")
	public List queryUsersByIds(List ids);

	/** 根据角色id获取角色信息 */
	@SuppressWarnings("rawtypes")
	public List queryRoleInfo(List ids);

	/** 获取单个用户所具有的角色权限的简单字段 */
	@SuppressWarnings("rawtypes")
	public List querySimpleRoleInfoByUserId(User u, boolean allFlag,
			boolean creatorFlag);
	
	/** 获取单个用户所具有的角色权限的简单字段 */
	@SuppressWarnings("rawtypes")
	public List querySimpleRoleInfoByUserIdNew(User u, boolean allFlag,
			boolean creatorFlag,String userNames);

	/**
	 * 获取用户所有的相关角色，包括拥有权限角色和其新建的角色
	 * 
	 * @param userId
	 *            用户id
	 * @return 角色id 角色名称 角色中文名 是否临时 用户数 资源数 有效开始时间 有效结束时间 创建者 创建时间 角色简介
	 */
	@SuppressWarnings("rawtypes")
	public List queryAllRolesByUser(User u,String userNames);

	/**
	 * 通过名称或者中文名获取角色数量(暂用于判断该名称是否已存在)
	 * 
	 * @param name
	 *            用户名或者中文名
	 * @param type
	 *            name:名称 nameCn：中文名
	 * @return
	 */
	public long queryRoleByName(String name, String type);

	/**
	 * 修改role基本信息
	 * 
	 * @param id
	 *            角色Id
	 * @param name
	 *            角色名称
	 * @param nameCn
	 *            角色中文名称
	 * @param validStartTime
	 *            角色有效期开始时间
	 * @param validEndTime
	 *            角色有效期结束时间
	 * @param description
	 *            角色描述
	 */
	public void updateRoleBasicInfo(String id, String name, String nameCn,
			String validStartTime, String validEndTime, String description);

	/**
	 * 修改角色资源权限
	 * 
	 * @param roleId
	 *            :当前所要编辑的角色id
	 * @param addIds
	 *            :新增权限资源id集合
	 * @param delIds
	 *            :丧失权限资源id集合
	 */
	public void updateRoleResourceRange(String roleId, String[] addIds,
			String[] delIds);

	/**
	 * 根据用户获取该用户的所有角色地区权限
	 * 
	 * @param u
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List queryRegionByUser(User u);

	/**
	 * 根据角色查询该角色拥有的资源权限
	 * 
	 * @param roleId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List queryRegionByRoleId(String roleId);

	/**
	 * 修改角色区域权限
	 * 
	 * @param roleId
	 *            :当前所要角色的id
	 * @param addIds
	 *            :新增区域权限集合
	 * @param delIds
	 *            :丧失区域权限集合
	 */
	public void updateRoleRegionRange(String roleId, String[] addIds,
			String[] delIds);

	/**
	 * 批量删除角色(暂时前台只传一个id)
	 * 
	 * @param userIds
	 *            roleId集合
	 */
	public void deleteRole(List<String> roleIds);

	/**
	 * 通过角色id获取该角色下的用户信息
	 * 
	 * @param roleId
	 * @return 用户中文名，用户部门名称
	 */
	@SuppressWarnings("rawtypes")
	public List queryUserInfoByRole(String[] roleIds);

	/**
	 * 获取所有角色与资源的关系(redis初始化用)
	 * 
	 * @return 角色name 资源id
	 */
	@SuppressWarnings("rawtypes")
	public List queryAllRoleResourceRelationships();
	
	/**
	 * 
	 * <p>Description:获取系统用户的所属角色</p>
	 * <p>Title: getUserRole</p>
	 * @return
	 */
	public List<JSONObject> getUserRole(String userName,boolean isLike);
}
