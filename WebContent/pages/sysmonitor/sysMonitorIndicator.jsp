<%@ page language="java" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<%@ include file="/common/ext4_2lib.jsp"%>
		<%@ include file="/common/lib.jsp"%>
		<title><fmt:message key="monitor.indicator"/></title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<link rel="stylesheet" type="text/css" href="${ctx}/static/styles/themes/blue/icon.css">
		<script src="${ctx}/static/locale/baseCore_${locale}.js" type="text/javascript"></script>
		<script src="${ext4}/ux/RowExpander.js" type="text/javascript"></script>		
		<script src="${ctx}/scripts/sysmonitor/sysMonitorIndicator.js" type="text/javascript"></script>
		<script type="text/javascript" src="${jslib}/My97DatePicker/WdatePicker.js"></script>
		<link rel="stylesheet" type="text/css" href="${jslib}/extjs-4.2.1/ux/DataView/data-view.css">
		
	</head>
  		<script type="text/javascript">
  			Ext.onReady(function(){
  				Ext.QuickTips.init();
  				Ext.state.Manager.setProvider(Ext.create('Ext.state.CookieProvider'));
  				eastcom.modules.sysMonitorIndicator.initComponent();
  			});
  		</script>
	<body>	
	</body>
</html>
