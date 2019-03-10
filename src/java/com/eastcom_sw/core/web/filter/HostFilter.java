package com.eastcom_sw.core.web.filter;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.Map;

public class HostFilter extends AuthorizationFilter {

	//private Logger log = LoggerFactory.getLogger(HostFilter.class);

	@SuppressWarnings("rawtypes")
	protected boolean isAccessAllowed(ServletRequest req, ServletResponse resp, Object mappedValue) throws Exception {
		HttpServletRequest request = (HttpServletRequest) req;
		//HttpServletResponse response = (HttpServletResponse) resp;
		String requestStr = getRequestString(request);

		if ("bingo".equals(guolv2(requestStr)) || "bingo".equals(guolv2(request.getRequestURL().toString()))) {
			return false;
		}
		// 主机ip和端口 或 域名和端口

		// String myhosts = request.getHeader("host");
		// if (!StringUtils.equals(myhosts, "xx.xx.xxx.xxx:xxxx") &&
		// !StringUtils.equals(myhosts, "xx.xx.xxx.xxx:xxxx")
		// && !StringUtils.equals(myhosts, "xx.xx.xxx.xxx:xxxx") &&
		// !StringUtils.equals(myhosts, "xx.xx.xxx.xxx")
		// && !StringUtils.equals(myhosts, "xx.xx.xxx.xxx") &&
		// !StringUtils.equals(myhosts, "xx.xx.xxx.xxx")
		// && !StringUtils.equals(myhosts, "localhost") &&
		// !StringUtils.equals(myhosts, "localhost:xxxx")) {
		// return false;
		// }

		//String currentURL = request.getRequestURI();
		// add by wangsk 过滤请求特殊字符，扫描跨站式漏洞
		Map parameters = request.getParameterMap();
		if (parameters != null && parameters.size() > 0) {
			for (Iterator iter = parameters.keySet().iterator(); iter.hasNext();) {
				String key = (String) iter.next();
				String[] values = (String[]) parameters.get(key);
				for (int i = 0; i < values.length; i++) {
					values[i] = guolv(values[i]);
				}
			}
		}
		return true;
	}

	public static String guolv(String a) {
		a = a.replaceAll("%22", "");
		a = a.replaceAll("%27", "");
		a = a.replaceAll("%3E", "");
		a = a.replaceAll("%3e", "");
		a = a.replaceAll("%3C", "");
		a = a.replaceAll("%3c", "");
		a = a.replaceAll("<", "");
		a = a.replaceAll(">", "");
		a = a.replaceAll("\"", "");
		a = a.replaceAll("'", "");
		a = a.replaceAll("\\+", "");
		a = a.replaceAll("\\(", "");
		a = a.replaceAll("\\)", "");
		a = a.replaceAll(" and ", "");
		a = a.replaceAll(" or ", "");
		a = a.replaceAll(" 1=1 ", "");
		return a;
	}

	private String getRequestString(HttpServletRequest req) {
		String requestPath = req.getServletPath().toString();
		String queryString = req.getQueryString();

		if (requestPath != null) {
			return requestPath + "?" + queryString;
		} else {
			return requestPath;
		}
	}

	public String guolv2(String a) {
		if (StringUtils.isNotEmpty(a)) {
			if (a.contains("%22") || a.contains("%3E") || a.contains("%3e") || a.contains("%3C") || a.contains("%3c")
					|| a.contains("<") || a.contains(">") || a.contains("\"") || a.contains("'") || a.contains("+")
					|| /* a.contains("%27") || */
					a.contains(" and ") || a.contains(" or ") || a.contains("1=1") || a.contains("(")
					|| a.contains(")")) {
				return "bingo";
			}
		}

		if (StringUtils.isNotEmpty(a)) {

		}

		return a;
	}
}