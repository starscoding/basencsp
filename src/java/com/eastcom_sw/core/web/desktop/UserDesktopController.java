package com.eastcom_sw.core.web.desktop;

import com.eastcom_sw.common.entity.CommonObject;
import com.eastcom_sw.common.entity.ShiroUser;
import com.eastcom_sw.common.service.ServiceException;
import com.eastcom_sw.common.utils.DateUtil;
import com.eastcom_sw.common.web.BaseController;
import com.eastcom_sw.core.entity.desktop.UserDesktop;
import com.eastcom_sw.core.entity.security.User;
import com.eastcom_sw.core.service.desktop.UserDesktopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springside.modules.mapper.BeanMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 用户个人桌面面板
 * 
 * @author SCM 2012-8-24
 */
@Controller
@RequestMapping(value = "/desktop")
public class UserDesktopController extends BaseController {

	@Autowired
	private UserDesktopService userDesktopService;

	/**
	 * 获取个人桌面图标的信息
	 * 
	 * @param sesstion
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "getDesktopResource", method = RequestMethod.POST)
	private void getDesktopResource(HttpSession sesstion,
                                    HttpServletRequest request, HttpServletResponse response) {

		String userid = this.getUser().getId();
		List records = userDesktopService.getDesktopResource(userid);
		this.responseResult(response, records.toString());
	}

	/**
	 * 文件夹操作
	 */
	@RequestMapping(value = "floderOpts", method = RequestMethod.POST)
	private void floderOpts(HttpSession sesstion, HttpServletRequest request,
                            HttpServletResponse response) {
		String type = request.getParameter("type");
		String data = request.getParameter("data");
		String _return = "";
		try {
			if ("create".equals(type)) {
				_return = userDesktopService.createFloder(data);
			} else if ("insert".equals(type)) {
				userDesktopService.addIconToFloder(data);
			} else if ("rename".equals(type)) {
				userDesktopService.renameFloder(data);
			}
			this.addResultInfo(SUCCESS, "", _return);
		} catch (Exception e) {
			e.printStackTrace();
			this.addResultInfo(FAILURE, "", null);
		}
		this.responseResult(response, getResultJSONStr());
	}

	/**
	 * 从桌面删除图标
	 * 
	 * @param sesstion
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "deleteIconFromDesktop", method = RequestMethod.POST)
	private void deleteIconFromDesktop(HttpSession sesstion,
                                       HttpServletRequest request, HttpServletResponse response) {
		String drId = request.getParameter("drId");
		try {
			userDesktopService.deleteIconFromDesktop(drId);
			this.addResultInfo(SUCCESS, getI18NReader().getDeleteSuccessMsg(),
					null);
		} catch (ServiceException e) {
			this.addResultInfo(FAILURE, getI18NReader().getDeleteFailureMsg()
					+ e.getMessage(), null);
			e.printStackTrace();
		}
		this.responseResult(response, this.getResultJSONStr());
	}

	@RequestMapping(value = "sortUserDesktopIcons", method = RequestMethod.POST)
	private void sortUserDesktopIcons(HttpSession sesstion,
                                      HttpServletRequest request, HttpServletResponse response) {

		String desktopId = request.getParameter("desktopId");
		String orders = request.getParameter("orders");
		String[] orders_ = orders.split(",");
		try {
			userDesktopService.sortUserDesktopIcons(desktopId, orders_);
			this.addResultInfo(SUCCESS, getI18NReader().getUpdateSuccessMsg(),
					null);
		} catch (ServiceException e) {
			this.addResultInfo(FAILURE, getI18NReader().getUpdateFailureMsg()
					+ e.getMessage(), null);
			e.printStackTrace();
		}
		this.responseResult(response, this.getResultJSONStr());
	}

	@RequestMapping(value = "serializeScreen", method = RequestMethod.POST)
	private void serializeScreen(HttpSession sesstion,
                                 HttpServletRequest request, HttpServletResponse response) {

		String serializeIds = request.getParameter("serializeIds");
		String[] serializeIds_ = serializeIds.split(",");
		try {
			userDesktopService.serializeScreen(serializeIds_);
			this.addResultInfo(SUCCESS, getI18NReader().getUpdateSuccessMsg(),
					null);
		} catch (ServiceException e) {
			this.addResultInfo(FAILURE, getI18NReader().getUpdateFailureMsg()
					+ e.getMessage(), null);
			e.printStackTrace();
		}
		this.responseResult(response, this.getResultJSONStr());
	}

	/**
	 * 添加用户屏幕
	 * 
	 * @param sesstion
	 * @param request
	 * @param response
	 */
	//@AuthToken
	@RequestMapping(value = "addUserDesktop", method = RequestMethod.POST)
	private void addUserDesktop(HttpSession sesstion,
                                HttpServletRequest request, HttpServletResponse response) {

		String order = request.getParameter("order");
		int order_ = Integer.valueOf(order);
		Date date = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String createTime = ft.format(date);
		UserDesktop ud = new UserDesktop();
		ud.setCreateTime(createTime);
		ud.setOrder(order_);
		ud.setUser(BeanMapper.map(getUser(), User.class));
		try {
			userDesktopService.saveUserDesktop(ud);
			this.addResultInfo(SUCCESS, getI18NReader().getAddSuccessMsg(),
					ud.getId());
		} catch (ServiceException e) {
			this.addResultInfo(FAILURE,
					getI18NReader().getAddFailureMsg() + e.getMessage(), null);
			e.printStackTrace();
		}
		this.responseResult(response, this.getResultJSONStr());
	}

