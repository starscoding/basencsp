package com.eastcom_sw.core.web.security;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springside.modules.mapper.BeanMapper;

import com.eastcom_sw.common.entity.ShiroUser;
import com.eastcom_sw.common.service.ServiceException;
import com.eastcom_sw.common.utils.CommonLogGenerator;
import com.eastcom_sw.common.utils.Constants;
import com.eastcom_sw.common.web.BaseController;
import com.eastcom_sw.core.entity.security.Role;
import com.eastcom_sw.core.entity.security.User;
import com.eastcom_sw.core.service.security.RoleService;
import com.eastcom_sw.core.service.security.UserService;
import com.eastcom_sw.core.web.SecurityBundle;
import com.eastcom_sw.security.extension.service.ResourceExtensionService;

/**
 * 角色管理
 * 
 * @author SCM
 * @time 2012-8-2
 */
@Controller
public class RoleController extends BaseController {

	@Autowired
	private RoleService roleService;

	@Autowired
	private UserService userService;

	@Autowired
	private ResourceExtensionService resourceExtensionService;

	/** 保存角色 */
	@RequestMapping(value = "/sysmng/security/saveRole", method = RequestMethod.POST)
	private void saveRole(HttpSession sesstion, HttpServletRequest request,
			HttpServletResponse response) {

		long lStart = Calendar.getInstance().getTimeInMillis();
		String id = request.getParameter("id") != null ? request
				.getParameter("id") : "";
		String name = request.getParameter("name");
		String nameCn = request.getParameter("nameCn");
		String description = request.getParameter("description");

		String moduleName = this.getModuleName(request);
		Role role = null;

		String opt = "0"; // 0:新增 1：修改

		/* log params */
		SecurityBundle.setLc(this.getI18NLocale());
		StringBuffer params = new StringBuffer();
		params.append(String.format(SecurityBundle.getString("role.name"), name));
		params.append(",");
		params.append(String.format(SecurityBundle.getString("role.label"),
				nameCn));
		params.append(",");
		params.append(String.format(SecurityBundle.getString("role.desc"),
				description));
		String logOptType = "";

		if (id.isEmpty()) {
			role = new Role();
			opt = "0";
			SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			role.setCreateTime(ft.format(new Date()));
			role.setCreator(this.getUser().getUserName());
			role.setName(name);
			logOptType = getCurrentRb().getString("opt.add");
		} else {
			opt = "1";
			role = roleService.findRole(id);
			logOptType = getCurrentRb().getString("opt.edit");
		}
		role.setNameCn(nameCn);
		role.setDescription(description);
		String opttype = opt.equals("0") ? Constants.SYSLOGSOPERATETYPE_ADD
				: Constants.SYSLOGSOPERATETYPE_EDIT;

		try {
			if (opt.equals("0")) {
				roleService.saveRole(role);
			} else {
				roleService.updateRole(role);
			}
			String rs = getCurrentRb().getString("rst.success");
			String log = CommonLogGenerator.genarateCommonLog(
					params.toString(), logOptType, logOptType, null, null, rs,
					getI18NLocale());
			saveSysLog(request, true, opttype, moduleName, log, (int) (Calendar
					.getInstance().getTimeInMillis() - lStart));
			String msg = opt.equals("0") ? getI18NReader().getAddSuccessMsg()
					: getI18NReader().getEditSuccessMsg();
			this.addResultInfo(SUCCESS, msg, opt.equals("0") ? role : null);
		} catch (ServiceException e) {
			e.printStackTrace();
			String msg = opt.equals("0") ? getI18NReader().getAddFailureMsg()
					: getI18NReader().getEditFailureMsg();
			String rs = getCurrentRb().getString("rst.failure");
			String log = CommonLogGenerator.genarateCommonLog(
					params.toString(), logOptType, logOptType, null,
					e.getMessage(), rs, getI18NLocale());
			saveSysLog(request, false, opttype, moduleName, log,
					(int) (Calendar.getInstance().getTimeInMillis() - lStart));
			this.addResultInfo(FAILURE, msg + e.getMessage(), null);
		}
		this.responseResult(response, this.getResultJSONStr());
	}

