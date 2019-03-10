package com.eastcom_sw.core.service.reqlog;

import com.eastcom_sw.common.service.BaseService;
import com.eastcom_sw.core.dao.reqlog.ReqLogRedisDao;
import com.eastcom_sw.core.dao.security.OnlineUserRedisDao;
import com.eastcom_sw.core.dao.security.OnlineUserRedisDao.OnlineUserRedisKey;
import com.eastcom_sw.core.web.extension.UserReqLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 用户请求日志记录service
 * 
 * @author JJF
 * @date May 08 2014
 * 
 */
@Component
public class ReqLogService extends BaseService {
	@Autowired
	private ReqLogRedisDao reqLogRedisDao;

	@Autowired
	private OnlineUserRedisDao onlineUserRedisDao;

	@Resource(name = "reqLogFilterUrls")
	private Map<String, String> filterUrls;

	@Resource(name = "reqLogFuzzyFilterUrls")
	private List<String> fuzzyFilterUrls;

	// 过滤配置文件中不需要进行日志记录的条目
	public boolean filterUrl(String url) {
		boolean allow = true;
		if (filterUrls.containsKey(url)) {
			allow = false;
		} else {
			if (fuzzyFilterUrls != null && fuzzyFilterUrls.size() > 0) {
				for (String u : fuzzyFilterUrls) {
					u = u.replace("*", "");
					if (url.startsWith(u)) {
						allow = false;
						break;
					}
				}
			}
		}
		return allow;
	}

	/**
	 * 记录日志
	 * 
	 * @param log
	 */
	public void recordLog(UserReqLog log) {
		try {
			refreshOnlineUser(OnlineUserRedisKey.getInstance(log.getUsername(),
					log.getClientIp()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (log != null && log.checkFields()) {
			reqLogRedisDao.recordLog(log);
		}
	}

	/**
	 * 刷新在线用户在线时间
	 * 
	 * @param username
	 */
	public void refreshOnlineUser(OnlineUserRedisKey k) {
		onlineUserRedisDao.refreshOnlineUser(k);
	}
}