	/**
	 * 删除屏幕及其图标
	 * 
	 * @param sesstion
	 * @param request
	 * @param response
	 */
	//@AuthToken
	@RequestMapping(value = "deleteDesktopAndResource", method = RequestMethod.POST)
	private void deleteDesktopAndResource(HttpSession sesstion,
                                          HttpServletRequest request, HttpServletResponse response) {

		// long lStart = Calendar.getInstance().getTimeInMillis();

		String desktopId = request.getParameter("desktopId");
		try {
			userDesktopService.deleteDesktopAndResource(desktopId);
			this.addResultInfo(SUCCESS, getI18NReader().getDeleteSuccessMsg(),
					null);
			// saveSysLog(request,true, Constants.SYSLOGSOPERATETYPE_DELETE,
			// moduleName,"删除用户桌面屏幕【"+desktopId+"】成功！",
			// (int)(Calendar.getInstance().getTimeInMillis()-lStart));
		} catch (ServiceException e) {
			this.addResultInfo(FAILURE, getI18NReader().getDeleteFailureMsg()
					+ e.getMessage(), null);
			// saveSysLog(request,false, Constants.SYSLOGSOPERATETYPE_DELETE,
			// moduleName,"删除用户桌面屏幕【"+desktopId+"】失败！",
			// (int)(Calendar.getInstance().getTimeInMillis()-lStart));
			e.printStackTrace();
		}
		this.responseResult(response, this.getResultJSONStr());
	}

	/**
	 * 添加当前屏幕桌面图标
	 * 
	 * @param sesstion
	 * @param request
	 * @param response
	 */
	//@AuthToken
	@RequestMapping(value = "addDesktopIcon", method = RequestMethod.POST)
	private void addDesktopIcon(HttpSession sesstion,
                                HttpServletRequest request, HttpServletResponse response) {

		// long lStart = Calendar.getInstance().getTimeInMillis();

		String desktopId = request.getParameter("desktopId");
		String resourceId = request.getParameter("resourceId");
		String order = request.getParameter("order");

		try {
			String _return = userDesktopService.addDesktopIcon(desktopId,
					resourceId, Integer.valueOf(order));
			this.addResultInfo(SUCCESS, getI18NReader().getAddSuccessMsg(),
					_return);
		} catch (ServiceException e) {
			this.addResultInfo(FAILURE,
					getI18NReader().getAddFailureMsg() + e.getMessage(), null);
			e.printStackTrace();
		}
		this.responseResult(response, this.getResultJSONStr());
	}

	/**
	 * 加载主题选项
	 * 
	 * @param sesstion
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "loadThemes", method = RequestMethod.POST)
	private void loadThemes(HttpSession sesstion, HttpServletRequest request,
                            HttpServletResponse response) {
		List<CommonObject> list;
		try {
			list = userDesktopService.getCommonList("sys_themes");
			List<CommonObject> sortedList = new ArrayList<CommonObject>();
			if (list != null && !list.isEmpty()) {
				int maxOrder = 0;
				for (CommonObject co : list) {
					if (Integer.parseInt(co.getOrder()) > maxOrder) {
						maxOrder = Integer.parseInt(co.getOrder());
					}
				}
				for (int i = 0; i <= maxOrder; i++) {
					for (CommonObject co : list) {
						if (Integer.parseInt(co.getOrder()) == i) {
							sortedList.add(co);
						}
					}
				}
			}
			this.addResultInfo(SUCCESS, getI18NReader().getQuerySuccessMsg(),
					sortedList);
		} catch (Exception e) {
			this.addResultInfo(FAILURE, getI18NReader().getQueryFailureMsg()
					+ e.getMessage(), null);
			e.printStackTrace();
		}
		this.responseResult(response, getResultJSONStr());
	}

	/**
	 * 更新用户主题
	 * 
	 * @param sesstion
	 * @param request
	 * @param response
	 */
	//@AuthToken
	@RequestMapping(value = "updateUserTheme", method = RequestMethod.POST)
	private void updateUserTheme(HttpSession sesstion,
                                 HttpServletRequest request, HttpServletResponse response) {
		String theme = request.getParameter("theme");
		try {
			userDesktopService.updateUserTheme(getUser().getId(), theme);
			// Cookie c = new Cookie("theme", theme);
			// c.setPath("/");
			// response.addCookie(c);
			request.getSession().setAttribute("theme", theme);
			this.addResultInfo(SUCCESS, getI18NReader().getUpdateSuccessMsg(),
					null);
		} catch (Exception e) {
			this.addResultInfo(FAILURE, getI18NReader().getUpdateFailureMsg()
					+ e.getMessage(), null);
			e.printStackTrace();
		}
		this.responseResult(response, getResultJSONStr());
	}
	
