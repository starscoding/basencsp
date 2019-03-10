/**
 * 
 */
package com.eastcom_sw.core.web.visit;

import com.eastcom_sw.common.service.ServiceException;
import com.eastcom_sw.common.web.BaseController;
import com.eastcom_sw.core.service.security.MenuVisitedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 用户菜单访问次数记录
 * 
 * @author SCM 2012-11-5
 */
@Controller
@RequestMapping(value = "/loginExtension")
public class MenuVisitedLogController extends BaseController {

	@Autowired
	private MenuVisitedService mVisitedService;

//	@AuthToken
	@RequestMapping(value = "addUserMenuVisitTime", method = RequestMethod.POST)
	public void addUserMenuVisitTime(HttpSession sesstion,
                                     HttpServletRequest request, HttpServletResponse response) {
		String menuId = request.getParameter("menuId");
		String userId = this.getUser().getId();
		try {
			boolean rlst = mVisitedService.addUserMenuVisitTime(userId, menuId);
			this.addResultInfo(SUCCESS, getI18NReader().getUpdateSuccessMsg(),
					rlst);
		} catch (ServiceException e) {
			this.addResultInfo(FAILURE, getI18NReader().getUpdateFailureMsg()
					+ e.getMessage(), null);
			e.printStackTrace();

		}
		this.responseResult(response, this.getResultJSONStr());
	}

	@RequestMapping(value = "getUserVisitedTopN", method = RequestMethod.POST)
	public void getUserVisitedTopN(HttpSession sesstion,
                                   HttpServletRequest request, HttpServletResponse response) {
		String topn = request.getParameter("topn");
		String userId = this.getUser().getId();
		int n = topn.isEmpty() ? 10 : Integer.valueOf(topn);
		try {
			Set<String> rslt = mVisitedService.getUserVisitedTopN(userId, n);
			this.addResultInfo(SUCCESS, "获取用户常用菜单TOP" + n + "成功！", rslt);
		} catch (ServiceException e) {
			this.addResultInfo(FAILURE, "获取用户常用菜单TOP" + n + "失败！",
					e.getMessage());
			e.printStackTrace();
		}
		this.responseResult(response, this.getResultJSONStr());
	}

	@RequestMapping(value = "delMenuFromUsermenus", method = RequestMethod.POST)
	public void delMenuFromUsermenus(HttpSession sesstion,
                                     HttpServletRequest request, HttpServletResponse response) {
		String menuId = request.getParameter("menuId");
		String userId = this.getUser().getId();
		try {
			mVisitedService.delMenuFromUsermenus(userId, menuId);
			this.addResultInfo(SUCCESS, "用户[" + userId + "]删除常用菜单项" + menuId
					+ "成功！", "");
		} catch (ServiceException e) {
			this.addResultInfo(FAILURE, "用户[" + userId + "]删除常用菜单项" + menuId
					+ "失败！", e.getMessage());
			e.printStackTrace();
		}
		this.responseResult(response, this.getResultJSONStr());
	}

	/**
	 * 获取用户访问远程
	 * 
	 * @param sesstion
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "getRandomVisitedInfo", method = RequestMethod.POST)
	public void getRandomVisitedInfo(HttpSession sesstion,
                                     HttpServletRequest request, HttpServletResponse response) {
		String menuId = request.getParameter("menuId");
		String project = request.getParameter("project");
		String userName = this.getUser().getUserName();

		Map<String, String> params = new HashMap<String, String>();
		try {
			params = mVisitedService.getRandomUUIDForMenuVisited(userName,
					request.getSession().getId(), menuId, project);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		this.responseResult(response, this.getResultJSONStr(params));
	}

	@RequestMapping(value = "deleteRandomByPageId", method = RequestMethod.POST)
	public void deleteRandomByPageId(HttpSession sesstion,
                                     HttpServletRequest request, HttpServletResponse response) {
		String menuId = request.getParameter("menuId");
		String userName = this.getUser().getUserName();
		try {
			mVisitedService.deleteRandomByPageId(userName, menuId);
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
