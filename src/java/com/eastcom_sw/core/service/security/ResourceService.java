/**
 * 
 */
package com.eastcom_sw.core.service.security;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.eastcom_sw.common.service.BaseService;
import com.eastcom_sw.common.service.ServiceException;
import com.eastcom_sw.common.utils.ParseJSONObject;
import com.eastcom_sw.common.utils.SelectTree;
import com.eastcom_sw.core.dao.security.ResourceDao;
import com.eastcom_sw.core.dao.security.ResourceObject;
import com.eastcom_sw.core.dao.security.ResourceRedisDao;
import com.eastcom_sw.core.entity.security.Resource;

/**
 * @author SCM
 * @time 下午1:45:51 资源管理
 */
// Spring Bean的标识.
@Component
// 默认将类中的所有public函数纳入事务管理.
@Transactional(readOnly = true)
public class ResourceService extends BaseService {

	@Autowired
	private ResourceDao resourceDao;
	@Autowired
	private ResourceRedisDao resourceRedisDao;

	public Resource findReource(String pid) {
		Resource r = resourceDao.findOne(pid);
		if (r != null) {
			r.getJSONdata(r);// 将数据懒加载进来
		}
		return r;
	}

	/**
	 * 保存资源
	 * 
	 * @param entity
	 * @throws ServiceException
	 */
	@Transactional(readOnly = false)
	public void saveResource(Resource entity) throws ServiceException {
		resourceDao.save(entity);
		entity.getJSONdata(entity);// 将数据懒加载进来
		resourceRedisDao.addResourceData(converEntity(entity), "0");
	}

	/**
	 * 更新资源
	 * 
	 * @param entity
	 * @throws ServiceException
	 */
	@Transactional(readOnly = false)
	public void updateResource(Resource entity) throws ServiceException {
		resourceDao.update(entity);
		resourceRedisDao.addResourceData(converEntity(entity), "1");
	}

	/**
	 * 删除资源及其子资源及其与个人桌面的关系
	 * 
	 * @param id
	 */
	public void removeResourceById(String id) throws ServiceException {
		ArrayList<String> ids = new ArrayList<String>();
		ids.add(id);
		ids = resourceRedisDao.getChildrenResouceIds(id, ids);
		// 删除所有资源及其子资源
		resourceDao.removeResourceAndChildren(ids);
		// redis中删除资源的数据及其子集
		resourceRedisDao.deleteResource(id);
	}

	/**
	 * 异步获取资源的子节点信息
	 * 
	 * @param parentId
	 * @return
	 */
	public String getChildrenResourcesById(String parentId) {
		JSONArray records = new JSONArray();
		List<Resource> list = resourceDao.getResourceChildren(parentId);
		for (Resource r : list) {
			records.add(r.getJSONdata(r));
		}
		return records.toString();
	}

	/**
	 * 根据父节点名称获取所有子节点
	 * 
	 * @param parentId
	 * @return
	 */
	public List<ResourceObject> getChildrenResourcesByName(String name) {
		String id = resourceDao.getIdByName(name);
		List<ResourceObject> records = new ArrayList<ResourceObject>();
		if (StringUtils.isNotBlank(id)) {
			List<Resource> list = resourceDao.getResourceChildren(id);
			for (Resource r : list) {
				records.add(converEntity(r));
			}
		}
		return records;
	}

	/**
	 * 获取所有有效的资源
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<ResourceObject> getAllValidResource() {
		List<ResourceObject> rslt = new ArrayList<ResourceObject>();
		List list = resourceDao.getAllValidResource();
		if (list != null) {
			for (int i = 0, length = list.size(); i < length; i++) {
				Object[] o = (Object[]) list.get(i);
				ResourceObject robj = new ResourceObject();
				robj.setId(String.valueOf(o[0]));
				robj.setPid(String.valueOf(o[1]));
				robj.setName(String.valueOf(o[2]));
				robj.setNameCn(String.valueOf(o[3]));
				robj.setLocation(String.valueOf(o[4]));
				robj.setStatus(String.valueOf(o[5]));
				robj.setOrder(Integer.valueOf(String.valueOf(o[6])));
				robj.setKind(String.valueOf(o[7]));
				robj.setIsShowDesktop(String.valueOf(o[8]));
				robj.setIsWebpage(String.valueOf(o[9]));
				robj.setImage(String.valueOf(o[10]));
				rslt.add(robj);
			}
		}
		return rslt;
	}

	/**
	 * 初始化所有有效的菜单数据
	 */
	public void initAllValidResouce() {
		List<ResourceObject> list = getAllValidResource();
		if (list != null && list.size() > 0) {
			resourceRedisDao.initAllValidResource(list);
		}
	}

	/**
	 * 通过请求的action的地址获取模块的名称
	 * 
	 * @param localtion
	 * @return
	 */
	public String getModuleNameByActionUrl(String localtion) {
		return resourceRedisDao.getModuleNameByActionUrl(localtion);
	}

	/**
	 * 根据权限url获取所属模块的vo
	 * 
	 * @param permissionUrl
	 * @param withParams
	 * @return
	 */
	public ResourceObject getResourceByPermissionUrl(String permissionUrl,
			boolean withParams) {
		ResourceObject ro = null;
		List<String> list = resourceRedisDao.getPermissionByAction(
				permissionUrl, withParams);

		if (list != null && list.size() > 0
				&& !"~!@#$illegal".equals(list.get(0))) {
			String name = list.get(0).split(":")[0];
			String id = resourceDao.getIdByName(name);
			if (StringUtils.isNotBlank(id)) {
				ro = resourceRedisDao.getById(id);
			}
		}
		return ro;
	}

