<%@ page language="java" pageEncoding="UTF-8"%>
<%
response.setHeader("X-UA-Compatible","IE=8");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@ include file="/common/lib.jsp"%>
<title><fmt:message key="main.desktop" /></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<link rel="stylesheet" type="text/css" href="${cssDir}/commonWindowNew.css">
<link rel="stylesheet" type="text/css" href="${cssDir}/desktop/desktop.css">
<script src="${ctx}/static/locale/base_${locale}.js"
	type="text/javascript"></script>
<script type="text/javascript"
	src="${jquery}/ui/jquery-ui.custom.min.js"></script>
<script type="text/javascript"
	src="${jquery}/ui/minified/jquery.ui.draggable.min.js"></script>
<script type="text/javascript"
	src="${jquery}/ui/minified/jquery.ui.sortable.min.js"></script>
<script type="text/javascript"
	src="${jquery}/ui/minified/jquery.ui.droppable.min.js"></script>
<script type="text/javascript" src="${jquery}/ux/jquery.ux.slidebox.js"></script>
<script type="text/javascript" src="${jquery}/ux/jquery.contextmenu.js"></script>
<script src="${jquery}/jquery.json-2.4.min.js" type="text/javascript"></script>
<script type="text/javascript"
	src="${ctx}/static/commonjs/commonModule.js"></script>
<script src="${ctx}/static/commonjs/components/CommonWindowNew.js" type="text/javascript"></script>
<script type="text/javascript"
	src="${ctx}/scripts/personaldesktop/desktop.js"></script>
<script type="text/javascript"
	src="${ctx}/scripts/personaldesktop/AppsContainer.js"></script>
<script type="text/javascript"
	src="${ctx}/scripts/personaldesktop/AppIcon.js"></script>
<script type="text/javascript"
	src="${ctx}/scripts/personaldesktop/AppPage.js"></script>	
<script type="text/javascript"
	src="${ctx}/scripts/personaldesktop/AppBox.js"></script>	

<script type="text/javascript">
	$(document).ready(function() {
		Desktop.initDesktop();
	});
</script>

<title><fmt:message key="main.personaldesktop" /></title>
</head>
<body style="background-color: transparent">
	<!-- 屏幕滑动条 -->
	<div id="control" class="control">
		<table align="center">
			<tbody>
				<tr>
					<td class="control-l"></td>
					<td class="control-c"></td>
					<td class="control-r"><a id="openAppBox" class="cfg"
						href="javascript: void(0)"
						title="<fmt:message key="main.desktop.settings"/>"></a></td>
				</tr>
			</tbody>
		</table>
	</div>

	<!-- 快捷桌面图标 -->
	<div class="slidebox">
		<!-- 回收站 -->
		<div id="trash" class="ui-droppable"></div>
		<div id="container"></div>
	</div>
	<div id="overlay"></div>

	<!-- 快捷桌面设置 -->
	<div id="setting_panel">
		<div id="desktopSetting" class="desktop_box">
			<div class="desktop_box_tit">
				<div class="seting_logo"></div>
				<a hidefocus="hidefocus" id="closeSettingBox" class="close"
					href="javascript:;" title="<fmt:message key="btn.close"/>"></a>
			</div>
			<div id="bar">
				<div id="btnAppSet" class="barDiv">
					<div class="btnicon"></div>
					<span class="float_l"><fmt:message
							key="main.desktop.settings1" /></span>
				</div>
				<div id="btnScreenSet" class="barDiv">
					<div class="btnicon"></div>
					<span class="float_l"><fmt:message
							key="main.desktop.settings2" /></span>
				</div>
				<div id="portalSettingMsg">
					<span></span>
				</div>
			</div>
			<div id="appPageAll">
				<div id="appPageDom">
					<div id="app_cate_list">
						<div class="scroll-up"></div>
						<ul></ul>
						<div class="scroll-down"></div>
					</div>
					<div id="app_list_box">
						<ul></ul>
						<div class="clearfix"></div>
					</div>
				</div>
				<div id="screenPageDom">
					<div id="screen_list">
						<div class="clearfix"></div>
						<ul></ul>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- 个人桌面右击菜单 -->
	<div id="rightMenu"></div>
</body>
</html>