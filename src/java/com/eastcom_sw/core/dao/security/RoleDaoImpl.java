/**
 * 
 */
package com.eastcom_sw.core.dao.security;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.eastcom_sw.common.dao.jpa.DaoSupport;
import com.eastcom_sw.common.dao.jpa.MyBatchPreparedStatementSetter;
import com.eastcom_sw.common.utils.ParseJSONObject;
import com.eastcom_sw.common.utils.RandomGUID;
import com.eastcom_sw.core.entity.security.Role;
import com.eastcom_sw.core.entity.security.User;

import net.sf.json.JSONObject;

/**
 * 角色管理
 * 
 * @author SCM
 * 
 */
@Component
public class RoleDaoImpl extends DaoSupport<Role> implements RoleDao {

	@PersistenceContext(unitName = "defaultPU")
	public EntityManager em;

	private JdbcTemplate jdbcTemplate;

	@Resource(name = "dataSource")
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/** 根据角色id获取所有关联用户id */
	@SuppressWarnings("rawtypes")
	public List queryUsersByIds(List ids) {
		StringBuffer idsStr = new StringBuffer();
		idsStr.append('?').append(0);
		for (int i = 1; i < ids.size(); i++) {
			idsStr.append(',').append('?').append(i);
		}
		String sql = "select ur.user_id from sys_userrole ur where ur.role_id in("
				+ idsStr + ")";
		Query query = getEm().createNativeQuery(sql);
		for (int i = 0; i < ids.size(); i++) {
			query.setParameter(i, ids.get(i));
		}
		return query.getResultList();
	}

	/** 根据角色id获取角色信息 */
	@SuppressWarnings("rawtypes")
	public List queryRoleInfo(List ids) {
		StringBuffer idsStr = new StringBuffer();
		idsStr.append('?').append(0);
		for (int i = 1; i < ids.size(); i++) {
			idsStr.append(',').append('?').append(i);
		}
		String sql = "select r.id_,r.name_cn from sys_role r where r.id_ in ("
				+ idsStr + ")";
		Query query = getEm().createNativeQuery(sql);
		for (int i = 0; i < ids.size(); i++) {
			query.setParameter(i, ids.get(i));
		}
		return query.getResultList();
	}
	
	/**
	 * 获取单个用户所具有的角色权限的简单字段
	 * 
	 * @param userId
	 *            用户id
	 * @param allFlag
	 *            是否返回所有角色
	 * @param creatorFlag
	 *            true:连带创建者是该用户的角色一起返回，false：不返回
	 * @return 角色id 角色中文名 角色简介
	 */
	@SuppressWarnings("rawtypes")
	public List querySimpleRoleInfoByUserId(User u, boolean allFlag,
			boolean creatorFlag) {
		Query query = null;
		String sql = "";
		if (allFlag) {
			sql = "select r.id_,r.name_cn,r.description_,r.name_ from sys_role r";
			query = getEm().createNativeQuery(sql);
		} else {
			sql += " SELECT R.ID_,R.NAME_CN,R.DESCRIPTION_,R.NAME_ FROM SYS_ROLE R INNER JOIN ( ";
			sql += " SELECT DISTINCT R.ID_ FROM SYS_ROLE R ";
			sql += " INNER JOIN SYS_USERROLE UR ON(R.ID_ = UR.ROLE_ID) ";
			sql += " WHERE UR.USER_ID = ?0 ";
			if (creatorFlag) {
				sql += " UNION ALL";
				sql += " SELECT R.ID_ FROM SYS_ROLE R WHERE R.CREATOR_ = ?1";
			}
			sql += " ) IDS ON (IDS.ID_ = R.ID_) ORDER BY R.NAME_CN ";
			query = getEm().createNativeQuery(sql);
			query.setParameter(0, u.getId());
			if (creatorFlag) {
				query.setParameter(1, u.getUserName());
			}
		}
		return query.getResultList();
	}


