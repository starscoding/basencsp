<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.eastcom_sw.core.service.security.SystemUserService"%>
<%@ page
	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%
	response.setHeader("X-UA-Compatible", "IE=8");
%>
<html>
<head>
<%@ include file="/common/lib.jsp"%>
<title></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<link rel="stylesheet" type="text/css"
	href="${jquery}/zTreeStyle/zTreeStyle.css">
<link rel="stylesheet" type="text/css" href="${cssDir}/main.css">
<link rel="stylesheet" type="text/css" href="${cssDir}/top_nav.css">
<link rel="stylesheet" type="text/css"
	href="${cssDir}/commonWindowNew.css">
<link rel="stylesheet" type="text/css" href="${cssDir}/sysnotice.css">
<link rel="stylesheet" type="text/css"
	href="${cssDir}/onlineuserList.css">
<link rel="stylesheet" type="text/css"
	href="${cssDir}/table/infoTable.css">
<link rel="stylesheet" type="text/css"
	href="${ctx}/static/styles/themes/common/userInfo.css">
<script src="${ctx}/static/locale/base_${locale}.js"
	type="text/javascript"></script>
<script src="${jquery}/jquery.tools.min.js"></script>
<script src="${jquery}/jquery.plugins.js" type="text/javascript"></script>
<script src="${jquery}/jquery.ztree.core-3.4.js" type="text/javascript"></script>
<script src="${jquery}/ux/jquery.ux.loadMask.js" type="text/javascript"></script>
<script src="${ctx}/scripts/index.js" type="text/javascript"></script>
<script src="${ctx}/static/commonjs/containerModule.js"
	type="text/javascript"></script>
<script src="${ctx}/static/commonjs/components/CommonWindowNew.js"
	type="text/javascript"></script>
<script src="${ctx}/scripts/operateMenu.js" type="text/javascript"></script>
<script src="${ctx}/scripts/main.js" type="text/javascript"></script>
<script src="${ctx}/scripts/sysnotice/sysNotice.js"
	type="text/javascript"></script>
<script src="${ctx}/scripts/userslist/userslist.js"
	type="text/javascript"></script>
<script src="${ctx}/scripts/sysnotice/NoticeWin.js"
	type="text/javascript"></script>
<script src="${ctx}/scripts/UserInfoWin.js" type="text/javascript"></script>
<script src="${ctx}/scripts/PinYin.js" type="text/javascript"></script>
<script src="${ctx}/scripts/CheckPasswordUtil.js" type="text/javascript"></script>
<script src="${ctx}/scripts/UserPasswordWin.js" type="text/javascript"></script>
<script src="${ctx}/scripts/ThemePanel.js" type="text/javascript"></script>
<script src="${ctx}/scripts/ContextMenu.js" type="text/javascript"></script>
<style type="text/css">
	body{
		-webkit-user-select:none;
		-moz-user-select:none;
		-ms-user-select:none;
		user-select:none;
	}
</style>
</head>
<script type="text/javascript">
	
