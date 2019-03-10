package com.eastcom_sw.core.service.rest.cas;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eastcom_sw.common.dao.redis.BaseRedisDao;
import com.eastcom_sw.common.entity.ShiroUser;
import com.eastcom_sw.common.utils.CommonUtil;
import com.eastcom_sw.common.utils.Constants;
import com.eastcom_sw.common.web.BaseController;

@Controller
@RequestMapping(value="/rs2")
public class CasSecurityService extends BaseController{
	
	@Autowired
    private BaseRedisDao baseRedisDao;
	
	protected Logger log = LoggerFactory.getLogger(getClass());
	
	@RequestMapping(value="/redirect")
    @ResponseBody
	public void redirect(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="service",defaultValue="")String service){
		ShiroUser user = CommonUtil.getUser();
		try{
			if(StringUtils.isNotBlank(service)){
				UUID uid = UUID.randomUUID();
				JSONObject res = new JSONObject();
				res.element("username", user.getUserName());
				res.element("service", service);
				baseRedisDao.setValue(String.format("sso:token:%s",uid.toString()), res.toString());
				if(service.indexOf("[?]")==-1){
					service+="?token="+uid;
				}else{
					service+="&token="+uid;
				}
				
				response.sendRedirect(service);
				
//				request.getRequestDispatcher(service).forward(request, response);
			}
		}catch(Exception e){
			
		}
	}
	
	@ResponseBody
	@RequestMapping(value="/cas",method=RequestMethod.POST)
    public Object cas(HttpServletRequest request,
			@RequestParam(value="token",defaultValue="")String token,
			@RequestParam(value="vcode",defaultValue="")String vcode){
		JSONObject res = new JSONObject();
		boolean resultState = false;
		long lStart = Calendar.getInstance().getTimeInMillis();
		String userIp = this.getCurrentUserIP(request), username = "", msg = "";
		res.element("success", "false");
		try{
			String key = String.format("sso:token:%s",token);
			String value = baseRedisDao.getValue(key);
			JSONObject user = JSONObject.fromObject(value);
			res.element("success", "true");
			res.element("user", user);
			res.element("msg", "");
			username = user.getString("username");
			msg = user.toString();
			res.remove("service");
			baseRedisDao.deleteKey(key);
			resultState=true;
		}catch(Exception e){
			res.element("msg", "系统错误");
			log.error("cas接口错误"+e.toString());
		}
		
		int opttime = (int) (Calendar.getInstance().getTimeInMillis() - lStart);
		//saveSysLog(userIp, username, resultState, Constants.SYSLOGSOPERATETYPE_OTHER, "login", msg, opttime);
		log.info(res.toString());
		return res.toString();
	}
	
	@RequestMapping(value="/test")
	public void cas(HttpServletResponse response,HttpServletRequest request,
			@RequestParam(value="token",defaultValue="")String token){
//		try {
//			cas(request,token,"");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		cas(request,token,"");
	}
}
