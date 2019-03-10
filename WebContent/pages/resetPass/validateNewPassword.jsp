<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/common/lib.jsp"%>
<title>忘记密码</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="${ctx}/static/styles/themes/common/forgetPassword.css" type="text/css" rel="stylesheet"/>
<script src="${ctx}/scripts/index.js" type="text/javascript"></script>
<script src="${ctx}/scripts/PinYin.js" type="text/javascript"></script>
<script src="${ctx}/scripts/CheckPasswordUtil.js" type="text/javascript"></script>
<script src="${ctx}/scripts/forgetPassword.js" type="text/javascript"></script>
</head>

<body>
<div id="mmtop">
    <p><img src="${ctx}/static/images/common/forgetPasswordImages/mmlogo.gif"/></p>
</div>
<div id="mmcon">
    <div class="mmdiv1"><img src="${ctx}/static/images/common/forgetPasswordImages/mm1top.gif"/></div>
    <div class="mmdiv2">
           
            <div id="mmszmm" style="display: block;">
            <div class="mmleft">
                 <p class="wjmmp"> <img src="${ctx}/static/images/common/forgetPasswordImages/step3.gif"/></p>
                <ul class="mmul3">
                    <li class="mmli2"><img src="${ctx}/static/images/common/forgetPasswordImages/mm2top.gif"/></li>
                    <li class="mmli1">
                        <p class="mmp1" class="bold">尊敬的客户：<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;已为您重置了密码，为了便于记忆，请输入新密码（密码由是由阿拉伯数字、字母、特殊符号等组成的一组字符，最多不超过18位）。</p>
                    </li>
                    <li class="mmli3"><img src="${ctx}/static/images/common/forgetPasswordImages/mm2bot.gif"/></li>
                </ul>
                
            </div>
            <div class="mmright" style="display: block;">
                <form id="form1" name="form1" method="post" onsubmit="return resetPassword()" action="${ctx}/login/validateNewPassword">
                <table class="mmtab1">
                    <tr class="tr1">
                      <td class="td1"><span class="yellow">*</span> 输入新密码： </td>
                      <td><input type="password" name="newPass" id="newPass"  class="mmtextfield1"/>
                      <input type="hidden" name="userName" id="userName" value=${username} />
                      <input type="hidden" name="phoneNumber" id="phoneNumber" value=${phoneNumber} />
                      <input type="hidden" name="userFullName" id="userFullName" value=${userFullName} />
                      </td>
                    </tr>
                    <tr class="tr1">
                      <td class="td1"><span class="yellow">*</span> 确认新密码： </td>
                      <td><input type="password" name="newPass" id="newPass1"  class="mmtextfield1"/></td>
                    </tr>
                    <tr>
                    	<td colspan="2" style="padding-left: 40px;color:red;">${message}
	                   	</td>
                    </tr>
                    <tr>
                        <td>&nbsp;</td>
                        <td class="td2">
                        <input type="submit" class="mmbutton1" value="提交"/>
                        <!-- 
                        <p class="mmbutton1"  onclick="resetPassword()">提 交</p>
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