<%int usersNum = 0;
			try {
				SystemUserService service = WebApplicationContextUtils
						.getRequiredWebApplicationContext(getServletContext())
						.getBean(SystemUserService.class);
				usersNum = service.getOnlineUsers().size();
				if (usersNum == 0) {
					usersNum = 1;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			String rootMenuName = request.getParameter("rootMenuName");
			if (rootMenuName == null) {
				rootMenuName = "";
			}

			String indexPageId = request.getParameter("indexPageId");
			String indexPageTitle = request.getParameter("indexPageTitle");
			String indexPageName = request.getParameter("indexPageName");
			String indexPageUrl = request.getParameter("indexPageUrl");
			out.write("var rootMenuName='"
					+ (rootMenuName == null ? "" : rootMenuName) + "';");
			out.write("var indexPageId='"
					+ (indexPageId == null ? "" : indexPageId) + "';");
			out.write("var indexPageTitle='"
					+ (indexPageTitle == null ? "" : indexPageTitle) + "';");
			out.write("var indexPageName='"
					+ (indexPageName == null ? "" : indexPageName) + "';");
			out.write("var indexPageUrl='"
					+ (indexPageUrl == null ? "" : indexPageUrl) + "';");
			out.flush();%>
	$(document).ready(function() {
		Eastcom.modules.main.initMain(
<%=usersNum%>
	);
		$(window).resize(function() {
			Eastcom.modules.main.resizeLayout();
		});
	});
</script>
<body>
	<div name="head_bg">
		<!-- 头部 -->
		<div id="north" name="northBack">
			<!-- 菜单导航栏 -->
			<div id="north_right">
				<div class="nav_left"></div>
				<div class="nav_center">
					<ul></ul>
				</div>
				<div class="nav_right"></div>
			</div>
			<div id="bottom_line" class="bottom_line"></div>
		</div>
		<!-- 任务栏 -->
		<div id="taskbar">
			<div id="taskbar_left"></div>
			<!--  tab容器区域-->
			<div id="taskbar_center">
				<div id="tabs_left_scroll"></div>
				<div id="tabs_container"></div>
				<div id="tabs_right_scroll"></div>
			</div>
			<!-- 右侧工具图标区域 -->
			<div id="taskbar_right">
				<table>
					<tr>
						<td><a id="minimenu" class="btn"
							title="<fmt:message key="main.menu"/>"></a></td>
						<td><a id="theme" class="btn"
							title="<fmt:message key="main.theme"/>"></a></td>
						<td><a id="userGuide" class="btn"
							title="<fmt:message key="main.userguide"/>"></a></td>
						<td><a id="logout" href="${ctx}/logout" class="btn"
							title="<fmt:message key="user.logout"/>"></a></td>
						<td><a id="hide_topbar" class="btn"
							title="<fmt:message key="main.hidehead"/>"></a></td>
					</tr>
				</table>
			</div>
		</div>
	</div>
	<!-- 当前位置栏-->
	<div id="funcbar">
		<div id="funcbar_left">
			<div class="breadcrumb">
				<font style="font-size: 12px;"><fmt:message
						key="main.currentposition" /></font> <span></span>
			</div>
		</div>
		<div id="funcbar_right"></div>
	</div>

	<!-- 中央区域 -->
	<div id="center" style="height: 385px;"></div>

	<!-- 覆盖层 -->
	<div id="overlay_panel"></div>

	<!-- 底部 -->
	<div id="south">
		<table>
			<tr style="position: relative;">
				<td class="left"><a id="show_desktop" class="desktop_icon btn"
					title="<fmt:message key="main.desktop"/>"></a></td>
				<td class="center">
					<div id="status_text"></div>
				</td>
				<td class="right">
					<div class="userInfo btn">
						<fmt:message key="main.hello" />
						, <a title=""> <shiro:user>
								<shiro:principal property="fullName" />
							</shiro:user>
						</a>
					</div>
					<div class="usersList btn"></div> <a id="nocbox"
					class="ipanel_tab btn" title="<fmt:message key="notify.message"/>"></a>
					<div class="numinfo">
						<fmt:message key="notify.youhave" />
						<span>0</span>
						<fmt:message key="notify.messageNum" />
					</div>
				</td>
			</tr>
		</table>
	</div>

	<!-- 收缩头部显示一级菜单面板 -->
	<div id="choiceMenu">
		<div class="choiceMenu_arrow"></div>
		<ul id="miniMenuUl"></ul>
	</div>

	<!-- 快捷桌面面板 -->
	<div id="desktop_panel">
		<div id="desktop_page" class="center"></div>
		<div class="tools-menu">
			<div class="icon"></div>
			<a hidefocus="hidefocus" class="close"
				onclick="jQuery('#show_desktop').click();"
				title="<fmt:message key="btn.close"/>"></a>
		</div>
	</div>

	<!-- 系统消息通知面板 -->
	<!-- <div id="new_noc_panel">
	   	<div id="new_noc_title">
   			<span class="noc_iterm_num"><fmt:message key="notify.youhave"/>&nbsp;<span></span>&nbsp;<fmt:message key="notify.messageNum"/></span>
   			<span class="noc_iterm_close"></span>
            <span class="noc_iterm_history"><a id="check_remind_histroy" class="btn" hidefocus="hidefocus"><fmt:message key="notify.showusernotify"/></a></span>
	   	</div> 
	   	<div id="nocbox_tips"></div>
	   	<div id="new_noc_list"></div>
	   	<div class="button">
	         <a id="ViewAllNoc" class="btn-white-big btn"  hidefocus="hidefocus"><fmt:message key="notify.allread"/></a>
	         <a id="CloseBtn" class="btn-white-big btn"  hidefocus="hidefocus"><fmt:message key="btn.close"/></a>
	      </div>					
	 </div>
	  -->
	<div style="display: none;"></div>

	<!-- 当前位置点击显示的菜单面板 -->
	<div id="menuContent" class="menuContent"
		style="display: none; position: absolute;">
		<ul id="menuTree" class="ztree"
			style="margin-top: 0; width: 250px; height: auto;"></ul>
	</div>

	<!-- 菜单面板 -->
	<div id="menu-panel">
		<!--菜单面板左边区域  -->
		<div id="l-menu-panel">
			<div class="menu-tree-panel"></div>
			<!-- 左边区域背景 -->
			<div id="l-menu-bg"></div>
			<div id="keywords-search" class="keywords-search">
				<a><fmt:message key="main.keywords"/> </a><input type="text">
			</div>
		</div>
		<!-- 菜单面板右边区域 -->
		<div id="r-menu-panel">
			<div class="menu-title"></div>
			<div id="choose-style"></div>
			<!-- 大图标菜单面板 -->
			<div id="icon-menu">
				<div class="scroll-up" style="display: block;"></div>
				<ul id="big-menu-ul"></ul>
				<div class="scroll-down" style="display: block;"></div>
			</div>
			<!-- 列表菜单面板 -->
			<div id="list-menu" style="display: none;">
				<ul id="small-menu-ul"></ul>
			</div>
			<!-- 右边区域背景 -->
			<div id="r-menu-bg"></div>
		</div>
	</div>

	<div id="baseUserInfo">
		<shiro:user>
			<li id="userName"><shiro:principal property="userName" /></li>
			<li id="sex"><shiro:principal property="sex" /></li>
			<li id="fullName"><shiro:principal property="fullName" /></li>
			<li id="email"><shiro:principal property="email" /></li>
			<li id="fixedNo"><shiro:principal property="fixedNo" /></li>
			<li id="mobileNo"><shiro:principal property="mobileNo" /></li>
			<li id="userLevel"><shiro:principal property="userLevel" /></li>
			<li id="pwdExpiredDays"><shiro:principal
					property="pwdExpiredDays" /></li>
			<li id="accoutExpiredEndtime"><shiro:principal
					property="accoutExpiredEndtime" /></li>
			<li id="times"><shiro:principal property="times" /></li>
			<li id="lastLoginTime"><shiro:principal property="lastLoginTime" /></li>
			<li id="lastLoginIp"><shiro:principal property="lastLoginIp" /></li>
			<li id="xtheme"><shiro:principal property="xtheme" /></li>
			<li id="category"><shiro:principal property="category" /></li>
		</shiro:user>
	</div>

</body>
</html>