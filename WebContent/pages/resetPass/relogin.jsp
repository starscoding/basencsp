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
           
            <div id="mmszmm3" style="display: block;">
            <div class="mmleft">
                <p class="wjmmp"><img src="${ctx}/static/images/common/forgetPasswordImages/cxdla.gif"/></p>
                <ul class="mmul3">
                    <li class="mmli2"><img src="${ctx}/static/images/common/forgetPasswordImages/mm2top.gif"/></li>
                    <li class="mmli1">
                        <p class="mmp1">尊敬的客户：<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您已成功设置新密码，请重新登录。</p>
                    </li>
                    <li class="mmli3"><img src="${ctx}/static/images/common/forgetPasswordImages/mm2bot.gif"/></li>
                </ul>
                
            </div>
            <div class="mmright">
                <p class="pp1"><a href="${ctx}/pages/login.jsp"><img src="${ctx}/static/images/common/forgetPasswordImages/cxdl.gif"/></a></p>
            </div>
        </div>
        
        
    </div>
    <div class="mmdiv1"><img src="${ctx}/static/images/common/forgetPasswordImages/mm1bot.gif"/></div>
</div>
<div id="mmbot">${forgetFootMsg }</div>
</body>
</html>
