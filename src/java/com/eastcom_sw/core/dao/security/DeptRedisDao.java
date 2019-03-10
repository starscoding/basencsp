/**
 * 基础数据维护对应的REDIS处理方法:
 * functions: 
 * addCommonData : 添加、修改基础数据维护redis存储相关的基础信息、与父节点的信息、名称与ID一一对应关系
 * removeCommondata ： 删除某节点相关的redis数据
 * getCommondataByParentName : 根据父节点英文名称查找下级所有节点
 */
package com.eastcom_sw.core.dao.security;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Component;

import com.eastcom_sw.common.utils.RedisKeyConstants;

/**
 * @author SCM
 *2012-8-3
 */
@Component
public class DeptRedisDao extends TreeBaseRedisDao{
	
	public void addDeptData(String pid,String rid){
		
		//存储名称与id的对应关系:set
		if(!pid.isEmpty()){
			String key = String.format(RedisKeyConstants.DEPARTMENT_RELATIONSHIP,pid);
			//存储与父节点的关系:sadd
			redisSet(key).add(rid);
		}
	}
	
	/**
	 * 获取部门的子部门
	 * @param pid
	 * @return
	 */
	public ArrayList<String> getChildDeptByPid(String pid,ArrayList<String> ids){
		
		String key = String.format(RedisKeyConstants.DEPARTMENT_RELATIONSHIP,pid);;
		boolean isEmpty = redisSet(key).isEmpty();
		if(!isEmpty){
			Iterator<String> iterator = redisSet(key).iterator();
			for(Iterator<String> it = iterator;it.hasNext();){
				String id = iterator.next();
				ids.add(id);
				getChildDeptByPid(id,ids);
			}
		}
		return ids;
	}
	
	/**
	 * 更新节点关系以及顺序
	 * @param params
	 */
	public void updateDeptParentAndOrder(ArrayList<JSONObject> params){
		for(JSONObject param: params){
			param.put("idKey",String.format(RedisKeyConstants.DEPARTMENT_RELATIONSHIP,param.getString("id")));
			if(param.getString("oldParentId").equals("none")==false){
				param.put("parentKey", String.format(RedisKeyConstants.DEPARTMENT_RELATIONSHIP,param.getString("parentId")));
				param.put("oldParentKey",String.format(RedisKeyConstants.DEPARTMENT_RELATIONSHIP,param.getString("oldParentId")));
			}
		}
		this.updateParentAndOrder(params, false);
	}
	
	/**
	 * 初始化部门上下级关系
	 * @param map
	 */
	public void initDeptRelation(Map<String,String> map){
		//初始化之前先删除匹配的key
		String pattern = String.format(RedisKeyConstants.DEPARTMENT_RELATIONSHIP,"*");
		Set<String> keys = fuzzyGetKeys(pattern);
		if(keys != null && !keys.isEmpty())
			deleteKeys(keys);
		
		for(Entry<String,String> entry : map.entrySet()){
			addDeptData(entry.getKey(),entry.getValue());
		}
	}
}
