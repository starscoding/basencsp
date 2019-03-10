package com.eastcom_sw.core.web.security;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springside.modules.mapper.BeanMapper;

import com.eastcom_sw.common.entity.CommonObject;
import com.eastcom_sw.common.entity.NameValuePair;
import com.eastcom_sw.common.entity.Page;
import com.eastcom_sw.common.entity.ShiroUser;
import com.eastcom_sw.common.utils.CommonLogGenerator;
import com.eastcom_sw.common.utils.CommonUtil;
import com.eastcom_sw.common.utils.Constants;
import com.eastcom_sw.common.utils.DateUtil;
import com.eastcom_sw.common.utils.ExcelReader;
import com.eastcom_sw.common.utils.PasswordUtil;
import com.eastcom_sw.common.utils.RandomPasswordUtil;
import com.eastcom_sw.common.web.BaseController;
import com.eastcom_sw.core.entity.security.Department;
import com.eastcom_sw.core.entity.security.Role;
import com.eastcom_sw.core.entity.security.User;
import com.eastcom_sw.core.entity.security.UserExtensionConfiguration;
import com.eastcom_sw.core.service.security.DepartmentService;
import com.eastcom_sw.core.service.security.SystemUserService;
import com.eastcom_sw.core.service.security.UserService;
import com.eastcom_sw.core.web.SecurityBundle;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 用户管理
 * 
 * @author SCM 2012-8-24
 */
@Controller
public class UserController extends BaseController {

	@Autowired
	private UserService userService;

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private SystemUserService systemUserService;

	/**
	 * 获取所有用户信息
	 * 
	 * @param sesstion
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/sysmng/security/getAllUserInfo", method = RequestMethod.POST)
	private void getAllUserInfo(HttpSession sesstion, HttpServletRequest request, HttpServletResponse response) {
		String result = userService.getAllUser();
		this.responseResult(response, result);
	}

	/**
	 * 根据汉字或者简拼搜索用户(只返回id，中文名，部门名称几个字段)
	 * 
	 * @param sesstion
	 * @param request
	 * @param response
	 * @param input
	 *            汉字或者简拼关键字
	 */
	@RequestMapping(value = "/sysmng/security/searchUser", method = RequestMethod.POST)
	private void searchUser(HttpSession sesstion, HttpServletRequest request, HttpServletResponse response) {
		String input = request.getParameter("input");
		String result = "";
		if (input == null || input.equals("")) {
			result = userService.getAllUser();
		} else {
			result = userService.searchUser(input);
		}
		this.responseResult(response, result);
	}

	/**
	 * 获取用户管理列表所需的字段数据
	 * 
	 * @param sesstion
	 * @param request
	 * @param response
	 * @param input
	 *            汉字或者简拼或者全拼关键字
	 * @param deptId
	 *            所属部门id集合字符串（以“，”分割）
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/sysmng/security/queryUser", method = RequestMethod.POST)
	private void queryUser(HttpSession sesstion, HttpServletRequest request, HttpServletResponse response) {
		String moduleName = this.getModuleName(request);
		long lStart = Calendar.getInstance().getTimeInMillis();
		String input = request.getParameter("input");
		// 以下为新增查询条件，创建时间，是否授权
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String havePermission = request.getParameter("havePermission");

		String deptId = request.getParameter("deptId");
		int start = Integer.parseInt(request.getParameter("start"));
		int limit = Integer.parseInt(request.getParameter("limit"));
		List<String> deptIds = new ArrayList<String>();
		if (deptId != null && deptId.length() > 0) {
			for (String did : deptId.split(",")) {
				deptIds.add(did);
			}
		} else {
			deptId = "";
		}
		HashMap<String, String> userLevel = userService.getCommonMultiFieldValue("userLevel", "value", "1,2,3", "label");
		HashMap<String, String> accountEnabled = userService.getCommonMultiFieldValue("accountEnabled", "value", "0,1",	"label");
		//ShiroUser currentUser = this.getUser();
		/* log params */
		SecurityBundle.setLc(this.getI18NLocale());
		StringBuffer params = new StringBuffer();
		params.append(String.format(SecurityBundle.getString("user.querykey"), input));

		String logOptType = getCurrentRb().getString("opt.query");
		
