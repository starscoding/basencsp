package com.eastcom_sw.core.dao.security;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.BulkMapper;
import org.springframework.data.redis.core.query.SortQuery;
import org.springframework.data.redis.core.query.SortQueryBuilder;
import org.springframework.data.redis.hash.DecoratingStringHashMapper;
import org.springframework.data.redis.hash.HashMapper;
import org.springframework.data.redis.hash.JacksonHashMapper;
import org.springframework.data.redis.support.collections.RedisSet;
import org.springframework.stereotype.Component;
import org.springside.modules.utils.Collections3;

import com.eastcom_sw.common.utils.RedisKeyConstants;
import com.eastcom_sw.core.entity.security.Resource;
import com.eastcom_sw.core.utils.security.GenerateResource;

/**
 * 资源权限redis交互
 * 
 * @author SCM 2012-8-3
 */
@Component
public class ResourceRedisDao extends TreeBaseRedisDao {

	private final HashMapper<ResourceObject, String, String> rMapper = new DecoratingStringHashMapper<ResourceObject>(
			new JacksonHashMapper<ResourceObject>(ResourceObject.class));

	/**
	 * 将资源对象(hash)存储到redis
	 * 
	 * @param rsObject
	 */
	public void addResourceData(ResourceObject rsObject, String optType) {

		String rid = rsObject.getId();
		String pid = rsObject.getPid();
		String rname = rsObject.getName();
		// 根据id保存整个对象
		redisMap(String.format(RedisKeyConstants.RESOURCE_BASE_INFO, rid))
				.putAll(rMapper.toHash(rsObject));

		// 根据name保存整个对象
		redisMap(String.format(RedisKeyConstants.RESOURCE_BASE_INFO, rname))
				.putAll(rMapper.toHash(rsObject));
		if ("1".equals(rsObject.getKind())) {
			// 如果是操作权限
			if (!pid.isEmpty()) {
				String pname = redisMap(
						String.format(RedisKeyConstants.RESOURCE_BASE_INFO, pid))
						.get("name");
				// 存储上级模块名+模块名与权限地址的关系
				redisMap(RedisKeyConstants.RESOURCE_NAME_LOCATION_REL).put(
						pname + ":" + rname, rsObject.getLocation());
			}
		} else {
			// 如果不是操作权限,存储模块名与权限地址的关系
			redisMap(RedisKeyConstants.RESOURCE_NAME_LOCATION_REL).put(
					rname + ":" + rname, rsObject.getLocation());
			// 保存资源名称与中文名称之间的关系
			valueOps().set(
					String.format(RedisKeyConstants.RESOURCE_NAME, rname),
					rsObject.getNameCn());
		}

		// 新增维护关系，修改不需要维护 0:新增；1：修改
		if (optType.equals("0")) {
			// 将新增资源ID加入redis key 为resource:list的Set中
			redisSet(RedisKeyConstants.RESOURCE_LIST).add(rid);
			// 维护上下级关系
			if (!pid.isEmpty())
				redisSet(
						String.format(RedisKeyConstants.RESOURCE_RELATIONSHIP,
								pid)).add(rid);
		}
	}

	/**
	 * 判断是否存在该资源名称 备注：redis中已经存储了模块名称与权限地址的对应关系，因此只需判断是否包括模块名称的key即可判断是否已存在
	 * 
	 * @param name
	 *            资源名称
	 * @param kind
	 *            0:资源类型 1：操作权限
	 * @return
	 */
	public boolean isExistResourceName(String pid, String name, String kind) {

		String permissionName = "";
		if ("1".equals(kind)) {
			// 操作权限父模块的名称
			String pname = redisMap(
					String.format(RedisKeyConstants.RESOURCE_BASE_INFO, pid))
					.get("name");
			permissionName = pname + ":" + name;
		} else {
			permissionName = name + ":" + name;
		}
		return redisMap(RedisKeyConstants.RESOURCE_NAME_LOCATION_REL)
				.containsKey(permissionName);
	}

	/**
	 * 资源地址是否重名校验(权限操作才需要验证)
	 * 
	 * @param localName
	 *            资源地址名称
	 * @return
	 */
	public boolean isExistResourceLocal(String localName) {

		List<ResourceObject> list = getAllResources();
		for (ResourceObject r : list) {
			String kind = r.getKind();
			if ("1".equals(kind) && localName.equals(r.getLocation()))
				return true;
		}
		return false;
	}

