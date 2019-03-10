<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<link rel="stylesheet" type="text/css" href="${cssDir}/commonWindowNew.css">
<script src="${ctx}/static/commonjs/components/CommonWindowNew.js"
	type="text/javascript"></script>
<script>
	$(function(){
		var ___b = $.browser, ___win;
		if (___b.msie && parseInt(___b.version, 10) < 9) {
			___win = new CommonWindow(
					{
						title : '提示',
						modal : true,
						flex : false,
						height:160,
						width:350,
						buttons : [ {
							text : '下载IE',
							handler : function() {
								window.open('${ie_url}');
							}
						}, {
							text : '下载Chrome',
							handler : function() {
								window.open('${chrome_url}');
							}
						}, {
							text : '取消',
							handler : function() {
								___win.close();
							}
						} ],
						content : '<p style="color:#fff;font-size:14px;padding:0px 10px;">&nbsp&nbsp&nbsp&nbsp您好，系统检测到您当前使用的IE 浏览器版本过低，为保证最佳浏览体验，建议您升级至最新版的IE 或者Chrome 浏览器(如果您使用Windows XP 操作系统，请选择Chrome 浏览器)。</p>'
					});
			___win.show();
		}
	})
</script>