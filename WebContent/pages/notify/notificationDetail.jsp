<%@ page language="java" pageEncoding="UTF-8"%>
<%
 request.setCharacterEncoding("UTF-8");
%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="com.eastcom_sw.common.web.CommonDataController"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<%@ include file="/common/ext4_2lib.jsp"%>
		<%@ include file="/common/lib.jsp"%>
		<title><fmt:message key="pt.notifyDetail"/></title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">    
		<link rel="stylesheet" type="text/css" href="${ctx}/static/styles/themes/blue/icon.css">
		<link rel="stylesheet" type="text/css" href="${ctx}/static/styles/themes/blue/notification/notificationDetail.css">
		<script src="${ext4}/ux/RowExpander.js" type="text/javascript"></script>
		<script src="${ctx}/static/locale/baseCore_<%=WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext()).getBean(CommonDataController.class).getI18NLocale()%>.js" type="text/javascript"></script>
		<script src="${ctx}/scripts/notify/notificationDetail.js" type="text/javascript"></script>
	</head>
  		<script type="text/javascript">
  		var id = '<%=request.getParameter("id")%>';
  			Ext.onReady(function(){
  				Ext.QuickTips.init();
  				Ext.state.Manager.setProvider(Ext.create('Ext.state.CookieProvider'));
  				eastcom.modules.notificationDetail.initComponent();
  			});
  		</script>
	<body>	
	</body>
</html>