	/**
	 * 通过请求地址获取匹配的权限名称
	 * 
	 * @param actionUrl
	 * @return
	 */
	public List<String> getPermissionByAction(String actionUrl) {
		return getPermissionByAction(actionUrl, true);
	}

	/**
	 * 通过请求地址获取匹配的权限名称
	 * 
	 * @param actionUrl
	 * @return
	 */
	public List<String> getPermissionByAction(String actionUrl,
			boolean withParams) {
		String baserqurl = actionUrl;
		if (baserqurl.indexOf("?") != -1) {
			baserqurl = baserqurl.substring(0, baserqurl.lastIndexOf("?"));
		}
		List<String> list = new ArrayList<String>();// 不带参数匹配
		List<String> realList = new ArrayList<String>();// 带参数匹配
		Set<Entry<String, String>> entrys = redisMap(
				RedisKeyConstants.RESOURCE_NAME_LOCATION_REL).entrySet();
		for (Entry<String, String> entry : entrys) {
			String key = entry.getKey();
			String value = entry.getValue();
			String subValue = value;
			if (subValue.indexOf("?") != -1) {
				subValue = subValue.substring(0, subValue.lastIndexOf("?"));
			}
			if (value.equals(actionUrl)) {// 全匹配
				realList.add(key);
			} else if (value.endsWith("*")
					&& actionUrl.startsWith(value.substring(0,
							value.length() - 1))) {// 结尾通配符匹配
				realList.add(key);
			}

			if (subValue != null && subValue.equals(baserqurl)) {// 去除？后参数匹配
				list.add(key);
			}
		}
		if (withParams) {// 匹配参数
			if (!realList.isEmpty()) {// 地址有完全匹配项目则返回这些项
				return realList;
			} else {
				// 没有完全匹配项，也没有去掉参数匹配到的项,返回空列表，跳过验证
				if (list.isEmpty()) {
					return list;
				} else {
					// 没有完全匹配项，含有去掉参数匹配到的项,判定为非法访问，不予通过验证
					realList.add("~!@#$illegal");
					return realList;
				}
			}
		} else {// 不匹配参数
			return list;
		}
	}

	/**
	 * 删除资源及其子资源
	 * 
	 * @param rid
	 */
	public void deleteResource(String rid) {

		String childrenId = String.format(
				RedisKeyConstants.RESOURCE_RELATIONSHIP, rid);
		boolean isEmpty = redisSet(childrenId).isEmpty();
		// 判断是否有子集
		if (!isEmpty) {
			Iterator<String> iterator = redisSet(childrenId).iterator();
			for (Iterator<String> it = iterator; it.hasNext();) {
				String id = iterator.next();
				deleteResource(id);
			}
		}
		String rkey = String.format(RedisKeyConstants.RESOURCE_BASE_INFO, rid);
		ResourceObject robj = getByKey(rkey);
		String pid = robj.getPid();
		String rname = robj.getName();

		// 从父节点中删除该关系
		if (pid != null && "null".equals(pid) && !pid.isEmpty()) {
			redisSet(
					String.format(RedisKeyConstants.RESOURCE_RELATIONSHIP, pid))
					.remove(rid);
		}
		// 删除资源名称与权限地址的关系
		if ("0".equals(robj.getKind())) {
			redisMap(RedisKeyConstants.RESOURCE_NAME_LOCATION_REL).remove(
					rname + ":" + rname);
		} else {
			String pkey = String.format(RedisKeyConstants.RESOURCE_BASE_INFO,
					pid);
			String pname = redisMap(pkey).get("name");
			redisMap(RedisKeyConstants.RESOURCE_NAME_LOCATION_REL).remove(
					pname + ":" + rname);
		}
		// 删除资源对象
		deleteKey(rkey);
		// 从redis key 为resource:list的Set中删除该标识
		redisSet(RedisKeyConstants.RESOURCE_LIST).remove(rid);

		// 删除与角色的关系
		String menu_role_key = String.format(
				RedisKeyConstants.MENU_ROLE_PERMISSION, rid);
		RedisSet<String> roleNames = redisSet(menu_role_key);
		for (String key : roleNames) {
			redisSet(String.format(RedisKeyConstants.ROLE_MENU_PERMISSION, key))
					.remove(rid);
		}
		deleteKey(menu_role_key);
	}

