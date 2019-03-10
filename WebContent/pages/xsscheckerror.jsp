<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%
	response.setStatus(200, "xss param name NOT safe");
%>

<html>
<head>
	<title>403 - 访问参数存在安全隐患,访问被拦截</title>
</head>

<body>
	<div><h1>访问参数存在安全隐患,访问被拦截.</h1></div>
	<div><a href="<c:url value="/"/>">返回首页</a></div>
</body>
</html>
