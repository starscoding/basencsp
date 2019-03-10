<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="com.eastcom_sw.common.web.CommonDataController"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<%@ include file="/common/ext4_2lib.jsp"%>
		<title><fmt:message key="pt.portalGadgetsMng"/></title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">    
		<link rel="stylesheet" type="text/css" href="${ctx}/static/styles/themes/blue/icon.css">
		<link rel="stylesheet" type="text/css" href="${ctx}/static/styles/themes/blue/checkboxdisable.css">
		<script src="${ctx}/static/commonjs/commonModule.js" type="text/javascript"></script>
		<script src="${ctx}/static/commonjs/components/BaseDataComboBox.js" type="text/javascript"></script>
		<script src="${ctx}/static/commonjs/components/ComboBoxCheckTree.js" type="text/javascript"></script>
		<script type="text/javascript" src="${ctx}/scripts/portal/dataModel.js"></script>
		<script src="${ctx}/scripts/portal/managerPortal.js" type="text/javascript"></script>
		<script src="${ctx}/static/locale/baseCore_<%=WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext()).getBean(CommonDataController.class).getI18NLocale()%>.js" type="text/javascript"></script>
	</head>
	<body>	
	</body>
</html>
