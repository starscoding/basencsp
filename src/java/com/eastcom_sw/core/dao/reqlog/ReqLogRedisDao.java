package com.eastcom_sw.core.dao.reqlog;

import com.eastcom_sw.common.dao.redis.BaseRedisDao;
import com.eastcom_sw.common.utils.RedisKeyConstants;
import com.eastcom_sw.core.web.extension.UserReqLog;
import net.sf.json.JSONObject;
import org.springframework.data.redis.support.collections.RedisList;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 用户请求日志记录REDIS DAO
 * 
 * @author JJF
 * @date May 08 2014
 * 
 */
@Component
public class ReqLogRedisDao extends BaseRedisDao {
	public void recordLog(UserReqLog log) {
		String key = String.format(RedisKeyConstants.SYS_USER_QUERY_LOG,
				log.getSessionId());
		RedisList<String> list = this.redisList(key);
		list.add(JSONObject.fromObject(log).toString());
		// 两天之后该数据将会自动从redis中清除,批处理采集该数据间隔必须小于该时间
		list.expire(2, TimeUnit.DAYS);
	}
}
