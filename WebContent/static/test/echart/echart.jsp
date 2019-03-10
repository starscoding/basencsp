<%@ page language="java" pageEncoding="UTF-8"%>
<%
 request.setCharacterEncoding("UTF-8");
%>
<!DOCTYPE HTML>
<html>
	<head>
		<%@ include file="/common/lib.jsp"%>
		<%@ include file="/common/echarts.jsp"%>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<script data-main="scripts/main" src="test.js"></script>
	</head>
	<body>	
		<div id="cnt" style="height:500px;width:700px"></div>
	</body>
</html>