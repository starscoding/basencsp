/**
 * 
 */
package com.eastcom_sw.core.service.security;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.eastcom_sw.common.entity.ShiroUser;
import com.eastcom_sw.common.service.ServiceException;
import com.eastcom_sw.common.utils.DateUtil;
import com.eastcom_sw.core.dao.security.ResourceObject;
import com.eastcom_sw.core.dao.security.ResourceRedisDao;
import com.eastcom_sw.core.dao.security.UserRoleRedisDao;
import com.eastcom_sw.core.entity.security.User;

/**
 * @author SCM
 * @time 上午9:59:29
 */
public class NcspRealm extends AuthorizingRealm {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserService userService;
	@Autowired
	private UserRoleRedisDao userRoleRedisDao;
	@Autowired
	private ResourceRedisDao resourceRedisDao;

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		ShiroUser user = (ShiroUser) principals.getPrimaryPrincipal();
		SimpleAuthorizationInfo simpleInfo = new SimpleAuthorizationInfo();
		List<String> roles = userRoleRedisDao.getUserRoleNames(user.getId());
		if (roles != null) {
			simpleInfo.addRoles(roles);
			simpleInfo.addStringPermissions(getRolePermissions(roles));
		}
		return simpleInfo;
	}

	/**
	 * 认证回调函数,登录时调用.
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {

		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		String userName = token.getUsername();

		// 清空权限缓存,保证每次登陆都重新同步缓存至最新
		SimplePrincipalCollection key = null;
		Cache<Object, AuthorizationInfo> cache = super.getAuthorizationCache();
		for (Object k : cache.keys()) {
			if (k.toString().equals(userName)) {
				key = (SimplePrincipalCollection) k;
				break;
			}
		}
		if (key != null) {
			cache.remove(key);
		}

		logger.info("doGetAuthenticationInfoAuthenticationToken............username=" + userName);
		if (userName != null && !"".equals(userName)) {
			User user = null;
			if (isMobileNO(userName)) {
				try {
					List<User> list = userService.loadUserByPhoneForLogin(userName);
					if (list == null || list.isEmpty()) {
						throw new UnknownAccountException("");
					} else if (list.size() > 1) {
						throw new UnknownAccountException("4");
					} else {
						user = list.get(0);
					}
					logger.info("username=" + userName + ";load=" + (user == null ? "false" : "true"));
				} catch (ServiceException e) {
					e.printStackTrace();
				}

			} else {
				try {
					user = userService.loadUserByUsername(userName);
					logger.info("username=" + userName + ";load=" + (user == null ? "false" : "true"));
				} catch (ServiceException e) {
					e.printStackTrace();
				}
			}

			if (user != null) {
				// 判断帐号是否可用
				if ("1".equals(user.getAccountEnabled())) {
					String acountst = user.getAccoutExpiredStarttime();
					Date nt = new Date();
					long ntl = nt.getTime();
					if (acountst != null && !"".equals(acountst.trim())) {
						Date st = DateUtil.convertStringToDate("yyyy-MM-dd", acountst);
						long stl = st.getTime();
						if (stl > ntl) {
							throw new LockedAccountException("账号未到生效时间!");
						}
					}

					String acountet = user.getAccoutExpiredEndtime();
					if (acountet != null && !"".equals(acountet.trim())) {
						Date et = DateUtil.convertStringToDate("yyyy-MM-dd", acountet);
						long etl = et.getTime();
						if (etl < ntl) {
							throw new LockedAccountException("账号已经超过有效期!");
						}
					}

					// long pswExpiredDay = user.getPwdExpiredDays();//密码有效时间
					// String pswModifyTime = user.getPwdModifyTime();
					// if (pswModifyTime != null &&
					// !"".equals(pswModifyTime.trim())) {
					// Date mt = DateUtil.convertStringToDate("yyyy-MM-dd
					// HH:mm:ss",pswModifyTime);
					// long nowmt = (pswExpiredDay * 24 * 60 * 60 * 1000) +
					// mt.getTime();
					// if(nowmt < ntl){
					// throw new LockedAccountException("密码已过期!");
					// }
					// }

				} else {
					throw new DisabledAccountException("");
				}

				SimpleAuthenticationInfo sai = new SimpleAuthenticationInfo(new ShiroUser(user.getId(),
						user.getUserName(), user.getSex(), user.getFullName(), user.getUserLevel(), user.getEmail(),
						user.getFixedNo(), user.getMobileNo(), user.getLastLoginTime(), user.getPwdExpiredDays(),
						user.getAccoutExpiredEndtime(), user.getTimes(), user.getLastLoginIp(), user.getXtheme(),
						user.getCategory(), user.getOwner(), user.getPwdModifyTime(), ""), user.getPassWrod(),
						getName());
				return sai;
			} else {
				throw new UnknownAccountException("该账号未注册,请尝试其他账号！");
			}
		}
		return null;
	}

	/**
	 * 获取角色的权限
	 * 
	 * @param roles
	 * @return
	 */
	private List<String> getRolePermissions(List<String> roles) {
		List<String> permissions = new ArrayList<String>();
		Set<ResourceObject> resources = resourceRedisDao.queryResourceByRoleNames(roles, false);

		Map<String, String> pidMap = new HashMap<String, String>();

		for (ResourceObject ro : resources) {
			String roid = ro.getId();
			String isPage = ro.getIsWebpage();
			if (roid != null && !roid.equals("") && "1".equals(isPage) && !pidMap.containsKey(roid)) {
				pidMap.put(roid, ro.getName());
			}
		}

		for (ResourceObject r : resources) {
			String kind = r.getKind();
			String isPage = r.getIsWebpage();
			String status = r.getStatus();
			if (kind != null && "1".equals(kind) && !("0".equals(status) || "3".equals(status))) {
				String pid = r.getPid();
				String moduleName = pidMap.get(pid);
				permissions.add(moduleName + ":" + r.getName());
			} else if (isPage != null && "1".equals(isPage) && !("0".equals(status) || "3".equals(status))) {
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