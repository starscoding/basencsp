<%@page import="com.eastcom_sw.common.utils.Constants"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="com.eastcom_sw.common.web.CommonDataController"%>
<%
	response.setHeader("X-UA-Compatible", "IE=8");
%>
<html>
	<head>
		<%@ include file="/common/ext4_2lib.jsp"%>
		<%@ include file="/common/lib.jsp"%>
		<%@ include file="/components/MultipleComboBox.jsp"%>
		<title><fmt:message key="pt.controlPanel"/></title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<link rel="stylesheet" type="text/css" href="${ext4}/ux/css/GroupTabPanel_${theme}.css" />
		<link rel="stylesheet" type="text/css" href="${ext4}/shared/example.css" />
		<link rel="stylesheet" type="text/css" href="${ctx}/static/styles/themes/blue/controlpanel/controlpanel.css" />
		<script src="${ctx}/static/locale/baseCore_${locale}.js" type="text/javascript"></script>
		<script type="text/javascript" src="${ext4}/shared/examples.js"></script>
	    <script src="${ctx}/scripts/controlpanel/controlpanel.js" type="text/javascript"></script>
	</head>
<body>
</body>
</html>