	/**
	 * 获取单个用户所具有的角色权限的简单字段
	 * 
	 * @param userId
	 *            用户id
	 * @param allFlag
	 *            是否返回所有角色
	 * @param creatorFlag
	 *            true:连带创建者是该用户的角色一起返回，false：不返回
	 * @return 角色id 角色中文名 角色简介
	 */
	@SuppressWarnings("rawtypes")
	public List querySimpleRoleInfoByUserIdNew(User u, boolean allFlag,
			boolean creatorFlag,String userNames) {
		Query query = null;
		String sql = "";
		if (allFlag) {
			sql = "select r.id_,r.name_cn,r.description_,r.name_ from sys_role r";
			query = getEm().createNativeQuery(sql);
		} else {
			sql += " SELECT R.ID_,R.NAME_CN,R.DESCRIPTION_,R.NAME_ FROM SYS_ROLE R INNER JOIN ( ";
			sql += " SELECT ID_ FROM SYS_ROLE WHERE CREATOR_ IN ("+userNames+") ";
			sql += " ) IDS ON (IDS.ID_ = R.ID_) ORDER BY R.NAME_CN ";
			query = getEm().createNativeQuery(sql);
		}
		return query.getResultList();
	}

	/**
	 * 获取用户所有的相关角色，包括拥有权限角色和其新建的角色
	 * 
	 * @param userId
	 *            用户id
	 * @return 角色id 角色名称 角色中文名 用户数 资源数 创建者 创建时间 角色简介
	 */
	@SuppressWarnings("rawtypes")
	public List queryAllRolesByUser(User u,String userNames) {
		Query query = null;
		String sql = " SELECT R.ID_,R.CREATE_TIME,R.CREATOR_,R.DESCRIPTION_,R.NAME_,R.NAME_CN,NUMS.USER_NUM,NUMS.RES_NUM ";
		sql += " FROM SYS_ROLE R ";
		sql += " LEFT JOIN ";
		sql += " (SELECT DISTINCT RESULTS.ROLE_ID ,RESULTS.USER_NUM,RESULTS.RES_NUM ";
		sql += " FROM(SELECT USER_NUM.ROLE_ID,USER_NUM.USER_NUM,RES_NUM.RES_NUM ";
		sql += " FROM(SELECT UR.ROLE_ID,COUNT(UR.USER_ID) USER_NUM ";
		sql += " FROM SYS_USERROLE UR GROUP BY UR.ROLE_ID) USER_NUM ";
		sql += " LEFT JOIN(SELECT RR.ROLE_ID,COUNT(RR.RESOURCE_ID) RES_NUM FROM ";
		sql += " (SELECT RR.ROLE_ID,RR.RESOURCE_ID FROM SYS_ROLERESOURCE RR ";
		sql += " LEFT JOIN SYS_RESCOURCE SR ON RR.RESOURCE_ID = SR.ID_ WHERE SR.STATUS_ != '0')RR ";
		sql += " GROUP BY RR.ROLE_ID) RES_NUM ";		
		sql += " ON (USER_NUM.ROLE_ID = RES_NUM.ROLE_ID) ";
		sql += " UNION ALL ";
		sql += " SELECT RES_NUM.ROLE_ID, USER_NUM.USER_NUM,RES_NUM.RES_NUM FROM (SELECT UR.ROLE_ID,COUNT(UR.USER_ID) USER_NUM ";
		sql += " FROM SYS_USERROLE UR GROUP BY UR.ROLE_ID) USER_NUM ";
		sql += " RIGHT JOIN (SELECT RR.ROLE_ID,COUNT(RR.RESOURCE_ID) RES_NUM FROM ";
		sql += " (SELECT RR.ROLE_ID,RR.RESOURCE_ID FROM SYS_ROLERESOURCE RR ";
		sql += " LEFT JOIN SYS_RESCOURCE SR ON RR.RESOURCE_ID = SR.ID_ WHERE SR.STATUS_ != '0')RR ";
		sql += " GROUP BY RR.ROLE_ID) RES_NUM ON (USER_NUM.ROLE_ID = RES_NUM.ROLE_ID)) RESULTS ";
		sql += " ) NUMS ON (R.ID_ = NUMS.ROLE_ID) ";
		/**
		 * 如果是超级管理员 可以看见全部角色
		 * 普通管理员 包括（自己所处的角色  自己创建的角色  ）
		 */
		if (u.getUserLevel().equals("1")) {
			sql += " ORDER BY R.NAME_CN ";
			query = getEm().createNativeQuery(sql);
		} else {
//			sql += " WHERE R.ID_ IN (SELECT ROLE_ID FROM SYS_USERROLE WHERE USER_ID = ?0 ";
//			sql += " UNION ALL";
			//2016-08-30 注释 
			//sql += " SELECT ID_ FROM SYS_ROLE WHERE CREATOR_ = ?1) ORDER BY R.NAME_CN ";
			if(userNames != null && userNames != ""){
				sql += " WHERE R.ID_ IN( ";
				sql += " SELECT ID_ FROM SYS_ROLE WHERE CREATOR_ IN ("+userNames+") )";
			}
			sql += " ORDER BY R.NAME_CN ";
			query = getEm().createNativeQuery(sql);
			//query.setParameter(0, u.getId());
			//query.setParameter(1, u.getUserName());
		}
		return query.getResultList();
	}

