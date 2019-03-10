<%@ page language="java" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<%@ include file="/common/ext4_2lib.jsp"%>
		<%@ include file="/common/lib.jsp"%>
		<title><fmt:message key="pt.roleMng"/></title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">    
	    <link rel="stylesheet" type="text/css" href="${ext4}/ux/css/LiveSearchGridPanel.css" />
	    <link rel="stylesheet" type="text/css" href="${ext4}/ux/statusbar/css/statusbar.css" />
		<link rel="stylesheet" type="text/css" href="${ctx}/static/styles/themes/blue/icon.css">
		<link rel="stylesheet" type="text/css" href="${ctx}/static/styles/themes/blue/security/usermng/usermng.css">
		<link rel="stylesheet" type="text/css" href="${ctx}/static/styles/themes/blue/checkboxdisable.css">
		<script src="${ctx}/static/locale/baseSecurity_${locale}.js" type="text/javascript"></script>
		<script src="${ctx}/scripts/sysmng/security/rolemng/roleMng.js" type="text/javascript"></script>
		<style type="text/css">
			.userInfoLabelWidth{
				width: 80px;
			}
		</style>
	</head>
  		<script type="text/javascript">
  			Ext.onReady(function(){
  				 Ext.QuickTips.init();
  				 Ext.state.Manager.setProvider(Ext.create('Ext.state.CookieProvider'));
  				 eastcom.modules.roleMng.initComponent();
  			});
  			function showRoleUser(value){
  				eastcom.modules.roleMng.showRoleUser(value);
  			}
  			function showRoleResource(id,name){
  				eastcom.modules.roleMng.showRoleResource(id,name);
  			}
  		</script>
	<body>	
	</body>
</html>
