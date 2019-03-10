package com.eastcom_sw.core.web.extension;

import com.eastcom_sw.common.web.BaseController;
import com.eastcom_sw.core.entity.security.OnlineUser;
import com.eastcom_sw.core.service.configuration.ArgumentsConfigurateService;
import com.eastcom_sw.core.service.log.SysLogAnalyzeService;
import com.eastcom_sw.core.service.security.SystemUserService;
import com.eastcom_sw.core.service.security.UserService;
import com.eastcom_sw.core.web.BaseBundle;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * @author SCM
 * 
 */
@Controller
@RequestMapping(value = "/loginExtension")
@Scope(value= ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LoginExtensionController extends BaseController {

	@Autowired
	private ArgumentsConfigurateService argumentsConfigurateService;
	@Autowired
	private UserService userService;
	@Autowired
	private SystemUserService systemUserService;
	@Autowired
	private SysLogAnalyzeService sysLogAnalyzeService;

	/**
	 * 获取当前用户的所有资源
	 * 
	 * @param sesstion
	 * @param request
	 * @param response
	 */
	//@AuthToken
	@RequestMapping(value = "getCurrentUserResource", method = RequestMethod.POST)
	private void getCurrentUserResource(HttpSession sesstion,
                                        HttpServletRequest request, HttpServletResponse response) {
		List<String> roles = this.getUserRoles();
		String rootMenuName = request.getParameter("rootMenuName");
		if (roles != null && roles.size() > 0) {
			try {
				JSONObject result = userService.getCurrentUserResources(roles,
						rootMenuName);
				if (result.getJSONArray("menus").size() > 0) {
					this.addResultInfo(SUCCESS, "", result);
				} else {
					BaseBundle.setLc(this.getI18NLocale());
					this.addResultInfo(FAILURE,
							BaseBundle.getString("userroleemptyres"), null);
				}
			} catch (Exception e) {
				this.addResultInfo(FAILURE, getI18NReader()
						.getQueryFailureMsg(), null);
				e.printStackTrace();
			}
		} else {
			BaseBundle.setLc(this.getI18NLocale());
			this.addResultInfo(FAILURE, BaseBundle.getString("useremptyrole"),
					null);
		}
		this.responseResult(response, this.getResultJSONStr());
	}

	@RequestMapping(value = "getCurrentUserTreeResources", method = RequestMethod.POST)
	private void getCurrentUserTreeResources(HttpSession sesstion,
                                             HttpServletRequest request, HttpServletResponse response) {
		List<String> roles = this.getUserRoles();
		String rootMenuName = request.getParameter("rootMenuName");
		String _controlStatus = request.getParameter("controlStatus");
		if (roles != null && roles.size() > 0) {
			try {
				JSONArray result = userService.getCurrentUserTreeResources(
						roles, rootMenuName, !"false".equals(_controlStatus),getUser().getId());
				if (result.size() > 0) {
					this.addResultInfo(SUCCESS, "", result);
				} else {
					BaseBundle.setLc(this.getI18NLocale());
					this.addResultInfo(FAILURE,
							BaseBundle.getString("userroleemptyres"), null);
				}
			} catch (Exception e) {
				this.addResultInfo(FAILURE, getI18NReader()
						.getQueryFailureMsg(), null);
				e.printStackTrace();
			}
		} else {
			BaseBundle.setLc(this.getI18NLocale());
			this.addResultInfo(FAILURE, BaseBundle.getString("useremptyrole"),
					null);
		}
		this.responseResult(response, this.getResultJSONStr());
	}

	/**
	 * 获取在线用户列表
	 * 
	 */
	//@AuthToken
	@RequestMapping(value = "getOnlineUsers", method = RequestMethod.POST)
	private void getOnlineUsers(HttpSession sesstion,
                                HttpServletRequest request, HttpServletResponse response) {
		String onlyQueryNum = request.getParameter("onlyQueryNum");
		String queryThisMonth = request.getParameter("queryThisMonth");
		try {
			List<OnlineUser> list = systemUserService.getOnlineUsers();
			if ("true".equals(onlyQueryNum)) {
				if ("true".equals(queryThisMonth)) {
					int thisMonth = sysLogAnalyzeService
							.countUserLoginCurrnetMonth();
					int current = list.size();
					this.addResultInfo(SUCCESS, "", "current:" + current
							+ ",thisMonth:" + thisMonth);
				} else {
					this.addResultInfo(SUCCESS, "", list.size());
				}
			} else {
				List<JSONObject> rs = new ArrayList<JSONObject>();
				for (OnlineUser u : list) {// 去除多余字段，以防信息泄露
					JSONObject jo = new JSONObject();
					jo.put("nameCn", u.getUsernameCn());
					jo.put("clientIp", u.getClientIp());
					jo.put("loginTime", u.getLoginTime());
					jo.put("sex", u.getSex());
					jo.put("department", u.getDepartment());
					rs.add(jo);
				}
				this.addResultInfo(SUCCESS, "", rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.addResultInfo(FAILURE, "", null);
		}
		this.responseResult(response, getResultJSONStr());
	}
}