	/**
	 * 通过名称或者中文名获取角色数量(暂用于判断该名称是否已存在)
	 * 
	 * @param name
	 *            用户名或者中文名
	 * @param type
	 *            name:名称 nameCn：中文名
	 * @return
	 */
	public long queryRoleByName(String name, String type) {
		String sql = "select count(id_) from sys_role where ";
		sql += type.equals("name") ? " name_" : "name_cn";
		sql += "=?0";
		Query query = getEm().createNativeQuery(sql).setParameter(0, name);
		return Long.parseLong(query.getSingleResult().toString());
	}

	/**
	 * 修改role基本信息
	 * 
	 * @param id
	 *            角色Id
	 * @param name
	 *            角色名称
	 * @param nameCn
	 *            角色中文名称
	 * @param validStartTime
	 *            角色有效期开始时间
	 * @param validEndTime
	 *            角色有效期结束时间
	 * @param description
	 *            角色描述
	 */
	public void updateRoleBasicInfo(String id, String name, String nameCn,
			String validStartTime, String validEndTime, String description) {
		String sql = "UPDATE SYS_ROLE SET NAME_CN = ?0,VALID_STARTTIME = ?1,VALID_ENDTIME = ?2,DESCRIPTION_ = ?3 WHERE ID_ = ?4";
		Query query = getEm().createNativeQuery(sql);
		query.setParameter(0, nameCn);
		query.setParameter(1, validStartTime);
		query.setParameter(2, validEndTime);
		query.setParameter(3, description);
		query.setParameter(4, id);
		query.executeUpdate();
	}

	/**
	 * 修改角色资源权限
	 * 
	 * @param roleId
	 *            :当前所要角色的id
	 * @param addIds
	 *            :新增权限资源id集合
	 * @param delIds
	 *            :丧失权限资源id集合
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void updateRoleResourceRange(String roleId, String[] addIds,
			String[] delIds) {
		if (addIds != null && addIds.length > 0) {
			String sql = "insert into SYS_ROLERESOURCE (ROLE_ID,RESOURCE_ID) values (?,?)";
			List params = new ArrayList();
			for (String id : addIds) {
				params.add(new String[][] { { "string", roleId },
						{ "string", id } });
			}
			MyBatchPreparedStatementSetter setter = new MyBatchPreparedStatementSetter(
					params);
			jdbcTemplate.batchUpdate(sql, setter);
		}

		if (delIds != null && delIds.length > 0) {
			StringBuffer delIdStr = new StringBuffer();
			for (String id : delIds) {
				delIdStr.append("'" + id + "',");
			}
			delIdStr.deleteCharAt(delIdStr.length() - 1);
			String sql = "delete from SYS_ROLERESOURCE where ROLE_ID = '"
					+ roleId + "' and RESOURCE_ID in (" + delIdStr + ")";
			getEm().createNativeQuery(sql).executeUpdate();
		}
	}

	/**
	 * 根据用户获取该用户的所有地区权限
	 * 
	 * @param u
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List queryRegionByUser(User u) {
		String sql = "";
		sql += " SELECT REGION FROM SYS_ROLEREGION WHERE ROLE_ID in (SELECT DISTINCT R.ID_ FROM SYS_ROLE R ";
		sql += " INNER JOIN SYS_USERROLE UR ON(R.ID_ = UR.ROLE_ID) ";
		sql += " WHERE UR.USER_ID = ?0)";
		return getEm().createNativeQuery(sql).setParameter(0, u.getId())
				.getResultList();
	}

	/**
	 * 根据角色查询该角色拥有的区域权限
	 * 
	 * @param roleId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List queryRegionByRoleId(String roleId) {
		String sql = "SELECT REGION FROM SYS_ROLEREGION WHERE ROLE_ID = ?0";
		return getEm().createNativeQuery(sql).setParameter(0, roleId)
				.getResultList();
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
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void updateRoleRegionRange(String roleId, String[] addIds,
			String[] delIds) {
		if (addIds != null && addIds.length > 0) {
			String sql = "insert into SYS_ROLEREGION (ID_,ROLE_ID,REGION) values (?,?,?)";
			List params = new ArrayList();
			for (String id : addIds) {
				params.add(new String[][] {
						{ "string", new RandomGUID().getUUID32() },
						{ "string", roleId }, { "string", id } });
			}
			MyBatchPreparedStatementSetter setter = new MyBatchPreparedStatementSetter(
					params);
			jdbcTemplate.batchUpdate(sql, setter);
		}

		if (delIds != null && delIds.length > 0) {
			StringBuffer delIdStr = new StringBuffer();
			for (String id : delIds) {
				delIdStr.append("'" + id + "',");
			}
			delIdStr.deleteCharAt(delIdStr.length() - 1);
			String sql = "delete from SYS_ROLEREGION where ROLE_ID = '"
					+ roleId + "' and REGION in (" + delIdStr + ")";
			getEm().createNativeQuery(sql).executeUpdate();
		}
	}

	/**
	 * 批量删除角色(暂时前台只传一个id)
	 * 
	 * @param userIds
	 *            roleId集合
	 */
	public void deleteRole(List<String> roleIds) {
		String idStr = "'";
		for (String id : roleIds) {
			idStr += (id + "','");
		}
		idStr = idStr.substring(0, idStr.length() - 2);
		String sql1 = "DELETE FROM SYS_ROLEREGION WHERE ROLE_ID IN(" + idStr
				+ ")";
		String sql2 = "DELETE FROM SYS_ROLERESOURCE WHERE ROLE_ID IN(" + idStr
				+ ")";
		String sql3 = "DELETE FROM SYS_USERROLE WHERE ROLE_ID IN(" + idStr
				+ ")";
		String sql4 = "DELETE FROM SYS_ROLE WHERE ID_ IN(" + idStr + ")";
		getEm().createNativeQuery(sql1).executeUpdate();
		getEm().createNativeQuery(sql2).executeUpdate();
		getEm().createNativeQuery(sql3).executeUpdate();
		getEm().createNativeQuery(sql4).executeUpdate();
	}