	/**
	 * 获取所有子资源的ids
	 * 
	 * @param id
	 * @param ids
	 * @return
	 */
	public ArrayList<String> getChildrenResouceIds(String rid,
			ArrayList<String> ids) {

		String childrenId = String.format(
				RedisKeyConstants.RESOURCE_RELATIONSHIP, rid);
		boolean isEmpty = redisSet(childrenId).isEmpty();
		if (!isEmpty) {
			Iterator<String> iterator = redisSet(childrenId).iterator();
			for (Iterator<String> it = iterator; it.hasNext();) {
				String id = iterator.next();
				ids.add(id);
				getChildrenResouceIds(id, ids);
			}
		}
		return ids;
	}

	/**
	 * 角色授权，菜单与角色关系维护
	 * 
	 * @param menuIds
	 * @param roleName
	 */
	public void roleAuthorization(String menuIds[], String delMenusIds[],
			String roleName) {
		// 角色新增的菜单权限
		int length = menuIds.length;
		RedisSet<String> roleMenuSet = redisSet(String.format(
				RedisKeyConstants.ROLE_MENU_PERMISSION, roleName));
		if (length > 0) {
			for (int i = 0; i < length; i++) {
				String menuId = menuIds[i];
				String key = String.format(
						RedisKeyConstants.MENU_ROLE_PERMISSION, menuId);
				redisSet(key).add(roleName);
				roleMenuSet.add(menuId);
			}
		}

		// 角色删除的菜单权限
		int dlength = delMenusIds.length;
		if (dlength > 0) {
			for (int j = 0; j < dlength; j++) {
				String menuId = delMenusIds[j];
				String mkey = String.format(
						RedisKeyConstants.MENU_ROLE_PERMISSION, menuId);
				redisSet(mkey).remove(roleName);
				roleMenuSet.remove(menuId);
			}
		}
	}

	/**
	 * 删除角色与菜单的关系
	 * 
	 * @param roleNames
	 */
	public void deleteRoleMenuRelation(String[] roleNames) {
		for (String roleName : roleNames) {
			String k = String.format(RedisKeyConstants.ROLE_MENU_PERMISSION,
					roleName);
			// 删除菜单角色关系
			RedisSet<String> menuSet = redisSet(k);
			for (String menuId : menuSet) {
				redisSet(
						String.format(RedisKeyConstants.MENU_ROLE_PERMISSION,
								menuId)).remove(roleName);
			}
			// 删除角色菜单关系
			deleteKey(k);
		}
	}

	/**
	 * 查询所有的菜单资源
	 */
	public Set<ResourceObject> queryAllResources() {
		return queryAllResources(true);
	}

	/**
	 * 查询所有的菜单资源
	 */
	public Set<ResourceObject> queryAllResources(boolean controlStatus) {
		Set<ResourceObject> records = new HashSet<ResourceObject>(
				getAllResources());
		if (controlStatus) {
			records = sliceStopMenus(records);
		}
		return records;
	}

	/**
	 * 通过角色名称查找资源
	 * 
	 * @param roleName
	 * @return
	 */
	public Set<ResourceObject> queryResourceByRoleName(String roleName) {
		return queryResourceByRoleName(roleName, true);
	}

	/**
	 * 通过角色名称查找资源
	 * 
	 * @param roleName
	 * @return
	 */
	public Set<ResourceObject> queryResourceByRoleName(String roleName,
			boolean controlStatus) {
		String key = String.format(RedisKeyConstants.ROLE_MENU_PERMISSION,
				roleName);
		RedisSet<String> menuIds = redisSet(key);
		Set<ResourceObject> records = new HashSet<ResourceObject>();
		String[] keys = new String[menuIds.size()];
		int i = 0;
		for (String id : menuIds) {
			keys[i] = String.format(RedisKeyConstants.RESOURCE_BASE_INFO, id);
			i += 1;
		}
		Map<String, ResourceObject> m = mGetMap(keys, rMapper,
				ResourceObject.class);
		for (String k : m.keySet()) {
			records.add(m.get(k));
		}

		if (controlStatus) {
			records = sliceStopMenus(records);
		}
		return records;
	}

