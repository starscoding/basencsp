/**
 *
 */
package com.eastcom_sw.core.web;

import com.eastcom_sw.common.service.BaseService;
import com.eastcom_sw.common.sms.SmsResponse;
import com.eastcom_sw.common.utils.*;
import com.eastcom_sw.common.web.BaseController;
import com.eastcom_sw.core.dao.redis.RedisKeyValuesDao.RedisBean;
import com.eastcom_sw.core.entity.security.User;
import com.eastcom_sw.core.service.configuration.ArgumentsConfigurateService;
import com.eastcom_sw.core.service.redis.RedisKeyValuesService;
import com.eastcom_sw.core.service.security.SystemUserService;
import com.eastcom_sw.core.service.security.UserService;
import com.eastcom_sw.core.web.filter.login.FilterChainUser;
import com.eastcom_sw.core.web.filter.login.FilterReq;
import com.eastcom_sw.core.web.filter.login.FilterResp;
import com.eastcom_sw.core.web.filter.login.LoginFilter;
import com.eastcom_sw.security.extension.service.UserExtensionService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.mapper.JsonMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author SCM
 * @time 下午5:32:34
 */
@Controller
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LoginController extends BaseController {

	public Logger log = LoggerFactory.getLogger(getClass());

	private static Map<String, Object> map =  new HashMap<String, Object>();

	private final static String AuthenticationCode="AuthenticationCode";

	protected static JsonMapper binder = JsonMapper.nonDefaultMapper();

	@Autowired
	private SystemUserService systemUserService;
	@Autowired
	private UserService userService;

	@Autowired
	private ArgumentsConfigurateService argumentsConfigurateService;

	@Autowired(required = false)
	private LoginFilter loginFilter;

	@Autowired
	private BaseService baseService;

	@Autowired
	private UserExtensionService userExtensionService;

	@Autowired
	private RedisKeyValuesService redisKeyValuesService;

	@RequestMapping(value = "/shiro-cas", method = RequestMethod.GET)
	public String cas() {
		return null;
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return "redirect:/pages/login.jsp";
	}

	/**
	 *
	 * 方法说明：忘记密码
	 * @Title: forgetPassword
	 * @author: fangqian
	 * @return
	 * @return String
	 * @date: 2015年8月14日 下午2:42:01
	 */
	@RequestMapping(value = "/forgetPassword", method = RequestMethod.GET)
	public String forgetPassword() {
		return "redirect:/pages/forget.jsp";
	}

	/**
	 *
	 * 方法说明：根据用户名和手机号码验证该用户是否存在 ，如果存在则向该手机号码发送验证码
	 * @Title: validatePhoneNumber
	 * @author: fangqian
	 * @param request
	 * @param response
	 * @return void
	 * @date: 2015年8月18日 下午4:42:39
	 */
	@RequestMapping(value = "/login/validatePhoneNumber", method = RequestMethod.POST)
	public String validatePhoneNumber(HttpSession session,
                                      HttpServletRequest request, HttpServletResponse response, Model model){
		String userName = request.getParameter("userName");
		String phoneNumber = request.getParameter("phoneNumber");
		boolean flag = true;
		String errorPage = "forget";
		try {
			if((userName != null && !"".equals(userName)) && (phoneNumber != null && !"".equals(phoneNumber))){

				//验证该用户是否存在
				flag = userService.userExsist(userName,phoneNumber);

				if(flag){
					//发送短信验证码
					String sendContext = authenticationCode();//随机生成6位验证码
					request.setAttribute("verification_code", sendContext);
					boolean result = sendAuthenticationCodeToPhone(phoneNumber,userName);
					if(!result){
						model.addAttribute("message", "短信发送失败，请联系管理员！");
						return errorPage;
					}

				}else{
					model.addAttribute("message", "身份验证失败，请确认后输入！");
					return errorPage;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", "身份验证发生错误！");
			return errorPage;
		}
		model.addAttribute("username", userName);
		model.addAttribute("phoneNumber", phoneNumber);
		return "resetPass/validateAuthCode";
	}



	@SuppressWarnings("unchecked")
	public boolean sendAuthenticationCodeToPhone(String phoneNumber, String userName){
		boolean flag = true;
		String result = "";
		String exeStatus = "";//短信回执参数
		String sendContext = authenticationCode();//随机生成6位验证码
		map.put(AuthenticationCode+userName, sendContext);
		sendContext="您的验证码是:"+sendContext+",请输入此验证码进行密码重置----移动网络投诉处理平台";

		log.info(sendContext);
		log.info("key:"+AuthenticationCode+userName);
		log.info("验证码："+map.get(AuthenticationCode+userName));


		Map<String,Object> smParams = new HashMap<String, Object>();
		Map<String,Object> reqParams = new HashMap<String, Object>();
		reqParams.put("content", sendContext);
		reqParams.put("mobileNos", phoneNumber);
		reqParams.put("modelName", "testzhj");
		reqParams.put("sendNoSuffix", "");

		smParams.put("authKey", "testaccount|pass");//用户名密码 ，用|隔开
		smParams.put("reqParams", JSONObject.fromObject(reqParams).toString());
		smParams.put("srvId", "ReceiveSmsServiceImpl.receiveSms");//默认
		smParams.put("version", "v1.0");//默认

		String params = JSONObject.fromObject(smParams).toString();

		log.info("短信参数"+params);

		//系统数据配置  短信接口的地址  格式：http://{host}:{port}/{webcontext}/rs/esm/delegate
		//http://10.212.40.166:7080 测试地址
		//String url = "http://10.212.40.166:7080";
		String url = baseService.getCommonFieldValueByName("smpp_url","value");
		url = url + "/rs/esm/delegate";

		log.info("短信接口url:"+url);

		try {
			//调用短信接口发送短信   详细内容参见：网络投诉接口说明-短信发送平台.docx文档

			result = InvokeRest.invokeRestService(params, url);

			Map<String,String> map = (Map<String, String>) JSONObject.fromObject(result);
			exeStatus = map.get("resultCode");

			if(!"200".equals(exeStatus)){
				flag = false;
			}
		} catch (Exception e) {
			log.error("短信发送失败，请联系管理员！",e);
			flag = false;
		}

		return flag;
	}

	/**
	 *
	 * 方法说明：确认验证码
	 * @Title: validateAuthCode
	 * @author: fangqian
	 * @param session
	 * @param request
	 * @param response
	 * @return void
	 * @date: 2015年8月20日 下午3:11:59
	 */
	@RequestMapping(value = "/login/validateAuthCode", method = RequestMethod.POST)
	public String validateAuthCode(HttpSession session,
                                   HttpServletRequest request, HttpServletResponse response, Model model){
		String userName = request.getParameter("userName");
		String phoneNumber = request.getParameter("phoneNumber");
		String authCode = request.getParameter("authCode");
		String code = "";
		String errorPage = "resetPass/validateAuthCode";
		if(map.get(AuthenticationCode+userName) != null){
			code = map.get(AuthenticationCode+userName).toString();
		}
		if(authCode != null && !"".equals(authCode)){
			if(!code.equals(authCode)){
				model.addAttribute("username", userName);
				model.addAttribute("phoneNumber", phoneNumber);
				model.addAttribute("message", "验证失败，请重试！");
				return errorPage;
			}
		}else{
			model.addAttribute("username", userName);
			model.addAttribute("phoneNumber", phoneNumber);
			model.addAttribute("message", "请输入验证码！");
			return errorPage;
		}

		User user = userService.getUserInfo(userName, phoneNumber);
		if(user != null){
			model.addAttribute("userFullName", user.getFullName());
		}
		model.addAttribute("username", userName);
		model.addAttribute("phoneNumber", phoneNumber);
		return "resetPass/validateNewPassword";
	}


	/**
	 *
	 * 方法说明：设置新密码
	 * @Title: updatePassword
	 * @author: fangqian
	 * @param session
	 * @param request
	 * @param response
	 * @return void
	 * @date: 2015年8月21日 下午2:08:03
	 */
	@RequestMapping(value = "/login/validateNewPassword", method = RequestMethod.POST)
	private String updatePassword(HttpSession session, HttpServletRequest request, HttpServletResponse response, Model model) {
		long start = Calendar.getInstance().getTimeInMillis();

		String userName = request.getParameter("userName");
		String phoneNumber = request.getParameter("phoneNumber");
		String password = CommonUtil.encodePassword(request.getParameter("newPass"));// 新密码

		User u = userService.getUserInfo(userName,phoneNumber);
//		JSONObject psd = userExtensionService.queryUserPsdAndOldPsd(u.getId());
//		String oldPassword = psd.getString("password");
//		String originOld = psd.getString("oldPassword");
		String oldPassword = "";
		String originOld ="";
		String user = "";
		String fullName = "";
		String userId = "";
		if(u != null){
			oldPassword = u.getPassWrod();
			originOld = u.getOldPassord();
			user = u.getUserName();
			fullName = u.getFullName();
			userId = u.getId();
		}else{
			model.addAttribute("username", userName);
			model.addAttribute("phoneNumber", phoneNumber);
			model.addAttribute("message", "密码设置失败，请重试！");
			return "resetPass/validateNewPassword";
		}
		CustomBundleReader cbr = new CustomBundleReader("i18n.SecurityExtBundle", this.getI18NLocale());

		// 判断是否可进行密码更新，不行则返回错误代码

		// 判断用户是否已被锁定
		if (userExtensionService.getPsdChangeErrorTimes(user) >= RedisKeyConstants.ALLOW_PSD_CHANGE_ERROR_TIMES) {
			model.addAttribute("username", userName);
			model.addAttribute("phoneNumber", phoneNumber);
			model.addAttribute("userFullName", fullName);
			model.addAttribute("message", "用户已锁定，请联系管理员！");
			return "resetPass/validateNewPassword";
		}

		try {
			String ValidMsg = PasswordUtil.validatePwd(u.getUserName(), request.getParameter("newPass"), u.getFullName());
			if(ValidMsg != null && ValidMsg.length() > 0){
				model.addAttribute("username", userName);
				model.addAttribute("phoneNumber", phoneNumber);
				model.addAttribute("userFullName", fullName);
				model.addAttribute("message", ValidMsg);
				return "resetPass/validateNewPassword";
			}

			if (originOld == null || originOld.isEmpty()) {
			} else {
				if (originOld.indexOf("|") != originOld.lastIndexOf("|")) {
					oldPassword = originOld.substring(originOld.indexOf("|") + 1)
							+ "|" + oldPassword;
				} else {
					oldPassword = originOld + "|" + oldPassword;
				}
			}
			userService.updateUserPassword(password,oldPassword,userId, DateUtil.getCurrentDatetime());
			saveSysLog(request, true, Constants.SYSLOGSOPERATETYPE_EDIT,
					"user_psd_change", String.format(cbr.getString("user.updatepsdsuccess"),fullName),
					(int) (Calendar.getInstance().getTimeInMillis() - start));
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("username", userName);
			model.addAttribute("phoneNumber", phoneNumber);
			model.addAttribute("userFullName", fullName);
			model.addAttribute("message", "密码设置失败！");
			return "resetPass/validateNewPassword";
		}
		return "resetPass/relogin";
	}

	/**
	 * 用户登录(前台js加密后登录,后台解密然后再执行数据库加密操作)
	 *
	 * @param username
	 * @param password
	 * @param rememberMe
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM) String username,
			@RequestParam(FormAuthenticationFilter.DEFAULT_PASSWORD_PARAM) String password,
			Model model) throws Exception {
		//获取登录方式 单点登录不经过验证码验证这一步骤
		String loginType = request.getParameter("loginType");

		Boolean extOpenFlag = (Boolean) session
				.getAttribute(Constants.EXT_OPEN_FLAG);
		String EXT_SYSTEM_ID = "";// 系统名称(单点登陆时使用)
		if (extOpenFlag == null) {
			extOpenFlag = false;
		}

		if (extOpenFlag) {
			session.setAttribute(Constants.EXT_OPEN_FLAG, false);// 单点登陆一次之后重新设置为false
			Object extSysId = session.getAttribute(Constants.EXT_SYSTEM_ID);
			if (extSysId != null) {
				EXT_SYSTEM_ID = extSysId.toString();
			}
		}

		String errorpage = "login";
		String mainPage = "";
		String newMainPageFlag = baseService.getCommonFieldValueByName("newMainPageFlag","value");//网投系统是否采用新版风格
		if(newMainPageFlag != null && newMainPageFlag.equals("1")){
			mainPage = "redirect:/main/v";
			//mainPage = "redirect:/pages/main.jsp";
		}else{
			mainPage = "redirect:/pages/main.jsp";
		}

		String correctpage = extOpenFlag ? "redirect:/pages/sso/moduleChoice.jsp"
				: mainPage;


		//从页面登录的用户需要经过验证码验证这一步骤
		if(loginType!=null&&"1".equals(loginType)){
			//登录是否有验证码（配置在系统数据维护中）
			String authCodeFlag = baseService.getCommonFieldValueByName("login_verification_code",
					"value");
			//需要验证码验证
			if(authCodeFlag != null && "1".equals(authCodeFlag)){
				String authCode = request.getParameter("verification_code");
				String verifyCode = (String)request.getSession().getAttribute(Constants.SIMPLE_CAPCHA_SESSION_KEY);
				String[] str = verifyCode.split("&&");
				String code = str[0];
				Long expirydate = Long.valueOf(str[1]);
				Long nowTime = Calendar.getInstance().getTimeInMillis();


				if(code.toLowerCase().equals(authCode.toLowerCase())){
					if(expirydate < nowTime){
						model.addAttribute("message", "验证码已失效，请重新输入！");
						return errorpage;
					}
				}else{
					model.addAttribute("message", "验证码输入错误！");
					return errorpage;
				}

			}
		}


		//验证登录IP是否锁定（配置在系统数据维护中）
		String ipLimitFlag = baseService.getCommonFieldValueByName("IpLimitFlag","value");
		String ipLimitTimes = baseService.getCommonFieldValueByName("IpLimitTimes","value");//限制IP登录错误次数
		String ipLimitTime = baseService.getCommonFieldValueByName("IpLimitTime","value");//限制IP锁定时间
		String sendMsgFlag = baseService.getCommonFieldValueByName("send_message_for_user","value");//发短信通知用户（默认不发送）
		//需要验证

		String userIP = this.getCurrentUserIP(request);

		if(ipLimitFlag != null && "0".equals(ipLimitFlag)){
			boolean userLocked = false;
			String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

			userLocked = userService.getUsernameStatus(userIP,ipLimitTimes,date);
			if(userLocked){
				model.addAttribute("message", "用户IP已锁定，请稍后再试！");
				return errorpage;
			}
		}

		String filterURL = null;

		if (!extOpenFlag) {// 正常登陆,加密密码
			//对js加密进行解密
			if(password != null){
				password = new Des().strDec(password, "dongxin", "eastcom", "ncsp");
			}
			password = CommonUtil.encodePassword(password);
		}

		// 登录前调用验证链
		if (loginFilter != null) {
			FilterReq req = new FilterReq(request, session, response, model,
					extOpenFlag);
			FilterResp resp = loginFilter.beforeLogin(req);
			filterURL = resp.getRedirectURL();
			if (StringUtils.isNotBlank(resp.getErrorURL())) {
				errorpage = resp.getErrorURL();
			}
			putModelData(model, resp.getExtensionMap());
			if (resp.isResult()) {// 验证成功
				FilterChainUser u = resp.getUser();
				if (u != null) {// 返回了新用户，则使用新用户进行登录
					username = StringUtils.isNotBlank(u.getUsername()) ? u
							.getUsername() : username;
					if (StringUtils.isNotBlank(u.getPassword())) {
						String _p = u.getPassword();
						if (!u.isEncodePsd()) {// 如果密码是未经加密的，则进行加密操作
							_p = CommonUtil.encodePassword(_p);
						}
						password = _p;
					}
				}
			} else {// 验证失败
				return errorpage;
			}
		}
		UsernamePasswordToken token = new UsernamePasswordToken(username,
				password, false);

		boolean haveLocked = systemUserService.haveLockUser(username);
		if (haveLocked) {
			model.addAttribute("message", "该账号已锁定，请稍后再试！");
			return errorpage;
		}

		User us = userService.loadUserByUsername(username);

		//开启密码过期不可登录功能
		String pswExpiredFlag = baseService.getCommonFieldValueByName("pswExpiredFlag","value");
		if(pswExpiredFlag != null && "1".equals(pswExpiredFlag)){
			Date nt = new Date();
			long ntl = nt.getTime();
			long pswExpiredDay = us.getPwdExpiredDays();//密码有效时间
			String pswModifyTime = us.getPwdModifyTime();
			if (pswModifyTime != null && !"".equals(pswModifyTime.trim())) {
				Date mt = DateUtil.convertStringToDate("yyyy-MM-dd HH:mm:ss",pswModifyTime);
				long nowmt = (pswExpiredDay * 24 * 60 * 60 * 1000) + mt.getTime();
				if(nowmt < ntl){
					model.addAttribute("message", "密码已过期！");
					return errorpage;
				}
			}
		}

		try {
			SecurityUtils.getSubject().login(token);

			//删除已锁定的IP
			if(ipLimitFlag != null && "0".equals(ipLimitFlag)){
				userService.deleteLockedHost(userIP);
			}

		} catch (Exception e) {
			if (e instanceof UnknownAccountException) {
				model.addAttribute("message", e.getMessage());
			} else if (e instanceof IncorrectCredentialsException) {
				int flag = systemUserService
						.logUserPasswordErrorTimesAndlocked(username);
				String message = "身份验证错误！";
				// edit by JJF JUN 20 2014
				// 约定返回0为不进行锁定，故不会返回锁定信息给前台页面
				if (flag != 0) {
					message += flag < 0 ? ("，账号被锁定" + (0 - flag) + "分钟，请稍后再试！")
							: "，您还可以尝试输入" + flag + "次！";

					//调用短信接口  提醒登录用户
					if(flag < 0){
						if(us != null){
							String mobileNo = us.getMobileNo();
							String msg = "温馨提示：您的账号连续登录失败，请确定是否为本人操作，如非本人操作，该账号存在盗号风险，请及时修改密码！";

							//是否发短信给用户
							if(sendMsgFlag != null && sendMsgFlag.equals("1")){

								try {
									SmsResponse res = this.sendMsg(mobileNo, msg);
									boolean f = res.isSuccess();
									if(f){
										log.info("短信发送成功");
									}else{
										log.info("短信发送失败");
									}
								} catch (Exception e1) {
									e1.printStackTrace();
									log.info("短信发送失败");
								}
							}
						}

					}
				}


				if(ipLimitFlag != null && "0".equals(ipLimitFlag)){
					List list = userService.getLockedUserIPInfo(userIP,ipLimitTimes,ipLimitTime);

					if(list != null && list.size()>0){
						Object[] o = (Object[])list.get(0);
						Long lockedTimes = Long.valueOf(o[1].toString());
						long wrongTimes = Long.valueOf(ipLimitTimes);
						if(lockedTimes < wrongTimes){
							if(lockedTimes == (wrongTimes - 1)){
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								Calendar cal = Calendar.getInstance();
								cal.add(Calendar.MINUTE, Integer.parseInt(ipLimitTime));
								String lt = sdf.format(cal.getTime());
								userService.updateLockedUserIPInfo(userIP,ipLimitTimes,lt);
								message += ";用户IP被锁定，请稍后再试！";
							}else{
								userService.updateLockedUserIPInfo(userIP, String.valueOf(lockedTimes+1),"");
							}

						}

					}else{
						userService.deleteLockedHost(userIP);
						userService.saveUserIpStatus(userIP,"1");
					}
				}

				model.addAttribute("message", message);
			} else if (e instanceof LockedAccountException) {
				model.addAttribute("message", e.getMessage());
			} else if (e instanceof DisabledAccountException) {
				model.addAttribute("message", "您的账户已被停用,请联系管理员");
			} else if (e instanceof AuthenticationException) {
				model.addAttribute("message", "登录失败,请重试");
			} else {
				e.printStackTrace();
			}
			if (loginFilter != null) {// 登录失败处理链
				FilterReq req = new FilterReq(request, session, response,
						model, extOpenFlag);
				FilterResp resp = loginFilter.failedLogin(req, e);
				if (StringUtils.isNotBlank(resp.getErrorURL())) {
					errorpage = resp.getErrorURL();
				}
				putModelData(model, resp.getExtensionMap());
			}
			return errorpage;
		}

		String clientIP = this.getCurrentUserIP(request);
		User currentUser = userService.findUser(getUser().getId());
		systemUserService.setUserToSession(request.getSession(), username + Constants.ONLINE_USER_SPLITER
				+ clientIP, currentUser);

		// 记录在线用户登陆日志并更新用户登陆信息
		Map<String, Object> extensionMap = new HashMap<String, Object>();
		extensionMap.put("clientIP", clientIP);
		// 如果不是第一次登陆或者是单点登陆，都将登陆次数做+1操作
		// 第一次登陆在修改密码完成之后会将times字段更新为1
		extensionMap.put("updateTimes",
				(currentUser.getTimes() > 0 || extOpenFlag));
		// 存入外部系统标识
		extensionMap.put("extSystemId", EXT_SYSTEM_ID);

		String isUpdateUser = request.getParameter("isUpdateUser");
		if(isUpdateUser != null && isUpdateUser.equals("0")){
		}else{
			Thread t = new Thread(new LogLoginUserInfoThread(this, request,
					request.getSession(), currentUser, systemUserService,
					userService, extensionMap));
			t.start();
		}

		// 将当前用户主题写入session
		// 先从系统配置中获取主题配置
		Map args = argumentsConfigurateService.getSysArguments("startup");
		Map startupArgs = (Map) args.get("startup");
		String theme = "";
		if (startupArgs.containsKey("theme")) {
			theme = startupArgs.get("theme").toString();
		}

		// 系统配置中没有强制制定主题，则读取用户配置
		if (StringUtils.isBlank(theme) || "null".equals(theme)
				|| "_ALL_".equals(theme)) {
			theme = currentUser.getXtheme();
		}
		session.setAttribute("theme", theme);

		// 登录成功后调用登录后链(不再使用是否成功字段)
		if (loginFilter != null) {
			FilterReq req = new FilterReq(request, session, response, model,
					extOpenFlag);
			FilterResp resp = loginFilter.afterLogin(req);
			if (StringUtils.isNotBlank(resp.getRedirectURL())) {
				filterURL = resp.getRedirectURL();
			}
			putModelData(model, resp.getExtensionMap());
		}

		// 验证是否存在登陆链自定义跳转地址,是则跳转到该页面
		if (StringUtils.isNotBlank(filterURL)) {
			return filterURL;
		}

		String keys = String.format(RedisKeyConstants.ONLINE_USER_NAME, username+"*");
		String results = redisKeyValuesService.getKeyValues("Hash", keys);
		List<RedisBean> list = (List<RedisBean>)binder.fromJson(results, List.class);

		if(us != null){
			String mobileNo = us.getMobileNo();
			String msg = "温馨提示：您的账号在其他终端登录，请确定是否为本人操作，如非本人操作，该账号存在盗号风险，请及时修改密码！";

			if(list != null && list.size() >= 2){
				//是否发短信给用户
				if(sendMsgFlag != null && sendMsgFlag.equals("1")){

					try {
						SmsResponse res = this.sendMsg(mobileNo, msg);
						boolean f = res.isSuccess();
						if(f){
							log.info("短信发送成功");
						}else{
							log.info("短信发送失败");
						}
					} catch (Exception e1) {
						e1.printStackTrace();
						log.info("短信发送失败");
					}
				}
			}
		}


		return correctpage;
	}

	private void putModelData(Model model, Map<String, Object> m) {
		if (m != null && !m.isEmpty()) {
			for (String key : m.keySet()) {
				model.addAttribute(key, m.get(key));
			}
		}
	};

	/**
	 * 判断用户是否已经登录
	 *
	 * @param sesstion
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/isAlreadyLogined", method = RequestMethod.GET)
	@ResponseBody
	public boolean isAlreadyLogined(HttpSession session,
									HttpServletRequest request,
									@RequestParam("username") String username,
									HttpServletResponse response) {
		return "".equals(systemUserService.getUserSession(username)) ? true
				: false;
	}

	public void logLoginLog(String userIp, String username,
                            boolean resultState, String operateType, String moduleName,
                            String msg, int opttime) {
		saveSysLog(userIp, username, resultState, operateType, "login", msg,
				opttime);
	}

	/**
	 * 更新用户登陆信息
	 */
	// @RequestMapping(value = "/updateUserLoginInfo", method =
	// RequestMethod.POST)
	// private void updateUserLoginInfo(HttpSession session,
	// HttpServletRequest request, HttpServletResponse response) {
	// // 更新用户访问信息
	// try {
	// Object flag = session.getAttribute("isAlreadyUpdatedLoginInfo");
	// if (flag == null) {
	// ShiroUser u = getUser();
	// User currentUser = userService.findUser(u.getId());
	// Map<String, String> lastLoginInfo = systemUserService
	// .getLastLoginTimeAndIp(u.getUserName());
	// currentUser.setTimes(currentUser.getTimes() + 1);
	// if (!lastLoginInfo.isEmpty()) {
	// currentUser
	// .setLastLoginTime(lastLoginInfo.get("loginTime"));
	// currentUser.setLastLoginIp(lastLoginInfo.get("ip"));
	// }
	// userService.updateUser(currentUser);
	// session.setAttribute("isAlreadyUpdatedLoginInfo", "true");
	// }
	// this.addResultInfo(SUCCESS, getI18NReader().getUpdateSuccessMsg(),
	// null);
	// } catch (Exception e) {
	// e.printStackTrace();
	// this.addResultInfo(FAILURE, getI18NReader().getUpdateFailureMsg(),
	// null);
	// }
	// this.responseResult(response, getResultJSONStr());
	// }

	// /**
	// * 退出登录
	// *
	// * @param request
	// */
	// @RequestMapping(value = "/logout", method = RequestMethod.GET)
	// @ResponseBody
	// public void logout(HttpServletRequest request) {
	// Subject subject = SecurityUtils.getSubject();
	// long lStart = Calendar.getInstance().getTimeInMillis();
	// if (subject != null) {
	// subject.logout();
	// }
	// /* syslog */
	// LoginBundle.setLc(this.getI18NLocale());
	// ShiroUser currentUser = getUser();
	// String usernames = currentUser.getFullName() + "("
	// + currentUser.getUserName() + ")";
	// String log = String.format(LoginBundle.getString("logoutmsg"),
	// usernames);
	// saveSysLog(request, true, Constants.SYSLOGSOPERATETYPE_OTHER,
	// "Logout", log, (int) (Calendar.getInstance()
	// .getTimeInMillis() - lStart));
	// request.getSession().invalidate();
	// }


	/**
	 *
	 * 方法说明：生成6位随机验证码
	 * @Title: AuthenticationCode
	 * @author: fangqian
	 * @return
	 * @return String
	 * @date: 2015年8月18日 下午4:51:13
	 */
	private String authenticationCode(){
		String sRand="";
		for(int i=0;i<6;i++){
			//取得一个随机字符
			String tmp=getRandomChar();
			sRand+=tmp;
			//将系统生成的随机字符添加到图形验证码图片上

		}
		return sRand;
	}

	private String getRandomChar() {
		long itmp = 0;
		itmp = Math.round(Math.random() * 9);
		return String.valueOf(itmp);

	}

	/**
	 * 用户登录(密码加密后登录)
	 *
	 * @param username
	 * @param password
	 * @param rememberMe
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/login/v3", method = RequestMethod.POST)
	public String loginNew(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM) String username,
			@RequestParam(FormAuthenticationFilter.DEFAULT_PASSWORD_PARAM) String password,
			Model model) throws Exception {
		//获取登录方式 单点登录不经过验证码验证这一步骤
		String loginType = request.getParameter("loginType");

		Boolean extOpenFlag = (Boolean) session
				.getAttribute(Constants.EXT_OPEN_FLAG);
		String EXT_SYSTEM_ID = "";// 系统名称(单点登陆时使用)
		if (extOpenFlag == null) {
			extOpenFlag = false;
		}

		if (extOpenFlag) {
			session.setAttribute(Constants.EXT_OPEN_FLAG, false);// 单点登陆一次之后重新设置为false
			Object extSysId = session.getAttribute(Constants.EXT_SYSTEM_ID);
			if (extSysId != null) {
				EXT_SYSTEM_ID = extSysId.toString();
			}
		}

		String errorpage = "login";
		String mainPage = "";
		String newMainPageFlag = baseService.getCommonFieldValueByName("newMainPageFlag","value");//网投系统是否采用新版风格
		if(newMainPageFlag != null && newMainPageFlag.equals("1")){
			mainPage = "redirect:/main/v";
			//mainPage = "redirect:/pages/main.jsp";
		}else{
			mainPage = "redirect:/pages/main.jsp";
		}

		String correctpage = extOpenFlag ? "redirect:/pages/sso/moduleChoice.jsp"
				: mainPage;


		//从页面登录的用户需要经过验证码验证这一步骤
		if(loginType!=null&&"1".equals(loginType)){
			//登录是否有验证码（配置在系统数据维护中）
//			String authCodeFlag = baseService.getCommonFieldValueByName("login_verification_code",
//					"value");
			//需要验证码验证
//			if(authCodeFlag != null && "1".equals(authCodeFlag)){
			String authCode = request.getParameter("verification_code");
			String verifyCode = (String)request.getSession().getAttribute(Constants.SIMPLE_CAPCHA_SESSION_KEY);
			if(verifyCode != null && verifyCode.length()>0){
				String[] str = verifyCode.split("&&");
				String code = str[0];
				Long expirydate = Long.valueOf(str[1]);
				Long nowTime = Calendar.getInstance().getTimeInMillis();


				if(code.toLowerCase().equals(authCode.toLowerCase())){
					if(expirydate < nowTime){
						model.addAttribute("message", "验证码已失效，请重新输入！");
						return errorpage;
					}
				}else{
					model.addAttribute("message", "验证码错误！");
					return errorpage;
				}
			}else{
				model.addAttribute("message", "验证码已失效，请重新输入！");
				return errorpage;
			}
//			}

		}

		//验证登录IP是否锁定（配置在系统数据维护中）
		String ipLimitFlag = baseService.getCommonFieldValueByName("IpLimitFlag","value");
		String ipLimitTimes = baseService.getCommonFieldValueByName("IpLimitTimes","value");//限制IP登录错误次数
		String ipLimitTime = baseService.getCommonFieldValueByName("IpLimitTime","value");//限制IP锁定时间
		String sendMsgFlag = baseService.getCommonFieldValueByName("send_message_for_user","value");//发短信通知用户（默认不发送）
		//需要验证

		String userIP = this.getCurrentUserIP(request);

		if(ipLimitFlag != null && "0".equals(ipLimitFlag)){
			boolean userLocked = false;
			String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

			userLocked = userService.getUsernameStatus(userIP,ipLimitTimes,date);
			if(userLocked){
				model.addAttribute("message", "用户IP已锁定，请稍后再试！");
				return errorpage;
			}
		}

		String filterURL = null;

		if (!extOpenFlag) {// 正常登陆,加密密码
//			password = CommonUtil.encodePassword(password);
		}

		// 登录前调用验证链
		if (loginFilter != null) {
			FilterReq req = new FilterReq(request, session, response, model,
					extOpenFlag);
			FilterResp resp = loginFilter.beforeLogin(req);
			filterURL = resp.getRedirectURL();
			if (StringUtils.isNotBlank(resp.getErrorURL())) {
				errorpage = resp.getErrorURL();
			}
			putModelData(model, resp.getExtensionMap());
			if (resp.isResult()) {// 验证成功
				FilterChainUser u = resp.getUser();
				if (u != null) {// 返回了新用户，则使用新用户进行登录
					username = StringUtils.isNotBlank(u.getUsername()) ? u
							.getUsername() : username;
					if (StringUtils.isNotBlank(u.getPassword())) {
						String _p = u.getPassword();
						if (!u.isEncodePsd()) {// 如果密码是未经加密的，则进行加密操作
							_p = CommonUtil.encodePassword(_p);
						}
						password = _p;
					}
				}
			} else {// 验证失败
				return errorpage;
			}
		}

		UsernamePasswordToken token = new UsernamePasswordToken(username,
				password, false);
		boolean haveLocked = systemUserService.haveLockUser(username);
		if (haveLocked) {
			model.addAttribute("message", "该账号已锁定，请稍后再试！");
			return errorpage;
		}

		User us = userService.loadUserByUsername(username);


		//是否开启验证账号在有效期内
		String userExpiredFlag = baseService.getCommonFieldValueByName("userExpiredFlag","value");
		if(userExpiredFlag != null && "1".equals(userExpiredFlag)){
			if("3".equals(us.getAccountEnabled())){
				model.addAttribute("message", "账号已注销，请重新申请！");
				return errorpage;
			}
		}

		//开启密码过期不可登录功能
		String pswExpiredFlag = baseService.getCommonFieldValueByName("pswExpiredFlag","value");
		if(pswExpiredFlag != null && "1".equals(pswExpiredFlag)){
			Date nt = new Date();
			long ntl = nt.getTime();
			long pswExpiredDay = us.getPwdExpiredDays();//密码有效时间
			String pswModifyTime = us.getPwdModifyTime();
			if (pswModifyTime != null && !"".equals(pswModifyTime.trim())) {
				Date mt = DateUtil.convertStringToDate("yyyy-MM-dd HH:mm:ss",pswModifyTime);
				long nowmt = (pswExpiredDay * 24 * 60 * 60 * 1000) + mt.getTime();
				if(nowmt < ntl){
					model.addAttribute("message", "密码已过期！");
					return errorpage;
				}
			}
		}

		String clientIP = this.getCurrentUserIP(request);
		// 记录在线用户登陆日志并更新用户登陆信息
		Map<String, Object> extensionMap = new HashMap<String, Object>();
		extensionMap.put("clientIP", clientIP);
		// 存入外部系统标识
		extensionMap.put("extSystemId", EXT_SYSTEM_ID);

		try {
			SecurityUtils.getSubject().login(token);

			//删除已锁定的IP
			if(ipLimitFlag != null && "0".equals(ipLimitFlag)){
				userService.deleteLockedHost(userIP);
			}

		} catch (Exception e) {
			if (e instanceof UnknownAccountException) {
				model.addAttribute("message", e.getMessage());
			} else if (e instanceof IncorrectCredentialsException) {
				int flag = systemUserService
						.logUserPasswordErrorTimesAndlocked(username);
				String message = "登录失败,请重试";
				// edit by JJF JUN 20 2014
				// 约定返回0为不进行锁定，故不会返回锁定信息给前台页面
				if (flag != 0) {
					message += flag < 0 ? ("，账号被锁定" + (0 - flag) + "分钟，请稍后再试！")
							: "，您还可以尝试输入" + flag + "次！";

					//调用短信接口  提醒登录用户
					if(flag < 0){
						String mobileNo = getUser().getMobileNo();
						String msg = "[网投]温馨提示：您的账号连续登录失败，请确定是否为本人操作，如非本人操作，该账号存在盗号风险，请及时修改密码！";
						if(sendMsgFlag != null && sendMsgFlag.equals("1")){
							try {
								SmsResponse res = this.sendMsg(mobileNo, msg);
								boolean f = res.isSuccess();
								if(f){
									log.info("短信发送成功");
								}else{
									log.info("短信发送失败");
								}
							} catch (Exception e1) {
								e1.printStackTrace();
								log.info("短信发送失败");
							}
						}
					}
				}


				if(ipLimitFlag != null && "0".equals(ipLimitFlag)){
					List list = userService.getLockedUserIPInfo(userIP,ipLimitTimes,ipLimitTime);

					if(list != null && list.size()>0){
						Object[] o = (Object[])list.get(0);
						Long lockedTimes = Long.valueOf(o[1].toString());
						long wrongTimes = Long.valueOf(ipLimitTimes);
						if(lockedTimes < wrongTimes){
							if(lockedTimes == (wrongTimes - 1)){
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								Calendar cal = Calendar.getInstance();
								cal.add(Calendar.MINUTE, Integer.parseInt(ipLimitTime));
								String lt = sdf.format(cal.getTime());
								userService.updateLockedUserIPInfo(userIP,ipLimitTimes,lt);
								message += ";用户IP被锁定，请稍后再试！";
								if (ipLimitTimes.equals(String.valueOf(lockedTimes+1))){
									if(us != null){
										String mobileNo = us.getMobileNo();
										String msg = "[网投]温馨提示：您的账号连续登录失败，请确定是否为本人操作，如非本人操作，该账号存在盗号风险，请及时修改密码！";
										//是否发短信给用户
										if(sendMsgFlag != null && sendMsgFlag.equals("1")){

											try {
												SmsResponse res = this.sendMsg(mobileNo, msg);
												boolean f = res.isSuccess();
												if(f){
													log.info("短信发送成功");
												}else{
													log.info("短信发送失败");
												}
											} catch (Exception e1) {
												e1.printStackTrace();
												log.info("短信发送失败");
											}
										}
									}
								}
							}else{
								userService.updateLockedUserIPInfo(userIP, String.valueOf(lockedTimes+1),"");
							}

						}

					}else{
						userService.deleteLockedHost(userIP);
						userService.saveUserIpStatus(userIP,"1");
					}
				}

				model.addAttribute("message", message);
			} else if (e instanceof LockedAccountException) {
				model.addAttribute("message", e.getMessage());
			} else if (e instanceof DisabledAccountException) {
				model.addAttribute("message", "登录失败,请重试");
			} else if (e instanceof AuthenticationException) {
				model.addAttribute("message", "登录失败,请重试");
			} else {
				e.printStackTrace();
			}
			if (loginFilter != null) {// 登录失败处理链
				FilterReq req = new FilterReq(request, session, response,
						model, extOpenFlag);
				FilterResp resp = loginFilter.failedLogin(req, e);
				if (StringUtils.isNotBlank(resp.getErrorURL())) {
					errorpage = resp.getErrorURL();
				}
				putModelData(model, resp.getExtensionMap());
			}
			if (us!=null){
				Thread t = new Thread(new LogLoginFailUserInfoThread(this,
						request.getSession(), us, extensionMap));
				t.start();
			}
			return errorpage;
		}


		User currentUser = userService.findUser(getUser().getId());
		systemUserService.setUserToSession(request.getSession(), username + Constants.ONLINE_USER_SPLITER
				+ clientIP, currentUser);

		// 如果不是第一次登陆或者是单点登陆，都将登陆次数做+1操作
		// 第一次登陆在修改密码完成之后会将times字段更新为1
		extensionMap.put("updateTimes",
				(currentUser.getTimes() > 0 || extOpenFlag));
		Thread t = new Thread(new LogLoginUserInfoThread(this, request,
				request.getSession(), currentUser, systemUserService,
				userService, extensionMap));
		t.start();
		t.join();

		// 将当前用户主题写入session
		// 先从系统配置中获取主题配置
		Map args = argumentsConfigurateService.getSysArguments("startup");
		Map startupArgs = (Map) args.get("startup");
		String theme = "";
		if (startupArgs.containsKey("theme")) {
			theme = startupArgs.get("theme").toString();
		}

		// 系统配置中没有强制制定主题，则读取用户配置
		if (StringUtils.isBlank(theme) || "null".equals(theme)
				|| "_ALL_".equals(theme)) {
			theme = currentUser.getXtheme();
		}
		session.setAttribute("theme", theme);

		// 登录成功后调用登录后链(不再使用是否成功字段)
		if (loginFilter != null) {
			FilterReq req = new FilterReq(request, session, response, model,
					extOpenFlag);
			FilterResp resp = loginFilter.afterLogin(req);
			if (StringUtils.isNotBlank(resp.getRedirectURL())) {
				filterURL = resp.getRedirectURL();
			}
			putModelData(model, resp.getExtensionMap());
		}

		// 验证是否存在登陆链自定义跳转地址,是则跳转到该页面
		if (StringUtils.isNotBlank(filterURL)) {
			return filterURL;
		}

		String keys = String.format(RedisKeyConstants.ONLINE_USER_NAME, username+"*");
		String results = redisKeyValuesService.getKeyValues("Hash", keys);
		List<RedisBean> list = (List<RedisBean>)binder.fromJson(results, List.class);
		if(us != null){
			String mobileNo = us.getMobileNo();
			String msg = "[网投]温馨提示：您的账号在其他终端登录，请确定是否为本人操作，如非本人操作，该账号存在盗号风险，请及时修改密码！";

			if(list != null && list.size() >= 2){
				//是否发短信给用户
				if(sendMsgFlag != null && sendMsgFlag.equals("1")){

					try {
						SmsResponse res = this.sendMsg(mobileNo, msg);
						boolean f = res.isSuccess();
						if(f){
							log.info("短信发送成功");
						}else{
							log.info("短信发送失败");
						}
					} catch (Exception e1) {
						e1.printStackTrace();
						log.info("短信发送失败");
					}
				}
			}
		}

		return correctpage;
	}

	public static void main(String[] args) throws Exception {
		//b6e75bec6dc78fbb4ec4c936df2af2c9c62fbf3a
		System.out.println(CommonUtil.encodePassword("4rfv$RFV"));
	}
}