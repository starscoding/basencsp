/**
 * 
 */
package com.eastcom_sw.core.dao.security;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.eastcom_sw.common.dao.jpa.Dao;
import com.eastcom_sw.common.entity.NameValuePair;
import com.eastcom_sw.common.entity.Page;
import com.eastcom_sw.core.entity.security.User;

/**
 * @author SCM
 * 
 */
public interface UserDao extends Dao<User> {
	/**
	 * 通过用户名获取用户信息
	 * 
	 * @param username
	 * @return
	 */
	public User loadUserByUsername(String username);

	/**
	 * 根据id集合获取用户集合
	 * 
	 * @param ids
	 * @return
	 */
	public List<User> findByIds(String[] ids);

	/**
	 * 通过手机号码获取用户
	 * 
	 * @param phone
	 * @return
	 */
	public List<User> loadUserByPhoneForLogin(String phone);

	/**
	 * 获取当前用户的所有资源
	 * 
	 * @param userid
	 *            用户ID
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List getCurrentUserResources(String userid);

	/**
	 * 通过拼音或者简拼搜索用户(公告通知中关联对象使用)
	 * 
	 * @param keys
	 *            拼音或者简拼关键字 simpleFlag 是否是简拼
	 * @return id,中文名称,部门名称
	 */
	@SuppressWarnings("rawtypes")
	public List searchUser(List keys, boolean simpleFlag);

	/**
	 * 通过拼音或者简拼搜索用户(用户管理中查询功能使用)
	 * 
	 * @param keys
	 *            拼音或者简拼关键字
	 * @param simpleFlag
	 *            是否全部字母
	 * @param deptIds
	 *            部门id集合
	 * @return ID_,USERNAME,FULLNAME,USERLEVEL,ACCOUNT_ENABLED,CREATOR_,
	 *         ACCOUNT_CREATE_TIME
	 *         ,ACCOUNT_EXPIRED_STARTTIME,ACCOUNT_EXPIRED_ENDTIME
	 */
	@SuppressWarnings("rawtypes")
	public Page searchUserPaged(int start, int limit, List keys, List deptIds, boolean simpleFlag, String startTime,
			String endTime, String havePermission);

	/**
	 * 通过拼音或者简拼搜索用户(用户管理中查询功能使用)
	 * 
	 * @param keys
	 *            拼音或者简拼关键字
	 * @param simpleFlag
	 *            是否全部字母
	 * @param deptIds
	 *            部门id集合
	 * @return ID_,USERNAME,FULLNAME,USERLEVEL,ACCOUNT_ENABLED,CREATOR_,
	 *         ACCOUNT_CREATE_TIME
	 *         ,ACCOUNT_EXPIRED_STARTTIME,ACCOUNT_EXPIRED_ENDTIME
	 */
	@SuppressWarnings("rawtypes")
	public Page searchUserPaged(int start, int limit, List keys, List deptIds, boolean simpleFlag, String startTime,
			String endTime, String havePermission, Map<String, String> paramMap);

	/**
	 * 返回user简单信息
	 * 
	 * @return 用户id 用户中文名称
	 */
	@SuppressWarnings("rawtypes")
	public List queryUserInfo(List ids);

	/**
	 * 根据用户名查找用户数量，暂用于判断用户名是否已存在
	 */
	public long queryUserByUsername(String username);

	/**
	 * 获取所有账户信息
	 */
	public Page queryAllUser(int start, int limit);

	/**
	 * 获取单个账户详细信息
	 */
	@SuppressWarnings("rawtypes")
	public List querySingleUserInfo(String id);

	/**
	 * 获取用户部门信息
	 * 
	 * @return 部门id+部门中文名称
	 */
	@SuppressWarnings("rawtypes")
	public List queryUserDeptInfo(String uid);

	/**
	 * 获取用户角色中文名称信息
	 * 
	 * @return 角色id+角色中文名称
	 */
	@SuppressWarnings("rawtypes")
	public List queryUserRoleInfo(String uid);

	/**
	 * 获取用户角色名称信息
	 * 
	 * @param uid
	 * @return 角色id+角色名称
	 */
	@SuppressWarnings("rawtypes")
	public List queryUserRoleEnInfo(String uid);

	/**
	 * 获取所有用户角色的关系
	 * 
	 * @return 用户ID + 角色名称
	 */
	@SuppressWarnings("rawtypes")
	public List queryAllUserRoleEnInfo();