		Map<String,String> paramMap = new HashMap<String,String>();
		try {
			paramMap.put("excludeSelf", request.getParameter("excludeSelf"));
			paramMap.put("selUserName", request.getParameter("selUserName"));
			
			Page result = userService.queryUser(start, limit, input, deptIds, startTime, endTime, havePermission, paramMap);
			List resultList = result.getElements();
			List<JSONObject> records = new ArrayList<JSONObject>();
			if (resultList != null && resultList.size() > 0) {
				for (Object o : resultList) {
					JSONArray ja = JSONArray.fromObject(o);
					//boolean addFlag = false;
					// if (currentUser.getId().equals(ja.getString(0)) == false)
					// {// 不返回当前登录用户
					// addFlag = true;
					// } else if (currentUser.getUserLevel().equals("1") == true
					// && ja.getString(3).equals("1") == true) {//
					// 若当前用户为管理员则返回管理员用户
					// addFlag = true;
					// }

					// if(currentUser.getUserLevel().equals("1")){//超级管理员可以看见所有的用户
					// addFlag = true;
					// }else{//普通管理员只能看见一般管理员 普通管理员用户 包括自己 都看不见
					// if(ja.getString(3).equals("3")){
					// addFlag = true;
					// }
					// }

					// if (addFlag) {
					JSONObject jo = new JSONObject();
					jo.put("id", ja.get(0));
					jo.put("userName", ja.get(1));
					jo.put("fullName", ja.get(2));
					jo.put("userLevel", userLevel.get(ja.get(3)));
					jo.put("accountEnabled", accountEnabled.get(ja.get(4)));
					jo.put("creator", ja.get(5));
					jo.put("accoutCreateTime", ja.get(6));
					jo.put("accoutExpiredStarttime", ja.get(7));
					jo.put("accoutExpiredEndtime", ja.get(8));
					jo.put("deptId", ja.get(9));
					jo.put("deptName", ja.get(10));
					records.add(jo);
					// }
				}
				result.setElements(records);
				//result.setTotal(records.size());
			}

			String rs = getCurrentRb().getString("rst.success");
			String log = CommonLogGenerator.genarateCommonLog(params.toString(), logOptType, logOptType,
					result.getTotal(), null, rs, getI18NLocale());
			saveSysLog(request, true, Constants.SYSLOGSOPERATETYPE_QUERY, moduleName, log,
					(int) (Calendar.getInstance().getTimeInMillis() - lStart));
			this.addResultInfo(SUCCESS, getI18NReader().getQuerySuccessMsg(), result);
		} catch (Exception e) {
			e.printStackTrace();
			String rs = getCurrentRb().getString("rst.failure");
			String log = CommonLogGenerator.genarateCommonLog(params.toString(), logOptType, logOptType, null, e.getMessage(), rs, getI18NLocale());
			saveSysLog(request, false, Constants.SYSLOGSOPERATETYPE_QUERY, moduleName, log,
					(int) (Calendar.getInstance().getTimeInMillis() - lStart));
			this.addResultInfo(FAILURE, getI18NReader().getQueryFailureMsg() + e.getMessage(), null);
		}
		this.responseResult(response, this.getResultJSONStr());
	}

	/**
	 * 保存用户信息
	 * 
	 * @param sesstion
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/sysmng/security/saveUser", method = RequestMethod.POST)
	public void saveUser(HttpSession sesstion, HttpServletRequest request, HttpServletResponse response) {
		String moduleName = this.getModuleName(request);
		long lStart = Calendar.getInstance().getTimeInMillis();
		String id = request.getParameter("id");
		String username = request.getParameter("username");
		String sex = request.getParameter("sex");
		String password = request.getParameter("password");
		int passwordExpridDays = Integer.parseInt(request.getParameter("passwordExpridDays"));
		String userLevel = request.getParameter("userLevel");
		String userDeptId = request.getParameter("userDeptId");
		String deptIds[] = userDeptId.split(",");
		String fullName = request.getParameter("fullName");
		String owner = request.getParameter("owner");
		String version = request.getParameter("version");
		String email = request.getParameter("email");
		String telNo = request.getParameter("telNo");
		String mobileNo = request.getParameter("mobileNo");
		String accoutExpiredStarttime = request.getParameter("accoutExpiredStarttime");
		String accoutExpiredEndtime = request.getParameter("accoutExpiredEndtime");
		String city = request.getParameter("city");
		HashMap<String, String> userLevelMap = userService.getCommonMultiFieldValue("userLevel", "value", "1,2,3", "label");
		HashMap<String, String> accountEnabledMap = userService.getCommonMultiFieldValue("accountEnabled", "value",	"0,1", "label");
		HashSet<Department> dd = new HashSet<Department>();
		for (String did : deptIds) {
			Department d = departmentService.findDeptment(did);
			dd.add(d);
		}
		String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

		/* log params */
		SecurityBundle.setLc(this.getI18NLocale());
		StringBuffer params = new StringBuffer();
		params.append(String.format(SecurityBundle.getString("user.username"), username));
		params.append(",");
		params.append(String.format(SecurityBundle.getString("user.fullname"), fullName));
		params.append(",");
		params.append(String.format(SecurityBundle.getString("user.email"), email));
		params.append(",");
		params.append(String.format(SecurityBundle.getString("user.mobile"), mobileNo));
		params.append(",");
		params.append(String.format(SecurityBundle.getString("user.userlevel"), userLevelMap.get(userLevel)));
		params.append(",");
		params.append(String.format(SecurityBundle.getString("user.sex"), sex));

		UserExtensionConfiguration cfg = userService.getUserExtensionConfiguration();
		String[] extFields = cfg.getFields();
		Map<String, String> extMap = new HashMap<String, String>();
		if (cfg.isEnabled()) {
			for (String f : extFields) {
				extMap.put(f, request.getParameter(f));
			}
		}
		String ValidMsg = "";
		if (StringUtils.isNotBlank(id)) {
			String logOptType = getCurrentRb().getString("opt.edit");
			User u = userService.findUser(id);
			if (!password.equals("******")) {
				ValidMsg = PasswordUtil.validatePwd(username, password, fullName);
				if(ValidMsg == null || ValidMsg.length() == 0){
					password = CommonUtil.encodePassword(password);// 加密密码
					if (!u.getPassWrod().equals(password)) {
						if (u.getOldPassord() == null || u.getOldPassord().isEmpty()) {
							u.setOldPassord(u.getPassWrod());
						} else {
							if (u.getOldPassord().indexOf("|") != u.getOldPassord().lastIndexOf("|")) {
								u.setOldPassord(u.getOldPassord().substring(u.getOldPassord().indexOf("|") + 1) + "|" + u.getPassWrod());
							} else {
								u.setOldPassord(u.getOldPassord() + "|" + u.getPassWrod());
							}
						}
						u.setPassWrod(password);
						u.setPwdModifyTime(nowTime);
					}
					// 强制修改密码之后将解除锁定状态
					systemUserService.unLockedUser(u.getUserName());
				}				
			}
			
			if(ValidMsg != null && ValidMsg.length() > 0){
				this.addResultInfo(FAILURE, getI18NReader().getUpdateFailureMsg() + ValidMsg, null);
			}else{
				u.setSex(sex);
				u.setPwdExpiredDays(passwordExpridDays);
				u.setUserLevel(userLevel);
				u.setFullName(fullName);
				u.setOwner(owner);
				if (version != null && version.equals("") == false) {
					u.setVersion(Integer.parseInt(version));
				}
				u.setEmail(email);
				u.setFixedNo(telNo);
				u.setMobileNo(mobileNo);
				u.setAccoutExpiredStarttime(accoutExpiredStarttime);
				u.setAccoutExpiredEndtime(accoutExpiredEndtime);
				u.setCity(city);
				u.setDepartments(dd);
				// u.setRoles(rr);
				try {
					userService.updateUser(u, extMap);
					this.addResultInfo(SUCCESS, getI18NReader().getUpdateSuccessMsg(), null);
					String rs = getCurrentRb().getString("rst.success");
					String log = CommonLogGenerator.genarateCommonLog(params.toString(), logOptType, logOptType, null, null, rs, getI18NLocale());
					saveSysLog(request, true, Constants.SYSLOGSOPERATETYPE_UPDATE_MEMORYDATA, moduleName, log,
							(int) (Calendar.getInstance().getTimeInMillis() - lStart));
				} catch (Exception e) {
					this.addResultInfo(FAILURE, getI18NReader().getUpdateFailureMsg(), null);
					String rs = getCurrentRb().getString("rst.failure");
					String log = CommonLogGenerator.genarateCommonLog(params.toString(), logOptType, logOptType, null,
							e.getMessage(), rs, getI18NLocale());
					saveSysLog(request, false, Constants.SYSLOGSOPERATETYPE_UPDATE_MEMORYDATA, moduleName, log,
							(int) (Calendar.getInstance().getTimeInMillis() - lStart));
					e.printStackTrace();
				}
			}
		} else {
			String logOptType = getCurrentRb().getString("opt.add");
			User u = new User();			
			if (password.equals("******")) {
				password = RandomPasswordUtil.getMixPassword(6);
			}else{
				ValidMsg = PasswordUtil.validatePwd(username, password, fullName);
			}
			if(ValidMsg != null && ValidMsg.length() > 0){
				this.addResultInfo(FAILURE, getI18NReader().getAddFailureMsg() + ValidMsg, null);
			}else{
				password = CommonUtil.encodePassword(password);// 加密密码
				u.setUserName(username);
				u.setPassWrod(password);
				// u.setOldPassord(password);
				u.setSex(sex);
				u.setPwdExpiredDays(passwordExpridDays);
				u.setUserLevel(userLevel);
				u.setFullName(fullName);
				u.setOwner(owner);
				if (version != null && version.equals("") == false) {
					u.setVersion(Integer.parseInt(version));
				}
				u.setEmail(email);
				u.setFixedNo(telNo);
				u.setMobileNo(mobileNo);
				u.setAccoutExpiredStarttime(accoutExpiredStarttime);
				u.setAccoutExpiredEndtime(accoutExpiredEndtime);
				u.setCity(city);
				u.setAccountEnabled("1");
				u.setTimes(0);
				u.setIsShowmaxtab("0");
				u.setPwdModifyTime(nowTime);
				u.setAccoutCreateTime(nowTime);
				// 改为保存英文名
				u.setCreator(this.getUser().getUserName());
				u.setDepartments(dd);
				// u.setRoles(rr);
				try {
					userService.saveUser(u, extMap);
					u.setDepartments(null);
					u.setUserLevel(userLevelMap.get(userLevel));
					u.setAccountEnabled(accountEnabledMap.get("1"));
					this.addResultInfo(SUCCESS, fullName + ":" + getI18NReader().getAddSuccessMsg(), u);
					String rs = getCurrentRb().getString("rst.success");
					String log = CommonLogGenerator.genarateCommonLog(params.toString(), logOptType, logOptType, null, null, rs, getI18NLocale());
					saveSysLog(request, true, Constants.SYSLOGSOPERATETYPE_ADD, moduleName, log,
							(int) (Calendar.getInstance().getTimeInMillis() - lStart));
				} catch (Exception e) {
					this.addResultInfo(FAILURE, getI18NReader().getAddFailureMsg() + e.getMessage(), null);
					String rs = getCurrentRb().getString("rst.failure");
					String log = CommonLogGenerator.genarateCommonLog(params.toString(), logOptType, logOptType, null, e.getMessage(), rs, getI18NLocale());
					saveSysLog(request, false, Constants.SYSLOGSOPERATETYPE_ADD, moduleName, log, (int) (Calendar.getInstance().getTimeInMillis() - lStart));
					e.printStackTrace();
				}
			}			
		}
		this.responseResult(response, this.getResultJSONStr());
	}

	/**
	 * 返回单个用户具体信息
	 * 
	 * @param sesstion
	 * @param request
	 * @param response
	 * @param id
	 *            用户id
	 * @param chineseFlag
	 *            是否返回中文信息
	 */
	@RequestMapping(value = "/sysmng/security/querySingleUserInfo", method = RequestMethod.POST)
	public void querySingleUserInfo(HttpSession sesstion, HttpServletRequest request, HttpServletResponse response) {
		String moduleName = this.getModuleName(request);
		long lStart = Calendar.getInstance().getTimeInMillis();
		String id = request.getParameter("id");
		String chineseFlag = request.getParameter("chineseFlag");// 是否返回中文value
		String basicFlag = request.getParameter("basicFlag");// 是否只返回基础字段
		String extensionFlag = request.getParameter("extensionFlag");// 是否返回用户扩展字段
		try {
			JSONObject u = userService.querySingleUser(id, basicFlag);
			if (chineseFlag.equals("true")) {
				HashMap<String, String> userLevel = userService.getCommonMultiFieldValue("userLevel", "value", "1,2,3", "label");
				HashMap<String, String> accountEnabled = userService.getCommonMultiFieldValue("accountEnabled", "value", "0,1", "label");
				HashMap<String, String> sex = userService.getCommonMultiFieldValue("sex", "value", "0,1", "label");
				u.put("userLevel", userLevel.get(u.get("userLevel")));
				u.put("accountEnabled", accountEnabled.get(u.get("accountEnabled")));
				u.put("sex", sex.get(u.get("sex")));
				try {
					HashMap<String, String> city = userService.getCommonMultiFieldValue("region", "value", "", "label");
					u.put("city", city.get(u.get("city")));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if ("true".equals(extensionFlag)) {
				Map<NameValuePair, String> ext = userService.queryUserExtension(id);
				List<JSONObject> exts = new ArrayList<JSONObject>();
				for (NameValuePair p : ext.keySet()) {
					JSONObject jo = new JSONObject();
					jo.put("name", p.getName());
					jo.put("nameCn", p.getValue());
					jo.put("value", ext.get(p));
					exts.add(jo);
				}
				u.put("extension", exts);
			}
			this.addResultInfo(SUCCESS, getI18NReader().getQuerySuccessMsg(), u);
			if (basicFlag != null && basicFlag.equals("true") == false) {
				/* log params */
				SecurityBundle.setLc(this.getI18NLocale());
				StringBuffer params = new StringBuffer();
				String logOptType = getCurrentRb().getString("opt.query");
				params.append(String.format(SecurityBundle.getString("user.username"), u.get("username")));
				params.append(String.format(SecurityBundle.getString("user.fullname"), u.get("fullName")));
				String rs = getCurrentRb().getString("rst.success");
				String log = CommonLogGenerator.genarateCommonLog(params.toString(), logOptType,
						SecurityBundle.getString("user.queryuserdetail"), null, null, rs, getI18NLocale());
				saveSysLog(request, true, Constants.SYSLOGSOPERATETYPE_QUERY, moduleName, log,
						(int) (Calendar.getInstance().getTimeInMillis() - lStart));
			}
		} catch (Exception e) {
			this.addResultInfo(FAILURE, getI18NReader().getQueryFailureMsg() + e.getMessage(), null);
			if (basicFlag != null && basicFlag.equals("true") == false) {
				/* log params */
				SecurityBundle.setLc(this.getI18NLocale());
				StringBuffer params = new StringBuffer();
				String logOptType = getCurrentRb().getString("opt.query");
				params.append(String.format(SecurityBundle.getString("user.id"), id));
				String rs = getCurrentRb().getString("rst.failure");
				String log = CommonLogGenerator.genarateCommonLog(params.toString(), logOptType,
						SecurityBundle.getString("user.queryuserdetail"), null, e.getMessage(), rs, getI18NLocale());
				saveSysLog(request, false, Constants.SYSLOGSOPERATETYPE_QUERY, moduleName, log,
						(int) (Calendar.getInstance().getTimeInMillis() - lStart));
			}
			e.printStackTrace();
		}
		this.responseResult(response, this.getResultJSONStr());
	}

	/**
	 * 获取用户扩展字段配置
	 */
	@RequestMapping(value = "/sysmng/security/queryUserExtCfg", method = RequestMethod.POST)
	private void queryUserExtCfg(HttpSession sesstion, HttpServletRequest request, HttpServletResponse response) {
		try {
			UserExtensionConfiguration cfg = userService.getUserExtensionConfiguration();
			this.addResultInfo(SUCCESS, "", cfg);
		} catch (Exception e) {
			e.printStackTrace();
			this.addResultInfo(FAILURE, "", "");
		}
		this.responseResult(response, this.getResultJSONStr());
	}

	/**
	 * 修改用户关联权限部门
	 * 
	 * @param sesstion
	 * @param request
	 * @param response
	 * @param addIdStr
	 *            新增权限部门id集合字符串
	 * @param delIdStr
	 *            失去权限的部门id集合字符串
	 * @param editUserId
	 *            被编辑用户id
	 */
	@RequestMapping(value = "/sysmng/security/changeUserDeptRange", method = RequestMethod.POST)
	public void changeUserDeptRange(HttpSession sesstion, HttpServletRequest request, HttpServletResponse response) {
		// String moduleName = this.getModuleName(request);
		// long lStart = Calendar.getInstance().getTimeInMillis();
		String addIdStr = request.getParameter("addIdStr");
		String delIdStr = request.getParameter("delIdStr");
		String editUserId = request.getParameter("editUserId");
		String addIds[] = null;
		if (addIdStr != null && addIdStr.length() > 0) {
			addIds = addIdStr.split(",");
		}
		String delIds[] = null;
		if (delIdStr != null && delIdStr.length() > 0) {
			delIds = delIdStr.split(",");
		}
		try {
			userService.changeUserDeptRange(editUserId, addIds, delIds);
			this.addResultInfo(SUCCESS, getI18NReader().getAuthorizeSuccessMsg(), null);
			// saveSysLog(request, true, Constants.SYSLOGSOPERATETYPE_EDIT,
			// moduleName, "修改用户【" + editUserId + "】权限部门成功！增加权限部门:"
			// + addIdStr + "--删除权限部门:" + delIdStr,
			// (int) (Calendar.getInstance().getTimeInMillis() - lStart));
		} catch (Exception e) {
			// saveSysLog(request, false, Constants.SYSLOGSOPERATETYPE_EDIT,
			// moduleName,
			// "修改用户【" + editUserId + "】权限部门失败！" + e.getMessage(),
			// (int) (Calendar.getInstance().getTimeInMillis() - lStart));
			// this.addResultInfo(FAILURE, getI18NReader()
			// .getAuthorizeFailureMsg() + e.getMessage(), null);
			e.printStackTrace();
		}
		this.responseResult(response, this.getResultJSONStr());
	}

	/**
	 * 修改用户关联权限角色
	 * 
	 * @param sesstion
	 * @param request
	 * @param response
	 * @param addIdStr
	 *            新增权限角色id集合字符串
	 * @param delIdStr
	 *            失去权限的角色id集合字符串
	 * @param editUserId
	 *            被编辑用户id
	 */
	@RequestMapping(value = "/sysmng/security/changeUserRoleRange", method = RequestMethod.POST)
	public void changeUserRoleRange(HttpSession sesstion, HttpServletRequest request, HttpServletResponse response) {
		String moduleName = this.getModuleName(request);
		long lStart = Calendar.getInstance().getTimeInMillis();
		String addIdStr = request.getParameter("addIdStr");
		String delIdStr = request.getParameter("delIdStr");
		String addNamesStr = request.getParameter("addNamesStr");
		String delNamesStr = request.getParameter("delNamesStr");
		String addNameCnsStr = request.getParameter("addNameCnsStr");
		String delNameCnsStr = request.getParameter("delNameCnsStr");
		String editUserId = request.getParameter("editUserId");
		String editUserFullName = request.getParameter("editUserFullName");
		String addIds[] = {}, addNames[] = {};
		if (addIdStr != null && addIdStr.length() > 0) {
			addIds = addIdStr.split(",");
		}
		if (addNamesStr != null && addNamesStr.length() > 0) {
			addNames = addNamesStr.split(",");
		}
		String delIds[] = {}, delNames[] = {};
		if (delIdStr != null && delIdStr.length() > 0) {
			delIds = delIdStr.split(",");
		}
		if (delNamesStr != null && delNamesStr.length() > 0) {
			delNames = delNamesStr.split(",");
		}

		/* log params */
		SecurityBundle.setLc(this.getI18NLocale());
		StringBuffer params = new StringBuffer();
		// JSONObject u = userService.querySingleUser(editUserId, "true");
		params.append(String.format(SecurityBundle.getString("user.fullname"), editUserFullName));
		if (StringUtils.isNotBlank(addNameCnsStr)) {
			params.append("---");
			params.append(String.format(SecurityBundle.getString("user.addroles"), addNameCnsStr));
		}
		if (StringUtils.isNotBlank(delNameCnsStr)) {
			params.append("---");
			params.append(String.format(SecurityBundle.getString("user.removeroles"), delNameCnsStr));
		}
		String optType = getCurrentRb().getString("opt.update");

		try {
			userService.changeUserRoleRange(editUserId, addIds, delIds, addNames, delNames);
			this.addResultInfo(SUCCESS, getI18NReader().getAuthorizeSuccessMsg(), null);
			String rs = getCurrentRb().getString("rst.success");
			String log = CommonLogGenerator.genarateCommonLog(params.toString(), optType, optType, null, null, rs, getI18NLocale());
			saveSysLog(request, true, Constants.SYSLOGSOPERATETYPE_EDIT, moduleName, log,
					(int) (Calendar.getInstance().getTimeInMillis() - lStart));
		} catch (Exception e) {
			String rs = getCurrentRb().getString("rst.failure");
			String log = CommonLogGenerator.genarateCommonLog(params.toString(), optType, optType, null, e.getMessage(), rs, getI18NLocale());
			saveSysLog(request, false, Constants.SYSLOGSOPERATETYPE_EDIT, moduleName, log,
					(int) (Calendar.getInstance().getTimeInMillis() - lStart));
			this.addResultInfo(SUCCESS, getI18NReader().getAuthorizeFailureMsg() + e.getMessage(), null);
			e.printStackTrace();
		}
		this.responseResult(response, this.getResultJSONStr());
	}

	/**
	 * 批量删除用户
	 * 
	 * @param sesstion
	 * @param request
	 * @param response
	 * @param idStr
	 *            被删除的用户id集合
	 */
	@RequestMapping(value = "/sysmng/security/deleteUser", method = RequestMethod.POST)
	public void deleteUser(HttpSession sesstion, HttpServletRequest request, HttpServletResponse response) {
		String moduleName = this.getModuleName(request);
		long lStart = Calendar.getInstance().getTimeInMillis();
		String idStr = request.getParameter("idStr");
		String usernames = request.getParameter("usernames");
		List<String> userIds = new ArrayList<String>();
		String idSplit[] = idStr.split(",");
		for (String id : idSplit) {
			userIds.add(id);
		}
		/* log params */
		SecurityBundle.setLc(this.getI18NLocale());
		StringBuffer params = new StringBuffer();
		params.append(String.format(SecurityBundle.getString("user.username"), usernames));
		String optType = getCurrentRb().getString("opt.delete");
		try {
			userService.deleteUser(userIds);
			String rs = getCurrentRb().getString("rst.success");
			String log = CommonLogGenerator.genarateCommonLog(params.toString(), optType, optType, null, null, rs, getI18NLocale());
			this.addResultInfo(SUCCESS, getI18NReader().getDeleteSuccessMsg(), null);
			saveSysLog(request, true, Constants.SYSLOGSOPERATETYPE_DELETE, moduleName, log,
					(int) (Calendar.getInstance().getTimeInMillis() - lStart));
		} catch (Exception e) {
			String rs = getCurrentRb().getString("rst.failure");
			String log = CommonLogGenerator.genarateCommonLog(params.toString(), optType, optType, null, e.getMessage(), rs, getI18NLocale());
			this.addResultInfo(FAILURE, getI18NReader().getDeleteFailureMsg() + e.getMessage(), e.getMessage());
			saveSysLog(request, false, Constants.SYSLOGSOPERATETYPE_DELETE, moduleName, log + e.getMessage(),
					(int) (Calendar.getInstance().getTimeInMillis() - lStart));
			e.printStackTrace();
		}
		this.responseResult(response, this.getResultJSONStr());
	}

	/**
	 * 批量新增用户
	 * 
	 */
	@RequestMapping(value = "/sysmng/security/batchSaveUser", method = RequestMethod.POST)
	public void batchSaveUser(HttpSession sesstion, HttpServletRequest request, HttpServletResponse response) {
		long lStart = Calendar.getInstance().getTimeInMillis();
		String moduleName = this.getModuleName(request);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=utf-8");
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile("uploadfile");
		ShiroUser user = this.getUser();
		/* log params */
		SecurityBundle.setLc(this.getI18NLocale());
		StringBuffer params = new StringBuffer();
		String optType = getCurrentRb().getString("opt.add");
		try {
			List<JSONObject> l = ExcelReader.read(file.getInputStream(), 1, 0,
					"username,fullName,sex,password,email,fixedNo,mobileNo,userLevel,city,deptIds,roleIds");
			HashMap<String, String> userLevel = userService.getCommonMultiFieldValue("userLevel", "label", "", "value");
			HashMap<String, String> userSex = userService.getCommonMultiFieldValue("sex", "label", "", "value");
			JSONArray rs = new JSONArray();
			String time = DateUtil.getCurrentDatetime();
			int successCount = 0;
			String ValidMsg = "";
			for (JSONObject jo : l) {
				ValidMsg = "";
				try {
					User u = new User();
					u.setUserName(jo.getString("username"));
					u.setFullName(jo.getString("fullName"));
					u.setSex(userSex.get(jo.getString("sex")));
					u.setPassWrod(jo.getString("password"));
					
					ValidMsg = PasswordUtil.validatePwd(u.getUserName(), jo.getString("src_password"), u.getFullName());
					if(ValidMsg != null && ValidMsg.length() > 0){
						rs.add("用户：" + jo.getString("username") + "导入失败!</br>原因：" + ValidMsg + "</br>");
					}else{
						u.setEmail(jo.getString("email"));
						u.setFixedNo(jo.getString("fixedNo"));
						u.setMobileNo(jo.getString("mobileNo"));
						u.setUserLevel(userLevel.get(jo.getString("userLevel")));
						u.setCity(jo.getString("city"));
	
						u.setCreator(user.getUserName());
						u.setAccoutCreateTime(time);
						u.setTimes(1);
	
						String deptIds = jo.getString("deptIds");
						String roleIds = jo.getString("roleIds");
	
						if (StringUtils.isNotBlank(deptIds)) {
							Set<Department> depts = new HashSet<Department>();
							for (String id : deptIds.split(",")) {
								Department d = new Department();
								d.setId(id);
								depts.add(d);
							}
							u.setDepartments(depts);
						}
	
						if (StringUtils.isNotBlank(roleIds)) {
							Set<Role> roles = new HashSet<Role>();
							for (String id : roleIds.split(",")) {
								Role r = new Role();
								r.setId(id);
								roles.add(r);
							}
							u.setRoles(roles);
						}
	
						Map<String, String> validRs = u.checkValid();
						if (validRs.size() > 0) {
							rs.add("用户：" + u.getUserName() + "导入失败!</br>");
							rs.add("-" + JSONObject.fromObject(validRs).toString() + "-");
						} else if (userService.userExsist(u.getUserName())) {
							rs.add("用户：" + u.getUserName() + "导入失败!</br>");
							rs.add("用户名已存在!</br>");
						} else {
							userService.saveUser(u);
							rs.add("用户：" + u.getUserName() + "导入成功!</br>");
							successCount += 1;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					rs.add("用户：" + jo.getString("username") + "导入失败!</br>原因：" + e.getMessage() + "</br>");
				} finally {
					rs.add("</br>-------------------------------------------------</br>");
				}
			}
			String lrs = getCurrentRb().getString("rst.success");
			params.append(String.format(SecurityBundle.getString("user.batchsavesuccess"), successCount));
			params.append(String.format(SecurityBundle.getString("user.batchsavefailure"), l.size() - successCount));
			String log = CommonLogGenerator.genarateCommonLog(params.toString(), optType, optType, null, null, lrs, getI18NLocale());
			this.addResultInfo(SUCCESS, getI18NReader().getDeleteSuccessMsg(), null);
			saveSysLog(request, true, Constants.SYSLOGSOPERATETYPE_ADD, moduleName, log,
					(int) (Calendar.getInstance().getTimeInMillis() - lStart));
			this.addResultInfo(SUCCESS, "", rs);
		} catch (Exception e) {
			e.printStackTrace();
			this.addResultInfo(FAILURE, "", null);
			String lrs = getCurrentRb().getString("rst.failure");
			String log = CommonLogGenerator.genarateCommonLog(params.toString(), optType, optType, null, e.getMessage(),
					lrs, getI18NLocale());
			saveSysLog(request, true, Constants.SYSLOGSOPERATETYPE_ADD, moduleName, log,
					(int) (Calendar.getInstance().getTimeInMillis() - lStart));
		}
		this.responseResult(response, getResultJSONStr());
	}

	/**
	 * 设置帐号可用状态
	 * 
	 * @param sesstion
	 * @param request
	 * @param response
	 * @param idStr
	 *            帐号id集合字符串
	 * @param flag
	 *            设为可用或不可用（true：可用，false：不可用）
	 */
	@RequestMapping(value = "/sysmng/security/setAccountEnabled", method = RequestMethod.POST)
	public void setAccountEnabled(HttpSession sesstion, HttpServletRequest request, HttpServletResponse response) {
		String moduleName = this.getModuleName(request);
		long lStart = Calendar.getInstance().getTimeInMillis();
		String idStr = request.getParameter("idStr");
		String usernames = request.getParameter("usernames");
		String flag = request.getParameter("flag");
		/* log params */
		SecurityBundle.setLc(this.getI18NLocale());
		StringBuffer params = new StringBuffer();
		params.append(String.format(SecurityBundle.getString("user.username"), usernames));
		String optType = getCurrentRb().getString("opt.update");
		try {
			userService.setAccountEnabled(idStr.split(","), flag.equals("true") ? true : false);
			String rs = getCurrentRb().getString("rst.success");
			String log = CommonLogGenerator.genarateCommonLog(params.toString(), optType,
					String.format(SecurityBundle.getString("user.enabled"), flag), null, null, rs, getI18NLocale());
			saveSysLog(request, true, Constants.SYSLOGSOPERATETYPE_EDIT, moduleName, log,
					(int) (Calendar.getInstance().getTimeInMillis() - lStart));
			this.addResultInfo(SUCCESS, getI18NReader().getUpdateSuccessMsg(), null);
		} catch (Exception e) {
			this.addResultInfo(SUCCESS, getI18NReader().getUpdateFailureMsg() + e.getMessage(), null);
			String rs = getCurrentRb().getString("rst.failure");
			String log = CommonLogGenerator.genarateCommonLog(params.toString(), optType,
					String.format(SecurityBundle.getString("user.enabled"), flag), null, e.getMessage(), rs,
					getI18NLocale());
			saveSysLog(request, false, Constants.SYSLOGSOPERATETYPE_EDIT, moduleName, log,
					(int) (Calendar.getInstance().getTimeInMillis() - lStart));
			e.printStackTrace();
		}
		this.responseResult(response, this.getResultJSONStr());
	}

	/**
	 * 解锁因密码错误被锁定的帐号
	 * 
	 * @param usernameStr
	 *            帐号username集合字符串
	 */
	@RequestMapping(value = "/sysmng/security/unlockAccounts", method = RequestMethod.POST)
	public void unlockAccounts(HttpSession sesstion, HttpServletRequest request, HttpServletResponse response) {
		String usernameStr = request.getParameter("usernameStr");
		try {
			if (StringUtils.isNotBlank(usernameStr)) {
				String[] usernames = usernameStr.split(",");
				for (String username : usernames) {
					systemUserService.unLockedUser(username);
				}
			}
			this.addResultInfo(SUCCESS, getI18NReader().getUpdateSuccessMsg(), null);
		} catch (Exception e) {
			this.addResultInfo(SUCCESS, getI18NReader().getUpdateFailureMsg() + e.getMessage(), null);
			e.printStackTrace();
		}
		this.responseResult(response, this.getResultJSONStr());
	}

	/**
	 * 获取当前登录用户有权限的用户级别列表(新增用户只能选择比自己级别低的用户级别，超级管理员除外)
	 * 
	 * @param sesstion
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/sysmng/security/loadUserLevelCommonDatas", method = RequestMethod.POST)
	public void loadUserLevelCommonDatas(HttpSession sesstion, HttpServletRequest request, HttpServletResponse response) {
		List<CommonObject> dataList = getCommonList("userLevel");
		int userLevel = Integer.parseInt(getUser().getUserLevel());
		// int userLevel = 1;
		List<CommonObject> returnList = new ArrayList<CommonObject>();
		if (userLevel == 1) {
			returnList = dataList;
		} else {
			for (CommonObject co : dataList) {
				if (Integer.parseInt(co.getValue()) > userLevel) {
					returnList.add(co);
				}
			}
		}
		this.responseResult(response, JSONArray.fromObject(returnList).toString());
	}

	/**
	 * 判断用户名是否已存在
	 * 
	 * @param sesstion
	 * @param request
	 * @param response
	 * @param username
	 *            待判断用户名
	 */
	@RequestMapping(value = "/sysmng/security/userExsistCheck", method = RequestMethod.POST)
	public void userExsistCheck(HttpSession sesstion, HttpServletRequest request, HttpServletResponse response) {
		try {
			String username = request.getParameter("username");
			boolean exsist = userService.userExsist(username);
			this.addResultInfo(SUCCESS, getI18NReader().getQuerySuccessMsg(), exsist);
		} catch (Exception e) {
			this.addResultInfo(FAILURE, getI18NReader().getQueryFailureMsg() + e.getMessage(), null);
			e.printStackTrace();
		}
		this.responseResult(response, this.getResultJSONStr());
	}

	/**
	 * 获取当前登录用户基本信息
	 * 
	 * @param sesstion
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/sysmng/security/getCurrentUserBaseInfo", method = RequestMethod.POST)
	public void getCurrentUserBaseInfo(HttpSession sesstion, HttpServletRequest request, HttpServletResponse response) {
		JSONObject jo;
		try {
			User currentUser = BeanMapper.map(getUser(), User.class);
			jo = new JSONObject();
			jo.put("id", currentUser.getId());
			jo.put("username", currentUser.getUserName());
			jo.put("userLevel", currentUser.getUserLevel());
			this.addResultInfo(SUCCESS, getI18NReader().getQuerySuccessMsg(), jo);
		} catch (Exception e) {
			this.addResultInfo(FAILURE, getI18NReader().getQueryFailureMsg() + e.getMessage(), null);
			e.printStackTrace();
		}
		this.responseResult(response, this.getResultJSONStr());
	}

	/**
	 * 更新用户基本信息
	 * 
	 */
	@RequestMapping(value = "/sysmng/security/updateBasicInfo", method = RequestMethod.POST)
	private void updateBasicInfo(HttpSession sesstion, HttpServletRequest request, HttpServletResponse response) {
		String email = request.getParameter("email");
		String tel = request.getParameter("tel");
		String mobile = request.getParameter("mobile");
		ShiroUser user = getUser();
		try {
			userService.updateBasicInfo(email, mobile, tel, user.getId());
			user.setEmail(email);
			user.setMobileNo(mobile);
			user.setFixedNo(tel);
			this.addResultInfo(SUCCESS, getI18NReader().getUpdateSuccessMsg(), null);
		} catch (Exception e) {
			this.addResultInfo(FAILURE, getI18NReader().getUpdateFailureMsg() + e.getMessage(), null);
			e.printStackTrace();
		}
		this.responseResult(response, this.getResultJSONStr());
	}

	/**
	 * 清除在线用户信息
	 * 
	 * @param sesstion
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/sysmng/security/clearAllOnlineUserInfo", method = RequestMethod.POST)
	private void clearAllOnlineUserInfo(HttpSession sesstion, HttpServletRequest request, HttpServletResponse response) {
		try {
			systemUserService.clearOnlineUsers();
			this.addResultInfo(SUCCESS, getI18NReader().getDeleteSuccessMsg(), null);
		} catch (Exception e) {
			this.addResultInfo(FAILURE, getI18NReader().getDeleteFailureMsg() + e.getMessage(), null);
			e.printStackTrace();
		}
		this.responseResult(response, this.getResultJSONStr());
	}

	/**
	 * 更新用户扩展字段
	 * 
	 */
	@RequestMapping(value = "/sysmng/security/updateExtInfo", method = RequestMethod.POST)
	private void updateExtInfo(HttpSession sesstion, HttpServletRequest request, HttpServletResponse response) {
		String userId = request.getParameter("extUserId");
		String userExtTable = request.getParameter("userExtTable");
		String extVal = request.getParameter("extVal");
		try {
			userService.updateExtInfo(userId, userExtTable, extVal);
			this.addResultInfo(SUCCESS, getI18NReader().getUpdateSuccessMsg(), null);
		} catch (Exception e) {
			this.addResultInfo(FAILURE, getI18NReader().getUpdateFailureMsg() + e.getMessage(), null);
			e.printStackTrace();
		}
		this.responseResult(response, this.getResultJSONStr());
	}
}