package com.eastcom_sw.core.service.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.eastcom_sw.common.entity.NameValuePair;
import com.eastcom_sw.common.entity.Page;
import com.eastcom_sw.common.service.BaseService;
import com.eastcom_sw.common.service.ServiceException;
import com.eastcom_sw.common.utils.CnChar2PinyinUtil;
import com.eastcom_sw.core.dao.security.ResourceObject;
import com.eastcom_sw.core.dao.security.ResourceRedisDao;
import com.eastcom_sw.core.dao.security.UserDao;
import com.eastcom_sw.core.dao.security.UserRoleRedisDao;
import com.eastcom_sw.core.entity.security.Department;
import com.eastcom_sw.core.entity.security.Resource;
import com.eastcom_sw.core.entity.security.User;
import com.eastcom_sw.core.entity.security.UserExtensionConfiguration;
import com.eastcom_sw.security.extension.service.ConfigurationExtensionService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 用户管理
 * 
 * @author SCM
 * @time 上午10:33:02
 */
@Component
@Transactional(readOnly = true)
public class UserService extends BaseService {

	@Autowired
	@Qualifier("userDaoImpl")
	private UserDao userDao;
	@Autowired
	private ResourceRedisDao resourceRedisDao;
	@Autowired
	private UserRoleRedisDao userRoleRedisDao;
	@Autowired
	private ConfigurationExtensionService configurationExtensionService;

	@Autowired
	private DepartmentService departmentService;

	@Transactional(readOnly = false)
	public String saveUser(User user) throws ServiceException {
		return userDao.save(generateUserPinyin(user)).getId();
	}

	public void updateUser(User user) throws ServiceException {
		userDao.update(generateUserPinyin(user));
	}

	public String saveUser(User user, Map<String, String> extMap) throws ServiceException {
		String id = userDao.save(generateUserPinyin(user)).getId();
		updateUserExt(id, extMap);
		return id;
	}

	public void updateUser(User user, Map<String, String> extMap) throws ServiceException {
		userDao.update(generateUserPinyin(user));
		updateUserExt(user.getId(), extMap);
	}

	public void updateUserExt(String id, Map<String, String> extMap) {
		UserExtensionConfiguration cfg = getUserExtensionConfiguration();
		if (cfg.isEnabled() && extMap.size() > 0) {
			NameValuePair[] fields = new NameValuePair[extMap.size()];
			int count = 0;
			for (String key : extMap.keySet()) {
				NameValuePair nv = new NameValuePair(key, extMap.get(key));
				fields[count] = nv;
				count++;
			}
			userDao.updateUserExtension(id, cfg.getTableName(), fields);
		}
	}

	private User generateUserPinyin(User user) {
		if (user.getFullName().length() > 0) {
			Map<String, String> map = CnChar2PinyinUtil.getPinyinMap(user.getFullName());
			String s0x20 = map.get(CnChar2PinyinUtil.F0X20);
			String s0x04 = map.get(CnChar2PinyinUtil.F0X04);
			String s0x01 = map.get(CnChar2PinyinUtil.F0X01);
			user.setNamePinyin((s0x04 + s0x20 + "|" + s0x01).replace(" ", ""));
		} else {
			user.setNamePinyin("|");
		}
		return user;
	}

	public List<User> loadUserByPhoneForLogin(String phone) throws ServiceException {
		return userDao.loadUserByPhoneForLogin(phone);
	}

	public User loadUserByUsername(String username) throws ServiceException {
		return userDao.loadUserByUsername(username);
	}

	/**
	 * 获取当前用户的所有权限资源
	 * 
	 * @param isAdmin
	 * @param roles
	 * @return
	 */
	public Set<ResourceObject> getCurrentUserResource(boolean isAdmin, ArrayList<String> roles) {
		return isAdmin ? resourceRedisDao.queryAllResources() : resourceRedisDao.queryResourceByRoleNames(roles);
	}