	/**
	 * 根据资源名称查找资源
	 * 
	 * @param name
	 * @throws ServiceException
	 */
	@SuppressWarnings({ "unchecked" })
	public String searchResource(String name) throws ServiceException {
		String names = "id,name,nameCn,location,status,order,kind,isShowDesktop,isWebpage,image,creator,createTime,remarks,parentId";
		List<JSONObject> results = new ArrayList<JSONObject>();
		List<JSONObject> rsList = ParseJSONObject.parse(names,
				resourceDao.searchResource(name));
		if (rsList != null && rsList.size() > 0) {
			List<JSONObject> totalList = ParseJSONObject.parse(names,
					resourceDao.getAllPageResource());
			List<JSONObject> rs = new SelectTree(totalList, rsList)
					.selectTreeNodes();
			for (JSONObject jo : rs) {
				if (StringUtils.isBlank(jo.getString("parentId"))
						|| "null".equals(jo.getString("parentId"))) {
					getResourceChildren(jo, rs);
					results.add(jo);
				}
			}
			// List<Map<String,Object>> records = convertResultData(list);
			// for(Map<String,Object> record : records){
			// if(record.get("pid") == null){
			// getResourceChildren(record,records);
			// results.add(record);
			// }
			// }
		}
		String records = JSONArray.fromObject(results).toString();
		return records;
	}

	private void getResourceChildren(JSONObject root,
			List<JSONObject> rescources) {

		List<JSONObject> children = new ArrayList<JSONObject>();

		for (JSONObject resource : rescources) {
			if (root.getString("id").equals(resource.getString("parentId"))) {
				getResourceChildren(resource, rescources);
				children.add(resource);
			}
		}
		if (children.size() > 0) {
			root.put("children", children);
			root.put("totalChildnum", children.size());
			root.put("leaf", false);
			root.put("expanded", true);
		} else {
			root.put("leaf", true);
		}
	}

	// @SuppressWarnings("rawtypes")
	// private List<Map<String, Object>> convertResultData(List result) {
	// List<Map<String, Object>> records = Lists.newArrayList();
	// for (Object o : result) {
	// records.add(convertObjectToMap((Object[]) o));
	// }
	// return records;
	// }

	// /**
	// * 转换SQL查询返回的菜单数据
	// *
	// * @param o
	// * @return
	// */
	// private Map<String, Object> convertObjectToMap(Object[] o) {
	//
	// Map<String, Object> map = Maps.newLinkedHashMap();
	// map.put("id", o[0]);
	// map.put("name", o[1]);
	// map.put("nameCn", o[2]);
	// map.put("location", o[3]);
	// map.put("status", o[4]);
	// map.put("order", o[5]);
	// map.put("kind", o[6]);
	// map.put("isShowDesktop", o[7]);
	// map.put("isWebpage", o[8]);
	// map.put("image", o[9]);
	// map.put("creator", o[10]);
	// map.put("createTime", o[11]);
	// map.put("remarks", o[12]);
	// map.put("pid", o[13]);
	// map.put("leaf", true);
	// return map;
	// }

	/**
	 * 将资源对象转换成redis需要的对象格式
	 * 
	 * @param r
	 * @return
	 */
	private ResourceObject converEntity(Resource r) {
		ResourceObject ro = new ResourceObject();
		ro.setId(r.getId());
		ro.setName(r.getName());
		ro.setNameCn(r.getNameCn());
		ro.setLocation(r.getLocation());
		ro.setStatus(r.getStatus());
		ro.setOrder(r.getOrder());
		ro.setKind(r.getKind());
		ro.setIsShowDesktop(r.getIsShowDesktop());
		ro.setIsWebpage(r.getIsWebpage());
		ro.setImage(r.getImage());
		String pid = "";
		Resource parent = r.getParent();
		if (parent != null)
			pid = parent.getId();
		ro.setPid(pid);
		return ro;
	}

	/**
	 * 资源重名验证
	 * 
	 * @param name
	 * @param pid
	 * @param kind
	 * @return
	 */
	public boolean checkResourceNameExsist(String pid, String name, String kind) {
		return resourceRedisDao.isExistResourceName(pid, name, kind);
	}

	/**
	 * 资源地址重名验证
	 * 
	 * @param location
	 * @return
	 */
	public boolean checkLocationExsist(String location) {
		return resourceRedisDao.isExistResourceLocal(location);
	}

	/**
	 * 更改部门的父节点以及顺序
	 * 
	 * @param jsonString
	 */
	public void changeParentAndOrder(String jsonString) {
		ArrayList<JSONObject> params = new ArrayList<JSONObject>();
		JSONArray ja = JSONArray.fromObject(jsonString);
		for (Object o : ja) {
			JSONObject jo = JSONObject.fromObject(o);
			if (jo.get("childs").toString().length() > 0) {
				JSONArray cja = JSONArray.fromObject(jo.get("childs"));
				for (Object co : cja) {
					JSONObject cjo = JSONObject.fromObject(co);
					cjo.put("parentId", jo.get("id"));
					params.add(cjo);
				}
			}
		}
		resourceDao.changeParentAndOrder(params);// 更新数据库
		resourceRedisDao.updateResourceParentAndOrder(params);// 更新redis
	}
}