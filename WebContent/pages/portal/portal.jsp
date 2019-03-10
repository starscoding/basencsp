<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="com.eastcom_sw.common.web.CommonDataController"%>
<%
response.setHeader("X-UA-Compatible","IE=8");
%>
<html>
	<head>
		<%@ include file="/common/ext4_2lib.jsp"%>
		<%@ include file="/common/lib.jsp"%>
		<title><fmt:message key="pt.portal"/></title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<link rel="stylesheet" type="text/css" href="${ctx}/static/styles/themes/blue/icon.css">
		<link rel="stylesheet" type="text/css" href="${cssDir}/commonWindowNew.css">
		<link rel="stylesheet" type="text/css" href="${cssDir}/portal/portal.css" />
		<link rel="stylesheet" type="text/css" href="${cssDir}/portal/data-view.css" />
		<link rel="stylesheet" type="text/css" href="${cssDir}/portal/navi.css" />
		<link rel="stylesheet" type="text/css" href="${cssDir}/portal/window.css" />
		<link rel="stylesheet" type="text/css" href="${ext4}/ux/css/GroupTabPanel_${theme}.css" />
		<script src="${ctx}/static/commonjs/components/CommonWindowNew.js" type="text/javascript"></script>
		<script type="text/javascript" src="${ctx}/scripts/portal/classes/all-classes.js"></script>
	    <script type="text/javascript" src="${ctx}/scripts/portal/layoutConfig.js"></script>
	    <script type="text/javascript" src="${ctx}/scripts/portal/portal.js"></script>
	    <script type="text/javascript" src="${ctx}/scripts/portal/portletfactory/GadgetsWin.js"></script>
	    <script type="text/javascript" src="${ctx}/scripts/portal/portletfactory/LayoutWin.js"></script>
	    <script type="text/javascript" src="${ctx}/scripts/portal/dataModel.js"></script>
	    <script src="${ctx}/static/locale/baseCore_<%=WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext()).getBean(CommonDataController.class).getI18NLocale()%>.js" type="text/javascript"></script>
	    <script type="text/javascript">
	    	function showAddItemWin(id){
	    		Ext.getCmp('app-viewport').showAddItemWin(id);
	    	}
			function showChangeLayoutWin(id){
				Ext.getCmp('app-viewport').showChangeLayoutWin(id);
			}
	    	function addPortalItem(id){
	    		Ext.getCmp('app-viewport').addWin.addPortalItem(id);
	    	}
	    	function changeLayout(id){
	    		Ext.getCmp('app-viewport').changeLayoutWin.changeLayout(id);
	    	}
	    	function newPortalAction(){
	    		Ext.getCmp('tabGroup').newPortalAction();
	    	}
	        Ext.onReady(function(){
	            Ext.create('Ext.app.Portal');
	        });
	    </script>
	</head>
<body>
</body>
</html>
