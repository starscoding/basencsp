<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.eastcom_sw.common.utils.CommonUtil"%>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@ page import="org.springframework.context.ApplicationContext"%>
<%@ page import="com.eastcom_sw.common.web.CommonDataController"%>
<%@ page import="java.lang.reflect.Field"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%-- 避免Host header attack漏洞,将路径变更为相对路径,各个省份自己修改--%>
<%-- <c:set var="ctx" value="<%=CommonUtil.getRelativeWebContext(request) %>" />
<c:set var="jslib" value="<%=CommonUtil.getRelativeJslib(request)%>" /> --%>
<c:set var="ctx" value="<%=CommonUtil.getFullWebContext(request) %>" />
<c:set var="jslib" value="<%=CommonUtil.getJslib(request)%>" />
<c:set var="theme" value="<%=CommonUtil.getTheme(request)%>" />
<c:set var="csrfToken" value="<%=CommonUtil.getLoginToken(request)%>" />
<c:set var="forgetFootMsg" value="<%=WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext()).getBean(CommonDataController.class).getForgetFootMsg()%>" />
<c:set var="locale" value="<%=WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext()).getBean(CommonDataController.class).getI18NLocale()%>" />
<c:set var="verifyImageFlag" value="<%=WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext()).getBean(CommonDataController.class).getVerifyImage()%>" />
<c:set var="cssDir" value="${ctx}/static/styles/themes/${theme}" />
<c:set var="imgDir" value="${ctx}/static/images/themes/${theme}" />
<fmt:setLocale value="${locale}" />
<script src="${ctx}/static/commonjs/commonModule.js" type="text/javascript"></script>
<script src="${ctx}/static/locale/common_${locale}.js" type="text/javascript"></script>
<script type="text/javascript">
	var currentUserTheme = '<%=CommonUtil.getTheme(request)%>';
	var jslib = '${jslib}';
	var csrfToken = '${csrfToken}';
	var hidden_base_locale = '${locale}';
</script>