<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/common/lib.jsp"%>
<title>忘记密码</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="${ctx}/static/styles/themes/common/forgetPassword.css" type="text/css" rel="stylesheet"/>
<script src="${ctx}/scripts/index.js" type="text/javascript"></script>
<script src="${ctx}/scripts/forgetPassword.js" type="text/javascript"></script>
</head>

<body>
<div id="mmtop">
    <p><img src="${ctx}/static/images/common/forgetPasswordImages/mmlogo.gif"/></p>
</div>
<div id="mmcon">
    <div class="mmdiv1"><img src="${ctx}/static/images/common/forgetPasswordImages/mm1top.gif"/></div>
    <div class="mmdiv2">
           
        <div id="mmszmm2" style="display: block;">
            <div class="mmleft">
                 <p class="wjmmp"> <img src="${ctx}/static/images/common/forgetPasswordImages/step2.gif"/></p>
                <ul class="mmul3">
                    <li class="mmli2"><img src="${ctx}/static/images/common/forgetPasswordImages/mm2top.gif"/></li>
                    <li class="mmli1">
                       <p class="mmp1" class="bold">尊敬的客户：<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;重置密码采用二次随机密码验证的方式，随机的验证密码已经以短消息的形式发送到您的手机上，请在下面空格中输入您收到的随机验证密码。
     密码30分钟内有效，若5分钟内仍未收到短信，可返回上一步重新获取随机验证密码。</p>
                    </li>
                    <li class="mmli3"><img src="${ctx}/static/images/common/forgetPasswordImages/mm2bot.gif"/></li>
                </ul>
                
            </div>
            <div class="mmright">
                <form id="form1" name="form1" method="post" onsubmit="return checkAuthCode()" action="${ctx}/login/validateAuthCode">
                <table class="mmtab1">
                    <tr class="tr1">
                      <td class="td1"><span class="yellow">*</span> 验证码： </td>
                      <td><input type="text" name="authCode" id="authCode"  class="mmtextfield1"/>
                      <input type="hidden" name="userName" id="userName" value=${username} />
                      <input type="hidden" name="phoneNumber" id="phoneNumber" value=${phoneNumber} />
                      </td>
                    </tr>
                    <tr>
                    	<td colspan="2" style="padding-left: 40px;color:red;">${message}
	                   	</td>
                    </tr>
                    <tr>
                        <td>&nbsp;</td>
                        <td class="td2">
                        <input type="submit" class="mmbutton1" value="下一步"/>
                        <!-- 
                        <p  onclick="checkAuthCode()"class="mmbutton1"></p>
                         -->
                        <p class="mmbutton1"><a href="${ctx}/pages/login.jsp" style="color: #fff;text-decoration: none;">返 回</a></p>
                        </td>
                    </tr>
                </table>
                </form>
            </div>
        </div>
        
        
    </div>
    <div class="mmdiv1"><img src="${ctx}/static/images/common/forgetPasswordImages/mm1bot.gif"/></div>
</div>
<div id="mmbot">${forgetFootMsg }</div>
</body>
</html>
