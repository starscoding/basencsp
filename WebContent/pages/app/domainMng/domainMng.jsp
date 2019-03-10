<%@ page language="java" pageEncoding="UTF-8"%>
<%
    request.setCharacterEncoding("UTF-8");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@ include file="/common/ext4_2lib.jsp"%>
    <%@ include file="/common/lib.jsp"%>
    <title>特殊提现</title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/styles/themes/blue/icon.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/styles/themes/blue/checkboxdisable.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/styles/themes/blue/security/usermng/usermng.css">
    <script src="${ctx}/static/locale/baseSecurity_${locale}.js" type="text/javascript"></script>
    <script src="${ctx}/scripts/PinYin.js" type="text/javascript"></script>
    <script src="${ctx}/scripts/CheckPasswordUtil.js" type="text/javascript"></script>
    <script src="${ctx}/scripts/app/domainMng/domainMng.js" type="text/javascript"></script>
    <script type="text/javascript" src="${jslib}/My97DatePicker/WdatePicker.js"></script>
</head>
<script type="text/javascript">
    Ext.onReady(function(){
        Ext.QuickTips.init();
        Ext.state.Manager.setProvider(Ext.create('Ext.state.CookieProvider'));
        rewardMng.initComponent();
    });
</script>
<body>
</body>
</html>