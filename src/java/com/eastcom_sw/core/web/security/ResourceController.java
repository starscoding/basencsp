package com.eastcom_sw.core.web.security;

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

import com.eastcom_sw.common.entity.CommonObject;
import com.eastcom_sw.common.service.ServiceException;
import com.eastcom_sw.common.utils.Constants;
import com.eastcom_sw.common.web.BaseController;
import com.eastcom_sw.core.entity.security.Resource;
import com.eastcom_sw.core.service.security.ResourceService;

/**
 * 资源管理控制器
 * 
 * @author SCM
 * @time 下午3:01:14
 */
@Controller
public class ResourceController extends BaseController {

	@Autowired
	private ResourceService resourceService;

	/**
	 * 异步获取资源的子节点信息
	 * 
	 * @param sesstion
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/sysmng/security/asynchronizeGetNodes", method = RequestMethod.POST)
	private void asynchronizeGetNodes(HttpSession sesstion,
			HttpServletRequest request, HttpServletResponse response) {

		String id = request.getParameter("node");
		String name = request.getParameter("name");
		if (id == null || id.equals("root")) {
			id = "";
		}
		id = id.trim();
		String result = "";
		if (StringUtils.isNotBlank(name)) {
			result = resourceService.searchResource(name);
		} else {
			result = resourceService.getChildrenResourcesById(id);
		}
		this.responseResult(response, result);
	}

	@RequestMapping(value = "/sysmng/security/searchResource", method = RequestMethod.POST)
	private void searchResource(HttpSession sesstion,
			HttpServletRequest request, HttpServletResponse response) {
		String name = request.getParameter("name");
		String result = "";
		if (name.isEmpty()) {
			result = resourceService.getChildrenResourcesById("");
		} else {
			result = resourceService.searchResource(name);
		}
		this.responseResult(response, result);
	}

	/** 保存资源信息 */
	@RequestMapping(value = "/sysmng/security/saveResource", method = RequestMethod.POST)
	private void saveResource(HttpSession sesstion, HttpServletRequest request,
			HttpServletResponse response) {

		long lStart = Calendar.getInstance().getTimeInMillis();

		String rid = request.getParameter("id");
		String pid = request.getParameter("pid");
		String name = request.getParameter("name");
		String nameCn = request.getParameter("nameCn");
		String status = request.getParameter("status");
		String order = request.getParameter("order");
		String kind = request.getParameter("kind");
		String location = request.getParameter("location");
		String isshowdesktop = request.getParameter("isshowdesktop");
		String iswebpage = request.getParameter("iswebpage");
		String remarks = request.getParameter("remarks");
		String image = request.getParameter("image");

		String moduleName = this.getModuleName(request);

		String opt = "0";
		Resource resource = null;
		if (rid.isEmpty()) {
			opt = "0";
			resource = new Resource();
			SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			resource.setCreateTime(ft.format(new Date()));
			resource.setCreator(getUser().getUserName());
			int order_ = 0;
			if (!order.isEmpty()) {
				order_ = Integer.valueOf(order);
			}
			resource.setOrder(order_);
			resource.setName(name);
			resource.setKind(kind);
			if (pid.isEmpty()) {
				resource.setParent(null);
			} else {
				resource.setParent(resourceService.findReource(pid));
			}
		} else {
			opt = "1";
			resource = resourceService.findReource(rid);
		}
		resource.setNameCn(nameCn);
		resource.setStatus(status);
		resource.setLocation(location);
		resource.setIsShowDesktop(isshowdesktop);
		resource.setIsWebpage(iswebpage);
		resource.setRemarks(remarks);
		resource.setImage(image);

		String optType = "0".equals(opt) ? Constants.SYSLOGSOPERATETYPE_ADD
				: Constants.SYSLOGSOPERATETYPE_EDIT;

		String info = "0".equals(opt) ? "新增" : "修改";
		try {
			if (rid.isEmpty()) {
				resourceService.saveResource(resource);
			} else {
				resourceService.updateResource(resource);
			}
			String msg = "0".equals(opt) ? getI18NReader().getAddSuccessMsg()
					: getI18NReader().getEditSuccessMsg();
			this.addResultInfo(SUCCESS, msg, resource.getJSONdata(resource));
			saveSysLog(request, true, optType, moduleName, info + "资源【"
					+ resource.getNameCn() + "】成功！", (int) (Calendar
					.getInstance().getTimeInMillis() - lStart));
		} catch (ServiceException e) {
			e.printStackTrace();
			String msg = "0".equals(opt) ? getI18NReader().getAddFailureMsg()
					: getI18NReader().getEditFailureMsg();
			this.addResultInfo(FAILURE, msg + e.getMessage(), null);
			saveSysLog(request, false, optType, moduleName, info + "资源【"
					+ resource.getNameCn() + "】失败！异常信息：" + e.getMessage(),
					(int) (Calendar.getInstance().getTimeInMillis() - lStart));
		}
		this.responseResult(response, this.getResultJSONStr());
	}

