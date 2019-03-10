<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
    response.setHeader("SET-COOKIE", "secure ; HttpOnly");
%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/common/lib.jsp"%>
<%@ include file="/common/bootstrap.jsp"%>
<%@ include file="/common/fontawesome/fontawesome4.3.0.jsp"%>
<meta charset="utf-8">
<title>${name}</title>
<link rel="stylesheet"
	href="${ctx}/static/styles/themes/common/mainv/main-default.css" />
<link rel="stylesheet"
	href="${ctx}/scripts/mainv/pop/css/pop.css" />
<script type="text/javascript"
	src="${ctx}/static/jslib/mustache.js-2.0.0/mustache.min.js"></script>
<script type="text/javascript"
	src="${ctx}/scripts/mainv/pop/pop.js"></script>
<script type="text/javascript">
	var __CONFIG = {
		menu : '<c:out value="${menu}"/>',
		theme : '<c:out value="${mainTheme}"/>',
		menuNum : '<c:out value="${menuNum}"/>',
		leftTreeParent : '<c:out value="${leftTreeParent}"/>',
		defaultPageLocation : '<c:out value="${defaultPageLocation}"/>',
		defaultPageName : '<c:out value="${defaultPageName}"/>',
		showTabBar : '<c:out value="${showTabBar}"/>',
		showFoot : '<c:out value="${showFoot}"/>',
		showToolbar : '<c:out value="${showToolbar}"/>',
		footMsg : '<c:out value="${footMsg}"/>',
		tabMaxNum : '<c:out value="${tabMaxNum}"/>'
	}
	
	window.onload=function(){
		showNotifications();
	}
