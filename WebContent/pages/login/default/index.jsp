<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
    response.setHeader("SET-COOKIE","secure ; HttpOnly");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<%@ include file="/common/lib.jsp"%>
	<title></title>
	<link href="${ctx}/pages/login/default/styles/index.css" rel="stylesheet" type="text/css" />
	<script src="${ctx}/scripts/login.js" type="text/javascript"></script>
	<script src="${ctx}/static/jslib/des/des.js" type="text/javascript"></script>
	
	<script type="text/javascript">
		function encryptPs(){
			var password = $('#password').val();
			//防止浏览器记录密码后导致密码不正常
			if(password.length <= 16){
				var p = strEnc(password, "dongxin", "eastcom", "ncsp");
				$('#password').val(p);
			}			
			return true;
		}
	</script>
	
</head>
<body>
	<div class="mainBox">
	    <div class="mainTop">
	        <div class="oneThird" name="loginPagePosition1">
	        </div>
	        <div class="oneThird" name="loginPagePosition2">
	        </div>
	        <div class="oneSixth purplishredBg_oneSixth" name="loginPagePosition3">
	        </div>
	        <div class="greenBg_oneSixth" name="loginPagePosition4">
	        	<div style="font-size:32px;margin-top:42px;" id="hminuteSecond"></div>
	        	<div id="datetime"></div>
	        	<div id="dayweek"></div>
	        </div>
	        <div class="clear"></div>
	    </div>
	    
	    <div class="mainLeft">
	        <div class="oneSixth" name="loginPagePosition5">
	        </div>
	        <div class="aHalf logo"  name="loginPagePosition6"></div>
	        <div class="clear"></div>
	        <div style="margin-top:12px;">
	        	<div class="oneSixth" name="loginPagePosition8">
	        	</div>
	        	<div class="oneSixth purplishredBg_oneSixth"  name="loginPagePosition9">
	            </div>
	        	<div class="greenBg_oneThird" name="loginPagePosition10">
		        	<form id="loginForm" action="${ctx}/login" method="post" onsubmit="return encryptPs();" autocomplete="off">
		        		<div class="alert-error">${message}</div>
		            	<div class="userVerify">
		                	<div class="userName"></div>
		                	<input style="display:none" />
		                    <input id="username" name="username" type="text" value="${username}" class="indexInput required" disableautocomplete autocomplete="off"/>
		                    <div class="clear"></div>
		                    <div style="margin:10px 0 0;">
		                    	<div class="userKey"></div>
		                    	<input name="password" type="password" id="password" class="indexInput required" disableautocomplete autocomplete="off"/>
		                    	<div class="clear"></div>
		                    </div>
		                    <%-- <c:if test="${verifyImageFlag eq 1 or verifyImageFlagCode eq 1}">
					            <div style="margin:5px 0 0 0;">
					            	区分登录方式，是页面登录还是单点登录，单点登录跳过验证码验证这一步骤
					                <input id="loginType" name="loginType" type="hidden" value="1"/>
					                <input id="verification_code" name="verification_code" type="text" class="indexInput required" style="margin-left:32px;width:80px;" disableautocomplete autocomplete="off" placeholder="请输入验证码..."></input>
									<img id="captcha_img" alt="看不清楚，换一张" title="看不清楚，换一张" src="${ctx}/getVerifyImage" onclick="changeImg();" style="width:72px;height:43px;">
					            </div>
				            </c:if> --%>
		                </div>
		                <div class="loginBtnRight">
		                	<input type="submit" class="loginBtn" value="" onmouseover="this.className='loginBtn_hover'" onmouseout="this.className='loginBtn'"/>
		                	<a href="${ctx}/forgetPassword"><fmt:message key="user.forget" /></a>
		                </div>
					</form>
	                <div class="clear"></div>
	            </div>
	            <div class="clear"></div>
	        </div>
	    </div>
	    
	    <div class="mainRight"  name="loginPagePosition7">
	    	<div class="noticeTitle">
	    		<div class="noticeIcon"></div>
	    		系统公告
	    	</div>
	    	<ul>
	    	<!-- 
	    		<li>由于新需求上线，需要于10月8日进行系统割接，届时系统将暂停使用。</li>
	    		<li>由于新需求上线，需要于10月8日进行系统割接，届时系统将暂停使用。</li>
	    		<li>由于新需求上线，需要于10月8日进行系统割接，届时系统将暂停使用。</li>
	    		<li>由于新需求上线，需要于10月8日进行系统割接，届时系统将暂停使用。</li>
	    	 -->
	    	</ul>
	    	<div class="noticeMore">
	    		<a href="#">更多...</a>
	    	</div>
	    </div>
	</div>
	<script type="text/javascript">
		function changeImg(){
			$("#captcha_img").attr('src','${ctx}/getVerifyImage?t='+(new Date()).getTime());
		}		
	</script>
</body>
</html>