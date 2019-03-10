<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<%@ include file="/common/ext4_2lib.jsp"%>
		<%@ include file="/common/lib.jsp"%>
		<%@ include file="/components/ExportExcel.jsp"%>
		<%@ include file="/common/echarts.jsp"%>
		<title>日志统计</title>
		<%response.setHeader("X-UA-Compatible","IE=8;IE=11;");%>
		<meta http-equiv="X-UA-Compatible" content="IE=8;IE=11" />
		<link rel="stylesheet" type="text/css" href="${ctx}/static/styles/themes/blue/icon.css">
		<script src="${ctx}/static/locale/baseCore_${locale}.js" type="text/javascript"></script>
		<script type="text/javascript" src="${jslib}/My97DatePicker/WdatePicker.js"></script>
		<script type="text/javascript" src="../../scripts/logManager/sysLogStat_zh.js"></script>
		<script type="text/javascript" src="../../scripts/logManager/SearchField.js"></script>
		<script type="text/javascript" src="../../scripts/logManager/Portlet.js"></script>
		<script type="text/javascript" src="../../scripts/logManager/Portal.js"></script>
		<script type="text/javascript" src="../../scripts/logManager/PortalColumn.js"></script>
		<script type="text/javascript" src="../../scripts/logManager/chartStatLogsGeneralInfo.js"></script>
		<script type="text/javascript" src="../../scripts/logManager/chartStatLogsByModuleForUser.js"></script>
		<script type="text/javascript" src="../../scripts/logManager/chartStatLogsByAreaForUser.js"></script>
		<script type="text/javascript" src="../../scripts/logManager/chartStatLogsByLoginForUser.js"></script>
		<script type="text/javascript" src="../../scripts/logManager/statLogByChartForUser.js"></script>
	</head>
	<!-- <style type="text/css">
		.x-panel-default-framed {
		    background-color: #F4FCFF;
		    border-style: none;
		    border-width: 1px;
		    padding: 4px;
		}
    </style> -->
	<body>
	
	</body>
</html>