</script>
<script src="${ctx}/scripts/PinYin.js" type="text/javascript"></script>
<script src="${ctx}/scripts/CheckPasswordUtil.js" type="text/javascript"></script>
<c:set var="mainJs" value="${ctx}/scripts/mainv/main" />
<%@ include file="/common/require.jsp"%>
</head>
<body>
	<div class="container-fluid" id="main-cnt">
	
		<div class="row head">
			<div id="logo" class="logo"></div>
			<div class="narrow-cnt" id="menu-right-narrow">
				<span class="menu-right-next hide"></span>
			</div>
			<div id="menu" class="menu">
				<ul id="nav" class="nav">
					<li>正在加载...</li>
					<!--top-menu-tpl-->
				</ul>
				<div id="menu-panel" class="menu-panel" style="max-height:300px;min-width:200px;overflow: auto;">
					<!--menu-level2-tpl-->
				</div>
			</div>
			<div class="narrow-cnt" id="menu-left-narrow">
				<span class="menu-left-previous hide"></span>
			</div>
		</div>
		<div class="row tabs hide" id="tabs-row" style="overflow: hidden;white-space:nowrap;">
			<ul id="tabs" class="nav">
			</ul>
		</div>
		<div id="content" class="content hide-left-tree" style="margin-left:-15px;margin-right: -15px;">
			<div id="menu_tree" class="left-tree">
				<div class="title">
					<span>我的快捷方式</span>
				</div>
				<div id="left-tree-cnt" class="left-tree-cnt">
					<ul class="list" id="left-menu-list">
					</ul>
				</div>
				<div class="collapse_cnt">
					<span>我的快捷方式</span>
					<div class="collapse-ico hide">
						<i class="fa fa-angle-left"></i>
					</div>
				</div>
			</div>
			<div class="frame" id="tabs-iframe-cnt">
				<iframe id="fakeiframe" class="active"></iframe>
			</div>
		</div>
		<div id="pop" style="display: none;">
			<div id="popHead" style="top:-20px;">
				<a id="popClose" title="关闭">关闭</a>
				<h2>温馨提示</h2>
			</div>
			<div id="popContent">
				<dl>
					<dt id="popTitle"></dt>
					<dd id="popIntro"></dd>
				</dl>
			</div>
		</div>
		<!--右下角pop弹窗 end-->
		<!-- 
		<div id="foot" class="row foot hide">
			<p>2015 © Eastcom-sw. All Rights Reserved.</p>
		</div>
		 -->
		<div id="context-menu" style="">
	      	<ul class="dropdown-menu" role="menu">
               <li><a tabindex="-1">关闭当前页面</a></li>
	           <li><a tabindex="-1">添加到快捷方式</a></li>
	      	</ul>
		</div>
	</div>
	<div id="toolbar" class="toolbar hide" style="text-align: right;" >
		<div class="toolbar-line" style="visibility:hidden;">
			<div id="go-top-btn" title="回到顶部" class="glyphicon toolbar_box"
				style="display: block;">
				<div class="toolbar_top"></div>
			</div>
			<div style="clear: both"></div>
		</div>
		<div class="toolbar-line" style="visibility:hidden;">
			<div id="user-info-btn" title="用户信息" class="glyphicon toolbar_box">
				<div class="toolbar_user_drafts"></div>
			</div>
			<div id="logout-btn" class="glyphicon toolbar_box hover_control"
				title="退出系统">
				<div class="toolbar_user_logout"></div>
			</div>
			<div style="clear: both"></div>
		</div>
		<div class="toolbar-line" style="visibility:hidden;">
			<div id="users-list-btn" title="在线用户" class="glyphicon toolbar_box" style="display: block;">
				<div class="toolbar_online"></div>
			</div>
			<div style="clear: both"></div>
		</div>
		<div class="toolbar-line" style="visibility:hidden;">
			<div id="user-guide-btn" title="用户手册" class="glyphicon toolbar_box" style="display: block;">
				<div class="toolbar_read_mode"></div>
			</div>
			<div style="clear: both"></div>
		</div>
	</div>
	<div id="msg-cnt" class="msg-cnt">
		<!--insert msg-tpl-->
	</div>
	<div class="userinfo modal fade" id="userinfo">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">
						<span class="glyphicon glyphicon-user"></span> 您好，
						<shiro:principal property="fullName" />
					</h4>
				</div>
				<div class="modal-body" >
					<div id="info-content" class="row">
						<div class="tab-cnt">
							<ul class="nav">
								<li class="active" name="tab-idx-1"><a href="#"
									data-target="#tab_1_1" data-toggle="tab" aria-expanded="true"><span
										class="glyphicon glyphicon-th-list"></span> 基本信息</a></li>
								<li name="tab-idx-2"><a href="#" data-target="#tab_1_2"
									data-toggle="tab" aria-expanded="true"><span
										class="glyphicon glyphicon-cog"></span> 修改信息</a></li>
								<li name="tab-idx-3"><a href="#" data-target="#tab_1_3"
									data-toggle="tab" aria-expanded="true"><span
										class="glyphicon glyphicon-pencil"></span> 修改密码</a></li>
								<c:if test="${userExtEnabled eq 1 }">
									<li name="tab-idx-4"><a href="#" data-target="#tab_1_4"
									data-toggle="tab" aria-expanded="true"><span
										class="glyphicon glyphicon-tasks"></span> 扩展信息</a></li>
								</c:if>		
							</ul>
							<!-- 
								<div class="login-times-cnt">
									<span class="login-times"><shiro:principal
											property="times" /></span> <span class="login-times-label">次登陆</span>
								</div> -->
						</div>
						<div class="tab-content-cnt">
							<div id="tab_1_1" class="active tab-content">
								<div class="row">
									<div class="col-md-5">用户名:</div>
									<div class="col-md-7">
										<shiro:principal property="userName" />
									</div>
								</div>
								<div class="row">
									<div class="col-md-5">中文名:</div>
									<div class="col-md-7">
										<shiro:principal property="fullName" />
									</div>
								</div>
								<div class="row">
									<div class="col-md-5">手机号码:</div>
									<div class="col-md-7">
										<shiro:principal property="mobileNo" />
									</div>
								</div>
								<div class="row">
									<div class="col-md-5">电话号码:</div>
									<div class="col-md-7">
										<shiro:principal property="fixedNo" />
									</div>
								</div>
								<div class="row">
									<div class="col-md-5">登陆次数:</div>
									<div class="col-md-7">
										<shiro:principal property="times" />
									</div>
								</div>
								<div class="row">
									<div class="col-md-5">上次登陆时间:</div>
									<div class="col-md-7">
										<shiro:principal property="lastLoginTime" />
									</div>
								</div>
							</div>
							<div id="tab_1_2" class="tab-content">
								<form id="user-info-form">
									<div class="form-group">
										<label class="control-label">电话号码:</label> <input type="text"
											name="fixedNo" placeholder="格式:区号-电话号码,例:010-88888888"
											class="form-control"
											value="<shiro:principal property="fixedNo"/>">
											
										<input type="text" style="display:none;" name="userName" value="<shiro:principal property="userName"/>">
										<input type="text" style="display:none;" name="userFullName" value="<shiro:principal property="fullName"/>">
									</div>
									<div class="form-group">
										<label class="control-label">手机号码:</label> <input type="text"
											name="mobileNo" placeholder="格式:11位手机号码,例:13888888888"
											class="form-control"
											value="<shiro:principal property="mobileNo"/>">
									</div>
									<div class="form-group">
										<label class="control-label">电子邮箱:</label> <input type="text"
											name="email" placeholder="格式:用户@域名.com,例:all@163.com"
											class="form-control"
											value="<shiro:principal property="email"/>">
									</div>
								</form>
							</div>
							<div id="tab_1_3" class="tab-content">
								<form id="psd-form">
									<div class="form-group">
										<label class="control-label">原密码:</label> <input name="oldPsd"
											type="password" placeholder="请输入原密码" class="form-control">
									</div>
									<div class="form-group">
										<label class="control-label">新密码:</label> <input name="newPsd"
											type="password"
											placeholder="密码由8-16个字符组成，区分大小写，需包含字母，数字和特殊字符"
											class="form-control">
									</div>
									<div class="form-group">
										<label class="control-label">确认密码:</label> <input
											name="retypeNewPsd" type="password" placeholder="再次输入新密码"
											class="form-control">
									</div>
								</form>
							</div>
							<c:if test="${userExtEnabled eq 1 }">
								<div id="tab_1_4" class="tab-content">
									<form id="ext-form">
										<input id="extUserId" type="hidden" name="extUserId"  value="${extUserId }" >
										<input id="userExtTable" type="hidden" name="userExtTable"  value="${userExtTable }" >
										<c:forEach var="extList" items="${userExtMapping }">
											<div class="form-group">
												<label class="control-label">${extList.nameCn }:</label> <input type="text"
													id="${extList.name }" name="${extList.name }" class="form-control" value="${extList.extValue }" >
											</div>
										</c:forEach>
									</form>
								</div>
							</c:if>
							
							
						</div>
					</div>
					<div id="userinfo-mask" class="mask hide">
						<div class="progress">
							<div class="progress-bar progress-bar-striped active"
								role="progressbar" aria-valuenow="100" aria-valuemin="0"
								aria-valuemax="100" style="width: 100%"></div>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" name="cancel" class="btn btn-default"
						data-dismiss="modal">取消</button>
					<button id="submit-userinfo" type="button" name="save"
						class="btn btn-primary hide">保存</button>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="${ctx}/scripts/mainv/dynamicTabWidth.js"></script>
	<script type="text/javascript">
		$('#toolbar').mouseover(
			function(){
				$('.toolbar-line').css('visibility','visible');	
			}
		);
		
		$('#toolbar').mouseout(
			function(){
				$('.toolbar-line').css('visibility','hidden');	
			}
		);
	</script>
</body>
</html>