	/**
	 * 获取当前用户的所有菜单资源
	 * 
	 * @param roles
	 *            角色名称列表
	 * @param rootResName
	 *            根节点资源名称,可选,为空返回所有权限节点
	 * @return
	 */
	public JSONObject getCurrentUserResources(List<String> roles, String rootMenuName) {
		JSONObject record = new JSONObject();
		String rootMenuId = "";
		Set<ResourceObject> resources = resourceRedisDao.queryResourceByRoleNames(roles);
		// System.out.println(JSONArray.fromObject(resources));
		// 组装菜单的数据：
		JSONArray menus = new JSONArray();

		if (StringUtils.isBlank(rootMenuName)) {
			rootMenuName = this.getCommonFieldValueByName("rootmenuname", "value");
		}
		// 计算有多少个parentid
		List<String> pids = new ArrayList<String>();
		for (ResourceObject resource : resources) {
			//String status = resource.getStatus();
			// //隐藏隐藏资源
			// if("2".equals(status))continue;
			String pid = resource.getPid();
			String kind = resource.getKind();
			String name = resource.getName();

			if (!rootMenuName.isEmpty() && rootMenuName.equalsIgnoreCase(name))
				rootMenuId = resource.getId();
			if (!kind.isEmpty()) {
				if ("null".equals(pid))
					pid = "";
				if (!pids.contains(pid)) {
					pids.add(pid);
					JSONObject menu = new JSONObject();
					menu.element("pid", pid);
					JSONArray items = new JSONArray();
					JSONObject resourceObj = JSONObject.fromObject(resource);
					if ("3".equals(resourceObj.getString("status"))) {
						resourceObj.element("nameCn", "*" + resourceObj.getString("nameCn"));
					}
					items.add(resourceObj);
					menu.element("items", items);
					menus.add(menu);
				} else {
					int index = pids.indexOf(pid);
					JSONObject obj = menus.getJSONObject(index);
					JSONObject resourceObj = JSONObject.fromObject(resource);
					if ("3".equals(resourceObj.getString("status"))) {
						resourceObj.element("nameCn", "*" + resourceObj.getString("nameCn"));
					}
					obj.getJSONArray("items").add(resourceObj);
				}
			}
		}
		record.element("menus", menus);
		record.element("rootId", rootMenuId);
		return record;
	}

	public JSONArray getCurrentUserTreeResources(List<String> roles, String rootMenuName, boolean controlStatus) {
		JSONArray roots = null;
		JSONArray result = new JSONArray();
		Set<ResourceObject> resources = resourceRedisDao.queryResourceByRoleNames(roles, controlStatus);
		Map<String, List<ResourceObject>> childs = new HashMap<String, List<ResourceObject>>();
		for (ResourceObject r : resources) {// 按照父节点进行分类
			if (r.getName().equals(rootMenuName)) {
				roots = new JSONArray();
				roots.add(JSONObject.fromObject(r));
			}
			String pIdKey = "";
			if (StringUtils.isBlank(r.getPid()) || "null".equals(r.getPid())) {
				pIdKey = "root";
			} else {
				pIdKey = r.getPid();
			}
			if (childs.containsKey(pIdKey)) {
				childs.get(pIdKey).add(r);
			} else {
				List<ResourceObject> l = new ArrayList<ResourceObject>();
				l.add(r);
				childs.put(pIdKey, l);
			}
		}

		if (roots == null) {// 未找到则从跟节点开始
			roots = JSONArray.fromObject(childs.get("root"));
		}

		for (Object o : roots) {
			JSONObject jo = JSONObject.fromObject(o);
			result.add(generateChilds(jo, childs));
		}

		Object[] os = result.toArray();
		Arrays.sort(os, new Comparator<Object>() {
			public int compare(Object o1, Object o2) {
				int jo1 = ((JSONObject) o1).getInt("order");
				int jo2 = ((JSONObject) o2).getInt("order");
				if (jo1 > jo2) {
					return 1;
				} else if (jo1 == jo2) {
					return 0;
				} else {
					return -1;
				}
			}
		});
		result = JSONArray.fromObject(os);
		return result;
	}