	@RequestMapping(value = "/sysmng/security/deleteResource", method = RequestMethod.POST)
	public void deleteResource(HttpSession sesstion,
			HttpServletRequest request, HttpServletResponse response) {
		long lStart = Calendar.getInstance().getTimeInMillis();
		String id = request.getParameter("id");

		String moduleName = this.getModuleName(request);
		try {
			resourceService.removeResourceById(id);
			this.addResultInfo(SUCCESS, getI18NReader().getDeleteSuccessMsg(),
					null);
			saveSysLog(request, true, Constants.SYSLOGSOPERATETYPE_DELETE,
					moduleName, "删除资源【" + id + "】成功！", (int) (Calendar
							.getInstance().getTimeInMillis() - lStart));
		} catch (ServiceException e) {
			this.addResultInfo(FAILURE, getI18NReader().getDeleteFailureMsg()
					+ e.getMessage(), null);
			saveSysLog(request, false, Constants.SYSLOGSOPERATETYPE_DELETE,
					moduleName, "删除资源【" + id + "】失败！异常信息：" + e.getMessage(),
					(int) (Calendar.getInstance().getTimeInMillis() - lStart));
			e.printStackTrace();

		}
		this.responseResult(response, this.getResultJSONStr());
	}

	/**
	 * 资源重名验证
	 * 
	 * @param sesstion
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/sysmng/security/checkResourceNameExsist", method = RequestMethod.POST)
	public void checkResourceNameExsist(HttpSession sesstion,
			HttpServletRequest request, HttpServletResponse response) {
		String name = request.getParameter("name");
		String other = request.getParameter("otherInput");// 验证名称时候为pid，验证地址时为‘location’
		String kind = request.getParameter("type");
		boolean exsist = false;
		if (other.equals("location")) {
			exsist = resourceService.checkLocationExsist(name);
		} else {
			exsist = resourceService.checkResourceNameExsist(other, name, kind);
		}
		String[] vals = { getI18NReader().OPT_OTHER,
				getI18NReader().RST_SUCCESS };
		this.addResultInfo(SUCCESS, getI18NReader().getReturnMsg(vals), exsist);
		this.responseResult(response, this.getResultJSONStr());
	}

	/**
	 * 批量更新资源顺序调整(父节点调整，order顺序调整)
	 * 
	 * @param sesstion
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/sysmng/security/changeResourceParentAndOrder", method = RequestMethod.POST)
	public void changeParentAndOrder(HttpSession sesstion,
			HttpServletRequest request, HttpServletResponse response) {
		String jsonString = request.getParameter("jsonString");
		try {
			resourceService.changeParentAndOrder(jsonString);
			this.addResultInfo(SUCCESS, getI18NReader().getUpdateSuccessMsg(),
					null);
		} catch (Exception e) {
			this.addResultInfo(FAILURE, getI18NReader().getUpdateFailureMsg(),
					null);
			e.printStackTrace();
		}
		this.responseResult(response, this.getResultJSONStr());
	}

	/**
	 * 获取资源管理图标配置
	 * 
	 * @param sesstion
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/sysmng/security/queryResourceIcons", method = RequestMethod.POST)
	public void queryResourceIcons(HttpSession sesstion,
			HttpServletRequest request, HttpServletResponse response) {
		List<JSONObject> list = new ArrayList<JSONObject>();
		FileFilter ff = new FileFilter() {
			public boolean accept(File f) {
				String name = f.getName();
				boolean _return = false;
				if (f.isFile()) {
					if (name.lastIndexOf(".") != -1) {
						String type = name.substring(name.lastIndexOf(".") + 1,
								name.length()).toLowerCase();
						if (type.equals("bmp") || type.equals("gif")
								|| type.equals("jpeg") || type.equals("jpg")
								|| type.equals("png")) {
							_return = true;
						}
					}
				}
				return _return;
			}
		};
		try {
			// File currentFile = new File(sesstion.getServletContext()
			// .getRealPath("/"));
			// System.out.println(sesstion.getServletContext().getRealPath(
			// "/static/images/common/appicons"));
			String path = sesstion.getServletContext().getRealPath(
					"/static/images/common/appicons");

			List<CommonObject> types = resourceService
					.getCommonList("resourceIconTypes");
			for (CommonObject co : types) {
				String customerPath = co.getAttribute();
				File file = new File(path + customerPath);
				// String filePath = (customerPath.endsWith("/")) ? customerPath
				// : customerPath + "/";
				String filePath = customerPath.replace("/", "");
				if (filePath.length() > 0) {
					filePath += "/";
				}
				if (file.exists() && file.isDirectory()) {
					File[] files = file.listFiles(ff);
					for (File f : files) {
						JSONObject jo = new JSONObject();
						String name = f.getName().substring(0,
								f.getName().lastIndexOf("."));
						jo.put("name", name);
						jo.put("thumb", filePath + f.getName());
						jo.put("type", co.getValue());
						jo.put("typeCn", co.getLabel());
						list.add(jo);
					}
				}
			}
			String[] vals = { getI18NReader().OPT_QUERY,
					getI18NReader().RST_SUCCESS };
			this.addResultInfo(SUCCESS, getI18NReader().getReturnMsg(vals),
					list);
		} catch (Exception e) {
			String[] vals = { getI18NReader().OPT_QUERY,
					getI18NReader().RST_FAILURE };
			this.addResultInfo(FAILURE, getI18NReader().getReturnMsg(vals),
					null);
			e.printStackTrace();
		}
		this.responseResult(response, getResultJSONStr());
	}
}
