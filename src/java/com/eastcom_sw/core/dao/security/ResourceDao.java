/**
 * 
 */
package com.eastcom_sw.core.dao.security;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import com.eastcom_sw.common.dao.jpa.Dao;
import com.eastcom_sw.core.entity.security.Resource;
import com.eastcom_sw.core.entity.security.User;

/**
 * @author SCM
 * 
 */
public interface ResourceDao extends Dao<Resource> {

	public List<Resource> getResourceChildren(String parentId);

	public void removeResourceAndChildren(ArrayList<String> ids);

	@SuppressWarnings("rawtypes")
	public List searchResource(String name);

	@SuppressWarnings("rawtypes")
	public List getAllPageResource();

	/**
	 * 根据用户查询他所拥有的所有资源权限
	 * 
	 * @param u
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List getRescourcesByUser(User u);

	/**
	 * 根据角色id查询该角色所拥有的资源权限(暂弃，当前该数据从redis中获取)
	 * 
	 * @param roleId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List getRescourcesByUserByRoleId(String roleId);

	/**
	 * 获取所有有效的资源
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List getAllValidResource();

	/**
	 * 批量更新资源关系（父节点以及order顺序）
	 * 
	 * @param params
	 */
	public void changeParentAndOrder(ArrayList<JSONObject> params);
	
	/**
	 * 通过名称获取id
	 * @param name
	 * @return
	 */
	public String getIdByName(String name);
}
