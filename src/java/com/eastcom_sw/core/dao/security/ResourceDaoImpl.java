/**
 * 
 */
package com.eastcom_sw.core.dao.security;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

import com.eastcom_sw.common.dao.jpa.DaoSupport;
import com.eastcom_sw.common.dao.jpa.MyBatchPreparedStatementSetter;
import com.eastcom_sw.core.entity.security.Resource;
import com.eastcom_sw.core.entity.security.User;

/**
 * @author SCM 资源管理
 */
@Component
public class ResourceDaoImpl extends DaoSupport<Resource> implements
		ResourceDao {

	@PersistenceContext(unitName = "defaultPU")
	public EntityManager em;

	private JdbcTemplate jdbcTemplate;

	@javax.annotation.Resource(name = "dataSource")
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/**
	 * 异步获取子节点
	 * 
	 * @param parentId
	 * @return
	 */
	public List<Resource> getResourceChildren(String parentId) {
		String hql = "";
		if (parentId.isEmpty()) {
			hql = "from Resource r where (r.parent.id is null or r.parent.id='') order by r.order asc";
			return getEm().createQuery(hql, Resource.class).getResultList();
		} else {
			hql = "from Resource r where r.parent.id = ? order by r.order asc";
			return getEm().createQuery(hql, Resource.class)
					.setParameter(1, parentId).getResultList();
		}

	}

	/**
	 * 删除资源及其与角色的关系及其与个人桌面的关系
	 */
	public void removeResourceAndChildren(ArrayList<String> ids) {
		StringBuffer jpql = new StringBuffer();
		int length = ids.size();
		for (int i = 0; i < length; i++) {
			jpql.append('?').append(i).append(',');
		}
		jpql.deleteCharAt(jpql.length() - 1);
		Query query = getEm().createNativeQuery(
				"DELETE FROM SYS_RESCOURCE WHERE ID_ IN (" + jpql.toString()
						+ ")");
		Query query2 = getEm().createNativeQuery(
				"DELETE FROM SYS_ROLERESOURCE WHERE RESOURCE_ID IN ("
						+ jpql.toString() + ")");
		Query query3 = getEm().createNativeQuery(
				"DELETE FROM SYS_DESKTOP_RESOURCE WHERE RESOURCE_ID IN ("
						+ jpql.toString() + ")");
		for (int i = 0; i < length; i++) {
			query.setParameter(i, ids.get(i));
			query2.setParameter(i, ids.get(i));
			query3.setParameter(i, ids.get(i));
		}
		query2.executeUpdate(); // 删除资源与角色的中间关系
		query3.executeUpdate(); // 删除资源与个人桌面的关系
		query.executeUpdate(); // 删除资源
	}

	/**
	 * 根据资源名称查找资源
	 * 
	 * @param name
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List searchResource(String name) {
		// String sql =
		// "SELECT DISTINCT(ID_),NAME_,NAME_CN,LOCATION_,STATUS_,ORDER_," +
		// "KIND_,IS_SHOWDESKTOP,IS_WEBPAGE,IMAGE_,CREATOR_," +
		// "CREATE_TIME,REMARKS_,PARENT_ID FROM SYS_RESCOURCE " +
		// "START WITH LOWER(NAME_CN) LIKE ? CONNECT BY PRIOR PARENT_ID = ID_";
		String sql = "SELECT DISTINCT(ID_),NAME_,NAME_CN,LOCATION_,STATUS_,ORDER_,KIND_,IS_SHOWDESKTOP,IS_WEBPAGE,IMAGE_,CREATOR_,CREATE_TIME,REMARKS_,PARENT_ID FROM SYS_RESCOURCE WHERE KIND_ = '0' AND (NAME_ LIKE ? OR NAME_CN LIKE ?)";
		return getEm().createNativeQuery(sql).setParameter(1, "%" + name + "%")
				.setParameter(2, "%" + name + "%").getResultList();
	}

	@SuppressWarnings("rawtypes")
	public List getAllPageResource() {
		String sql = "SELECT DISTINCT(ID_),NAME_,NAME_CN,LOCATION_,STATUS_,ORDER_,KIND_,IS_SHOWDESKTOP,IS_WEBPAGE,IMAGE_,CREATOR_,CREATE_TIME,REMARKS_,PARENT_ID FROM SYS_RESCOURCE";
		return getEm().createNativeQuery(sql).getResultList();
	}

	/**
	 * 根据用户查询他所拥有的所有资源权限(暂弃，当前该数据从redis中获取)
	 * 
	 * @param u
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List getRescourcesByUser(User u) {
		String sql = "";
		sql += " SELECT RE.ID_,RE.REMARKS_,RE.NAME_,RE.NAME_CN,RE.ORDER_,RE.PARENT_ID FROM SYS_RESCOURCE RE ";
		sql += " WHERE RE.ID_ IN (SELECT DISTINCT RE.ID_ FROM SYS_RESCOURCE RE ";
		sql += " INNER JOIN SYS_ROLERESOURCE RR ON (RE.ID_ = RR.RESOURCE_ID) ";
		sql += " WHERE RR.ROLE_ID IN (SELECT DISTINCT R.ID_ FROM SYS_ROLE R ";
		sql += " INNER JOIN SYS_USERROLE UR ON(R.ID_ = UR.ROLE_ID) ";
		sql += " WHERE UR.USER_ID = ?0 ))";
		return getEm().createNativeQuery(sql).setParameter(0, u.getId())
				.getResultList();
	}

	@SuppressWarnings("rawtypes")
	public List getRescourcesByUserByRoleId(String roleId) {
		String sql = "";
		sql += " SELECT RE.ID_,RE.REMARKS_,RE.NAME_,RE.NAME_CN,RE.ORDER_,RE.PARENT_ID FROM SYS_RESCOURCE RE ";
		sql += " INNER JOIN SYS_ROLERESOURCE RR ON (RE.ID_=RR.RESOURCE_ID) ";
		sql += " WHERE RR.ROLE_ID = ?0 ";
		return getEm().createNativeQuery(sql).setParameter(0, roleId)
				.getResultList();
	}

	@SuppressWarnings("rawtypes")
	public List getAllValidResource() {
		String sql = "SELECT ID_,PARENT_ID,NAME_,NAME_CN,LOCATION_,STATUS_,ORDER_,"
				+ "KIND_,IS_SHOWDESKTOP,IS_WEBPAGE,IMAGE_ FROM SYS_RESCOURCE"
				+ " ORDER BY KIND_ ASC";
		return getEm().createNativeQuery(sql).getResultList();
	}

	/**
	 * 批量更新资源关系（父节点以及order顺序）
	 * 
	 * @param params
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void changeParentAndOrder(ArrayList<JSONObject> params) {
		String sql = "UPDATE SYS_RESCOURCE SET ORDER_ = ?,PARENT_ID = ? where ID_ = ?";
		String sql2 = "UPDATE SYS_RESCOURCE SET ORDER_ = ?,PARENT_ID = null where ID_ = ?";
		List dbParams = new ArrayList();
		List dbParams2 = new ArrayList();
		for (JSONObject jo : params) {
			Object _pid = jo.get("parentId");
			if (_pid == null || StringUtils.isBlank(_pid.toString())) {
				String[][] p = new String[][] {
						{ "int", jo.get("order").toString() },
						{ "string", jo.get("id").toString() } };
				dbParams2.add(p);
			} else {
				String[][] p = new String[][] {
						{ "int", jo.get("order").toString() },
						{ "string", _pid.toString() },
						{ "string", jo.get("id").toString() } };
				dbParams.add(p);
			}
		}
		MyBatchPreparedStatementSetter setter = new MyBatchPreparedStatementSetter(
				dbParams);
		MyBatchPreparedStatementSetter setter2 = new MyBatchPreparedStatementSetter(
				dbParams2);
		jdbcTemplate.batchUpdate(sql, setter);
		jdbcTemplate.batchUpdate(sql2, setter2);
	}

	/**
	 * 通过名称获取id
	 * 
	 * @param name
	 * @return
	 */
	public String getIdByName(String name) {
		String sql = "SELECT ID_ FROM SYS_RESCOURCE WHERE NAME_ = ?";
		List l = getEm().createNativeQuery(sql).setParameter(1, name)
				.getResultList();
		if (l != null && l.size() > 0 && l.get(0) != null) {
			return l.get(0).toString();
		} else {
			return "";
		}
	}
}
