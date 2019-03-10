<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="com.eastcom_sw.common.web.CommonDataController"%>

<html>
	<head>
		<%@ include file="/common/ext4_2lib.jsp"%>
		<%@ include file="/common/lib.jsp"%>
		<title><fmt:message key="pt.redis"/></title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<link rel="stylesheet" type="text/css" href="${ext4}/ux/css/LiveSearchGridPanel.css" />
    	<link rel="stylesheet" type="text/css" href="${ext4}/ux/statusbar/css/statusbar.css" />
		<link rel="stylesheet" type="text/css" href="${ext4}/shared/example.css" />
		<link rel="stylesheet" type="text/css" href="${ctx}/static/styles/themes/blue/redismng/redismng.css" />
		<script src="${ctx}/static/locale/baseCore_<%=WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext()).getBean(CommonDataController.class).getI18NLocale()%>.js" type="text/javascript"></script>
		<script type="text/javascript" src="${ext4}/shared/examples.js"></script>
	    <script type="text/javascript">
		    Ext.Loader.setConfig({
				enabled : true
			});
	    </script>
	    <script src="${ctx}/scripts/redismng/redisMng.js" type="text/javascript"></script>
	</head>
<body>
</body>
</html>
