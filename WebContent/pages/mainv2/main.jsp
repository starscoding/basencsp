<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%
	response.setHeader("X-UA-Compatible", "IE=edge");
%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/common/lib.jsp"%>
<%@ include file="/common/bootstrap.jsp"%>
<meta charset="utf-8">
<title>安徽移动集中性能管理平台</title>
<link rel="stylesheet"
	href="${ctx}/static/styles/themes/blue/mainv2/main2.css" />
<script type="text/javascript" src="${jquery}/ux/jquery.tmpl.min.js"></script>
<script src="${ctx}/scripts/PinYin.js" type="text/javascript"></script>
<script src="${ctx}/scripts/CheckPasswordUtil.js" type="text/javascript"></script>
<c:set var="mainJs" value="${ctx}/scripts/mainv2/main" />
<%@ include file="/common/require.jsp"%>
</head>

<body>
	<div class="container-fluid">
		<div class="row head">
			<div id="logo" class="logo"></div>
			<div class="narrow-cnt" id="menu-right-narrow">
				<span class="menu-right-next"></span>
			</div>
			<div id="menu" class="menu">
				<ul id="nav" class="nav">
					<li>正在加载菜单...</li>
					<!--top-menu-tpl-->
				</ul>
				<div id="menu-panel" class="menu-panel">
					<!--menu-level2-tpl-->
				</div>
			</div>
			<div class="narrow-cnt" id="menu-left-narrow">
				<span class="menu-left-previous"></span>
			</div>

		</div>
		<div class="row tabs">
			<ul id="tabs" class="nav">
			</ul>
		</div>
		<div class="row frame" id="tabs-iframe-cnt">
			<iframe id="fakeiframe" class="active"></iframe>
		</div>
		<div id="foot" class="row foot">
			<p>2015 © Eastcom-sw. All Rights Reserved.</p>
		</div>
	</div>
	<div id="toolbar" class="toolbar">
		<div id="go-top-btn" title="回到顶部">
       		<div class="glyphicon toolbar_box" style="display:block;"><div class="toolbar_top"></div></div>
       		<div class="glyphicon toolbar_box_over" style="display:none;"><div class="toolbar_top" style="float:left;"></div><span style="float:left;">回到顶部</span><div style="clear"></div></div>
    	</div>
    	<div id="user-info-btn" title="用户信息">
			<div class="glyphicon toolbar_box" style="display:block;"><div class="toolbar_user_drafts"></div></div>
       		<div class="glyphicon toolbar_box_over" style="display:none;"><div class="toolbar_user_drafts" style="float:left;"></div><span style="float:left;">用户信息</span><div style="clear"></div></div>
       	</div>
       	<div id="users-list-btn" title="在线用户">
			<div class="glyphicon toolbar_box" style="display:block;"><div class="toolbar_online"></div></div>
       		<div class="glyphicon toolbar_box_over" style="display:none;"><div class="toolbar_online" style="float:left;"></div><span style="float:left;">在线用户</span><div style="clear"></div></div>
       	</div>
        <div id="user-guide-btn" title="用户手册"> 
       		<div class="glyphicon toolbar_box" style="display:block;"><div class="toolbar_read_mode"></div></div>
       		<div class="glyphicon toolbar_box_over" style="display:none;"><div class="toolbar_read_mode" style="float:left;"></div><span style="float:left;">用户手册</span><div style="clear"></div></div>
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
				<div class="modal-body">
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
											placeholder="密码由6-16个字符组成，区分大小写，需包含字母，数字和特殊字符"
											class="form-control">
									</div>
									<div class="form-group">
										<label class="control-label">确认密码:</label> <input
											name="retypeNewPsd" type="password" placeholder="再次输入新密码"
											class="form-control">
									</div>
								</form>
							</div>
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
	<div class="modal fade userslist" id="userslist">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">
						<span class="glyphicon glyphicon-list-alt"></span> 在线用户列表
					</h4>
				</div>
				<div class="modal-body">
					<ul class="userslist-ul" id="online-user-ul">
					</ul>
					<div id="userslist-mask" class="mask hide">
						<div class="progress">
							<div class="progress-bar progress-bar-striped active"
								role="progressbar" aria-valuenow="100" aria-valuemin="0"
								aria-valuemax="100" style="width: 100%"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="TPL">
		<div id="msg-tpl">
			<div class="alert alert-\${type} alert-dismissible" role="alert">
				<button type="button" class="close" data-dismiss="alert"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				\${msg}
			</div>
		</div>
		<div id="top-menu-tpl">
			<li><span class="single-menu"> <!-- <span name="icon" class="glyphicon \${icon}"></span> -->
					<span class="menu-name">\${nameCn}</span> <span
					class="glyphicon top-menu-arrowdown"></span>
			</span></li>
		</div>
		<div id="menu-level2-tpl">
			<div class="menu-level-2">
				<h5>
					<!--menu-level2-title-inner-tpl-->
				</h5>
				<div name="menu-level3-cnt" class="menu-level3-cnt">
					<!--insert menu-level3-tpl-->
				</div>
			</div>
		</div>
		<div id="menu-level2-title-inner-tpl">
			<span class="\${type}">\${nameCn}</span>
		</div>
		<div id="menu-level3-tpl">
			<a class="\${disabledclass}">\${nameCn}</a>
		</div>
		<div id="tab-tpl">
			<li class=""><span>\${nameCn}</span></li>
		</div>
		<div id="iframe-tpl">
			<iframe></iframe>
		</div>
		<div id="online-user-detail-tpl">
			<li><img src="${ctx}/static/images/common/userslist/\${sex}.png">
				<span class="users-label">\${nameCn}</span> <span
				class="users-label">\${department}</span> <span class="users-label">\${clientIp}</span><span
				class="users-label">\${loginTime}</span></li>
		</div>
	</div>
</body>
</html>