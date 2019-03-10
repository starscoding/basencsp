<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<%@ include file="/common/ext4_2lib.jsp"%>
		<%@ include file="/common/lib.jsp"%>
		<%@ include file="/components/ExportExcel.jsp"%>
		<title>用户在线日志</title>
		<%response.setHeader("X-UA-Compatible","IE=8;IE=11;");%>
		<meta http-equiv="X-UA-Compatible" content="IE=8;IE=11" />
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<link rel="stylesheet" type="text/css" href="${ctx}/static/styles/themes/blue/icon.css">
		<script type="text/javascript" src="${jslib}/My97DatePicker/WdatePicker.js"></script>
		  <script src="http://echarts.baidu.com/build/dist/echarts.js"></script>
		<script type="text/javascript" 	src="${jslib}/anychart/AnyChart.js"></script>
		<script type="text/javascript" src="${ctx}/scripts/onlinelog/userOnlineLog.js"></script>
	
	</head>
	<body>
	</body>
</html>