	/**
	 * 获取当前用户的菜单资源
	 * 
	 * @param roles
	 *            角色名称列表
	 * @param userId
	 * @param rootResName
	 *            根节点资源名称,可选,为空返回所有权限节点
	 * @return
	 */
	public JSONArray getCurrentUserTreeResources(List<String> roles, String rootMenuName, boolean controlStatus,
			String userId) {
		JSONArray roots = null;
		JSONArray result = new JSONArray();
		Set<ResourceObject> resources = resourceRedisDao.queryResourceByRoleNames(roles, controlStatus);
		Map<String, List<ResourceObject>> childs = new HashMap<String, List<ResourceObject>>();
		for (ResourceObject r : resources) {// 按照父节点进行分类
			if (r.getName().equals(rootMenuName)) {
				roots = new JSONArray();
				roots.add(JSONObject.fromObject(r));
			}
			String pIdKey = "";
			if (StringUtils.isBlank(r.getPid()) || "null".equals(r.getPid())) {
				pIdKey = "root";
			} else {
				pIdKey = r.getPid();
			}
			if (childs.containsKey(pIdKey)) {
				childs.get(pIdKey).add(r);
			} else {
				List<ResourceObject> l = new ArrayList<ResourceObject>();
				l.add(r);
				childs.put(pIdKey, l);
			}
		}

		if (roots == null) {// 未找到则从跟节点开始
			roots = JSONArray.fromObject(childs.get("root"));
		}

		for (Object o : roots) {
			JSONObject jo = JSONObject.fromObject(o);
			result.add(generateChilds(jo, childs));
		}

		// 是否显示个人快捷方式(上海网投要求)
		String showShortCutFlag = this.getCommonFieldValueByName("showShortCutFlag", "value");
		if (showShortCutFlag != null && "1".equals(showShortCutFlag)) {
			List<JSONObject> js = userDao.getShortCutResources(userId);

			JSONObject j = new JSONObject();
			j.put("id", "2c9004345982088501598269cc510000");
			j.put("image", "");
			j.put("isShowDesktop", "0");
			j.put("isWebpage", "0");
			j.put("kind", "0");
			j.put("location", "welcome.jsp");
			j.put("name", "user_shortcut");
			j.put("nameCn", "快捷方式");
			j.put("order", "-1");
			j.put("pid", "");
			j.put("status", "1");
			j.put("childs", js.toArray());
			//Object[] sss = js.toArray();
			result.add(j);
		}

		Object[] os = result.toArray();
		Arrays.sort(os, new Comparator<Object>() {
			public int compare(Object o1, Object o2) {
				int jo1 = ((JSONObject) o1).getInt("order");
				int jo2 = ((JSONObject) o2).getInt("order");
				if (jo1 > jo2) {
					return 1;
				} else if (jo1 == jo2) {
					return 0;
				} else {
					return -1;
				}
			}
		});
		result = JSONArray.fromObject(os);
		return result;
	}

	private JSONObject generateChilds(JSONObject p, Map<String, List<ResourceObject>> childs) {
		String id = p.getString("id");
		List<JSONObject> js = new ArrayList<JSONObject>();
		if (childs.containsKey(id)) {
			List<ResourceObject> cs = childs.get(id);
			for (ResourceObject r : cs) {
				js.add(generateChilds(JSONObject.fromObject(r), childs));
			}
		}

		Object[] os = js.toArray();
		Arrays.sort(os, new Comparator<Object>() {
			public int compare(Object o1, Object o2) {
				int jo1 = ((JSONObject) o1).getInt("order");
				int jo2 = ((JSONObject) o2).getInt("order");
				if (jo1 > jo2) {
					return 1;
				} else if (jo1 == jo2) {
					return 0;
				} else {
					return -1;
				}
			}
		});

		p.put("childs", os);
		return p;
	}

