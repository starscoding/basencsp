package com.eastcom_sw.core.dao.security;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.support.collections.DefaultRedisMap;
import org.springframework.data.redis.support.collections.RedisMap;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.eastcom_sw.common.dao.jpa.DaoSupport;
import com.eastcom_sw.common.dao.jpa.MyBatchPreparedStatementSetter;
import com.eastcom_sw.common.dao.redis.RedisTemplateUtil;
import com.eastcom_sw.common.entity.NameValuePair;
import com.eastcom_sw.common.entity.Page;
import com.eastcom_sw.common.entity.PageObject;
import com.eastcom_sw.common.utils.ParseJSONObject;
import com.eastcom_sw.common.utils.RandomGUID;
import com.eastcom_sw.common.utils.RedisKeyConstants;
import com.eastcom_sw.core.entity.security.User;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 用户管理
 * 
 * @author SCM
 * 
 */
@Component
public class UserDaoImpl extends DaoSupport<User> implements UserDao {

	@PersistenceContext(unitName = "defaultPU")
	public EntityManager em;

	private String[] category = {};
	private String selectSql1 = " AND U.CATEGORY_ IN ";
	private String selectSql2 = " AND CATEGORY_ IN ";
	private String selectHql = " and u.category in ";

	// private String selectSql3 = "";

	private JdbcTemplate jdbcTemplate;

	@Resource(name = "dataSource")
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Autowired
	private RedisTemplateUtil redisTemplateUtil;

	@PostConstruct
	public void init() {
		String idName = String.format(RedisKeyConstants.COMMONDATA_NAME_ID, "sys_user_category");
		StringRedisTemplate redisTemplate = redisTemplateUtil.getRedisTemplate();
		boolean hasKey = redisTemplate.hasKey(idName);
		if (hasKey) {
			String key = redisTemplate.opsForValue().get(idName);
			Set<Entry<String, String>> entitys = new DefaultRedisMap<String, String>(
					String.format(RedisKeyConstants.COMMONDATA_BASE_INFO, key), redisTemplate).entrySet();
			for (Entry<String, String> entity : entitys) {
				if (entity.getKey().equals("value")) {
					String cate = entity.getValue();
					category = cate.split(",");
				}
			}
		} else {
			category = new String[1];
			category[0] = "0";
		}
		String categoryCondition = initCategory();
		selectSql1 += categoryCondition;
		selectSql2 += categoryCondition;
		selectHql += categoryCondition;
	}

	/**
	 * 获取用户管理的用户等级sql
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private String getUserLevelSql() {
		String sql = "";
		String key = String.format(RedisKeyConstants.SYSTEM_ARGUMENTS, "security");
		StringRedisTemplate redisTemplate = redisTemplateUtil.getRedisTemplate();
		if (redisTemplate.hasKey(key)) {
			RedisMap map = new DefaultRedisMap<String, String>(key, redisTemplate);
			if (map.containsKey("userArgs")) {
				JSONObject userArgs = JSONObject.fromObject(map.get("userArgs"));
				String userLevels = userArgs.containsKey("userLevels") ? userArgs.getString("userLevels") : "";
				if (StringUtils.isNotBlank(userLevels) && !"null".equals(userLevels)) {
					String[] ls = userArgs.getString("userLevels").split(",");
					StringBuffer condition = new StringBuffer();
					condition.append("(");
					for (String l : ls) {
						condition.append("'");
						condition.append(l);
						condition.append("',");
					}
					if (condition.length() > 1) {
						condition.deleteCharAt(condition.length() - 1);
					}
					condition.append(")");
					sql = " AND U.USERLEVEL IN " + condition.toString();
				}
			}
		}
		return sql;
	}

	/**
	 * 获取用户类型
	 * 
	 * @return
	 */
	protected String initCategory() {
		StringBuffer condition = new StringBuffer();
		condition.append("(");
		if (category != null && category.length > 0) {
			for (String c : category) {
				condition.append("'");
				condition.append(c);
				condition.append("',");
			}
			condition.deleteCharAt(condition.length() - 1);
		}
		condition.append(")");
		return condition.toString();
	}

