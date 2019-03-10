package com.eastcom_sw.core.web.security;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.eastcom_sw.common.service.ServiceException;
import com.eastcom_sw.common.utils.CommonLogGenerator;
import com.eastcom_sw.common.utils.Constants;
import com.eastcom_sw.common.web.BaseController;
import com.eastcom_sw.core.entity.security.Department;
import com.eastcom_sw.core.service.security.DepartmentService;
import com.eastcom_sw.core.service.security.UserService;
import com.eastcom_sw.core.web.SecurityBundle;

/**
 * 部门管理
 * 
 * @author SCM
 * @time 2012-7-31
 */
@Controller
public class DepartmentController extends BaseController {

	@Autowired
	private DepartmentService departmentService;
	
	@Autowired
	private UserService userService;

	/**
	 * 获取部门tree数据
	 * 
	 * @param sesstion
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/sysmng/security/findAllDeptTreeData", method = RequestMethod.POST)
	private void findAllDeptTreeData(HttpSession sesstion,
			HttpServletRequest request, HttpServletResponse response) {
		long lStart = Calendar.getInstance().getTimeInMillis();
		boolean expanded = false, checkbox = false;
		String deptName = "";
		String checkbox_ = request.getParameter("checkbox");
		String expanded_ = request.getParameter("expanded");
		String name = request.getParameter("name");

		String moduleName = this.getModuleName(request);

		if (checkbox_ != null && Boolean.valueOf(checkbox_)) {
			checkbox = true;
		}
		if (expanded_ != null && Boolean.valueOf(expanded_)) {
			expanded = true;
		}
		if (name != null) {
			deptName = name;
		}
		/* log params */
		SecurityBundle.setLc(this.getI18NLocale());
		StringBuffer params = new StringBuffer();
		params.append(String.format(SecurityBundle.getString("dept.name"), name));

		String logOptType = getCurrentRb().getString("opt.query");

