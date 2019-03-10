package com.eastcom_sw.core.web;

import com.eastcom_sw.common.utils.Constants;
import com.eastcom_sw.common.web.BaseController;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLDecoder;

/**
 * 单点登陆
 * 
 * @author JJF
 * @date Mar 11, 2015
 *
 */
@Controller
public class SSOController extends BaseController {
	Logger log = LoggerFactory.getLogger(getClass());

	@RequestMapping(value = "/sso", method = { RequestMethod.POST,
			RequestMethod.GET })
	private String sso(Model model, HttpSession session,
                       HttpServletRequest request, HttpServletResponse response) {
		String entryparams = request.getParameter("entryparams");
		String moduleparams = request.getParameter("moduleparams");
		log.info("entryparams: " + entryparams);
		log.info("moduleparams: " + moduleparams);

		if (entryparams != null) {
			try {
				entryparams = com.eastcom_sw.common.utils.CryptoUtilOri
						.decrypt(entryparams);
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				entryparams = URLDecoder.decode(entryparams, "UTF-8");
			} catch (java.io.UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}
		log.info("entryparams after decode: " + entryparams);
		if (StringUtils.isNotBlank(entryparams)) {
			String[] params = entryparams.split("&nics,");
			if (params.length >= 4) {
				String username = params[0];
				String password = params[1];
				String module = params[2];
				String systemId = params[3];
				String extension_params = "";
				if (params.length >= 5) {
					extension_params = params[4];
					session.setAttribute(Constants.EXT_EXTENSIONPARAMS,
							extension_params);
				}

				session.setAttribute(Constants.EXT_OPEN_FLAG, true);
				session.setAttribute(Constants.EXT_ENTRY_MODULE, module);
				session.setAttribute(Constants.EXT_SYSTEM_ID, systemId);
				session.setAttribute(Constants.EXT_MODULEPARAMS, moduleparams);
				model.addAttribute("username", username);
				model.addAttribute("password", password);

				String sessionKey = "NCSP_LINK_USERNAME";
				Object o = session.getAttribute(sessionKey);
				
				if(o!=null && StringUtils.isNotBlank(o.toString())){
					System.out.print("session->username:"+username);
					if(o.toString().equals(username)){
						return "sso/moduleChoice";
					}else{
						session.setAttribute(sessionKey,username);
						return "sso/excuteLogin";
					}
				}else{
					session.setAttribute(sessionKey,username);
					return "sso/excuteLogin";
				}
				
				//return "sso/excuteLogin";
			}
		}
		throw new IllegalArgumentException("参数错误:" + entryparams);
	}
	
	public static void main(String[] args) {
		String url = "zhfxxt&nics,fa0f4af641cfb2e0482bc6c1d7dce9a348d3f774&nics,usermng&nics,ECIP";
		String urlString =  com.eastcom_sw.common.utils.CryptoUtilOri.encrypt(url);
		System.out.println(urlString);
	}
	
	
}