	/**
	 * 查询单个账户信息
	 * 
	 * @param id
	 *            账户id
	 * @param basicFlag
	 *            是否只返回基本信息
	 * @return 账户所有信息附带部门信息以及角色信息
	 */
	@SuppressWarnings("rawtypes")
	public JSONObject querySingleUser(String id, String basicFlag) {
		List userList = userDao.querySingleUserInfo(id);
		List deptList = userDao.queryUserDeptInfo(id);
		List roleList = userDao.queryUserRoleInfo(id);
		JSONArray ja = JSONArray.fromObject(userList.get(0));
		JSONObject jo = new JSONObject();
		jo.put("id", ja.get(0));
		jo.put("username", ja.get(1));
		// jo.put("version", ja.get(2));
		jo.put("sex", ja.get(3));
		jo.put("fullName", ja.get(5));
		jo.put("email", ja.get(6));
		jo.put("fixedNo", ja.get(7));
		jo.put("mobileNo", ja.get(8));
		jo.put("owner", ja.get(12));
		jo.put("userLevel", ja.get(13));
		jo.put("lastLoginTime", ja.get(19));
		jo.put("city", ja.get(21));

		if (basicFlag == null || basicFlag.equals("false")) {
			jo.put("password", "******");
			jo.put("accountEnabled", ja.get(9));
			jo.put("times", ja.get(10));
			jo.put("pwdModifyTime", ja.get(15));
			jo.put("pwdExpiredDays", ja.get(14));
			jo.put("creator", ja.get(11));
			jo.put("accoutExpiredStarttime", ja.get(16));
			jo.put("accoutExpiredEndtime", ja.get(17));
			jo.put("accoutCreateTime", ja.get(18));
			jo.put("oldPassord", ja.get(20));
		}

		String deptIdStr = "";
		String deptNameStr = "";
		String roleIdStr = "";
		String roleNameStr = "";
		for (Object o : deptList) {
			JSONArray jad = JSONArray.fromObject(o);
			deptIdStr += jad.getString(0) + ",";
			deptNameStr += jad.getString(1) + ",";
		}
		for (Object o : roleList) {
			JSONArray jar = JSONArray.fromObject(o);
			roleIdStr += jar.getString(0) + ",";
			roleNameStr += jar.getString(1) + ",";
		}
		if (deptIdStr.length() > 0) {
			deptIdStr = deptIdStr.substring(0, deptIdStr.length() - 1);
			deptNameStr = deptNameStr.substring(0, deptNameStr.length() - 1);
		}
		if (roleIdStr.length() > 0) {
			roleIdStr = roleIdStr.substring(0, roleIdStr.length() - 1);
			roleNameStr = roleNameStr.substring(0, roleNameStr.length() - 1);
		}
		jo.put("deptId", deptIdStr);
		jo.put("deptName", deptNameStr);
		jo.put("roleId", roleIdStr);
		jo.put("roleName", roleNameStr);
		return jo;
	}

