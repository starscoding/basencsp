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
        
        <div id="mmszmm1">
            <div class="mmleft">
            	<p class="wjmmp1"><img src="${ctx}/static/images/common/forgetPasswordImages/step1.gif"/></p>
                
                <ul class="mmul3">
                    <li class="mmli2"><img src="${ctx}/static/images/common/forgetPasswordImages/mm2top.gif"/></li>
                    <li class="mmli1">
                         <p class="mmp1" class="bold">尊敬的客户：<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     请在右侧区域输入您的手机号码，并点击提交。系统将对您输入的手机号码进行验证，若与系统中登记的资料相符，系统将以短信的形式向您的手机发送随机验证密码。</p>
                    </li>
                    <li class="mmli3"><img src="${ctx}/static/images/common/forgetPasswordImages/mm2bot.gif"/></li>
                </ul>
                
            </div>
            <div class="mmright">
                <form id="form1" name="form1" method="post" onsubmit="return checkPhoneNumber()" action="${ctx}/login/validatePhoneNumber">
           
                <table class="mmtab1">
                	<tr class="tr1">
                      <td class="td1"><span class="yellow">*</span> 用户账号： </td>
                      <td><input type="text" name="userName" id="userName"  class="mmtextfield1"/></td>
                    </tr>
                    <tr class="tr1">
                      <td class="td1"><span class="yellow">*</span> 手机号码： </td>
                      <td><input type="text" name="phoneNumber" id="phoneNumber"  class="mmtextfield1"/></td>
                    </tr>
	                <tr>
	                   	<td colspan="2" style="padding-left: 40px;color:red;">${message}
	                   	</td>
	                </tr>
                    <tr>
                        <td>&nbsp;</td>
                        <td class="td2">
                        <input type="submit" class="mmbutton1" value="下一步"/>
                        <p class="mmbutton1" onClick="history.back();"><a href="${ctx}/pages/login.jsp" style="color: #fff;text-decoration: none;">返 回</a></p>
                        </td>
                    </tr>
                </table>
                </form>
            </div>
        </div>
     
    </div>
    <div class="mmdiv1"><img src="../static/images/common/forgetPasswordImages/mm1bot.gif"/></div>
</div>
<!-- 
<div id="mmbot">中国移动通信集团浙江有限公司 版权所有 2009-2015</div>
 -->
 <div id="mmbot">${forgetFootMsg }</div>
</body>
</html>
