<%@ page import="com.eastcom_sw.common.utils.CommonUtil"%>
<%@ page import="java.util.*"%>
<%@ page import="com.eastcom_sw.common.utils.Constants"%>
<%@ page
	import="com.eastcom_sw.security.extension.service.ResourceExtensionService"%>
<%@page
	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%
	String url = "/pages/main.jsp";
	String moduleName = (String) session
			.getAttribute(Constants.EXT_ENTRY_MODULE);
	if (StringUtils.isNotBlank(moduleName)) {
		String rsUrl = WebApplicationContextUtils
				.getRequiredWebApplicationContext(getServletContext())
				.getBean(ResourceExtensionService.class)
				.findUrlByModuleName(moduleName);
		if (StringUtils.isNotBlank(rsUrl)) {
			url = rsUrl;
			String params = (String)session.getAttribute(Constants.EXT_MODULEPARAMS);
			if(StringUtils.isNotBlank(params)){
				url+="?"+params;
			}
		}
	}
	response.sendRedirect(request.getContextPath() + url);
%>