		try {
			String rslt = departmentService.findAllDeptTreeData(deptName,
					checkbox, expanded);
			String rs = getCurrentRb().getString("rst.success");
			String log = CommonLogGenerator.genarateCommonLog(
					params.toString(), logOptType, logOptType, null, null, rs,
					getI18NLocale());
			saveSysLog(request, true, Constants.SYSLOGSOPERATETYPE_QUERY,
					moduleName, log, (int) (Calendar.getInstance()
							.getTimeInMillis() - lStart));
			this.responseResult(response, rslt);
		} catch (ServiceException e) {
			String rs = getCurrentRb().getString("rst.failure");
			String log = CommonLogGenerator.genarateCommonLog(
					params.toString(), logOptType, logOptType, null, e.getMessage(), rs,
					getI18NLocale());
			this.addResultInfo(FAILURE, getI18NReader().getQueryFailureMsg()
					+ e.getMessage(), null);
			saveSysLog(request, false, Constants.SYSLOGSOPERATETYPE_QUERY,
					moduleName, log,
					(int) (Calendar.getInstance().getTimeInMillis() - lStart));
			e.printStackTrace();
		}
	}

	/** 保存部门信息 */
	@RequestMapping(value = "/sysmng/security/saveDepartment", method = RequestMethod.POST)
	private void saveDepartment(HttpSession sesstion,
			HttpServletRequest request, HttpServletResponse response) {

		long lStart = Calendar.getInstance().getTimeInMillis();

		String rid = request.getParameter("id");
		String pid = request.getParameter("pid");
		String name = request.getParameter("name");
		String nameCn = request.getParameter("nameCn");
		String order = request.getParameter("order");
		String desc = request.getParameter("desc");

		String moduleName = this.getModuleName(request);
		String opt = "0";
		Department dept = null;
		if (rid.isEmpty()) {
			opt = "0";
			dept = new Department();
			int order_ = 0;
			if (!order.isEmpty()) {
				order_ = Integer.valueOf(order);
			}
			dept.setName(name);
			dept.setOrder(order_);
			dept.setPid(pid);
		} else {
			opt = "1";
			dept = departmentService.findDeptment(rid);
		}
		dept.setNameCn(nameCn);
		dept.setDesc(desc);

		String optType = "0".equals(opt) ? Constants.SYSLOGSOPERATETYPE_ADD
				: Constants.SYSLOGSOPERATETYPE_EDIT;

		/* log params */
		SecurityBundle.setLc(this.getI18NLocale());
		StringBuffer params = new StringBuffer();
		params.append(String.format(SecurityBundle.getString("dept.name"), name));
		params.append(String.format(SecurityBundle.getString("dept.desc"), desc));
		params.append(String.format(SecurityBundle.getString("dept.label"),
				nameCn));
		String logOptType = "0".equals(opt) ? getCurrentRb().getString(
				"opt.add") : getCurrentRb().getString("opt.edit");
		try {
			String rs = getCurrentRb().getString("rst.success");
			String log = "";
			if (rid.isEmpty()) {
				departmentService.saveDepartment(dept);
				log = CommonLogGenerator
						.genarateCommonLog(params.toString(), logOptType,
								logOptType, null, null, rs, getI18NLocale());
				this.addResultInfo(SUCCESS, getI18NReader().getAddSuccessMsg(),
						dept.getJSONdata(dept));
			} else {
				log = CommonLogGenerator.genarateCommonLog(params.toString(),
						logOptType, params.toString(), null, null, rs,
						getI18NLocale());
				departmentService.updateDepartment(dept);
				this.addResultInfo(SUCCESS,
						getI18NReader().getEditSuccessMsg(), null);
			}
			saveSysLog(request, true, optType, moduleName, log, (int) (Calendar
					.getInstance().getTimeInMillis() - lStart));
		} catch (ServiceException e) {
			e.printStackTrace();
			String msg = "0".equals(opt) ? getI18NReader().getAddFailureMsg()
					: getI18NReader().getEditFailureMsg();
			this.addResultInfo(FAILURE, msg + e.getMessage(), null);
			String rs = getCurrentRb().getString("rst.failure");
			String log = CommonLogGenerator.genarateCommonLog(
					params.toString(), logOptType, logOptType, null,
					e.getMessage(), rs, getI18NLocale());
			saveSysLog(request, false, optType, moduleName, log,
					(int) (Calendar.getInstance().getTimeInMillis() - lStart));
		}
		this.responseResult(response, this.getResultJSONStr());
	}

	/**
	 * 删除部门及其子部门以及所有的关联关系
	 * 
	 * @param sesstion
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/sysmng/security/deleteDepartment", method = RequestMethod.POST)
	public void deleteDepartment(HttpSession sesstion,
			HttpServletRequest request, HttpServletResponse response) {
		long lStart = Calendar.getInstance().getTimeInMillis();
		String id = request.getParameter("id");
		String label = request.getParameter("label");
		String moduleName = this.getModuleName(request);

		/* log params */
		SecurityBundle.setLc(this.getI18NLocale());
		StringBuffer params = new StringBuffer();
		params.append(String.format(SecurityBundle.getString("dept.label"),
				label));
		String optType = getCurrentRb().getString("opt.delete");

		try {
			departmentService.deleteDepartment(id);
			this.addResultInfo(SUCCESS, getI18NReader().getDeleteSuccessMsg(),
					null);
			String rs = getCurrentRb().getString("rst.success");
			String log = CommonLogGenerator.genarateCommonLog(
					params.toString(), optType, optType, null, null, rs,
					getI18NLocale());
			saveSysLog(request, true, Constants.SYSLOGSOPERATETYPE_DELETE,
					moduleName, log, (int) (Calendar.getInstance()
							.getTimeInMillis() - lStart));
		} catch (ServiceException e) {
			this.addResultInfo(FAILURE, getI18NReader().getDeleteFailureMsg()
					+ e.getMessage(), null);
			String rs = getCurrentRb().getString("rst.failure");
			String log = CommonLogGenerator.genarateCommonLog(
					params.toString(), optType, optType, null, e.getMessage(),
					rs, getI18NLocale());
			saveSysLog(request, false, Constants.SYSLOGSOPERATETYPE_DELETE,
					moduleName, log, (int) (Calendar.getInstance()
							.getTimeInMillis() - lStart));
			e.printStackTrace();

		}
		this.responseResult(response, this.getResultJSONStr());
	}

	/**
	 * 部门重名验证
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
	@RequestMapping(value = "/sysmng/security/deptNameExsistCheck", method = RequestMethod.POST)
	public void deptNameExsistCheck(HttpSession sesstion,
			HttpServletRequest request, HttpServletResponse response) {
		String name = request.getParameter("name");
		String type = request.getParameter("type");
		boolean exsist = departmentService.nameExsistCheck(name, type);
		String[] vals = { getI18NReader().OPT_OTHER,
				getI18NReader().RST_SUCCESS };
		this.addResultInfo(SUCCESS, getI18NReader().getReturnMsg(vals), exsist);
		this.responseResult(response, this.getResultJSONStr());
	}

	/**
	 * 批量更新部门调整(父部门调整，order顺序调整)
	 * 
	 * @param sesstion
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/sysmng/security/changeParentAndOrder", method = RequestMethod.POST)
	public void changeParentAndOrder(HttpSession sesstion,
			HttpServletRequest request, HttpServletResponse response) {
		String jsonString = request.getParameter("jsonString");
		try {
			departmentService.changeParentAndOrder(jsonString);
			this.addResultInfo(SUCCESS, getI18NReader().getUpdateSuccessMsg(),
					null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			this.addResultInfo(FAILURE, getI18NReader().getUpdateFailureMsg()
					+ e.getMessage(), null);
			e.printStackTrace();
		}
		this.responseResult(response, this.getResultJSONStr());
	}
}