	// /**
	// * 从已知菜单列表中匹配有权限的role所有的菜单集合
	// *
	// * @return
	// */
	// public Set<ResourceObject> fitchResourceByRoleName(String[] menuids,
	// String roleName, boolean controlStatus) {
	// Set<ResourceObject> records = new HashSet<ResourceObject>();
	// for (String menuid : menuids) {
	// String key = String.format(RedisKeyConstants.MENU_ROLE_PERMISSION,
	// menuid);
	// boolean isContain = redisSet(key).contains(roleName);
	// if (isContain) {
	// ResourceObject resource = getByKey(String.format(
	// RedisKeyConstants.RESOURCE_BASE_INFO, menuid));
	// records.add(resource);
	// }
	// }
	// if (controlStatus) {
	// records = sliceStopMenus(records);
	// }
	// return records;
	// }

	/**
	 * 剔除已停用的所有节点及其子节点
	 */
	private Set<ResourceObject> sliceStopMenus(Set<ResourceObject> records) {
		Set<ResourceObject> newRecords = new HashSet<ResourceObject>();
		Set<String> stopList = new HashSet<String>();
		for (ResourceObject res : records) {
			if (!"0".equals(res.getStatus())) {
				newRecords.add(res);
			} else {
				stopList.add(res.getId());
			}
		}
		return sliceStopMenusByPids(stopList, newRecords);
	}

	/**
	 * 剔除已停用的所有子节点
	 */
	private Set<ResourceObject> sliceStopMenusByPids(Set<String> stopList,
			Set<ResourceObject> records) {
		if (stopList == null || stopList.isEmpty()) {
			return records;
		} else {
			Set<String> newStopList = new HashSet<String>();
			Set<ResourceObject> newRecords = new HashSet<ResourceObject>();
			for (ResourceObject res : records) {
				if (stopList.contains(res.getPid())) {
					newStopList.add(res.getId());
				} else {
					newRecords.add(res);
				}
			}
			return sliceStopMenusByPids(newStopList, newRecords);
		}
	}

	/**
	 * 获取多个角色的资源
	 * 
	 * @param roleNames
	 * @return
	 */
	public Set<ResourceObject> queryResourceByRoleNames(
			Collection<String> roleNames) {
		return queryResourceByRoleNames(roleNames, true);
	}

	/**
	 * 获取多个角色的资源
	 * 
	 * @param roleNames
	 * @param controlStatus是否控制菜单状态
	 * @return
	 */
	public Set<ResourceObject> queryResourceByRoleNames(
			Collection<String> roleNames, boolean controlStatus) {
		Set<ResourceObject> records = new HashSet<ResourceObject>();
		if (roleNames != null && roleNames.size() > 0) {
			for (String roleName : roleNames) {
				records.addAll(queryResourceByRoleName(roleName, controlStatus));
			}
		}
		return records;
	}

	/**
	 * 获取多个角色指定父节点的资源
	 * 
	 * @param roleNames
	 * @return
	 */
	public Resource queryResourceByRoleNamesAndParentName(
			Collection<String> roleNames, String parentName) {
		Set<ResourceObject> totalRs = queryResourceByRoleNames(roleNames);
		return GenerateResource.generate(totalRs, parentName);
	}

	/**
	 * 判断用户是否具有某模块的指定操作权限
	 * 
	 * @param roleNames
	 *            当前用户的所有角色
	 * @param moduleName
	 *            模块名称
	 * @param permission
	 *            操作权限名称
	 * @return
	 */
	public boolean hasPermissionToModule(Collection<String> roleNames,
			String moduleName, String permission) {
		boolean auth = false;
		Set<ResourceObject> resouces = queryResourceByRoleNames(roleNames);
		for (ResourceObject resource : resouces) {
			String mname = resource.getName();
			if (mname != null && mname.equals(moduleName)) {
				if (StringUtils.isBlank(permission)) {
					// 如果permission为空，只是对菜单进行鉴权，则只需匹配菜单名称
					auth = true;
				} else {
					String rid = resource.getId();
					for (ResourceObject r : resouces) {
						String pid = r.getPid();
						if (pid != null && rid.equals(pid)) {
							if (permission.equals(r.getName())) {
								auth = true;
								break;
							}
						}
					}
				}
				break;
			}
		}
		return auth;
	}