	/**
	 * 
	* @Title: showPasswordNotifications 
	* @Description: TODO(密码过期显示过期提醒，剩余一周的时候开始提醒) 
	* @param @param session
	* @param @param request
	* @param @param response
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	@RequestMapping(value = "/showPasswordNotifications", method = RequestMethod.POST)
	public void showPasswordNotifications(HttpSession session, HttpServletRequest request,
                                          HttpServletResponse response) {
		//List list = userService.getUser
		ShiroUser u = this.getUser();
		long expiredDays= u.getPwdExpiredDays();
		String modifyTime = u.getPwdModifyTime();
		
		long ntl = new Date().getTime();
		String msg = "";
		if (modifyTime != null && !"".equals(modifyTime.trim())) {
			Date st = DateUtil.convertStringToDate("yyyy-MM-dd HH:mm:ss",modifyTime);
			long ed = (expiredDays * 24 * 60 * 60 * 1000)+st.getTime();
			long bed = ((expiredDays-7) * 24 * 60 * 60 * 1000)+st.getTime();
			String expiredTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(ed));
			
			if(bed<ntl){
				Map<String,String> map = new HashMap<String, String>();
				map.put("userName", u.getFullName());
				map.put("expiredTime", expiredTime);
			    msg = "尊敬的"+u.getFullName()+";您的密码将在"+expiredTime+"过期，为了不影响您的正常使用，请及时修改密码，谢谢合作！";
				this.addResultInfo(SUCCESS, "", msg);
			}else{
				this.addResultInfo(FAILURE, "", msg);
			}
		
		}
		else{
			//修复当密码修改时间为空时，提示上次其他用户的密码过期信息bug
			this.addResultInfo(FAILURE, "", msg);
		}
		this.responseResult(response, getResultJSONStr());
	}
	
	/**
	 * 
	* @Title: saveUserShortCut 
	* @Description: TODO(添加菜单到我的快捷方式) 
	* @param @param sesstion
	* @param @param request
	* @param @param response    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	@RequestMapping(value = "/saveUserShortCut", method = RequestMethod.POST)
	private void saveUserShortCut(HttpSession sesstion,
                                  HttpServletRequest request, HttpServletResponse response) {
		String menuId = request.getParameter("menuId");
		String userId = getUser().getId();
		try {
			userDesktopService.saveUserShortCut(userId, menuId);
			this.addResultInfo(SUCCESS, "加入快捷方式成功！",
					null);
		} catch (Exception e) {
			this.addResultInfo(FAILURE, "加入快捷方式失败！", null);
			e.printStackTrace();
		}
		this.responseResult(response, this.getResultJSONStr());
	}
	
	/**
	 * 
	* @Title: findMyShortcut 
	* @Description: TODO(我的快捷方式) 
	* @param @param sesstion
	* @param @param request
	* @param @param response    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	@RequestMapping(value = "/findMyShortcut", method = RequestMethod.POST)
	private void findMyShortcut(HttpSession sesstion,
                                HttpServletRequest request, HttpServletResponse response) {
		String userId = getUser().getId();
		try {
			String result = userDesktopService.findMyShortcut(userId);
			this.responseResult(response, result);
		} catch (Exception e) {
			this.addResultInfo(FAILURE, "", null);
			e.printStackTrace();
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
	@RequestMapping(value = "/deleteMyShortcut", method = RequestMethod.POST)
	public void deleteDepartment(HttpSession sesstion,
                                 HttpServletRequest request, HttpServletResponse response) {
		String resourceId = request.getParameter("resourceId");
		String userId = this.getUser().getId();
		try {
			userDesktopService.deleteMyShortcut(userId,resourceId);
			this.addResultInfo(SUCCESS, getI18NReader().getDeleteSuccessMsg(),
					null);
		} catch (ServiceException e) {
			this.addResultInfo(FAILURE, getI18NReader().getDeleteFailureMsg()
					+ e.getMessage(), null);
			e.printStackTrace();

		}
		this.responseResult(response, this.getResultJSONStr());
	}
	
	
}
