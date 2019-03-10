/**
 * 
 */
package com.eastcom_sw.core.dao.security;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import com.eastcom_sw.common.dao.jpa.Dao;
import com.eastcom_sw.core.entity.security.Department;


/**
 * 部门管理
 * @author SCM
 *
 */
public interface DepartmentDao extends Dao<Department> {
	/**
	 * 获取所有部门信息
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List queryAllDeptBasicInfo();
	
	/**通过部门名称查询*/
	@SuppressWarnings("rawtypes")
	public List searchDepartment(String name);
	
	public List<Object> queryDepartmentByName(String name);
	
	public List<Object> queryDepartmentByNameAndDeptIds(String name,String deptIds);
	
	/**删除数据项及其子项*/
	public void removeDepartments(List<String> deptIds);
	/**查找所有部门数据*/
	@SuppressWarnings("rawtypes")
	public List findAllDept();
	/**根据部门id获取部门信息*/
	@SuppressWarnings("rawtypes")
	public List queryDepartmentInfo(List ids);
	
	/**
	 * 通过名称或者中文名获取部门(暂用于判断该名称是否已存在)
	 * @param name	用户名或者中文名
	 * @param type	name:名称		nameCn：中文名
	 * @return	
	 */
	public long queryDeptByName(String name,String type);
	
	/**
	 * 批量更新部门关系（父部门以及order顺序）
	 * @param jsonString
	 */
	public void changeParentAndOrder(ArrayList<JSONObject> params);

	public List getDeptInfoByIds(String ids);

	public String findDepartmentHavaNoParent();

	public List<Department> findChildDepts(String deptids);
}
