package com.eastcom_sw.core.service.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.eastcom_sw.common.service.BaseService;
import com.eastcom_sw.common.service.ServiceException;
import com.eastcom_sw.common.utils.ParseJSONObject;
import com.eastcom_sw.core.dao.security.ResourceDao;
import com.eastcom_sw.core.dao.security.ResourceObject;
import com.eastcom_sw.core.dao.security.ResourceRedisDao;
import com.eastcom_sw.core.dao.security.RoleDao;
import com.eastcom_sw.core.dao.security.UserDao;
import com.eastcom_sw.core.dao.security.UserRoleRedisDao;
import com.eastcom_sw.core.entity.security.Role;
import com.eastcom_sw.core.entity.security.User;
import com.eastcom_sw.core.utils.security.DepartmentTreeNode;

/**
 * 角色管理
 * 
 * @author SCM
 * @time 2012-8-2
 */
@Component
@Transactional(readOnly = true)
public class RoleService extends BaseService {
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private ResourceDao resourceDao;
	@Autowired
	private ResourceRedisDao redisDao;
	@Autowired
	private UserRoleRedisDao userRoleRedisDao;
	@Autowired
	private UserDao userDao;

	@Transactional(readOnly = false)
	public void saveRole(Role role) throws ServiceException {
		roleDao.save(role);
	}

	public Role findRole(String id) {
		return roleDao.findOne(id);
	}

	/**
	 * 查找角色列表
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Role> findAllRole() {
		return (List<Role>) roleDao.findAll();
	}

	/**
	 * 获取对某个用户角色授权列表数据(用户管理-用户角色授权使用)
	 * 
	 * @param currentUser
	 *            当前登录用户
	 * @param List
	 *            <User> editUsers 被授权用户集合（如果被判定为权限不相等，则返回失败）
	 * @return 每条数据都带有checked属性(被授权用户已经拥有的权限)以及disabled属性(当前用户没有权限操作的权限)
	 * 
	 */