	/**
	 * 通过用户名获取用户信息
	 * 
	 * @param username
	 * @return
	 */
	public User loadUserByUsername(String username) {
		String hql = "from User u where u.userName = ?";
		hql += selectHql;
		List<User> users = null;
		try {
			users = getEm().createQuery(hql, User.class).setParameter(1, username).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (users != null && users.size() > 0) {
			return users.get(0);
		}
		return null;
	}

	/**
	 * 根据id集合获取用户集合
	 * 
	 * @param ids
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<User> findByIds(String[] ids) {
		StringBuffer idsStr = new StringBuffer();
		idsStr.append('?').append(0);
		for (int i = 1; i < ids.length; i++) {
			idsStr.append(',').append('?').append(i);
		}
		String hql = "from User where id in (" + idsStr + ")";
		Query query = getEm().createQuery(hql);
		for (int i = 0; i < ids.length; i++) {
			query.setParameter(i, ids[i]);
		}
		return query.getResultList();
	}

	/**
	 * 通过手机号码获取用户
	 * 
	 * @param phone
	 * @return
	 */
	public List<User> loadUserByPhoneForLogin(String phone) {
		String hql = "from User u where u.mobileNo = ?";
		hql += selectHql;
		hql += " order by u.accoutExpiredStarttime";
		List<User> users = getEm().createQuery(hql, User.class).setParameter(1, phone).getResultList();
		return users;
	}

	/**
	 * 获取当前用户的所有资源
	 * 
	 * @param userid
	 *            用户ID
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List getCurrentUserResources(String userid) {
		String sql = "SELECT R.ID_,R.NAME_,R.NAME_CN,R.IMAGE_,R.IS_SHOWDESKTOP,";
		sql += " R.IS_WEBPAGE,R.ORDER_,R.LOCATION_,R.KIND_ ,R.PARENT_ID FROM SYS_USERROLE UR";
		sql += " LEFT JOIN SYS_ROLERESOURCE RS ON UR.ROLE_ID = RS.ROLE_ID";
		sql += " LEFT JOIN SYS_RESCOURCE R ON RS.RESOURCE_ID = R.ID_";
		sql += " WHERE UR.USER_ID = ? AND R.STATUS_ = '1' AND R.KIND_ = '0' ORDER BY ORDER_ ASC";
		return getEm().createNativeQuery(sql).setParameter(1, userid).getResultList();
	}

	/**
	 * 通过拼音或者简拼搜索用户(公告通知中关联对象使用)
	 * 
	 * @param keys
	 *            拼音或者简拼关键字 simpleFlag 是否是简拼
	 * @return id,中文名称,部门名称
	 */
	@SuppressWarnings("rawtypes")
	public List searchUser(List keys, boolean simpleFlag) {
		String sql = "";
		if (simpleFlag) {
			sql = "select u.id_,u.fullname,d.name_cn from sys_user u,sys_userdepartment ud,sys_department d where substr(u.name_pinyin,(SELECT INSTR(u.name_pinyin, '|', 1, 1)  FROM DUAL)) like '%";
			for (int i = 0; i < keys.size(); i++) {
				if (i == 0) {
					sql += keys.get(i);
				} else {
					sql = sql + "%" + keys.get(i);
				}
			}
			sql += "%' and u.id_=ud.user_id and ud.dept_id=d.id_";
		}

		else {
			sql = "select u.id_,u.fullname,d.name_cn from sys_user u,sys_userdepartment ud,sys_department d where substr(u.name_pinyin,1,(SELECT INSTR(u.name_pinyin, '|', 1, 1)  FROM DUAL)) like '%";
			for (int i = 0; i < keys.size(); i++) {
				if (i == 0) {
					sql += keys.get(i);
				} else {
					sql = sql + "%" + keys.get(i);
				}
			}
			sql += "%' and u.id_=ud.user_id and ud.dept_id=d.id_";
		}
		sql += selectSql1;
		return getEm().createNativeQuery(sql).getResultList();
	}

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
	public Page searchUserPaged(int start, int limit, List keys, List deptIds, boolean simpleFlag, String startTime, String endTime, String havePermission) {
		return searchUserPaged(start, limit, keys, deptIds, simpleFlag, startTime, endTime, havePermission, null);
	}
	
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
	public Page searchUserPaged(int start, int limit, List keys, List deptIds, boolean simpleFlag, String startTime, String endTime, String havePermission, Map<String,String> paramMap) {
		String sql = "SELECT U.ID_ ,U.USERNAME,U.FULLNAME,U.USERLEVEL,U.ACCOUNT_ENABLED,U.CREATOR_,U.ACCOUNT_CREATE_TIME,U.ACCOUNT_EXPIRED_STARTTIME,U.ACCOUNT_EXPIRED_ENDTIME,D.ID_ AS DEPTID,D.NAME_CN AS DEPTNAME FROM SYS_USER U";
		sql += " LEFT JOIN SYS_USERDEPARTMENT UD ON U.ID_ = UD.USER_ID ";
		sql += " LEFT JOIN SYS_DEPARTMENT D ON UD.DEPT_ID = D.ID_ ";
		sql += " WHERE 1=1 ";

		if(paramMap != null && paramMap.size() >0){
			if ("true".equals(paramMap.get("excludeSelf")) 
				&& StringUtils.isNotBlank(paramMap.get("selUserName"))) {
				sql += " AND Instr('," + paramMap.get("selUserName") + ",', ',' || U.USERNAME || ',') = 0";
			}
		}		
		
		if (keys.size() > 0) {
			if (simpleFlag) {// 不带中文则不将keys拆分查询 NAMEPINYIN或者USERNAME的LIKE
								// '%key%'查询
				String queryString = "";
				for (Object key : keys) {
					queryString += ((String) key).toLowerCase();
				}
				sql += " AND (U.NAME_PINYIN LIKE '%";
				sql += queryString;
				sql += "%' OR lower(U.USERNAME) LIKE '%";
				sql += queryString;
				sql += "%')";
			} else {// 带中文将keys拆分查询 NAMEPINYIN LIKE '%key%key%key%...' 查询
				sql += " and substr(u.name_pinyin,1,(SELECT INSTR(u.name_pinyin, '|', 1, 1)  FROM DUAL)) like '%";
				String firstKey = "";
				for (int i = 0; i < keys.size(); i++) {
					if (i == 0) {
						sql += keys.get(i);
						firstKey = ((String) keys.get(i)).substring(0, 1).toLowerCase();
					} else {
						sql = sql + "%" + keys.get(i);
					}
				}
				sql += "%'";
				sql += " AND u.name_pinyin like '" + firstKey + "%'";
			}
		}
		if (deptIds.size() > 0) {
			StringBuffer deptIdCondition = new StringBuffer();
			for (int i = 0; i < deptIds.size(); i++) {
				deptIdCondition.append("'" + deptIds.get(i) + "',");
			}
			deptIdCondition.deleteCharAt(deptIdCondition.length() - 1);
			sql += " and u.id_ in (select ud.user_id from sys_userdepartment ud where ud.dept_id in(" + deptIdCondition
					+ "))";
		}
		if (StringUtils.isNotBlank(startTime)) {
			sql += " and u.ACCOUNT_CREATE_TIME >= '" + startTime + "'";
		}
		if (StringUtils.isNotBlank(endTime)) {
			sql += " and u.ACCOUNT_CREATE_TIME <= '" + endTime + "'";
		}
		if ("1".equals(havePermission)) {
			sql += " and u.id_ in ( select distinct(user_id) from sys_userrole where role_id in (select distinct(role_id) from sys_roleresource )) ";
		} else if ("0".equals(havePermission)) {
			sql += " and u.id_ not in ( select distinct(user_id) from sys_userrole where role_id in (select distinct(role_id) from sys_roleresource )) ";
		}
		// if(!CommonUtil.getUser().getUserLevel().equals("1")){
		// sql += " and u.userlevel = '3'";
		// }

		sql += selectSql1;
		sql += getUserLevelSql();
		log.info(sql);
		return pagedSQLQuery(sql, start, limit);
	}

	/**
	 * 返回user简单信息
	 * 
	 * @return 用户id 用户中文名称
	 */
	@SuppressWarnings("rawtypes")
	public List queryUserInfo(List ids) {
		StringBuffer idsStr = new StringBuffer();
		idsStr.append('?').append(0);
		for (int i = 1; i < ids.size(); i++) {
			idsStr.append(',').append('?').append(i);
		}
		String sql = "select u.id_,u.fullname from sys_user u where u.id_ in (" + idsStr + ")";
		Query query = getEm().createNativeQuery(sql);
		for (int i = 0; i < ids.size(); i++) {
			query.setParameter(i, ids.get(i));
		}
		return query.getResultList();
	}

	/**
	 * 根据用户名查找用户数量，暂用于判断用户名是否已存在
	 */
	public long queryUserByUsername(String username) {
		String sql = "SELECT COUNT(ID_) FROM SYS_USER WHERE USERNAME=?0";
		Query query = getEm().createNativeQuery(sql);
		query.setParameter(0, username);
		return Long.parseLong(query.getSingleResult().toString());
	}

	/**
	 * 根据用户名和手机号码查找用户是否存在
	 */
	public long queryUserByUsername(String userName, String phoneNumber) {
		String sql = "SELECT COUNT(ID_) FROM SYS_USER WHERE USERNAME=? AND MOBILE_NO=?";
		Query query = getEm().createNativeQuery(sql);
		query.setParameter(1, userName);
		query.setParameter(2, phoneNumber);
		return Long.parseLong(query.getSingleResult().toString());
	}

	/**
	 * 获取所有账户信息
	 */
	public Page queryAllUser(int start, int limit) {
		String sql = "SELECT ID_,USERNAME,FULLNAME,USERLEVEL,ACCOUNT_ENABLED,CREATOR_,ACCOUNT_CREATE_TIME,ACCOUNT_EXPIRED_STARTTIME,ACCOUNT_EXPIRED_ENDTIME FROM SYS_USER";
		sql += selectSql2;
		return pagedSQLQuery(sql, start, limit);
	}

	/**
	 * 获取单个账户详细信息
	 */
	@SuppressWarnings("rawtypes")
	public List querySingleUserInfo(String id) {
		String sql = "SELECT ID_,USERNAME,VERSION_,SEX_,PASSWORD_,FULLNAME,EMAIL_,FIXED_NO,MOBILE_NO,ACCOUNT_ENABLED,TIMES_,CREATOR_,OWNER_,USERLEVEL,PWD_EXPIRED_DAYS,PWD_MODIFY_TIME,ACCOUNT_EXPIRED_STARTTIME,ACCOUNT_EXPIRED_ENDTIME,ACCOUNT_CREATE_TIME,LAST_LOGIN_TIME,OLD_PASSWORD,CITY_ FROM SYS_USER where id_=?0";
		Query query = getEm().createNativeQuery(sql);
		query.setParameter(0, id);
		return query.getResultList();
	}

	/**
	 * 查询用户扩展字段，表明以及字段名从外部传入
	 * 
	 * @param id
	 * @param tableName
	 * @param fields
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Object queryUserExtension(String id, String tableName, String[] fields) {
		String fs = StringUtils.join(fields, ",");
		String sql = "SELECT " + fs + " FROM " + tableName + " where USER_ID = ?";
		Query query = getEm().createNativeQuery(sql).setParameter(1, id);
		List<Object> l = query.getResultList();
		return (l != null && l.size() > 0) ? l.get(0) : null;
	}

	/**
	 * 更新用户扩展字段
	 * 
	 * @param id
	 * @param tableName
	 * @param fields
	 */
	public void updateUserExtension(String id, String tableName, NameValuePair[] fields) {
		if (fields != null && fields.length > 0) {
			Object[] vals = new String[fields.length + 1];
			String[] insert_condition = new String[fields.length + 2];
			StringBuffer update_sql = new StringBuffer("UPDATE " + tableName + " SET ");
			StringBuffer insert_sql = new StringBuffer("INSERT INTO " + tableName + " (");
			for (int i = 0; i < fields.length; i++) {
				NameValuePair nv = fields[i];
				if (i != 0) {
					update_sql.append(",");
				}
				update_sql.append(nv.getName() + " = ? ");
				insert_sql.append(nv.getName() + ",");
				vals[i] = nv.getValue();
				insert_condition[i] = "?";
			}
			update_sql.append(" WHERE USER_ID=?");
			vals[fields.length] = id;

			insert_condition[fields.length] = "?";
			insert_condition[fields.length + 1] = "?";
			insert_sql.append("USER_ID,ID_) VALUES(");
			insert_sql.append(StringUtils.join(insert_condition, ","));
			insert_sql.append(")");
			int rows = getQuery(update_sql.toString(), true, vals).executeUpdate();

			if (rows < 1) {// 没有纪录进行insert
				String[] uuidCdt = { new RandomGUID().getUUID32() };
				getQuery(insert_sql.toString(), true, ArrayUtils.addAll(vals, uuidCdt)).executeUpdate();
			}
		}
	}

	/**
	 * 获取用户部门信息
	 * 
	 * @return 部门id+部门中文名称
	 */
	@SuppressWarnings("rawtypes")
	public List queryUserDeptInfo(String uid) {
		String sql = "select d.id_,d.name_cn from sys_userdepartment ud , sys_department d where ud.user_id = ?0 and d.id_ = ud.dept_id";
		Query query = getEm().createNativeQuery(sql);
		query.setParameter(0, uid);
		return query.getResultList();
	}

	/**
	 * 获取用户角色中文名称信息
	 * 
	 * @return 角色id+角色中文名称
	 */
	@SuppressWarnings("rawtypes")
	public List queryUserRoleInfo(String uid) {
		String sql = "select r.id_,r.name_cn from sys_userrole ur , sys_role r where ur.user_id = ?0 and r.id_ = ur.role_id";
		Query query = getEm().createNativeQuery(sql);
		query.setParameter(0, uid);
		return query.getResultList();
	}

	/**
	 * 获取用户角色名称信息
	 * 
	 * @param uid
	 * @return 角色id+角色名称
	 */
	@SuppressWarnings("rawtypes")
	public List queryUserRoleEnInfo(String uid) {
		String sql = "SELECT R.ID_,R.NAME_ FROM SYS_USERROLE UR INNER JOIN SYS_ROLE R ON (UR.ROLE_ID = R.ID_) WHERE UR.USER_ID = ?0";
		return getEm().createNativeQuery(sql).setParameter(0, uid).getResultList();
	}

	/**
	 * 获取所有用户角色的关系
	 */
	@SuppressWarnings("rawtypes")
	public List queryAllUserRoleEnInfo() {
		String sql = "SELECT UR.USER_ID,R.NAME_ FROM SYS_USERROLE UR INNER JOIN SYS_ROLE R ON UR.ROLE_ID = R.ID_";
		return getEm().createNativeQuery(sql).getResultList();
	}

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
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void changeUserDeptRange(String userId, String[] addIds, String[] delIds) {
		if (addIds != null && addIds.length > 0) {
			String sql = "insert into sys_user_depart_range (user_id,dept_id) values (?,?)";
			List params = new ArrayList();
			for (String id : addIds) {
				params.add(new String[][] { { "string", userId }, { "string", id } });
			}
			MyBatchPreparedStatementSetter setter = new MyBatchPreparedStatementSetter(params);
			jdbcTemplate.batchUpdate(sql, setter);
		}

		if (delIds != null && delIds.length > 0) {
			StringBuffer delIdStr = new StringBuffer();
			for (String id : delIds) {
				delIdStr.append("'" + id + "',");
			}
			delIdStr.deleteCharAt(delIdStr.length() - 1);
			String sql = "delete from sys_user_depart_range where user_id = '" + userId + "' and dept_id in ("
					+ delIdStr + ")";
			getEm().createNativeQuery(sql).executeUpdate();
		}
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
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void changeUserRoleRange(String[] userIds, String[] addIds, String[] delIds) {
		/**
		 * Edit by JJF JUN 05 2014 修改为可同时更新多个具有相同角色的账户
		 */
		if (userIds != null && userIds.length > 0) {
			if (addIds != null && addIds.length > 0) {
				String sql = "insert into sys_userrole (user_id,role_id) values (?,?)";
				List params = new ArrayList();
				for (String userId : userIds) {
					for (String id : addIds) {
						params.add(new String[][] { { "string", userId }, { "string", id } });
					}
				}
				MyBatchPreparedStatementSetter setter = new MyBatchPreparedStatementSetter(params);
				jdbcTemplate.batchUpdate(sql, setter);
			}
			if (delIds != null && delIds.length > 0) {
				StringBuffer delIdStr = new StringBuffer();
				StringBuffer userIdStr = new StringBuffer();
				for (String id : userIds) {
					userIdStr.append("'" + id + "',");
				}
				for (String id : delIds) {
					delIdStr.append("'" + id + "',");
				}
				userIdStr.deleteCharAt(userIdStr.length() - 1);
				delIdStr.deleteCharAt(delIdStr.length() - 1);
				String sql = "delete from sys_userrole where user_id in(" + userIdStr + ") and role_id in (" + delIdStr + ")";
				getEm().createNativeQuery(sql).executeUpdate();
			}
		}
	}

	/**
	 * 批量删除用户
	 * 
	 * @param userIds
	 *            用户id集合
	 */
	public void deleteUser(List<String> userIds) {
		String idStr = "'";
		for (String id : userIds) {
			idStr += (id + "','");
		}
		idStr = idStr.substring(0, idStr.length() - 2);
		String sql1 = "DELETE FROM SYS_USER_DEPART_RANGE WHERE USER_ID IN(" + idStr + ")";
		String sql2 = "DELETE FROM SYS_USERDEPARTMENT WHERE USER_ID IN(" + idStr + ")";
		String sql3 = "DELETE FROM SYS_DESKTOP_RESOURCE WHERE DESKTOP_ID IN (SELECT ID_ FROM SYS_USERDESKTOP WHERE USER_ID IN ("
				+ idStr + "))";
		String sql4 = "DELETE FROM SYS_USERDESKTOP WHERE USER_ID IN(" + idStr + ")";
		String sql5 = "DELETE FROM SYS_USERNOTIFICATION_DELETED WHERE USER_ID IN(" + idStr + ")";
		String sql6 = "DELETE FROM SYS_USERROLE WHERE USER_ID IN(" + idStr + ")";
		String sql7 = "DELETE FROM SYS_USER WHERE ID_ IN(" + idStr + ")";
		getEm().createNativeQuery(sql1).executeUpdate();
		getEm().createNativeQuery(sql2).executeUpdate();
		getEm().createNativeQuery(sql3).executeUpdate();
		getEm().createNativeQuery(sql4).executeUpdate();
		getEm().createNativeQuery(sql5).executeUpdate();
		getEm().createNativeQuery(sql6).executeUpdate();
		getEm().createNativeQuery(sql7).executeUpdate();
	}

	/**
	 * 批量删除用户(包括扩展字段)
	 * 
	 * @param userIds
	 *            用户id集合
	 */
	public void deleteUser(List<String> userIds, String extTableName) {
		String idStr = "'";
		for (String id : userIds) {
			idStr += (id + "','");
		}
		idStr = idStr.substring(0, idStr.length() - 2);
		String sql = "DELETE FROM " + extTableName + " WHERE USER_ID IN(" + idStr + ")";
		getEm().createNativeQuery(sql).executeUpdate();
		deleteUser(userIds);
	}

	/**
	 * 设置帐号可用或者不可用
	 * 
	 * @param ids
	 *            被设置帐号集合
	 * @param flag
	 *            true 可用 false 不可用
	 */
	public void setAccountEnabled(String[] ids, boolean flag) {
		String idStr = "'";
		for (String id : ids) {
			idStr += (id + "','");
		}
		idStr = idStr.substring(0, idStr.length() - 2);
		String sql = "update sys_user set ACCOUNT_ENABLED = '" + (flag ? "1" : "0") + "' where id_ in (" + idStr + ")";
		getEm().createNativeQuery(sql).executeUpdate();
	}

	/**
	 * 更新用户基本信息
	 * 
	 * @param email
	 * @param mobile
	 * @param tel
	 */
	public void updateBasicInfo(String email, String mobile, String tel, String id) {
		String sql = "UPDATE SYS_USER SET EMAIL_ = ?,FIXED_NO = ?,MOBILE_NO = ? WHERE ID_ = ?";
		Query query = getEm().createNativeQuery(sql).setParameter(1, email).setParameter(2, tel).setParameter(3, mobile).setParameter(4, id);
		query.executeUpdate();
	}

	/**
	 * 根据用户名和手机号确定用户基本信息
	 */
	public User getUserInfo(String userName, String phoneNumber) {
		String hql = "select u from User u where u.userName = ? and u.mobileNo = ?";
		List<User> users = null;
		try {
			users = getEm().createQuery(hql, User.class).setParameter(1, userName).setParameter(2, phoneNumber).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (users != null && users.size() > 0) {
			return users.get(0);
		}
		return null;
	}

	public User getUserInfo(String username) {
		String hql = "select u from User u where u.userName = ? ";
		List<User> users = null;
		try {
			users = getEm().createQuery(hql, User.class).setParameter(1, username).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (users != null && users.size() > 0) {
			return users.get(0);
		}
		return null;
	}

	/**
	 * 设置新密码
	 */
	public void updateUserPassword(String password, String oldPassword, String userId, String currentDatetime) {
		String sql = "UPDATE SYS_USER SET PASSWORD_ = ?,OLD_PASSWORD = ?,PWD_MODIFY_TIME = ? WHERE ID_ = ?";
		Object[] args = { password, oldPassword, currentDatetime, userId };
		jdbcTemplate.update(sql, args);
	}

	public void updateExtInfo(String userId, String userExtTable, String extVal) {
		String[] valMap = extVal.split(";");
		StringBuffer sb = new StringBuffer();
		sb.append("UPDATE ").append(userExtTable).append(" SET ");
		int i = 0;
		for (String s : valMap) {
			i++;
			String tabName = s.split(":").length > 0 ? s.split(":")[0] : "";
			String tabVal = s.split(":").length > 0 ? s.split(":")[1] : "";
			sb.append(tabName).append(" = ").append("'" + tabVal + "'");
			if (valMap.length > 1 && i != valMap.length) {
				sb.append(" , ");
			}
		}
		sb.append(" WHERE USER_ID = ").append("'" + userId + "'");
		jdbcTemplate.update(sb.toString());
	}

	/**
	 * 动态查询用户信息（接口调用）
	 */
	@SuppressWarnings("rawtypes")
	public JSONObject queryUserInfo(String queryColumns, String queryCondition) {
		JSONObject rst = new JSONObject();
		boolean success = true;
		String message = "";
		StringBuffer sb = new StringBuffer();
		if (!"".equals(queryColumns)) {
			if (queryColumns.endsWith(",")) {
				queryColumns = queryColumns.substring(0, queryColumns.length() - 1);
			}
			sb.append("select ").append(queryColumns).append(" from sys_user where 1=1 ");

			if (!"".equals(queryCondition)) {
				JSONObject jo = JSONObject.fromObject(queryCondition);
				Iterator it = jo.keys();
				while (it.hasNext()) {
					String columns = it.next().toString();
					String val = jo.getString(columns);
					sb.append(" and ").append(columns).append(" = ").append("'" + val + "'");
				}
			}

		}
		Query query = getEm().createNativeQuery(sb.toString());
		List list = query.getResultList();
		JSONArray ja = new JSONArray();
		if (!"".equals(queryColumns)) {
			String[] col = queryColumns.split(",");
			for (int i = 0; i < list.size(); i++) {
				JSONObject js = new JSONObject();
				Object[] o = (Object[]) list.get(i);
				for (int j = 0; j < col.length; j++) {
					js.put(col[j], o[j]);
				}
				ja.add(js);
			}
		}
		rst.put("success", success);
		rst.put("message", message);
		rst.put("data", ja.toString());
		return rst;

	}

	@SuppressWarnings("rawtypes")
	public List getUserDepartmentRange(String userId) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select d.id_ as deptId,d.name_,d.name_cn,d.parent_id,d.order_,d.desc_,d.login_mode ");
		sb.append(" from sys_department d ");
		// 目前因为功能问题只返回全部数据 后续进行修改
		// sb.append(" from sys_user_depart_range udr left join sys_department d
		// on udr.dept_id = d.id_ ");
		// sb.append(" where 1=1 and udr.user_id = ?0 ");
		Query query = getEm().createNativeQuery(sb.toString());
		// query.setParameter(0, userId);
		List list = query.getResultList();
		return list;
	}

	@SuppressWarnings("rawtypes")
	public List getUserAllRole(String uid) {
		// 目前也是返回全部角色 后续功能在开发
		StringBuffer sb = new StringBuffer();
		sb.append(" select r.id_,r.name_,r.name_cn,r.description_ from sys_role r ");
		Query query = getEm().createNativeQuery(sb.toString());
		List list = query.getResultList();
		return list;
	}

	@SuppressWarnings("rawtypes")
	public List queryUserInfo(String input, List deptIds) {
		String sql = "SELECT U.ID_ ,U.USERNAME,U.FULLNAME,U.EMAIL_,U.FIXED_NO,U.MOBILE_NO,D.ID_ AS DEPTID,D.NAME_CN AS DEPTNAME FROM SYS_USER U  ";
		sql += " LEFT JOIN SYS_USERDEPARTMENT UD ON U.ID_ = UD.USER_ID ";
		sql += " LEFT JOIN SYS_DEPARTMENT D ON UD.DEPT_ID = D.ID_ ";
		sql += " WHERE 1=1 ";
		if (input != null && !"".equals(input)) {
			sql += " and (u.name_pinyin like '%" + input + "%'  OR U.USERNAME LIKE '%" + input
					+ "%' or u.fullname like '%" + input + "%')";
		}

		// if (keys.size() > 0) {
		// if (simpleFlag) {// 不带中文则不将keys拆分查询 NAMEPINYIN或者USERNAME的LIKE
		// // '%key%'查询
		// String queryString = "";
		// for (Object key : keys) {
		// queryString += ((String) key).toLowerCase();
		// }
		// sql += " AND (U.NAME_PINYIN LIKE '%";
		// sql += queryString;
		// sql += "%' OR lower(U.USERNAME) LIKE '%";
		// sql += queryString;
		// sql += "%')";
		// } else {// 带中文将keys拆分查询 NAMEPINYIN LIKE '%key%key%key%...' 查询
		// sql += " and substr(u.name_pinyin,1,(SELECT INSTR(u.name_pinyin, '|',
		// 1, 1) FROM DUAL)) like '%";
		// String firstKey = "";
		// for (int i = 0; i < keys.size(); i++) {
		// if (i == 0) {
		// sql += keys.get(i);
		// firstKey = ((String) keys.get(i)).substring(0, 1)
		// .toLowerCase();
		// } else {
		// sql = sql + "%" + keys.get(i);
		// }
		// }
		// sql += "%'";
		// sql += " AND u.name_pinyin like '" + firstKey + "%'";
		// }
		// }
		if (deptIds != null && deptIds.size() > 0) {
			StringBuffer deptIdCondition = new StringBuffer();
			for (int i = 0; i < deptIds.size(); i++) {
				deptIdCondition.append("'" + deptIds.get(i) + "',");
			}
			deptIdCondition.deleteCharAt(deptIdCondition.length() - 1);
			sql += " and u.id_ in (select ud.user_id from sys_userdepartment ud where ud.dept_id in(" + deptIdCondition
					+ "))";
			sql += " and d.id_ in(" + deptIdCondition + ")";
		}
		log.info(sql);
		Query query = getEm().createNativeQuery(sql);
		List list = query.getResultList();
		return list;
	}

	@SuppressWarnings("rawtypes")
	public List getMoreUserInfo(List user) {
		StringBuffer sb = new StringBuffer();
		StringBuffer userList = new StringBuffer();
		for (int i = 0; i < user.size(); i++) {
			userList.append("'" + user.get(i) + "',");
		}
		userList.deleteCharAt(userList.length() - 1);
		sb.append(" SELECT U.ID_,U.USERNAME,U.FULLNAME,U.EMAIL_,U.FIXED_NO,U.MOBILE_NO,SU.DEPT_ID AS DEPTID,DE.NAME_CN ");
		sb.append(" FROM SYS_USER U LEFT JOIN SYS_USERDEPARTMENT SU ON U.ID_ = SU.USER_ID ");
		sb.append(" LEFT JOIN SYS_DEPARTMENT DE ON SU.DEPT_ID=DE.ID_");
		sb.append(" WHERE U.USERNAME IN (" + userList + ")");
		Query query = getEm().createNativeQuery(sb.toString());
		return query.getResultList();

	}

	@SuppressWarnings("rawtypes")
	public List queryUserInfoByRoleIds(String input, List roleIds) {
		String sql = "SELECT DISTINCT(u.ID_),U.USERNAME,U.FULLNAME,U.EMAIL_,U.FIXED_NO,U.MOBILE_NO,SU.DEPT_ID AS DEPTID,DE.NAME_CN FROM SYS_USER U  ";
		sql += " LEFT JOIN SYS_USERROLE UR ON U.ID_ = UR.USER_ID ";
		sql += " LEFT JOIN SYS_ROLE R ON UR.ROLE_ID = R.ID_ ";
		sql += " LEFT JOIN SYS_USERDEPARTMENT SU ON U.ID_ = SU.USER_ID";
		sql += " LEFT JOIN SYS_DEPARTMENT DE ON SU.DEPT_ID=DE.ID_";
		sql += " WHERE 1=1 ";

		if (input != null || !"".equals(input)) {
			sql += " and (u.name_pinyin like '%" + input + "%'  OR U.USERNAME LIKE '%" + input
					+ "%' or u.fullname like '%" + input + "%')";
		}

		// if (keys.size() > 0) {
		// if (simpleFlag) {// 不带中文则不将keys拆分查询 NAMEPINYIN或者USERNAME的LIKE
		// // '%key%'查询
		// String queryString = "";
		// for (Object key : keys) {
		// queryString += ((String) key).toLowerCase();
		// }
		// sql += " AND (U.NAME_PINYIN LIKE '%";
		// sql += queryString;
		// sql += "%' OR lower(U.USERNAME) LIKE '%";
		// sql += queryString;
		// sql += "%')";
		// } else {// 带中文将keys拆分查询 NAMEPINYIN LIKE '%key%key%key%...' 查询
		// sql += " and substr(u.name_pinyin,1,(SELECT INSTR(u.name_pinyin, '|',
		// 1, 1) FROM DUAL)) like '%";
		// String firstKey = "";
		// for (int i = 0; i < keys.size(); i++) {
		// if (i == 0) {
		// sql += keys.get(i);
		// firstKey = ((String) keys.get(i)).substring(0, 1)
		// .toLowerCase();
		// } else {
		// sql = sql + "%" + keys.get(i);
		// }
		// }
		// sql += "%'";
		//// sql += " AND u.name_pinyin like '" + firstKey + "%'";
		// }
		// }
		if (roleIds != null && roleIds.size() > 0) {
			StringBuffer roleIdCondition = new StringBuffer();
			for (int i = 0; i < roleIds.size(); i++) {
				roleIdCondition.append("'" + roleIds.get(i) + "',");
			}
			roleIdCondition.deleteCharAt(roleIdCondition.length() - 1);
			sql += " and r.id_ in (" + roleIdCondition + ")";
		}
		log.info(sql);
		Query query = getEm().createNativeQuery(sql);
		List list = query.getResultList();
		return list;
	}

	/**
	 * 获取用户角色名称信息
	 * 
	 * @param uid
	 * @return 角色id+角色名称
	 */
	@SuppressWarnings("rawtypes")
	public List queryRoleInfoByUnames(List userNames) {
		String sql = "SELECT R.ID_,R.NAME_,R.NAME_CN,R.DESCRIPTION_ FROM SYS_USERROLE UR INNER JOIN SYS_ROLE R ON (UR.ROLE_ID = R.ID_) ";
		sql += " LEFT JOIN  SYS_USER U ON UR.USER_ID=U.ID_ WHERE 1=1";
		if (userNames != null && userNames.size() > 0) {
			StringBuffer roleIdCondition = new StringBuffer();
			for (int i = 0; i < userNames.size(); i++) {
				roleIdCondition.append("'" + userNames.get(i) + "',");
			}
			roleIdCondition.deleteCharAt(roleIdCondition.length() - 1);
			sql += " and U.USERNAME  in (" + roleIdCondition + ")";
		}
		log.info(sql);
		return getEm().createNativeQuery(sql).getResultList();
	}

	/**
	 * 获取角色信息
	 * 
	 * @param uid
	 * @return 角色id+角色名称
	 */
	@SuppressWarnings("rawtypes")
	public List queryRoleInfoByRnames(String roleName) {
		String sql = "select R.ID_,R.NAME_,R.NAME_CN,R.DESCRIPTION_ from SYS_ROLE R WHERE R.NAME_CN LIKE '%" + roleName + "%'";
		log.info(sql);
		return getEm().createNativeQuery(sql).getResultList();
	}

	@SuppressWarnings("rawtypes")
	public JSONObject getUserByRolename(String roleName) {
		JSONObject rst = new JSONObject();
		boolean success = true;
		String message = "";
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT R.NAME_CN AS ROLENAME_CN,R.NAME_ AS ROLENAME,U.USERNAME AS USERNAME,U.FULLNAME AS USERNAME_CN ");
		sb.append(" FROM SYS_ROLE R LEFT JOIN SYS_USERROLE UR ON R.ID_ = UR.ROLE_ID LEFT JOIN SYS_USER U ");
		sb.append(" ON UR.USER_ID = U.ID_ WHERE 1=1 ");
		if (roleName != null || !"".equals(roleName)) {
			sb.append(" AND (R.NAME_ LIKE '%" + roleName + "%' OR R.NAME_CN LIKE '%" + roleName + "%')");
		}
		List list = getEm().createNativeQuery(sb.toString()).getResultList();
		JSONArray ja = new JSONArray();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				JSONObject jo = new JSONObject();
				Object[] o = (Object[]) list.get(i);
				jo.put("roleNameCn", o[0]);
				jo.put("roleName", o[1]);
				jo.put("userName", o[2]);
				jo.put("userNameCn", o[3]);
				ja.add(jo);
			}
		}
		rst.put("success", success);
		rst.put("message", message);
		rst.put("data", ja.toString());
		return rst;
	}

	/**
	 * 判断用户登录IP是否锁定
	 */
	@SuppressWarnings("rawtypes")
	public boolean getUsernameStatus(String userIP, String ipLimitTimes, String date) {
		boolean locked = false;
		String sql = "select host_,limit_times,limit_time from sys_login_limit where host_ = ?0 and limit_times >= ?1 and limit_time > ?2 ";
		Query query = getEm().createNativeQuery(sql);
		query.setParameter(0, userIP);
		query.setParameter(1, ipLimitTimes);
		query.setParameter(2, date);
		List list = query.getResultList();
		if (list != null && list.size() > 0) {
			locked = true;
		}
		return locked;
	}

	/**
	 * 删除已锁定的IP
	 */
	public void deleteLockedHost(String userIP) {
		String sql = "delete from sys_login_limit where host_ = '" + userIP + "' ";
		getEm().createNativeQuery(sql).executeUpdate();
	}

	@SuppressWarnings("rawtypes")
	public List getLockedUserIPInfo(String userIP, String ipLimitTimes, String ipLimitTime) {
		String sql = "select host_,limit_times,limit_time from sys_login_limit where host_ = ?0 and limit_times < ?1 and (limit_time <= ?2 or limit_time is null)";
		Query query = getEm().createNativeQuery(sql);
		query.setParameter(0, userIP);
		query.setParameter(1, ipLimitTimes);
		query.setParameter(2, ipLimitTime);
		List list = query.getResultList();
		return list;
	}

	public void saveUserIpStatus(String userIP, String str) {
		String sql = "insert into sys_login_limit(host_,limit_times)values (?,?)";
		getEm().createNativeQuery(sql).setParameter(1, userIP).setParameter(2, str).executeUpdate();
	}

	public void updateLockedUserIPInfo(String userIP, String wrongTimes, String lockedTime) {
		String sql = "update sys_login_limit set limit_times = '" + wrongTimes + "' ";
		if (lockedTime != null || !"".equals(lockedTime)) {
			sql += ",limit_time = '" + lockedTime + "'";
		}
		sql += " where host_ = '" + userIP + "'";
		getEm().createNativeQuery(sql).executeUpdate();
	}

	/**
	 * 根据用户id 得到用户所处部门id
	 */
	@SuppressWarnings("rawtypes")
	public String getUserDeptId(String userId) {
		String sql = " SELECT DEPT_ID FROM SYS_USERDEPARTMENT WHERE USER_ID = ?0";
		List list = getEm().createNativeQuery(sql).setParameter(0, userId).getResultList();
		String deptId = "";
		if (list != null && list.size() > 0) {
			deptId = list.get(0).toString();
		}
		return deptId;
	}

	/**
	 * 根据部门ids 查询用户 目前只查询普通管理员用户 一般操作人员忽略
	 */
	@SuppressWarnings("rawtypes")
	public String findAllUserByDeptIds(String deptids) {
		String sql = "SELECT U.USERNAME FROM SYS_USER U LEFT JOIN SYS_USERDEPARTMENT UD "
				+ " ON U.ID_ = UD.USER_ID WHERE U.USERLEVEL='2' AND UD.DEPT_ID IN(" + deptids + ")";
		List list = getEm().createNativeQuery(sql).getResultList();
		String userNames = "";
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				userNames += "'" + list.get(i).toString() + "',";
			}
		}
		if (!"".equals(userNames) && userNames.length() > 1 && userNames.endsWith(",")) {
			userNames = userNames.substring(0, userNames.length() - 1);
		}
		return userNames;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Page getAllSysUser(String deptName, String userName, String seledUser, boolean isNotin, int start, int limit) {
		List<String> values = new ArrayList<String>();
		StringBuffer whereStr = new StringBuffer("WHERE 1=1");
		if (StringUtils.isNotBlank(deptName)) {
			whereStr.append(" AND EXISTS (SELECT 1 FROM SYS_USERDEPARTMENT DR WHERE DR.DEPT_ID = ? AND DR.USER_ID = T.ID_)");
			values.add(deptName);
		}
		if (StringUtils.isNotBlank(userName)) {
			whereStr.append(" AND (T.USERNAME LIKE ? OR T.FULLNAME LIKE ?)");
			values.add("%" + userName + "%");
			values.add("%" + userName + "%");
		}
		if (StringUtils.isNotBlank(seledUser)) {
			String[] userIdArr = seledUser.split(",");
			if (userIdArr.length > 0) {
				String userId = "";
				for (int i = 0; i < userIdArr.length; i++) {
					if (StringUtils.isNotBlank(userIdArr[i])) {
						userId += "?";
						if (i + 1 < userIdArr.length) {
							userId += ",";
						}
						values.add(userIdArr[i]);
					}
				}
				whereStr.append(" AND T.USERNAME " + (isNotin ? "NOT IN" : "IN") + " (" + userId + ")");
			}
		}
		String sql = "SELECT T.ID_,T.USERNAME,T.FULLNAME,"
				+ "(SELECT TO_CHAR(WM_CONCAT(D.ID_)) DEPT_ID FROM SYS_USERDEPARTMENT DR,SYS_DEPARTMENT D"
				+ " WHERE DR.DEPT_ID = D.ID_ AND DR.USER_ID = T.ID_ GROUP BY DR.USER_ID) DEPT_ID,"
				+ "(SELECT TO_CHAR(WM_CONCAT(D.NAME_CN)) DEPT_NAME FROM SYS_USERDEPARTMENT DR,SYS_DEPARTMENT D"
				+ " WHERE DR.DEPT_ID = D.ID_ AND DR.USER_ID = T.ID_ GROUP BY DR.USER_ID) DEPT_NAME"
				+ " FROM SYS_USER T " + "" + whereStr.toString() + " ORDER BY DEPT_NAME,T.USERNAME";
		log.debug("获取系统用户的sql:" + sql);
		// Query query = getEm().createNativeQuery(sql);
		// for (int i = 0; i < values.size(); i++) {
		// query.setParameter(i+1, values.get(i));
		// }
		// List resultList = query.getResultList();
		// List<JSONObject> jsonList =
		// ParseJSONObject.parse("ID_,USER_NAME,FULL_NAME,DEPT_ID,DEPT_NAME",
		// resultList);
		if (start == -1 && limit == -1) {
			Query query = getEm().createNativeQuery(sql);
			for (int i = 0; i < values.size(); i++) {
				query.setParameter(i + 1, values.get(i));
			}
			List resultList = query.getResultList();
			List<JSONObject> jsonList = ParseJSONObject.parse("ID_,USER_NAME,FULL_NAME,DEPT_ID,DEPT_NAME", resultList);
			int n = jsonList.size();
			return new PageObject(JSONArray.fromObject(jsonList), n, n, n + 1);
		} else {
			Page page = pagedSQLQuery(sql.toString(), start, limit, values.toArray());
			List rellist = page.getElements();
			List<JSONObject> jsonList = ParseJSONObject.parse("ID_,USER_NAME,FULL_NAME,DEPT_ID,DEPT_NAME", rellist);
			page.setElements(jsonList);
			return page;
		}
	}

