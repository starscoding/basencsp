<%@ page language="java" pageEncoding="UTF-8"%>
<%
 request.setCharacterEncoding("UTF-8");
%>
<!DOCTYPE HTML>
<html>
	<head>
		<%@ include file="/common/lib.jsp"%>
		<%@ include file="/common/bootstrap.jsp"%>
		<%@ include file="/common/fontawesome/fontawesome4.3.0.jsp"%>
		<%@ include file="/common/eui.jsp"%>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<c:set var="mainJs" value="test" />
		<%@ include file="/common/require.jsp"%>
	</head>
	<body>	
		<div id="cnt" style="height:500px;width:700px"></div>
		<div id="cnt2" style="height:500px;width:700px"></div>
	</body>
</html>