	/**
	 * 获取所有用户信息
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getAllUser() {
		JSONArray items = new JSONArray();
		List<User> list = userDao.findAll();
		for (User user : list) {
			JSONObject r = new JSONObject();
			String deptStr = "";
			for (Department d : user.getDepartments()) {
				deptStr = deptStr + d.getNameCn() + "|";
			}
			r.put("name", user.getUserName());
			r.put("nameCn", user.getFullName());
			r.put("value", user.getId());
			r.put("order", user.getAccoutCreateTime());
			r.put("department", deptStr);
			items.add(r);
		}
		return items.toString();
	}

	/**
	 * 得到所有用户
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<User> getAllUserInfo() {
		List<User> list = userDao.findAll();
		return list;
	}

	/**
	 * 判断该用户名是否已存在
	 * 
	 * @param username
	 * @return
	 */
	public boolean userExsist(String username) {
		long num = userDao.queryUserByUsername(username);
		if (num > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取用户grid所需信息
	 * 
	 * @param start
	 * @param limit
	 * @param input
	 *            关键字
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Page queryUser(int start, int limit, String input, List deptIds, String startTime, String endTime, String havePermission) {
		return queryUser(start, limit, input, deptIds, startTime, endTime, havePermission, null);
	}
	
	/**
	 * 获取用户grid所需信息
	 * 
	 * @param start
	 * @param limit
	 * @param input
	 *            关键字
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Page queryUser(int start, int limit, String input, List deptIds, String startTime, String endTime, String havePermission, Map<String,String> paramMap) {
		Page daoResult;
		List<String> keys = new ArrayList<String>();
		if (input != null && input.length() > 0) {
			boolean containChinese = false;
			for (int i = 0; i < input.length(); i++) {
				if (input.substring(i, i + 1).matches("[\\u4e00-\\u9fa5]+"))
					containChinese = true;
			}
			if (containChinese) {
				Map<String, String> map = CnChar2PinyinUtil.getPinyinMap(input);
				String[] keysArray = map.get(CnChar2PinyinUtil.F0X04).split(" ");
				for (String k : keysArray) {
					if (StringUtils.isNotBlank(k)) {
						keys.add(k);
					}
				}
				daoResult = userDao.searchUserPaged(start, limit, keys, deptIds, false, startTime, endTime, havePermission, paramMap);
			} else {
				char[] keysChar = input.toCharArray();
				for (char c : keysChar) {
					keys.add(c + "");
				}
				daoResult = userDao.searchUserPaged(start, limit, keys, deptIds, true, startTime, endTime, havePermission, paramMap);
			}
		} else {
			daoResult = userDao.searchUserPaged(start, limit, keys, deptIds, true, startTime, endTime, havePermission, paramMap);
		}
		return daoResult;
	}

	/**
	 * 根据中文或者简拼查询用户
	 * 
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String searchUser(String input) {
		boolean containChinese = false;
		List<String> keys = new ArrayList<String>();
		JSONArray items = new JSONArray();
		List daoResult;
		// String[] keys;
		for (int i = 0; i < input.length(); i++) {

			if (input.substring(i, i + 1).matches("[\\u4e00-\\u9fa5]+"))
				containChinese = true;
		}
		if (containChinese) {
			Map<String, String> map = CnChar2PinyinUtil.getPinyinMap(input);
			String[] keysArray = map.get(CnChar2PinyinUtil.F0X04).split(" ");
			for (String k : keysArray) {
				keys.add(k);
			}
			daoResult = userDao.searchUser(keys, false);
		} else {
			char[] keysChar = input.toCharArray();
			for (char c : keysChar) {
				keys.add(c + "");
			}
			daoResult = userDao.searchUser(keys, true);
			if (daoResult == null) {
				daoResult = userDao.searchUser(keys, false);
			}
		}

		if (daoResult != null && daoResult.size() > 0) {
			JSONArray js = JSONArray.fromObject(daoResult);
			for (Object o : js) {
				String str = o.toString().replace("[", "").replace("\"", "").replace("]", "");
				String[] oneDataStr = str.split(",");
				JSONObject r = new JSONObject();
				r.put("value", oneDataStr[0]);
				r.put("nameCn", oneDataStr[1]);
				r.put("department", oneDataStr[2]);
				items.add(r);
			}
		}
		return items.toString();
	}

	public User findUser(String id) {
		return userDao.findOne(id);
	}

	/**
	 * 根据id集合获取用户集合
	 * 
	 * @param ids
	 * @return
	 */
	public List<User> findByIds(String[] ids) {
		return userDao.findByIds(ids);
	}

	/**
	 * 修改用户权限部门信息
	 * 
	 * @param userId
	 *            :当前所要编辑的用户的id
	 * @param addIds
	 *            :用户新增权限部门
	 * @param delIds
	 *            :用户丧失权限的部门
	 */
	public void changeUserDeptRange(String userId, String[] addIds, String[] delIds) {
		userDao.changeUserDeptRange(userId, addIds, delIds);
	}

	/**
	 * 修改用户权限角色信息
	 * 
	 * @param userIds
	 *            当前所要编辑的用户的id集合
	 * @param addIds
	 *            用户新增权限角色
	 * @param delIds
	 *            用户丧失权限角色
	 */
	public void changeUserRoleRange(String userIds, String[] addIds, String[] delIds, String[] addNames,
			String[] delNames) {
		/**
		 * Edit by JJF JUN 05 2014 修改为可同时更新多个具有相同角色的账户
		 */
		if (StringUtils.isNotBlank(userIds)) {
			String[] ids = userIds.split(",");
			userDao.changeUserRoleRange(ids, addIds, delIds);
			// 维护redis中用户角色的关系
			for (String id : ids) {
				userRoleRedisDao.userRoleAuthorization(id, addNames, delNames);
			}
		}
	}

	/**
	 * 批量删除用户
	 * 
	 * @param userIds
	 */
	public void deleteUser(List<String> userIds) {
		UserExtensionConfiguration cfg = getUserExtensionConfiguration();
		if (cfg.isEnabled()) {
			userDao.deleteUser(userIds, cfg.getTableName());
		} else {
			userDao.deleteUser(userIds);
		}
		// 删除redis中用户角色相关数据
		userRoleRedisDao.removeRelationByUsers(userIds);
	}

	/**
	 * 设置帐号可用状态
	 * 
	 * @param ids
	 *            被设置帐号集合
	 * @param flag
	 *            true 可用 false 不可用
	 */
	public void setAccountEnabled(String[] ids, boolean flag) {
		userDao.setAccountEnabled(ids, flag);
	}

	/**
	 * 获取用户的角色信息
	 * 
	 * @param uid
	 * @return Map<roleID,roleName>
	 */
	@SuppressWarnings("rawtypes")
	public Map<String, String> queryUserRoleEnInfo(String uid) {
		List daoRs = userDao.queryUserRoleEnInfo(uid);
		Map<String, String> returnMap = new HashMap<String, String>();
		for (Object o : daoRs) {
			JSONArray ja = JSONArray.fromObject(o);
			returnMap.put(ja.getString(0), ja.getString(1));
		}
		return returnMap;
	}

	/**
	 * 从redis中获取角色的名称
	 * 
	 * @param userId
	 * @return
	 */
	public List<String> getUserRoleNames(String userId) {
		return userRoleRedisDao.getUserRoleNames(userId);
	}

	/**
	 * 初始化用户角色关系到redis
	 */
	@SuppressWarnings("rawtypes")
	public void initUserRoleRelation() {
		List list = userDao.queryAllUserRoleEnInfo();
		if (list != null && list.size() > 0)
			userRoleRedisDao.initUserRoleRelation(list);
	}

	/**
	 * 更新用户基本信息
	 * 
	 * @param email
	 * @param mobile
	 * @param tel
	 */
	public void updateBasicInfo(String email, String mobile, String tel, String id) {
		userDao.updateBasicInfo(email, mobile, tel, id);
	}

	/**
	 * 查询用户扩展字段，表明以及字段名从外部传入
	 * 
	 * @return
	 */
	public Map<NameValuePair, String> queryUserExtension(String id) {
		Map<NameValuePair, String> rs = new HashMap<NameValuePair, String>();
		UserExtensionConfiguration cfg = getUserExtensionConfiguration();
		if (cfg.isEnabled()) {
			if (StringUtils.isNotBlank(cfg.getTableName()) && cfg.getRules() != null && cfg.getRules().size() > 0) {
				Set<UserExtensionConfiguration.UserExtensionRule> rules = cfg.getRules();
				String[] fields = new String[rules.size()];
				String[] fields_names = new String[rules.size()];
				int count = 0;
				for (UserExtensionConfiguration.UserExtensionRule r : rules) {
					fields[count] = r.getName();
					fields_names[count] = r.getNameCn();
					count++;
				}

				Object o = userDao.queryUserExtension(id, cfg.getTableName(), fields);
				if (o != null) {
					if (fields.length > 1) {
						JSONArray ja = JSONArray.fromObject(o);
						for (int i = 0; i < fields.length; i++) {
							String f = fields[i];
							NameValuePair p = new NameValuePair(f, fields_names[i]);
							rs.put(p, ja.getString(i));
						}
					} else {
						NameValuePair p = new NameValuePair(fields[0], fields_names[0]);
						rs.put(p, o.toString());
					}
				}
			}
		}
		return rs;
	}

	/**
	 * 获取用户扩展字段配置
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public UserExtensionConfiguration getUserExtensionConfiguration() {
		UserExtensionConfiguration cfg = new UserExtensionConfiguration();
		String[] types = { "security" };
		Map m = configurationExtensionService.getSysArguments(types);
		Map<String, String> map = (Map<String, String>) m.get("security");
		String userExtArgs = map.get("userExtArgs");
		if (StringUtils.isNotBlank(userExtArgs)) {
			JSONObject jo = JSONObject.fromObject(userExtArgs);
			String enabled = jo.getString("userExtEnabled");
			String userExtTable = jo.getString("userExtTable");
			String userExtMappingRules = jo.getString("userExtMappingRules");
			if ("null".equals(enabled))
				enabled = "";
			if ("null".equals(userExtTable))
				userExtTable = "";
			if ("null".equals(userExtMappingRules))
				userExtMappingRules = "";
			cfg.loadCfg(enabled, userExtTable, userExtMappingRules);
		}
		return cfg;
	}

	/**
	 * 
	 * 方法说明：忘记密码时，根据用户名和手机号码，判断该用户是否存在
	 * 
	 * @Title: userExsist
	 * @author: fangqian
	 * @param userName
	 * @param phoneNumber
	 * @return
	 * @return boolean
	 * @date: 2015年8月18日 下午4:29:18
	 */
	public boolean userExsist(String userName, String phoneNumber) {
		long num = userDao.queryUserByUsername(userName, phoneNumber);
		if (num > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * 方法说明：根据用户名和手机号确定用户基本信息
	 * 
	 * @Title: getUserInfo
	 * @author: fangqian
	 * @param userName
	 * @param phoneNumber
	 * @return
	 * @return User
	 * @date: 2015年8月20日 下午4:00:04
	 */
	public User getUserInfo(String userName, String phoneNumber) {
		return userDao.getUserInfo(userName, phoneNumber);
	}

	public void updateUserPassword(String password, String oldPassword, String userId, String currentDatetime) {
		userDao.updateUserPassword(password, oldPassword, userId, currentDatetime);
	}

	public void updateExtInfo(String userId, String userExtTable, String extVal) {
		userDao.updateExtInfo(userId, userExtTable, extVal);
	}

	/**
	 * 获取用户权限内的所有部门
	 * 
	 * @param uid
	 *            用户id
	 */
	@SuppressWarnings("rawtypes")
	public List getUserDepartmentRange(String uid) {
		return userDao.getUserDepartmentRange(uid);
	}

	/**
	 * 获取该用户所能看见的所有角色
	 * 
	 * @param uid
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List getUserAllRole(String uid) {
		return userDao.getUserAllRole(uid);
	}

	/**
	 * 根据用户名，拼音，部门id列表模糊查询用户信息
	 * 
	 * @param input
	 * @param deptIds
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List queryUserInfoByDeptIds(String input, List deptIds) {
		List list;
		list = userDao.queryUserInfo(input, deptIds);
		// List<String> keys = new ArrayList<String>();
		// if (input != null && input.length() > 0) {
		// boolean containChinese = false;
		// for (int i = 0; i < input.length(); i++) {
		// if (input.substring(i, i + 1).matches("[\\u4e00-\\u9fa5]+"))
		// containChinese = true;
		// }
		// if (containChinese) {
		// Map<String, String> map = CnChar2PinyinUtil.getPinyinMap(input);
		// String[] keysArray = map.get(CnChar2PinyinUtil.F0X04)
		// .split(" ");
		// for (String k : keysArray) {
		// if (StringUtils.isNotBlank(k)) {
		// keys.add(k);
		// }
		// }
		// list = userDao.queryUserInfo(keys,deptIds,false);
		// } else {
		// char[] keysChar = input.toCharArray();
		// for (char c : keysChar) {
		// keys.add(c + "");
		// }
		// list = userDao.queryUserInfo(keys,deptIds,true);
		// }
		// } else {
		// list = userDao.queryUserInfo(keys,deptIds,true);
		// }
		return list;
	}

	/**
	 * 根据多个用户账号查看用户的信息
	 * 
	 * @param userList
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List getMoreUserInfo(List userList) {
		return userDao.getMoreUserInfo(userList);
	}

	/**
	 * 根据用户名，拼音，角色id列表查询用户信息
	 * 
	 * @param input
	 * @param roleIds
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List queryUserInfoByRoleIds(String input, List roleIds) {
		List list;
		list = userDao.queryUserInfoByRoleIds(input, roleIds);
		// List<String> keys = new ArrayList<String>();
		// if (input != null && input.length() > 0) {
		// boolean containChinese = false;
		// for (int i = 0; i < input.length(); i++) {
		// if (input.substring(i, i + 1).matches("[\\u4e00-\\u9fa5]+"))
		// containChinese = true;
		// }
		// if (containChinese) {
		// Map<String, String> map = CnChar2PinyinUtil.getPinyinMap(input);
		// String[] keysArray = map.get(CnChar2PinyinUtil.F0X04)
		// .split(" ");
		// for (String k : keysArray) {
		// if (StringUtils.isNotBlank(k)) {
		// keys.add(k);
		// }
		// }
		// list = userDao.queryUserInfoByRoleIds(keys,roleIds,false);
		// } else {
		// char[] keysChar = input.toCharArray();
		// for (char c : keysChar) {
		// keys.add(c + "");
		// }
		// list = userDao.queryUserInfoByRoleIds(keys,roleIds,true);
		// }
		// } else {
		// list = userDao.queryUserInfoByRoleIds(keys,roleIds,true);
		// }
		return list;
	}

	@SuppressWarnings("rawtypes")
	public List queryRoleInfoByUnames(List unames) {
		return userDao.queryRoleInfoByUnames(unames);
	}

	@SuppressWarnings("rawtypes")
	public List queryRoleInfoByRnames(String roleName) {
		return userDao.queryRoleInfoByRnames(roleName);
	}

	public boolean getUsernameStatus(String userIP, String ipLimitTimes, String date) {
		return userDao.getUsernameStatus(userIP, ipLimitTimes, date);
	}

	public void deleteLockedHost(String userIP) {
		userDao.deleteLockedHost(userIP);

	}

	@SuppressWarnings("rawtypes")
	public List getLockedUserIPInfo(String userIP, String ipLimitTimes, String ipLimitTime) {
		return userDao.getLockedUserIPInfo(userIP, ipLimitTimes, ipLimitTime);
	}

	public void saveUserIpStatus(String userIP, String str) {
		userDao.saveUserIpStatus(userIP, str);
	}

	public void updateLockedUserIPInfo(String userIP, String wrongTimes, String lockedTime) {
		userDao.updateLockedUserIPInfo(userIP, wrongTimes, lockedTime);

	}

	/**
	 * 根据用户id 返回改用户所处部门的用户 以及该用户下级部门的用户 （该方法一般为普通管理员使用）
	 * 
	 * @param userId
	 * @return
	 */
	public String getUserByDeptId(String userId) {
		String deptId = userDao.getUserDeptId(userId);// 根据用户id得到用户所处部门id
		String deptids = departmentService.findAllChildrenIdById(deptId);
		String userNames = userDao.findAllUserByDeptIds(deptids);
		return userNames;
	}

	/**
	 * 根据传入的用户ID和资源名称，判断该用户是否有该资源的权限
	 * 
	 * @param userId
	 *            用户ID
	 * @param rosourceName
	 *            资源名称
	 * @param parentName
	 *            资源名称的父节点名称
	 * @return
	 */
	public boolean getRoleByUsername(String userId, String parentName, String rosourceName) {
		boolean flag = false;
		// 根据用户ID获取角色
		List<String> roles = this.getUserRoles(userId);

		Resource resources = resourceRedisDao.queryResourceByRoleNamesAndParentName(roles, parentName);

		if (resources != null) {
			for (Resource resource : resources.getChildResources()) {
				String name = resource.getName();

				if (rosourceName.equals(name)) {
					flag = true;
					break;
				}
			}
		}

		return flag;
	}

}