	/**
	 * 修改用户部门权限信息
	 * 
	 * @param userId
	 *            :当前所要编辑的用户的id
	 * @param addIds
	 *            :用户新增权限部门
	 * @param delIds
	 *            :用户丧失权限的部门
	 */
	public void changeUserDeptRange(String userId, String[] addIds, String[] delIds);

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
	public void changeUserRoleRange(String[] userIds, String[] addIds, String[] delIds);

	/**
	 * 批量删除用户
	 * 
	 * @param userIds
	 *            用户id集合
	 */
	public void deleteUser(List<String> userIds);

	/**
	 * 批量删除用户
	 * 
	 * @param userIds
	 *            用户id集合
	 */
	public void deleteUser(List<String> userIds, String extTableName);

	/**
	 * 设置帐号可用或者不可用
	 * 
	 * @param ids
	 *            被设置帐号集合
	 * @param flag
	 *            true 可用 false 不可用
	 */
	public void setAccountEnabled(String[] ids, boolean flag);

	/**
	 * 更新用户基本信息
	 * 
	 * @param email
	 * @param mobile
	 * @param tel
	 */
	public void updateBasicInfo(String email, String mobile, String tel, String id);

	/**
	 * 查询用户扩展字段，表明以及字段名从外部传入
	 * 
	 * @param id
	 * @param tableName
	 * @param fields
	 * @return
	 */
	public Object queryUserExtension(String id, String tableName, String[] fields);

	/**
	 * 更新用户扩展字段
	 * 
	 * @param id
	 * @param tableName
	 * @param fields
	 */
	public void updateUserExtension(String id, String tableName, NameValuePair[] fields);

	/**
	 * 
	 * 方法说明：根据用户名和手机号码查询用户
	 * 
	 * @Title: queryUserByUsername
	 * @author: fangqian
	 * @param userName
	 * @param phoneNumber
	 * @return
	 * @return long
	 * @date: 2015年8月18日 下午4:44:19
	 */
	public long queryUserByUsername(String userName, String phoneNumber);

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
	 * @date: 2015年8月20日 下午3:56:27
	 */
	public User getUserInfo(String userName, String phoneNumber);

	/**
	 * 
	 * 方法说明：设置新密码
	 * 
	 * @Title: updateUserPassword
	 * @author: fangqian
	 * @param password
	 * @param oldPassword
	 * @param userId
	 * @param currentDatetime
	 * @return void
	 * @date: 2015年8月20日 下午4:12:50
	 */
	public void updateUserPassword(String password, String oldPassword, String userId, String currentDatetime);

	public void updateExtInfo(String userId, String userExtTable, String extVal);

	/**
	 * 动态查询用户信息
	 * 
	 * @param queryColumns
	 * @param queryCondition
	 * @return
	 */
	public JSONObject queryUserInfo(String queryColumns, String queryCondition);

	@SuppressWarnings("rawtypes")
	public List getUserDepartmentRange(String uid);

	@SuppressWarnings("rawtypes")
	public List queryUserInfo(String input, List deptIds);

	@SuppressWarnings("rawtypes")
	public List getMoreUserInfo(List userList);

	@SuppressWarnings("rawtypes")
	public List queryUserInfoByRoleIds(String input, List roleIds);

	@SuppressWarnings("rawtypes")
	public List getUserAllRole(String uid);

	@SuppressWarnings("rawtypes")
	public List queryRoleInfoByUnames(List userNames);

	@SuppressWarnings("rawtypes")
	public List queryRoleInfoByRnames(String userName);

	public JSONObject getUserByRolename(String roleName);

	public boolean getUsernameStatus(String userIP, String ipLimitTimes, String date);

	public void deleteLockedHost(String userIP);

	@SuppressWarnings("rawtypes")
	public List getLockedUserIPInfo(String userIP, String ipLimitTimes, String ipLimitTime);

	public void saveUserIpStatus(String userIP, String str);

	public void updateLockedUserIPInfo(String userIP, String wrongTimes, String lockedTime);

	public String getUserDeptId(String userId);

	public String findAllUserByDeptIds(String deptids);

	public User getUserInfo(String username);

	/**
	 * 
	 * <p>
	 * Description:获取系统用户列表
	 * </p>
	 * <p>
	 * Title: getAllSysUser
	 * </p>
	 * 
	 * @return
	 */
	public Page getAllSysUser(String deptName, String userName, String seledUser, boolean isNotin, int start, int limit);

	public List<JSONObject> getShortCutResources(String userId);

}