	/**
	 * 查找用户关联的所有角色列表
	 * 
	 * @param sesstion
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/sysmng/security/getAllRoleList", method = RequestMethod.GET)
	private void getAllRoleList(HttpSession sesstion,
			HttpServletRequest request, HttpServletResponse response) {
		ShiroUser u = getUser();
		
		String userNames = userService.getUserByDeptId(u.getId());
		List<JSONObject> list = roleService.queryAllRolesByUser(BeanMapper.map(
				u, User.class),userNames);
		this.responseResult(response, list.toString());
	}

	/**
	 * 获取所有角色
	 */
	@RequestMapping(value = "/sysmng/security/findAllRole", method = RequestMethod.GET)
	private void findAllRole(HttpSession session, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			List<Role> roles = roleService.findAllRole();
			this.addResultInfo(SUCCESS, "", roles);
		} catch (Exception e) {
			e.printStackTrace();
			this.addResultInfo(FAILURE, "", "");
		}
		this.responseResult(response, getResultJSONStr());
	}

	/**
	 * 角色重名验证
	 * 
	 * @param sesstion
	 * @param request
	 * @param response
	 * @param name
	 *            名称 （用户名或中文名）
	 * @param type
	 *            类型 (name:名称 nameCn：中文名)
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/sysmng/security/roleNameExsistCheck", method = RequestMethod.POST)
	public void roleNameExsistCheck(HttpSession sesstion,
			HttpServletRequest request, HttpServletResponse response) {
		String name = request.getParameter("name");
		String type = request.getParameter("type");
		boolean exsist = roleService.nameExsistCheck(name, type);
		String[] vals = { getI18NReader().OPT_OTHER,
				getI18NReader().RST_SUCCESS };
		this.addResultInfo(SUCCESS, getI18NReader().getReturnMsg(vals), exsist);
		this.responseResult(response, this.getResultJSONStr());
	}

	/**
	 * 获取用户授权列表数据(用户角色授权使用)
	 * 
	 * @param sesstion
	 * @param request
	 * @param response
	 * @param editUserId
	 *            被编辑用户id
	 */
	@RequestMapping(value = "/sysmng/security/getUserRoleAccreditData", method = RequestMethod.POST)
	public void getUserRoleAccreditData(HttpSession sesstion,
			HttpServletRequest request, HttpServletResponse response) {
		String editUserId = request.getParameter("editUserId");
		editUserId = editUserId == null ? "" : editUserId;
		try {
			List<JSONObject> returnList = null;
			ShiroUser currentUser = this.getUser();
			
			String userNames = userService.getUserByDeptId(currentUser.getId());
			
			returnList = roleService.getUserRoleAccreditData(
					BeanMapper.map(currentUser, User.class),
					userService.findByIds(editUserId.split(",")),userNames);
			if (returnList == null) {
				this.addResultInfo(FAILURE, "", null);// 多个账户之间的角色不统一，无法进行授权操作
			} else {
				this.addResultInfo(SUCCESS, getI18NReader()
						.getQuerySuccessMsg(), returnList);
			}
		} catch (Exception e) {
			this.addResultInfo(FAILURE, getI18NReader().getQueryFailureMsg()
					+ e.getMessage(), null);
			e.printStackTrace();
		}
		this.responseResult(response, this.getResultJSONStr());
	}

	/**
	 * 获取角色资源授权树（角色管理，单个角色资源授权用）
	 * 
	 * @param sesstion
	 * @param request
	 * @param response
	 * @param roleName
	 *            角色名称
	 * @param onlyShow
	 *            是否只显示不可编辑（true：返回的为查看当前角色权限数据 false：返回的为当前用户对当前角色的授权数据）
	 */
	@RequestMapping(value = "/sysmng/security/getRoleResourceAccreditTree", method = RequestMethod.POST)
	public void getRoleResourceAccreditTree(HttpSession sesstion,
			HttpServletRequest request, HttpServletResponse response) {
		String roleName = request.getParameter("roleName");
		String onlyShow = request.getParameter("onlyShow");
		try {
			String result = null;
			if (onlyShow.equals("true")) {
				result = roleService.getRoleResourceTree(roleName);
			} else {
				if (this.isSuperAdmin(getUser())) {
					result = roleService.getRoleResourceAccreditTree(null,
							roleName);
				} else {
					List<String> roleNames = this.getUserRoles();
					result = roleService.getRoleResourceAccreditTree(roleNames,
							roleName);
				}
			}
			this.responseResult(response, result);
		} catch (Exception e) {
			this.responseResult(response, getI18NReader().getQueryFailureMsg()
					+ e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 获取角色资源授权树（角色管理，多个角色资源授权用）
	 * 
	 **/
	@RequestMapping(value = "/sysmng/security/getRoleMultiAccreditTree", method = RequestMethod.POST)
	public void getRoleMultiAccreditTree(HttpSession sesstion,
			HttpServletRequest request, HttpServletResponse response) {
		String result = "";
		try {
			if (this.isSuperAdmin(getUser())) {
				result = roleService.getRoleResourceAccreditTree(null, "");
			} else {
				List<String> roleNames = this.getUserRoles();
				result = roleService.getRoleResourceAccreditTree(roleNames, "");
			}
			this.responseResult(response, result);
		} catch (Exception e) {
			e.printStackTrace();
			this.responseResult(response, getI18NReader().getQueryFailureMsg()
					+ e.getMessage());
		}
	}

	/**
	 * 更新角色资源权限
	 * 
	 * @param sesstion
	 * @param request
	 * @param response
	 * @param addIdStr
	 *            新增的资源id字符串（以“,”分割）
	 * @param delIdStr
	 *            失去权限的资源id字符串（以“,”分割）
	 * @param roleId
	 *            被编辑角色id字符串
	 */
	@RequestMapping(value = "/sysmng/security/updateRoleResourceRange", method = RequestMethod.POST)
	public void updateRoleResourceRange(HttpSession sesstion,
			HttpServletRequest request, HttpServletResponse response) {
		long lStart = Calendar.getInstance().getTimeInMillis();
		String addIdStr = request.getParameter("addIdStr");
		String delIdStr = request.getParameter("delIdStr");
		String roleId = request.getParameter("roleId");
		String roleName = request.getParameter("roleName");

		String moduleName = this.getModuleName(request);

		String[] addIds = {};
		String[] delIds = {};
		String[] roleIds = {};
		String[] roleNames = {};
		if (StringUtils.isNotBlank(addIdStr)) {
			addIds = addIdStr.split(",");
		}
		if (StringUtils.isNotBlank(delIdStr)) {
			delIds = delIdStr.split(",");
		}

		if (StringUtils.isNotBlank(roleId)) {
			roleIds = roleId.split(",");
		}
		if (StringUtils.isNotBlank(roleName)) {
			roleNames = roleName.split(",");
		}

		/* log params */
		SecurityBundle.setLc(this.getI18NLocale());
		StringBuffer params = new StringBuffer();
		params.append(String.format(SecurityBundle.getString("role.name"),
				roleName));
		params.append(",");
		params.append(String.format(SecurityBundle.getString("role.addres"),
				resourceExtensionService.translateResIdToFullNameString(addIds)));
		params.append(",");
		params.append(String.format(SecurityBundle.getString("role.removeres"),
				resourceExtensionService.translateResIdToFullNameString(delIds)));
		String optType = getCurrentRb().getString("opt.authorize");

		try {
			roleService.updateRoleResourceRange(roleIds, addIds, delIds,
					roleNames);
			this.addResultInfo(SUCCESS, getI18NReader()
					.getAuthorizeSuccessMsg(), null);
			String rs = getCurrentRb().getString("rst.success");
			String log = CommonLogGenerator.genarateCommonLog(
					params.toString(), optType, optType, null, null, rs,
					getI18NLocale());
			saveSysLog(request, true,
					Constants.SYSLOGSOPERATETYPE_UPDATE_MEMORYDATA, moduleName,
					log,
					(int) (Calendar.getInstance().getTimeInMillis() - lStart));
		} catch (Exception e) {
			String rs = getCurrentRb().getString("rst.failure");
			String log = CommonLogGenerator.genarateCommonLog(
					params.toString(), optType, optType, null, e.getMessage(),
					rs, getI18NLocale());
			saveSysLog(request, false,
					Constants.SYSLOGSOPERATETYPE_UPDATE_MEMORYDATA, moduleName,
					log,
					(int) (Calendar.getInstance().getTimeInMillis() - lStart));
			this.addResultInfo(SUCCESS, getI18NReader()
					.getAuthorizeFailureMsg() + e.getMessage(), null);
			e.printStackTrace();
		}
		this.responseResult(response, this.getResultJSONStr());
	}

	/**
	 * 获取角色区域授权列表数据
	 * 
	 * @param sesstion
	 * @param request
	 * @param response
	 * @param roleId
	 *            角色id
	 * @param onlyShow
	 *            是否只显示不可编辑（true：返回的为查看当前角色权限数据 false：返回的为当前用户对当前角色的授权数据）
	 */
	@RequestMapping(value = "/sysmng/security/queryRoleRegionAccreditGridData", method = RequestMethod.POST)
	public void queryRoleRegionAccreditGridData(HttpSession sesstion,
			HttpServletRequest request, HttpServletResponse response) {
		String roleId = request.getParameter("roleId");
		String onlyShow = request.getParameter("onlyShow");
		try {
			List<JSONObject> resultList = null;
			if (onlyShow.equals("true")) {
				User fakeUser = new User();// 模拟生成一个假的user
				fakeUser.setId("");
				fakeUser.setUserName("");
				fakeUser.setUserLevel("3");
				resultList = roleService.getRoleRegionAccreditGridData(
						fakeUser, roleId);
			} else {
				resultList = roleService.getRoleRegionAccreditGridData(
						BeanMapper.map(getUser(), User.class), roleId);
			}
			this.addResultInfo(SUCCESS, getI18NReader().getQuerySuccessMsg(),
					resultList);
		} catch (Exception e) {
			this.addResultInfo(FAILURE, getI18NReader().getQueryFailureMsg()
					+ e.getMessage(), null);
			e.printStackTrace();
		}
		this.responseResult(response, this.getResultJSONStr());
	}

	/**
	 * 更新角色地区权限
	 * 
	 * @param sesstion
	 * @param request
	 * @param response
	 * @param addIdStr
	 *            新增的区域代码字符串（以“,”分割）
	 * @param delIdStr
	 *            失去权限的区域代码字符串（以“,”分割）
	 * @param roleId
	 *            被编辑角色id字符串
	 */
	@RequestMapping(value = "/sysmng/security/updateRoleRegionRange", method = RequestMethod.POST)
	public void updateRoleRegionRange(HttpSession sesstion,
			HttpServletRequest request, HttpServletResponse response) {
		long lStart = Calendar.getInstance().getTimeInMillis();
		String addIdStr = request.getParameter("addIdStr");
		String delIdStr = request.getParameter("delIdStr");
		String roleId = request.getParameter("roleId");
		String roleName = request.getParameter("roleName");

		String moduleName = this.getModuleName(request);

		String addIds[] = {};
		if (addIdStr != null && addIdStr.length() > 0) {
			addIds = addIdStr.split(",");
		}
		String delIds[] = {};
		if (delIdStr != null && delIdStr.length() > 0) {
			delIds = delIdStr.split(",");
		}

		HashMap<String, String> regionMap = roleService
				.getCommonMultiFieldValue("region", "value", "", "label");
		StringBuffer addCns = new StringBuffer();
		StringBuffer delCns = new StringBuffer();
		for (String id : addIds) {
			addCns.append(regionMap.get(id) + ",");
		}
		for (String id : delIds) {
			delCns.append(regionMap.get(id) + ",");
		}
		if (addCns.length() > 0) {
			addCns.deleteCharAt(addCns.length() - 1);
		}
		if (delCns.length() > 0) {
			delCns.deleteCharAt(delCns.length() - 1);
		}

		/* log params */
		SecurityBundle.setLc(this.getI18NLocale());
		StringBuffer params = new StringBuffer();
		params.append(String.format(SecurityBundle.getString("role.name"),
				roleName));
		params.append(",");
		params.append(String.format(SecurityBundle.getString("role.addregion"),
				addCns.toString()));
		params.append(",");
		params.append(String.format(
				SecurityBundle.getString("role.removeregion"),
				delCns.toString()));
		String optType = getCurrentRb().getString("opt.authorize");

		try {
			roleService.updateRoleRegionRange(roleId, addIds, delIds);
			this.addResultInfo(SUCCESS, getI18NReader()
					.getAuthorizeSuccessMsg(), null);
			String rs = getCurrentRb().getString("rst.success");
			String log = CommonLogGenerator.genarateCommonLog(
					params.toString(), optType, optType, null, null, rs,
					getI18NLocale());
			saveSysLog(request, true,
					Constants.SYSLOGSOPERATETYPE_UPDATE_MEMORYDATA, moduleName,
					log,
					(int) (Calendar.getInstance().getTimeInMillis() - lStart));
		} catch (Exception e) {
			String rs = getCurrentRb().getString("rst.failure");
			String log = CommonLogGenerator.genarateCommonLog(
					params.toString(), optType, optType, null, e.getMessage(),
					rs, getI18NLocale());
			saveSysLog(request, false,
					Constants.SYSLOGSOPERATETYPE_UPDATE_MEMORYDATA, moduleName,
					log,
					(int) (Calendar.getInstance().getTimeInMillis() - lStart));
			this.addResultInfo(SUCCESS, getI18NReader()
					.getAuthorizeFailureMsg() + e.getMessage(), null);
			e.printStackTrace();
		}
		this.responseResult(response, this.getResultJSONStr());
	}

	/**
	 * 批量删除角色（当前前台只传单个id）
	 * 
	 * @param sesstion
	 * @param request
	 * @param response
	 * @param idStr
	 *            被删除的id字符串（以“，”分割）
	 */
	@RequestMapping(value = "/sysmng/security/deleteRole", method = RequestMethod.POST)
	public void deleteRole(HttpSession sesstion, HttpServletRequest request,
			HttpServletResponse response) {
		long lStart = Calendar.getInstance().getTimeInMillis();
		String idStr = request.getParameter("idStr");
		String nameStr = request.getParameter("nameStr");

		String moduleName = this.getModuleName(request);

		List<String> roleIds = new ArrayList<String>();
		String idSplit[] = idStr.split(",");
		for (String id : idSplit) {
			roleIds.add(id);
		}

		String roleNames[] = nameStr.split(",");
		/* log params */
		SecurityBundle.setLc(this.getI18NLocale());
		StringBuffer params = new StringBuffer();
		params.append(String.format(SecurityBundle.getString("dept.name"),
				nameStr));
		String optType = getCurrentRb().getString("opt.delete");

		try {
			roleService.deleteRole(roleIds, roleNames);
			this.addResultInfo(SUCCESS, getI18NReader().getDeleteSuccessMsg(),
					null);
			String rs = getCurrentRb().getString("rst.success");
			String log = CommonLogGenerator.genarateCommonLog(
					params.toString(), optType, optType, null, null, rs,
					getI18NLocale());
			saveSysLog(request, true, Constants.SYSLOGSOPERATETYPE_DELETE,
					moduleName, log, (int) (Calendar.getInstance()
							.getTimeInMillis() - lStart));
		} catch (Exception e) {
			this.addResultInfo(FAILURE, getI18NReader().getDeleteFailureMsg()
					+ e.getMessage(), e.getMessage());
			String rs = getCurrentRb().getString("rst.failure");
			String log = CommonLogGenerator.genarateCommonLog(
					params.toString(), optType, optType, null, e.getMessage(),
					rs, getI18NLocale());
			saveSysLog(request, false, Constants.SYSLOGSOPERATETYPE_DELETE,
					moduleName, log + e.getMessage(), (int) (Calendar
							.getInstance().getTimeInMillis() - lStart));
			e.printStackTrace();
		}
		this.responseResult(response, this.getResultJSONStr());
	}

	/**
	 * 通过角色id获取该角色下的用户信息(返回用户中文名和所有部门)
	 * 
	 * @param sesstion
	 * @param request
	 * @param response
	 * @param roleId
	 *            角色id
	 */
	@RequestMapping(value = "/sysmng/security/queryUserInfoByRole", method = RequestMethod.POST)
	public void queryUserInfoByRole(HttpSession sesstion,
			HttpServletRequest request, HttpServletResponse response) {
		String roleId = request.getParameter("roleId");
		try {
			this.addResultInfo(SUCCESS, getI18NReader().getQuerySuccessMsg(),
					roleService.queryUserInfoByRole(roleId));
		} catch (Exception e) {
			this.addResultInfo(FAILURE, getI18NReader().getQueryFailureMsg()
					+ e.getMessage(), null);
			e.printStackTrace();
		}
		this.responseResult(response, this.getResultJSONStr());
	}
}
