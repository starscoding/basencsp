<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="com.eastcom_sw.core.service.projectInfo.ProjectInfoConfigService"%>
<%ServletContext sc = getServletContext(); %>
<%ApplicationContext ac1 = WebApplicationContextUtils.getRequiredWebApplicationContext(sc);%>
<%String loginUrl = ac1.getBean(ProjectInfoConfigService.class).getLoginUrl(); %>
<jsp:include page="<%=loginUrl %>"></jsp:include>