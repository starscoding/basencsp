package com.eastcom_sw.core.service.security;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.eastcom_sw.common.service.BaseService;
import com.eastcom_sw.core.dao.security.UserDepartmentDao;
import com.eastcom_sw.core.utils.security.DepartmentTreeNode;

/**
 * 用户部门关系管理
 * @author JJF
 * @Date NOV 22 2012
 */
@Component
@Transactional(readOnly = true)
public class UserDepartmentService extends BaseService{
	@Autowired
	private UserDepartmentDao userDepartmentDao;
	
	/**
	 * 通过用户id获取该用户的权限部门树
	 * @param userId	用户id
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String getUserDeptTreeJson(String userId,String checkedFlag){
		List resultList = userDepartmentDao.getAllAccreditDeptByUser(userId);
		DepartmentTreeNode root = DepartmentTreeNode.makeTree(resultList);
		if("true".equals(checkedFlag)){
			return root.getTreeJson(true);
		}
		else{
			return root.getTreeJson(false);
		}
	}
	
	/**
	 * 获取用户授权树(用户管理-用户授权使用)
	 * @param userId	当前被授权用户id
	 * @param myId		当前登录用户的id
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String getAccreditDeptTreeJson(String userId,String myId){
		List userDeptList = userDepartmentDao.getAllAccreditDeptByUser(userId);
		List myDeptList = userDepartmentDao.getAllAccreditDeptByUser(myId);
		DepartmentTreeNode root = DepartmentTreeNode.makeAccreditTree(userDeptList, myDeptList);
		return root.getAccreditTreeJson();
	}
	
	/**
	 * 获取修改用户部门树(用户管理-编辑用户部门使用)
	 * @param userId	当前被修改用户id
	 * @param myId		当前登录用户的id
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String getChangeUserDeptTreeJson(String userId,String myId){
		List userDeptList = userDepartmentDao.getAllDeptByUser(userId);
		List myDeptList = userDepartmentDao.getAllAccreditDeptByUser(myId);
		DepartmentTreeNode root = DepartmentTreeNode.makeAccreditTree(userDeptList, myDeptList);
		return root.getAccreditTreeJson();
	}
	
	
	/**
	 * 获取当前部门下用户基本信息
	 * @param did	部门id
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List queryDeptUsers(String did){
		List rs = userDepartmentDao.queryDeptUsers(did);
		List<JSONObject> returnList  = new ArrayList<JSONObject>();
		for(Object o: rs){
			JSONArray ja = JSONArray.fromObject(o);
			JSONObject jo = new JSONObject();
			jo.put("id", ja.getString(0));
			jo.put("userNameCn", ja.getString(1));
			returnList.add(jo);
		}
		return returnList;
	}
	
	
}
