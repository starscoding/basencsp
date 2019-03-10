package com.eastcom_sw.core.service.rest.security;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eastcom_sw.common.service.BaseService;
import com.eastcom_sw.common.utils.InvokeRest;
import com.eastcom_sw.core.dao.security.UserDao;
import com.eastcom_sw.core.service.security.UserService;
import com.google.gson.JsonObject;
import com.sun.jersey.api.spring.Autowire;


/**
 * 基础框架接口 通过esm的方式
 * @author fangqian
 * 
 */
@Component("BaseFrameSysmngService")
public class BaseFrameSysmngService extends BaseService{

	private static Logger log = LoggerFactory.getLogger(BaseFrameSysmngService.class);
	
	@Autowired
	private UserDao userDao;
	
	
	/**
	 * 根据查询列，查询条件返回查询结果
	 * @param JsonString
	 * @param query_columns 查询列  中间用","隔开
	 * @param query_condition 查询条件 json格式
	 * 
	 * @return
	 */
	public String getUserInfo(String JsonString){
		JSONObject rst = new JSONObject();
		boolean success = true;
		String msg = "";
		JSONObject methodRst = null;
		log.info(JsonString);
		
		if(StringUtils.isNotBlank(JsonString)){
			try{
				JSONObject jo = JSONObject.fromObject(JsonString);
				String queryColumns = jo.containsKey("query_columns") ? jo.getString("query_columns"): "";//查询列
				String queryCondition = jo.containsKey("query_condition") ? jo.getString("query_condition"): "";//查询条件
				methodRst = userDao.queryUserInfo(queryColumns,queryCondition);
			}catch(Exception e){
				e.printStackTrace();
				success = false;
				msg = "查询失败！";
			}
		}else{
			success = false;
			msg = "查询列表为空！";
		}
		if (methodRst != null) {
			rst = methodRst;
		} else {
			rst.put("success", success);
			rst.put("message", msg);
			rst.put("data", "");
		}
		String str = rst.toString();
		return str;
	}

	/**
	 * 根据角色名称查询用户信息
	 * @param JsonString
	 * @return
	 */
	public String getUserByRolename(String JsonString){
		JSONObject rst = new JSONObject();
		boolean success = true;
		String msg = "";
		JSONObject methodRst = null;
		log.info(JsonString);
		
		if(StringUtils.isNotBlank(JsonString)){
			JSONObject jo = JSONObject.fromObject(JsonString);
			String roleName = jo.containsKey("roleName") ? jo.getString("roleName"): "";
			methodRst = userDao.getUserByRolename(roleName);
		}else{
			success = false;
			msg = "查询列表为空！";
		}
		
		if (methodRst != null) {
			rst = methodRst;
		} else {
			rst.put("success", success);
			rst.put("message", msg);
			rst.put("data", "");
		}
		String str = rst.toString();
		return str;
		
	}
	
	
	public static void main(String[] args) {
		JSONObject d = new JSONObject();
		d.put("USERNAME", "root");
		
		JSONObject param = new JSONObject();
		//查询字段  ID,用户名，中文名，email，电话号码，手机号码
		param.put("query_columns", "ID_,USERNAME,FULLNAME,EMAIL_,FIXED_NO,MOBILE_NO");
		//查询条件
		param.put("query_condition", "\"" + d.toString() + "\"");
		
		JSONObject jo = new JSONObject();
		jo.put("authKey", "testaccount|pass");
		jo.put("srvId", "BaseFrameSysmngService.getUserInfo");
		jo.put("version", "v1.0");
		jo.put("reqParams", "\"" + param.toString() + "\"");
		
		String rs = InvokeRest.invokeRestService(jo.toString(),
				"http://10.8.132.71:7080/web-esm/rs/esm/delegate");
		
		System.out.println(rs);
	}
	
}