	/**
	 * 判断是否拥有指定权限名
	 * 
	 * @param roleNames
	 * @param permission
	 * @return
	 */
	public boolean hasSinglePermission(Collection<String> roleNames,
			String permission) {
		boolean auth = false;
		Set<ResourceObject> resouces = queryResourceByRoleNames(roleNames);
		for (ResourceObject resource : resouces) {
			if ("1".equals(resource.getKind())
					&& permission.equals(resource.getName())) {
				auth = true;
				break;
			}
		}
		return auth;
	}

	public String getMenuRoles(String menuId) {
		String key = String.format(RedisKeyConstants.MENU_ROLE_PERMISSION,
				menuId);
		String[] roles = redisSet(key).toArray(new String[] {});
		return Collections3.convertToString(Arrays.asList(roles), ",");
	}

	public String[] getMenusRoles(String menuId) {
		String key = String.format(RedisKeyConstants.MENU_ROLE_PERMISSION,
				menuId);
		return redisSet(key).toArray(new String[] {});
	}

	public Set<String> getRolesByMenu(String menuId) {
		String key = String.format(RedisKeyConstants.MENU_ROLE_PERMISSION,
				menuId);
		RedisSet<String> set = redisSet(key);
		Set<String> roles = new HashSet<String>();
		for (String role : set) {
			roles.add(role);
		}
		return roles;
	}

	/**
	 * 获取所有菜单与角色的关系 menuid与roles string的关系hashmap
	 */
	public Map<String, String> getAllMenuAndRolesRelation() {
		Map<String, String> map = new HashMap<String, String>();
		Set<String> keys = fuzzyGetKeys(String.format(
				RedisKeyConstants.MENU_ROLE_PERMISSION, "*"));
		for (String key : keys) {
			String[] roles = redisSet(key).toArray(new String[] {});
			String roleNames = Collections3.convertToString(
					Arrays.asList(roles), ",");
			String menuid = key.split(":")[1];
			map.put(menuid, roleNames);
		}
		return map;
	}

	/**
	 * 通过action请求的url获取模块的名称
	 * 
	 * @param url
	 * @return
	 */
	public String getModuleNameByActionUrl(String actionUrl) {

		Set<Entry<String, String>> entrys = redisMap(
				RedisKeyConstants.RESOURCE_NAME_LOCATION_REL).entrySet();
		for (Entry<String, String> entry : entrys) {
			String key = entry.getKey();
			String value = entry.getValue();
			if (value != null && value.equals(actionUrl)) {
				return key;
			}
		}
		return "";
	}

