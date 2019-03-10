package com.eastcom_sw.core.dao.security;

import java.util.ArrayList;

import net.sf.json.JSONObject;
import com.eastcom_sw.common.dao.redis.BaseRedisDao;

/**
 * 树结构redisdao
 * @author JJF
 * @date JAN 16 2013
 *
 */
public class TreeBaseRedisDao extends BaseRedisDao {
	/**
	 * 更新节点关系以及顺序
	 * 
	 * @param params
	 *            信息集合
	 * @param updateOrder
	 *            是否更新顺序
	 */
	public void updateParentAndOrder(ArrayList<JSONObject> params,
			boolean updateOrder) {
		for (JSONObject param : params) {
			String id = param.getString("id");
			String idKey = param.getString("idKey");
			String pid = param.getString("parentId");
			Object parentKey = param.get("parentKey");
			Object oldParentKey = param.get("oldParentKey");
			if (updateOrder) {
				String order = param.getString("order");
				redisMap(idKey).put("order", order);
			}
			if (parentKey != null && oldParentKey != null) {
				redisMap(idKey).put("pid", pid);
				redisSet(parentKey.toString()).add(id);
				redisSet(oldParentKey.toString()).remove(id);
			}
		}
	}
}