	/**
	 * 通过角色id获取该角色下的用户信息
	 * 
	 * @param roleId
	 * @return 用户id,用户中文名
	 */
	@SuppressWarnings("rawtypes")
	public List queryUserInfoByRole(String[] roleIds) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT U.ID_,U.USERNAME,U.FULLNAME,UR.ROLE_ID FROM SYS_USERROLE UR LEFT JOIN SYS_USER U ON (U.ID_ = UR.USER_ID)  WHERE UR.ROLE_ID IN (");
		if (roleIds.length > 0) {
			for (int i = 0; i < roleIds.length; i++) {
				sql.append("?,");
			}
			sql.deleteCharAt(sql.length() - 1);
		}
		sql.append(")");
		Query q = getEm().createNativeQuery(sql.toString());
		for (int i = 0; i < roleIds.length; i++) {
			q.setParameter(i + 1, roleIds[i]);
		}
		return q.getResultList();
	}

	@SuppressWarnings("rawtypes")
	public List queryAllRoleResourceRelationships() {
		String sql = "SELECT R.NAME_,RR.RESOURCE_ID FROM SYS_ROLERESOURCE RR INNER JOIN SYS_ROLE R ON (RR.ROLE_ID = R.ID_)";
		return getEm().createNativeQuery(sql).getResultList();
	}
	
	public List<JSONObject> getUserRole(String userName,boolean isLike) {
		List<String> values = new ArrayList<String>();
		StringBuffer whereStr = new StringBuffer();
		if (StringUtils.isNotBlank(userName)) {
			if (isLike) {
				whereStr.append(" AND (T.USERNAME LIKE ? OR T.FULLNAME LIKE ?) ");
				values.add("%"+userName+"%");
				values.add("%"+userName+"%");
			}else {
				whereStr.append(" AND (T.USERNAME = ? OR T.FULLNAME = ?) ");
				values.add(userName);
				values.add(userName);
			}
		}
		String sql = "SELECT DISTINCT R.ROLE_ID ID_,R.ROLE_ID FROM SYS_USERROLE R"
				+ " WHERE EXISTS (SELECT 1 FROM SYS_USER T WHERE R.USER_ID = T.ID_"
				+ " " +whereStr.toString()
				+ ") ";
		log.debug("获取系统用户所属角色的sql:"+sql);
		Query query = getEm().createNativeQuery(sql);
		for (int i = 0; i < values.size(); i++) {
			query.setParameter(i+1, values.get(i));
		}
		List resultList = query.getResultList();
		List<JSONObject> jsonList = ParseJSONObject.parse("ID_", resultList);
		return jsonList;
	}
}

