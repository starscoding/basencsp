package com.eastcom_sw.core.web.extension;

import com.eastcom_sw.common.entity.CommonObject;
import com.eastcom_sw.common.web.BaseController;
import com.eastcom_sw.core.service.baseinfo.SysCommonService;
import com.eastcom_sw.core.service.projectInfo.ProjectInfoConfigService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 具体项目登录界面以及主界面配置信息
 * 
 * @author JJF
 * @date MAR 10 2013
 */
@Controller
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ProjectInfoController extends BaseController {
	@Autowired
	private ProjectInfoConfigService projectInfoConfigService;
	@Autowired
	private SysCommonService sysCommonService;

	/**
	 * 获取登录页面界面配置信息
	 * 
	 * @param sesstion
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/projectInfo-unAuth/getLoginPageConfig", method = RequestMethod.POST)
	private void getLoginPageConfig(HttpSession sesstion,
                                    HttpServletRequest request, HttpServletResponse response) {
		String types = request.getParameter("types");
		try {
			boolean isConfigExsist = projectInfoConfigService.isConfigExsist();
			Map<String, Object> rs;
			if (isConfigExsist) {
				if (StringUtils.isNotBlank(types)) {
					rs = getLoginPageConfigFromRedis(types);
					this.addResultInfo(SUCCESS, "", rs);
				} else {
					String errMsg = "参数错误！";
					log.error(errMsg);
					this.addResultInfo(FAILURE, errMsg, null);
				}

			} else {
				String errMsg = "找不到配置项或者redis无法连接,读取默认配置！";
				log.error(errMsg);
				this.addResultInfo(FAILURE, errMsg, null);
			}
		} catch (Exception e) {
			this.addResultInfo(FAILURE, "系统错误！", null);
			log.error(e.getMessage());
		}
		this.responseResult(response, getResultJSONStr());
	}

	/**
	 * 从redis获取页面界面配置信息
	 */
	private Map<String, Object> getLoginPageConfigFromRedis(String types) {
		Map<String, Object> confs = new HashMap<String, Object>();
		String customLoginPageURL = projectInfoConfigService
				.getCommonFieldValueByName("customLoginPageURL", "value");
		List<CommonObject> mainPageConfig = projectInfoConfigService
				.getCommonList("mainPageConfig");
		List<CommonObject> defaultLoginConfig = projectInfoConfigService
				.getCommonList("defaultLoginConfig");
		if (types.indexOf("U") != -1) {
			confs.put("customLoginPageURL", customLoginPageURL);
		}
		if (types.indexOf("M") != -1) {
			confs.put("mainPageConfig", mainPageConfig);
		}
		if (types.indexOf("L") != -1) {
			confs.put("defaultLoginConfig", defaultLoginConfig);
		}
		return confs;
	}

	/**
	 * 保存登录页面界面配置信息
	 * 
	 * @param sesstion
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "saveLoginPageConfig", method = RequestMethod.POST)
	private void saveLoginPageConfig(HttpSession sesstion,
                                     HttpServletRequest request, HttpServletResponse response) {

	}
}
