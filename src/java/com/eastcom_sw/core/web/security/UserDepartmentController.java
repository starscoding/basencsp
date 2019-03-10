package com.eastcom_sw.core.web.security;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.eastcom_sw.common.entity.ShiroUser;
import com.eastcom_sw.common.web.BaseController;
import com.eastcom_sw.core.service.security.UserDepartmentService;

@Controller
public class UserDepartmentController extends BaseController{
	@Autowired
	private UserDepartmentService userDepartmentService;
	
	/**
	 * 获取用户拥有权限的部门及其子部门信息(一次性取出整个权限部门树)
	 * 
	 * @param sesstion
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/sysmng/security/getDepartmentByUser", method = RequestMethod.POST)
	private void getDepartmentByUser(HttpSession sesstion,
			HttpServletRequest request, HttpServletResponse response) {
		String userId = request.getParameter("userId");
		String checkedFlag = request.getParameter("checkedFlag");
		String result = "";
		if (userId == null || userId.length() == 0) {
			ShiroUser currentUser = this.getUser();
			if (currentUser.getUserLevel().equals("1")) {
				result = userDepartmentService.getUserDeptTreeJson("", checkedFlag);
			} else {
				result = userDepartmentService.getUserDeptTreeJson(
						currentUser.getId(), checkedFlag);
			}
		} else {
			result = userDepartmentService.getUserDeptTreeJson(userId, checkedFlag);
		}
		this.responseResult(response, result);
	}
	
	/**
	 * 获取修改用户所属部门树(用户管理用)
	 * 
	 * @param sesstion
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/sysmng/security/getChangeDepartmentTreeByUser", method = RequestMethod.POST)
	public void getChangeDepartmentTreeByUser(HttpSession sesstion,
			HttpServletRequest request, HttpServletResponse response) {
		String userId = request.getParameter("userId");// 当前被编辑的用户的id
		ShiroUser currentUser = this.getUser();
		String result = "";
		if (currentUser.getUserLevel().equals("1")) {
			result = userDepartmentService.getChangeUserDeptTreeJson(userId, "");
		} else {
			result = userDepartmentService.getChangeUserDeptTreeJson(userId,
					currentUser.getId());
		}
		this.responseResult(response, result);
	}
	
	
	/**
	 * 获取用户部门授权树(一次性取出整个权限部门树,用户管理用)
	 * 
	 * @param sesstion
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/sysmng/security/getDepartmentAccreditTree", method = RequestMethod.POST)
	public void getDepartmentAccreditTree(HttpSession sesstion,
			HttpServletRequest request, HttpServletResponse response) {
		String userId = request.getParameter("userId");
		ShiroUser currentUser = this.getUser();
		String result = "";
		if (currentUser.getUserLevel().equals("1")) {
			if (userId.equals(currentUser.getId())) {
				result = userDepartmentService.getAccreditDeptTreeJson("", "");
			} else {
				result = userDepartmentService.getAccreditDeptTreeJson(userId, "");
			}
		} else {
			result = userDepartmentService.getAccreditDeptTreeJson(userId,
					currentUser.getId());
		}
		this.responseResult(response, result);
	}
	
	/**
	 * 获取部门下 的用户信息
	 * @param sesstion
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/sysmng/security/queryDeptUsers", method = RequestMethod.POST)
	public void queryDeptUsers(HttpSession sesstion,
			HttpServletRequest request, HttpServletResponse response){
		String id = request.getParameter("id");
		List result;
		try {
			result = userDepartmentService.queryDeptUsers(id);
			this.addResultInfo(SUCCESS, getI18NReader().getQuerySuccessMsg(), result);
		} catch (Exception e) {
			this.addResultInfo(FAILURE, getI18NReader().getQueryFailureMsg()+e.getMessage(), null);
			e.printStackTrace();
		}
		this.responseResult(response, this.getResultJSONStr());
	}
}
