package com.eastcom_sw.core.dao.security;

import java.util.List;

/**
 * 用户部门相关查询
 * @author JJF
 * @Date NOV 22 2012
 */
public interface UserDepartmentDao {
	/**
	 * 获取用户所有权限部门信息
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List getAllAccreditDeptByUser(String userId);
	/**
	 * 获取用户所有所属部门信息
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List getAllDeptByUser(String userId);
	/**根据部门id获取所有关联用户id*/
	@SuppressWarnings("rawtypes")
	public List queryUsersById(List ids);
	/**
	 * 获取当前部门下用户基本信息
	 * @param did	部门id
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List queryDeptUsers(String did);
	
}
