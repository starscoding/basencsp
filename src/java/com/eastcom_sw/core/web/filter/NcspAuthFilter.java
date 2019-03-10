package com.eastcom_sw.core.web.filter;

import com.eastcom_sw.common.utils.CommonUtil;
import com.eastcom_sw.core.dao.security.ResourceRedisDao;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NcspAuthFilter extends AuthorizationFilter {

	private Logger logger = LoggerFactory.getLogger(NcspAuthFilter.class);

	@Autowired
	private ResourceRedisDao resourceRedisDao;

	@Override
	protected boolean isAccessAllowed(ServletRequest request,
                                      ServletResponse response, Object mappedValue) throws Exception {

		long lStart = Calendar.getInstance().getTimeInMillis();
		Subject subject = getSubject(request, response);
		HttpServletRequest req = (HttpServletRequest) request;

		String rqurl = CommonUtil.getRequestURL(req);
		rqurl = URLDecoder.decode(rqurl);
		List<String> permissions = new ArrayList<String>();
		boolean isPermitted = false;

		if (rqurl != null && rqurl.trim().length() > 0) {
			permissions = resourceRedisDao.getPermissionByAction(rqurl);
			// 如果没有配置该权限，那么默认通过；
			if (permissions.isEmpty()) {
				return true;
			} else {
				if (!permissions.get(0).equals("~!@#$illegal")) {// 先判定是否为非法访问
					for (String permission : permissions) {
						isPermitted = subject.isPermitted(permission);
						if (isPermitted) {
							long lEnd = Calendar.getInstance()
									.getTimeInMillis();
							logger.info("~~~~~~~~~~getModuleTime ===== "
									+ (lEnd - lStart));
							return true;
						}
					}
					logger.info("request url:" + rqurl + " is intercepted!");
				} else {
					logger.info("Illegal request!! request url:" + rqurl
							+ "  IP:" + request.getRemoteAddr());
				}
			}
		}
		return false;
	}
}
