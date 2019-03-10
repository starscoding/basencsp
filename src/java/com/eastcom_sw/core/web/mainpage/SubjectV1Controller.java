package com.eastcom_sw.core.web.mainpage;

import com.eastcom_sw.common.entity.NameValuePair;
import com.eastcom_sw.common.web.BaseController;
import com.eastcom_sw.core.service.configuration.ArgumentsConfigurateService;
import com.eastcom_sw.core.service.security.UserService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/subject")
public class SubjectV1Controller extends BaseController {

	@Autowired
	private ArgumentsConfigurateService argumentsConfigurateService;
	
	@Autowired
	private UserService userService;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/v1", method = RequestMethod.GET)
	private String v3(HttpServletRequest request, Model model) {
		Map m = argumentsConfigurateService
				.getSysArguments(new String[] { "startup2" });
		Map<String, String> args = (Map<String, String>) m.get("startup2");

		Map ms = argumentsConfigurateService
				.getSysArguments(new String[] { "security" });
		
		Map<String, String> secargs = (Map<String, String>) ms.get("security");
		
		
		if(StringUtils.isNotBlank(secargs.get("userExtArgs"))) {
			String userId = getUser().getId();
			String userExtArgs = secargs.get("userExtArgs");
			JSONObject jo = JSONObject.fromObject(userExtArgs);
			String userExtEnabled = jo.get("userExtEnabled")!=null?jo.get("userExtEnabled").toString():"";
			String userExtTable = jo.get("userExtTable")!=null?jo.get("userExtTable").toString():"";
			String userExtMapping = jo.get("userExtMappingRules")!=null?jo.get("userExtMappingRules").toString():"";
			if(userExtMapping != null && !"".equals(userExtMapping)){
				JSONArray ja = JSONArray.fromObject(userExtMapping);
				List list = new ArrayList();
				
				Map<NameValuePair, String> ext = userService
						.queryUserExtension(userId);
				
				for(int i=0;i<ja.size();i++){
					JSONObject o = (JSONObject)ja.get(i);
					Map map = new HashMap();
					map.put("name", o.get("name"));
					map.put("nameCn", o.get("nameCn"));
					for (NameValuePair p : ext.keySet()) {
						if((p.getName() != null && !"".equals(p.getName()))
								&& (p.getValue() != null && !"".equals(p.getValue()))
								&& p.getName().equals(o.get("name")) && p.getValue().equals(o.get("nameCn"))  ){
							map.put("extValue", ext.get(p));
						}
					}
					list.add(map);
				}
				model.addAttribute("userExtEnabled", userExtEnabled);
				model.addAttribute("userExtTable", userExtTable);
				model.addAttribute("extUserId", userId);
				model.addAttribute("userExtMapping", list);
			}
		
		}
		
		model.addAllAttributes(args);
		if (StringUtils.isBlank(args.get("mainTheme"))) {// 初始化必须要的参数
			model.addAttribute("mainTheme", "default");
		}

		String name = request.getParameter("name");
		Map<String, String> subjects = argumentsConfigurateService
				.getCommonMultiFieldValue("subject", "name", "", "desc");
//
		String conf = subjects.get(name);
		if (StringUtils.isNotBlank(conf)) {
			String[] confs = conf.split(",");
			for (String c : confs) {
				if (StringUtils.isNotBlank(c)) {
					String c_name = c.split(":")[0], c_val = c.split(":")[1];
					model.addAttribute(c_name, c_val);
				}
			}
		}
		return "subjectv1/main";
	}
}