	@SuppressWarnings("rawtypes")
	public List<JSONObject> getUserRoleAccreditData(User currentUser,
			List<User> editUsers,String userNames) {
		// edit by JJF JUN 04 2014
		// 由单用户，改为允许权限相同的多用户
		if (editUsers != null && editUsers.size() > 0) {
			List currentUserRoles = roleDao.querySimpleRoleInfoByUserIdNew(
					currentUser, currentUser.getUserLevel().equals("1"), true,userNames);
			List editUserRoles = roleDao.querySimpleRoleInfoByUserId(
					editUsers.get(0), false, false);
			Map<String, Object> cMap = new HashMap<String, Object>();
			Map<String, Object> aMap = new HashMap<String, Object>();
			for (Object o : editUserRoles) {
				JSONArray ja = JSONArray.fromObject(o);
				cMap.put(ja.getString(0), o);
			}
			for (Object o : currentUserRoles) {
				JSONArray ja = JSONArray.fromObject(o);
				aMap.put(ja.getString(0), o);
			}
			boolean roleChecked = true;// 同时编辑多个用户时需进行角色一致性验证
			if (editUsers.size() > 1) {
				usersloop: for (int i = 1; i < editUsers.size(); i++) {
					List roles = roleDao.querySimpleRoleInfoByUserId(
							editUsers.get(i), false, false);
					if (roles.size() != cMap.size()) {
						roleChecked = false;
						break usersloop;
					} else {
						for (Object o : roles) {
							JSONArray ja = JSONArray.fromObject(o);
							if (!cMap.containsKey(ja.getString(0))) {
								roleChecked = false;
								break usersloop;
							}
						}
					}
				}
			}
			if (roleChecked) {
				LinkedList<String> attributeList = new LinkedList<String>();
				attributeList.add("id");
				attributeList.add("nameCn");
				attributeList.add("desc");
				attributeList.add("name");
				return AccreditGrid.makeAccreditGrid(cMap, aMap)
						.getJsonObjectList(attributeList, 0);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * 检查是否存在同名角色，名称或中文名称
	 * 
	 * @param name
	 * @param type
	 * @return
	 */
	public boolean nameExsistCheck(String name, String type) {
		long num = roleDao.queryRoleByName(name, type);
		if (num > 0) {
			return true;// 存在
		} else {
			return false;// 不存在
		}
	}

	/**
	 * 获取用户所有的相关角色，包括拥有权限角色和其新建的角色(角色管理主界面列表数据)
	 * 
	 * @param userId
	 *            用户id
	 * @return 角色id 角色名称 角色中文名 是否临时 用户数 资源数 有效开始时间 有效结束时间 创建者 创建时间 角色简介
	 */
	@SuppressWarnings("rawtypes")
	public List queryAllRolesByUser(User u,String userNames) {
		List daoRs = roleDao.queryAllRolesByUser(u,userNames);
		List<JSONObject> returnList = new ArrayList<JSONObject>();
		if (daoRs != null) {
			for (Object o : daoRs) {
				JSONArray ja = JSONArray.fromObject(o);
				JSONObject record = new JSONObject();
				record.put("id", ja.getString(0));
				record.put("createTime", ja.getString(1));
				record.put("creator", ja.getString(2));
				record.put("description", ja.getString(3));
				record.put("name", ja.getString(4));
				record.put("nameCn", ja.getString(5));
				record.put("userNum", ja.getString(6) != null ? ja.getString(6)
						: 0);
				// if (ja.getString(4).equals(// 判断是否是超级管理员角色
				// this.getCommonFieldValue("roleMng", "name",
				// "currentAdminName", "value"))) {
				// record.put("resourceNum", redisDao.queryAllResources()
				// .size());
				// } else {
				record.put("resourceNum",
						ja.getString(7) != null ? ja.getString(7) : 0);
				// }
				returnList.add(record);
			}
		}
		return returnList;
	}

	public void updateRole(Role r) {
		roleDao.update(r);
	}

	/**
	 * 更新角色基本信息
	 * 
	 * @param id
	 * @param name
	 * @param nameCn
	 * @param validStartTime
	 * @param validEndTime
	 * @param description
	 */
	public void updateRoleBasicInfo(String id, String name, String nameCn,
			String validStartTime, String validEndTime, String description) {
		roleDao.updateRoleBasicInfo(id, name, nameCn, validStartTime,
				validEndTime, description);
	}

	/**
	 * 获取角色资源树
	 * 
	 * @param roleName
	 * @return
	 */
	public String getRoleResourceTree(String roleName) {
		Set<ResourceObject> resultSet = null;
		if (roleName != null) {
			resultSet = redisDao.queryResourceByRoleName(roleName);
		} else {
			resultSet = redisDao.queryAllResources();
		}
		List<String[]> newList = new ArrayList<String[]>();
		for (ResourceObject ro : resultSet) {
			String strs[] = { ro.getId(), "", ro.getName(), ro.getNameCn(),
					ro.getOrder() + "", ro.getPid() };
			newList.add(strs);
		}
		DepartmentTreeNode dtn = DepartmentTreeNode.makeTree(newList);
		return dtn.getTreeJson(false);
	}

	/**
	 * 获取角色资源授权树
	 * 
	 * @param roleNameList
	 *            当前登录用户角色名称集合
	 * @param roleId
	 *            当前被授权的角色id
	 * @return
	 */
	public String getRoleResourceAccreditTree(List<String> roleNameList,
			String roleName) {
		Set<ResourceObject> currentUserResultSet = new HashSet<ResourceObject>();
		Set<ResourceObject> currentRoleResultSet = new HashSet<ResourceObject>();
		if (roleNameList != null) {
			currentUserResultSet = redisDao.queryResourceByRoleNames(
					roleNameList, false);
		} else {
			//currentUserResultSet = redisDao.queryAllResources(false);
			currentUserResultSet = redisDao.queryAllResources(true);//剔除已经停用的节点以及自节点
		}
		if (roleName != null) {
			if (!"".equals(roleName)) {
				currentRoleResultSet = redisDao.queryResourceByRoleName(
						roleName, false);
			}
		} else {
//			currentRoleResultSet = redisDao.queryAllResources(false);
			currentUserResultSet = redisDao.queryAllResources(true);//剔除已经停用的节点以及自节点
		}

		List<String[]> sendUserResourceList = new ArrayList<String[]>();
		List<String[]> sendRoleResourceList = new ArrayList<String[]>();
		for (ResourceObject ro : currentUserResultSet) {
			String strs[] = { ro.getId(), "", ro.getName(), ro.getNameCn(),
					ro.getOrder() + "", ro.getPid() };
			sendUserResourceList.add(strs);
		}
		for (ResourceObject ro : currentRoleResultSet) {
			String strs[] = { ro.getId(), "", ro.getName(), ro.getNameCn(),
					ro.getOrder() + "", ro.getPid() };
			sendRoleResourceList.add(strs);
		}
		DepartmentTreeNode dtn = DepartmentTreeNode.makeAccreditTree(
				sendRoleResourceList, sendUserResourceList);
		return dtn.getAccreditTreeJson();
	}

	/**
	 * 修改角色资源权限
	 * 
	 * @param roleId
	 *            :当前所要编辑的角色id
	 * @param addIds
	 *            :新增权限资源id集合
	 * @param delIds
	 *            :丧失权限资源id集合
	 */
	public void updateRoleResourceRange(String roleId, String[] addIds,
			String[] delIds, String roleName) {
		roleDao.updateRoleResourceRange(roleId, addIds, delIds);
		// 更新redis中角色与菜单的关系
		redisDao.roleAuthorization(addIds, delIds, roleName);
	}

	/**
	 * 批量授权角色资源
	 * 
	 * @param roleIds
	 * @param addIds
	 * @param delIds
	 * @param roleNames
	 */
	public void updateRoleResourceRange(String[] roleIds, String[] addIds,
			String[] delIds, String[] roleNames) {
		Map<String, Set<String>> resRolesCache = new HashMap<String, Set<String>>();
		for (int i = 0; i < roleNames.length; i++) {
			String roleName = roleNames[i];
			List<String> _addIds = new ArrayList<String>();
			for (String a_id : addIds) {
				Set<String> rolesSet = resRolesCache.get(a_id);
				if (rolesSet == null) {
					rolesSet = redisDao.getRolesByMenu(a_id);
					resRolesCache.put(a_id, rolesSet);
				}
				if (!rolesSet.contains(roleName)) {// 该角色未拥有该资源权限，加入权限
					_addIds.add(a_id);
				}
			}
			updateRoleResourceRange(roleIds[i],
					_addIds.toArray(new String[_addIds.size()]), delIds,
					roleName);
		}
	}

	/**
	 * 修改角色区域权限
	 * 
	 * @param roleId
	 *            :当前所要角色的id
	 * @param addIds
	 *            :新增区域权限集合
	 * @param delIds
	 *            :丧失区域权限集合
	 */
	public void updateRoleRegionRange(String roleId, String[] addIds,
			String[] delIds) {
		roleDao.updateRoleRegionRange(roleId, addIds, delIds);
	}

	/**
	 * 获取角色区域授权列表(角色管理，区域授权使用)
	 * 
	 * @param u
	 *            当前登录用户
	 * @param roleId
	 *            当前被编辑的角色
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<JSONObject> getRoleRegionAccreditGridData(User u, String roleId) {
		int totalRegionNum = this.getCommonList("region").size();
		String valueStr = "";
		for (int i = 1; i <= totalRegionNum; i++) {
			String valueString = "0" + i;
			valueString = valueString.length() > 2 ? valueString.substring(
					valueString.length() - 2, valueString.length())
					: valueString;
			valueStr += (valueString + ",");
		}
		valueStr = valueStr.substring(0, valueStr.length() - 1);
		HashMap<String, String> regionMapLabel = this.getCommonMultiFieldValue(
				"region", "value", valueStr, "label");
		List currentRegionDaoRs = new ArrayList();
		List editRegionDaoRs = new ArrayList();
		Map<String, Object> aMap = new HashMap<String, Object>();
		Map<String, Object> cMap = new HashMap<String, Object>();
		if (u.getUserLevel().equals("1") == false) {
			currentRegionDaoRs = roleDao.queryRegionByUser(u);
			if (currentRegionDaoRs != null && currentRegionDaoRs.size() > 0) {
				for (Object o : currentRegionDaoRs) {
					String[] regionInfo = { o.toString(),
							regionMapLabel.get(o.toString()) };
					aMap.put(o.toString(), regionInfo);
				}
			}
		} else {
			for (int i = 1; i <= totalRegionNum; i++) {
				String valueString = "0" + i;
				valueString = valueString.length() > 2 ? valueString.substring(
						valueString.length() - 2, valueString.length())
						: valueString;
				String[] str = { valueString, regionMapLabel.get(valueString),
						"" };
				aMap.put(valueString, str);
			}
		}
		editRegionDaoRs = roleDao.queryRegionByRoleId(roleId);
		if (editRegionDaoRs != null && editRegionDaoRs.size() > 0) {
			for (Object o : editRegionDaoRs) {
				String[] regionInfo = { o.toString(),
						regionMapLabel.get(o.toString()) };
				cMap.put(o.toString(), regionInfo);
			}
		}
		LinkedList<String> attributeList = new LinkedList<String>();
		attributeList.add("id");
		attributeList.add("nameCn");
		AccreditGrid ag = AccreditGrid.makeAccreditGrid(cMap, aMap);
		List<JSONObject> returnList = new LinkedList<JSONObject>();
		Map<String, JSONObject> resultMap = ag.getJsonObjectMap(attributeList,
				0);
		for (int i = 1; i <= totalRegionNum; i++) {
			String valueString = "0" + i;
			valueString = valueString.length() > 2 ? valueString.substring(
					valueString.length() - 2, valueString.length())
					: valueString;
			if (resultMap.get(valueString) != null) {
				returnList.add(resultMap.get(valueString));
			}
		}
		return returnList;
	}

	/**
	 * 获取用户当前权限地域(baseController使用)
	 * 
	 * @param u
	 *            用户对象
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<JSONObject> getUserRegionRange(User u) {
		int totalRegionNum = Integer.parseInt(this.getCommonFieldValue(
				"region", "name", "totalRegionNum", "value"));
		String valueStr = "";
		for (int i = 1; i <= totalRegionNum; i++) {
			String valueString = "0" + i;
			valueString = valueString.length() > 2 ? valueString.substring(
					valueString.length() - 2, valueString.length())
					: valueString;
			valueStr += (valueString + ",");
		}
		valueStr = valueStr.substring(0, valueStr.length() - 1);
		HashMap<String, String> regionMapLabel = this.getCommonMultiFieldValue(
				"region", "value", valueStr, "label");
		List currentRegionDaoRs = new ArrayList();
		List<JSONObject> returnList = new ArrayList<JSONObject>();
		if (u.getUserLevel().equals("1") == false) {
			currentRegionDaoRs = roleDao.queryRegionByUser(u);
			if (currentRegionDaoRs != null && currentRegionDaoRs.size() > 0) {
				for (Object o : currentRegionDaoRs) {
					JSONObject jo = new JSONObject();
					jo.put("label", regionMapLabel.get(o.toString()));
					jo.put("value", o.toString());
					returnList.add(jo);
				}
			}
		} else {
			for (int i = 1; i <= totalRegionNum; i++) {
				String valueString = "0" + i;
				valueString = valueString.length() > 2 ? valueString.substring(
						valueString.length() - 2, valueString.length())
						: valueString;
				JSONObject jo = new JSONObject();
				jo.put("label", regionMapLabel.get(valueString));
				jo.put("value", valueString);
				returnList.add(jo);
			}
		}
		return returnList;
	}

	/**
	 * 批量删除角色(暂时前台只传一个id)
	 * 
	 * @param userIds
	 *            roleId集合
	 */
	public void deleteRole(List<String> roleIds, String roleNames[]) {
		roleDao.deleteRole(roleIds);
		// redis中删除角色与菜单的关系
		redisDao.deleteRoleMenuRelation(roleNames);

		// redis中删除角色与用户的关系
		userRoleRedisDao.removeRelationByRoles(roleIds,
				Arrays.asList(roleNames));
	}

	/**
	 * 通过角色id获取该角色下的用户信息
	 * 
	 * @param roleId
	 * @return 用户中文名
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List queryUserInfoByRole(String roleId) {
		String[] roleIds = { roleId };
		List daoRs = roleDao.queryUserInfoByRole(roleIds);
		return ParseJSONObject.parse("id,name,nameCn,roleId", daoRs);
	}

	/**
	 * 初始化redis中资源与角色的关系
	 */
	@SuppressWarnings("rawtypes")
	public void initResourceAndRoleRelationToRedis() {
		List list = roleDao.queryAllRoleResourceRelationships();
		if (list != null) {
			redisDao.initMenuAndRoleRelation(list);
		}
	}

	/**
	 * 判断角色是否拥有该权限
	 * 
	 * @param roleName
	 * @param resId
	 * @return
	 */
	public boolean hasResPermission(String roleName, String resId) {
		Set<String> roles = redisDao.getRolesByMenu(resId);
		return roles != null && roles.contains(roleName);
	}
}
