<%@ page language="java" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<%@ include file="/common/ext4_2lib.jsp"%>
		<%@ include file="/common/lib.jsp"%>
		<title>快捷方式管理</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<link rel="stylesheet" type="text/css" href="${ext4}/ux/css/LiveSearchGridPanel.css" />
		<link rel="stylesheet" type="text/css" href="${ctx}/static/styles/themes/blue/icon.css">
		<script src="${ctx}/static/commonjs/commonModule.js" type="text/javascript"></script>
		<script type="text/javascript" src="${ctx}/scripts/personaldesktop/shortcutMng.js"></script>
	</head>
  		<script type="text/javascript">
  			Ext.onReady(function(){
  				 Ext.QuickTips.init();
  				 Ext.state.Manager.setProvider(Ext.create('Ext.state.CookieProvider'));
  				 eastcom.modules.shortcutMng.initComponent();
  			});
  		</script>
	<body>	
	</body>
</html>