	@SuppressWarnings("rawtypes")
	public List<JSONObject> getShortCutResources(String userId) {
		StringBuffer sb = new StringBuffer();
		List<JSONObject> js = new ArrayList<JSONObject>();
		sb.append("SELECT SR.ID_,SR.PARENT_ID,SR.NAME_,SR.NAME_CN,SR.LOCATION_,SR.STATUS_, ");
		sb.append("SR.ORDER_,SR.KIND_,SR.IS_SHOWDESKTOP,SR.IS_WEBPAGE,SR.IMAGE_ ");
		sb.append("FROM (SELECT RESOURCE_ID FROM SYS_USER_SHORTCUT WHERE USER_ID = ? ) SUS ");
		sb.append("LEFT JOIN SYS_RESCOURCE SR ON SUS.RESOURCE_ID = SR.ID_ ");
		List list = getEm().createNativeQuery(sb.toString()).setParameter(1, userId).getResultList();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				JSONObject jo = new JSONObject();
				Object[] o = (Object[]) list.get(i);
				Object[] os = new Object[] {};
				jo.put("id", o[0]);
				jo.put("image", o[10]);
				jo.put("isShowDesktop", o[8]);
				jo.put("isWebpage", o[9]);
				jo.put("kind", o[7]);
				jo.put("location", o[4]);
				jo.put("name", o[2]);
				jo.put("nameCn", o[3]);
				jo.put("order", o[6]);
				jo.put("pid", "2c9004345982088501598269cc510000");
				jo.put("status", o[5]);
				jo.put("childs", os);

				js.add(jo);
			}
		}
		return js;
	}
}