	/**
	 * 通过菜单字段获取菜单对象
	 * 
	 * @param url
	 * @return
	 */
	public ResourceObject getResourceObjectByParam(String xname, String xvalue) {
		List<ResourceObject> resources = getAllResources();
		if (resources == null)
			return null;
		for (ResourceObject resource : resources) {
			Field xfield = null;
			try {
				xfield = resource.getClass().getDeclaredField(xname);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
			Field.setAccessible(new Field[] { xfield }, true);
			String _xvalue = "";
			try {
				_xvalue = String.valueOf(xfield.get(resource));
			} catch (IllegalArgumentException e1) {
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			}
			if (_xvalue.equals(xvalue)) {
				return resource;
			}
		}
		return null;
	}

	public List<ResourceObject> getAllResources() {
		return getResourcesByKey(RedisKeyConstants.RESOURCE_LIST);
	}

	/**
	 * 该方法可获取通过RedisKeyConstants.RESOURCE_LIST获取所有资源，
	 * 亦可通过RedisKeyConstants.RESOURCE_RELATIONSHIP获取资源下级节点
	 * 
	 * @param key
	 *            该key的value为资源id的Set
	 * @return
	 */
	private List<ResourceObject> getResourcesByKey(String key) {

		String keyPattern = String.format(RedisKeyConstants.RESOURCE_BASE_INFO,
				"*");
		SortQuery<String> query = SortQueryBuilder.sort(key).noSort()
				.alphabetical(true).get(keyPattern + "->id")
				.get(keyPattern + "->pid").get(keyPattern + "->name")
				.get(keyPattern + "->nameCn").get(keyPattern + "->location")
				.get(keyPattern + "->status").get(keyPattern + "->order")
				.get(keyPattern + "->kind").get(keyPattern + "->isShowDesktop")
				.get(keyPattern + "->isWebpage").get(keyPattern + "->image")
				.build();

		BulkMapper<ResourceObject, String> bulkMapper = new BulkMapper<ResourceObject, String>() {
			@Override
			public ResourceObject mapBulk(List<String> tuple) {

				ResourceObject ro = new ResourceObject();
				Iterator<String> iterator = tuple.iterator();
				ro.setId(iterator.next());
				ro.setPid(iterator.next());
				ro.setName(iterator.next());
				ro.setNameCn(iterator.next());
				ro.setLocation(iterator.next());
				ro.setStatus(iterator.next());
				ro.setOrder(Integer.valueOf(iterator.next()));
				ro.setKind(iterator.next());
				ro.setIsShowDesktop(iterator.next());
				ro.setIsWebpage(iterator.next());
				ro.setImage(iterator.next());
				return ro;
			}
		};
		List<ResourceObject> list = getRedisTemplate().sort(query, bulkMapper);
		return list;
	}

	/**
	 * 初始化菜单与角色的关系
	 */
	@SuppressWarnings("rawtypes")
	public void initMenuAndRoleRelation(List list) {

		// 初始化之前先删除所有菜单与角色的关系
		Set<String> keys_menu_role = fuzzyGetKeys(String.format(
				RedisKeyConstants.MENU_ROLE_PERMISSION, "*"));
		Set<String> keys_role_menu = fuzzyGetKeys(String.format(
				RedisKeyConstants.ROLE_MENU_PERMISSION, "*"));

		if (!keys_menu_role.isEmpty()) {
			deleteKeys(keys_menu_role);
		}
		if (!keys_role_menu.isEmpty()) {
			deleteKeys(keys_role_menu);
		}

		// 初始化数据
		if (list != null) {
			int length = list.size();
			for (int i = 0; i < length; i++) {
				Object[] o = (Object[]) list.get(i);
				String menuId = String.valueOf(o[1]);
				String roleName = String.valueOf(o[0]);

				String key_menu_role = String.format(
						RedisKeyConstants.MENU_ROLE_PERMISSION, menuId);
				redisSet(key_menu_role).add(roleName);

				String key_role_menu = String.format(
						RedisKeyConstants.ROLE_MENU_PERMISSION, roleName);
				redisSet(key_role_menu).add(menuId);
			}
		}
	}

	/**
	 * 初始化所有有效的资源数据
	 * 
	 * @param list
	 */
	public void initAllValidResource(List<ResourceObject> list) {

		// 初始化之前先删除资源数据
		Set<String> keys = fuzzyGetKeys("resource:*");
		if (!keys.isEmpty())
			deleteKeys(keys);

		for (ResourceObject r : list) {
			// 0:表示新增
			addResourceData(r, "0");
		}
	}

	/**
	 * 通过key获取资源对象
	 * 
	 * @param key
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ResourceObject getByKey(String key) {
		Set<Entry<String, String>> entitys = redisMap(key).entrySet();
		Map map = new HashMap();
		for (Entry<String, String> entity : entitys) {
			map.put(entity.getKey(), entity.getValue());
		}
		return rMapper.fromHash(map);
	}

	public ResourceObject getById(String id) {
		String key = String.format(RedisKeyConstants.RESOURCE_BASE_INFO, id);
		return getByKey(key);
	}

	public ResourceObject getByName(String name) {
		return getById(name);// 和id的key生成方式是一样的
	}

	/**
	 * 更新节点关系以及顺序
	 * 
	 * @param params
	 */
	public void updateResourceParentAndOrder(ArrayList<JSONObject> params) {

		for (JSONObject param : params) {
			param.put(
					"idKey",
					String.format(RedisKeyConstants.RESOURCE_BASE_INFO,
							param.getString("id")));
			if (param.getString("oldParentId").equals("none") == false) {
				param.put("parentKey", String.format(
						RedisKeyConstants.RESOURCE_RELATIONSHIP,
						param.getString("parentId")));
				param.put("oldParentKey", String.format(
						RedisKeyConstants.RESOURCE_RELATIONSHIP,
						param.getString("oldParentId")));
			}
		}
		this.updateParentAndOrder(params, true);
	}
}
