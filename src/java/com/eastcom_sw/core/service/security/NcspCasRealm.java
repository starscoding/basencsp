/**
 * 
 */
package com.eastcom_sw.core.service.security;

import com.eastcom_sw.common.entity.ShiroUser;
import com.eastcom_sw.core.dao.security.ResourceObject;
import com.eastcom_sw.core.dao.security.ResourceRedisDao;
import com.eastcom_sw.core.dao.security.UserRoleRedisDao;
import com.eastcom_sw.core.entity.security.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cas.CasAuthenticationException;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.cas.CasToken;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.util.StringUtils;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.jasig.cas.client.validation.TicketValidationException;
import org.jasig.cas.client.validation.TicketValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author cason
 * 
 */
public class NcspCasRealm extends CasRealm {

	private static Logger log = LoggerFactory.getLogger(com.eastcom_sw.core.service.security.NcspCasRealm.class);

	@Autowired
	private UserService userService;
	@Autowired
	private UserRoleRedisDao userRoleRedisDao;
	@Autowired
	private ResourceRedisDao resourceRedisDao;

	// 认证
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {

		log.info("进入登录认证方法doGetAuthenticationInfo");

		CasToken casToken = (CasToken) token;
		if (token == null) {
			log.info("token=null");
			return null;
		}

		String ticket = (String) casToken.getCredentials();
		if (!StringUtils.hasText(ticket)) {
			log.info("has no ticket");
			return null;
		}

		TicketValidator ticketValidator = ensureTicketValidator();
//		Cas20ProxyTicketValidator proxyTicketValidator = new Cas20ProxyTicketValidator(getCasServerUrlPrefix());
		try {
			// contact CAS server to validate service ticket
			((Cas20ServiceTicketValidator)ticketValidator).setEncoding("utf8");
			Assertion casAssertion = ticketValidator.validate(ticket,
					getCasService());
			// get principal, user id and attributes
			AttributePrincipal casPrincipal = casAssertion.getPrincipal();
			String userId = casPrincipal.getName();
			log.info("userId="+userId);
			log.debug(
					"Validate ticket : {} in CAS server : {} to retrieve user : {}",
					new Object[] { ticket, getCasServerUrlPrefix(), userId });

			Map<String, Object> attributes = casPrincipal.getAttributes();
			Set keys = attributes.keySet();
			Iterator keyIt = keys.iterator();
			Object obj = attributes.get("resType");
			String extend1 = "";
			if(obj!=null){
				extend1 = (String) obj;
			}
			while(keyIt.hasNext()){
				String key  = (String) keyIt.next();
				log.info("***********"+key+"=" + attributes.get(key));
			}
			// refresh authentication token (user id + remember me)
			casToken.setUserId(userId);
			String rememberMeAttributeName = getRememberMeAttributeName();
			String rememberMeStringValue = (String) attributes
					.get(rememberMeAttributeName);
			boolean isRemembered = rememberMeStringValue != null
					&& Boolean.parseBoolean(rememberMeStringValue);
			if (isRemembered) {
				casToken.setRememberMe(true);
			}
			// create simple authentication info
			List<Object> principals = CollectionUtils.asList(userId, attributes);
			PrincipalCollection principalCollection = new SimplePrincipalCollection(
					principals, getName());

			// *************************自定义处理
			User user = userService.loadUserByUsername(userId);
			if (user != null) {
				log.info("获取用户信息成功");
				// 要放在作用域中的东西，请在这里进行操作
				ShiroUser su = new ShiroUser(user.getId(), user.getUserName(),
						user.getSex(), user.getFullName(),
						user.getUserLevel(), user.getEmail(),
						user.getFixedNo(), user.getMobileNo(),
						user.getLastLoginTime(),
						user.getPwdExpiredDays(),
						user.getAccoutExpiredEndtime(),
						user.getTimes(), user.getLastLoginIp(),
						user.getXtheme(), user.getCategory(),
						user.getOwner(), extend1);
				SecurityUtils.getSubject().getSession()
						.setAttribute("c_user", su);

				return new SimpleAuthenticationInfo(principalCollection, ticket);
				
//				SimpleAuthenticationInfo sai = new SimpleAuthenticationInfo(
//						new ShiroUser(user.getId(), user.getUserName(),
//								user.getSex(), user.getFullName(),
//								user.getUserLevel(), user.getEmail(),
//								user.getFixedNo(), user.getMobileNo(),
//								user.getLastLoginTime(),
//								user.getPwdExpiredDays(),
//								user.getAccoutExpiredEndtime(),
//								user.getTimes(), user.getLastLoginIp(),
//								user.getXtheme(), user.getCategory(),
//								user.getOwner()), ticket, getName());
//				return sai;
			}

			// 认证没有通过
			return null;

		} catch (TicketValidationException e) {
			throw new CasAuthenticationException("Unable to validate ticket ["
					+ ticket + "]", e);
		}

	}

	// 授权
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principalCollection) {
		log.info("*************进入权限认证方法doGetAuthenticationInfo");
//		AttributePrincipal principal = (AttributePrincipal) principalCollection.getPrimaryPrincipal();
		ShiroUser u = (ShiroUser) SecurityUtils.getSubject().getSession().getAttribute("c_user");
		log.info("u=" + u.getId());
//		List principals = principalCollection.asList();
//		log.info("user=" + principals.get(0));
//		Map<String, Object> attrs = (Map<String, Object>) principals.get(1);
//		Set keys = attrs.keySet();
//		Iterator keyIt = keys.iterator();
//		while(keyIt.hasNext()){
//			String key  = (String) keyIt.next();
//			log.info(key+"=" + attrs.get(key));
//		}
//		ShiroUser user = (ShiroUser) principalCollection.getPrimaryPrincipal();
		log.info("*************获取用户信息，user=" + u.getUserName());
		SimpleAuthorizationInfo simpleInfo = new SimpleAuthorizationInfo();
		List<String> roles = userRoleRedisDao.getUserRoleNames(u.getId());
		if (roles != null) {
			simpleInfo.addRoles(roles);
			simpleInfo.addStringPermissions(getRolePermissions(roles));
		}
		return simpleInfo;
	}


	/**
	 * 获取角色的权限
	 * 
	 * @param roles
	 * @return
	 */
	private List<String> getRolePermissions(List<String> roles) {
		List<String> permissions = new ArrayList<String>();
		Set<ResourceObject> resources = resourceRedisDao
				.queryResourceByRoleNames(roles);

		Map<String, String> pidMap = new HashMap<String, String>();

		for (ResourceObject ro : resources) {
			String roid = ro.getId();
			String isPage = ro.getIsWebpage();
			if (roid != null && !roid.equals("") && "1".equals(isPage)
					&& !pidMap.containsKey(roid)) {
				pidMap.put(roid, ro.getName());
			}
		}

		for (ResourceObject r : resources) {
			String kind = r.getKind();
			String isPage = r.getIsWebpage();
			if (kind != null && "1".equals(kind)) {
				String pid = r.getPid();
				String moduleName = pidMap.get(pid);
				permissions.add(moduleName + ":" + r.getName());
			} else if (isPage != null && "1".equals(isPage)) {
				String moduleName_ = r.getName();
				permissions.add(moduleName_ + ":" + moduleName_);
			}
		}
		return permissions;
	}

	/**
	 * 验证是否是手机号
	 * 
	 * @param mobile
	 * @return
	 */
	private boolean isMobileNO(String mobile) {
		Pattern p = Pattern.compile("^1\\d{10}$");
		Matcher m = p.matcher(mobile);
		return m.matches();
	}
}
