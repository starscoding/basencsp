<%@ page language="java" pageEncoding="UTF-8"%>

<html>
<head>
<%@ include file="/common/lib.jsp"%>
<%response.setHeader("P3P","CP=CAO PSA OUR"); %>
</head>
<body>
<form name="subSysForm" action="${ctx}/login" method="post">
	<input type="hidden" name="username" value="${username}"></input>
	<input type="hidden" name="password" value="${password}"></input>
</form>
<script type="text/javascript">
	document.subSysForm.submit();
</script>
</body>
</html>