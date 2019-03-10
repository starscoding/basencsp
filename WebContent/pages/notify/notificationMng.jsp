<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="com.eastcom_sw.common.web.CommonDataController"%>
<%request.setCharacterEncoding("UTF-8");%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/common/ext4_2lib.jsp"%>
<%@ include file="/common/lib.jsp"%>
<title><fmt:message key="pt.notifyMng"/></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<link rel="stylesheet" type="text/css"
	href="${ctx}/static/styles/themes/blue/icon.css">
<link rel="stylesheet" type="text/css"
	href="${ctx}/static/styles/themes/blue/notification/notificationGrid.css">
<link rel="stylesheet" type="text/css"
	href="${ctx}/static/styles/themes/blue/notification/notificationDetail.css">
	<link rel="stylesheet" type="text/css"
	href="${ctx}/static/styles/themes/blue/uploadFile.css">
<script src="${ctx}/static/locale/baseCore_${locale}.js" type="text/javascript"></script>
<script src="${ctx}/scripts/notify/notificationMng.js"
	type="text/javascript"></script>
<script src="${ctx}/scripts/notify/utils.js"
	type="text/javascript"></script>
<script type="text/javascript"
	src="${jslib}/My97DatePicker/WdatePicker.js"></script>
</head>
<script type="text/javascript">
	function downFile(fileRealName, fileName) {
		//eastcom.modules.notificationMng.downFile(fileRealName, fileName);
		location.href = eastcom.baseURL
				+ '/sysmng/notification/attachmentDownload' + '?mngFileName='
				+ encodeURI(encodeURI(fileName)) + '&&mngFileRealName='
				+ fileRealName;
	}
	Ext.onReady(function() {
		Ext.QuickTips.init();
		Ext.state.Manager.setProvider(Ext.create('Ext.state.CookieProvider'));
		eastcom.modules.notificationMng.initComponent();
	});
</script>
<body>
</body>
</html>
