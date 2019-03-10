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

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.eastcom_sw.common.dao.jpa.DaoSupport;
import com.eastcom_sw.common.dao.jpa.MyBatchPreparedStatementSetter;
import com.eastcom_sw.core.entity.security.Department;

/**
 * 部门管理
 * 
 * @author SCM
 */
@Component
public class DepartmentDaoImpl extends DaoSupport<Department> implements
		DepartmentDao {

	@PersistenceContext(unitName = "defaultPU")
	public EntityManager em;

	private JdbcTemplate jdbcTemplate;

	@Resource(name = "dataSource")
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/**
	 * 获取所有部门信息
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List queryAllDeptBasicInfo() {
		String sql = "SELECT ID_,DESC_,NAME_,NAME_CN,ORDER_,PARENT_ID FROM SYS_DEPARTMENT";
		return getEm().createNativeQuery(sql).getResultList();
	}

	/**
	 * 根据部门名称查找部门及其子部门
	 * 
	 * @param name
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List searchDepartment(String name) {
		String sql = "SELECT ID_,DESC_,NAME_,NAME_CN,ORDER_,PARENT_ID FROM SYS_DEPARTMENT WHERE ID_ IN (SELECT DISTINCT ID_ FROM SYS_DEPARTMENT START WITH NAME_CN LIKE ? CONNECT BY PRIOR PARENT_ID = ID_)";
		return getEm().createNativeQuery(sql).setParameter(1, "%" + name + "%")
				.getResultList();

	}

	@SuppressWarnings("unchecked")
	public List<Object> queryDepartmentByName(String name) {
		// List<Department> depts = new ArrayList<Department>();
		// String sql
		// ="SELECT DISTINCT(ID_),NAME_,NAME_CN,ORDER_,PARENT_ID,DESC_ FROM SYS_DEPARTMENT START WITH NAME_CN LIKE ? CONNECT BY PRIOR PARENT_ID = ID_";
		String sql = "SELECT DISTINCT(ID_),NAME_,NAME_CN,ORDER_,PARENT_ID,DESC_ FROM SYS_DEPARTMENT WHERE NAME_CN LIKE ? OR NAME_ LIKE ?";
		char[] chars = name.toCharArray();
		String name_ = "";
		for (char c : chars) {
			name_ += "%" + c;
		}
		name_ += "%";
		List<Object> list = getEm().createNativeQuery(sql)
				.setParameter(1, name_).setParameter(2, "%" + name + "%")
				.getResultList();
		// for (Object[] o : list) {
		// Department dept = new Department();
		// dept.setId(String.valueOf(o[0]));
		// dept.setName(String.valueOf(o[1]));
		// dept.setNameCn(String.valueOf(o[2]));
		// dept.setOrder(Integer.parseInt(String.valueOf(o[3])));
		// dept.setPid(o[4] != null ? String.valueOf(o[4]) : "");
		// dept.setDesc(String.valueOf(o[5]));
		// depts.add(dept);
		// }
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> queryDepartmentByNameAndDeptIds(String name,String deptIds) {
		String sql = "SELECT DISTINCT(ID_),NAME_,NAME_CN,ORDER_,PARENT_ID,DESC_ FROM SYS_DEPARTMENT WHERE (NAME_CN LIKE ? OR NAME_ LIKE ?) AND ID_ IN("+deptIds+")";
		char[] chars = name.toCharArray();
		String name_ = "";
		for (char c : chars) {
			name_ += "%" + c;
		}
		name_ += "%";
		List<Object> list = getEm().createNativeQuery(sql)
				.setParameter(1, name_).setParameter(2, "%" + name + "%")
				.getResultList();
		return list;
	}

	/**
	 * 批量删除部门及其关联关系
	 * 
	 * @param deptIds
	 *            部门id集合
	 */
	public void removeDepartments(List<String> deptIds) {
		String condition = "'" + deptIds.get(0) + "'";
		for (int i = 1; i < deptIds.size(); i++) {
			condition += (",'" + deptIds.get(i) + "'");
		}
		String sql1 = "DELETE FROM SYS_USER_DEPART_RANGE WHERE DEPT_ID IN("
				+ condition + ")";
		String sql2 = "DELETE FROM SYS_USERDEPARTMENT WHERE DEPT_ID IN ("
				+ condition + ")";
		String sql3 = "DELETE FROM SYS_DEPARTMENT WHERE ID_ IN (" + condition
				+ ")";
		getEm().createNativeQuery(sql1).executeUpdate();
		getEm().createNativeQuery(sql2).executeUpdate();
		getEm().createNativeQuery(sql3).executeUpdate();
	}

	@SuppressWarnings("rawtypes")
	public List findAllDept() {
		String sql = "SELECT PARENT_ID,ID_ FROM SYS_DEPARTMENT WHERE PARENT_ID IS NOT NULL";
		return getEm().createNativeQuery(sql).getResultList();
	}

	/** 根据部门id获取部门信息 */
	@SuppressWarnings("rawtypes")
	public List queryDepartmentInfo(List ids) {
		StringBuffer idsStr = new StringBuffer();
		idsStr.append('?').append(0);
		for (int i = 1; i < ids.size(); i++) {
			idsStr.append(',').append('?').append(i);
		}
		String sql = "select d.id_,d.name_cn from sys_department d where d.id_ in ("
				+ idsStr + ")";
		Query query = getEm().createNativeQuery(sql);
		for (int i = 0; i < ids.size(); i++) {
			query.setParameter(i, ids.get(i));
		}
		return query.getResultList();
	}

	/**
	 * 通过名称或者中文名获取部门(暂用于判断该名称是否已存在)
	 * 
	 * @param name
	 *            用户名或者中文名
	 * @param type
	 *            name:名称 nameCn：中文名
	 * @return
	 */
	public long queryDeptByName(String name, String type) {
		String sql = "select count(id_) from sys_department where ";
		sql += type.equals("name") ? " name_" : "name_cn";
		sql += "=?0";
		long num = Long.parseLong(getEm().createNativeQuery(sql)
				.setParameter(0, name).getSingleResult().toString());
		return num;
	}

	/**
	 * 批量更新部门关系（父部门以及order顺序）
	 * 
	 * @param jsonString
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void changeParentAndOrder(ArrayList<JSONObject> params) {
		String sql = "UPDATE SYS_DEPARTMENT SET ORDER_ = ?,PARENT_ID = ? where ID_ = ?";
		String sql2 = "UPDATE SYS_DEPARTMENT SET ORDER_ = ?,PARENT_ID = null where ID_ = ?";
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
	
	public List getDeptInfoByIds(String ids){
		String sql = "select d.id_,d.name_cn from sys_department d where d.id_ in ("
				+ ids + ")";
		return getEm().createNativeQuery(sql).getResultList();
	}

	public String findDepartmentHavaNoParent(){
		String sql = " select id_ from sys_department where (parent_id is null or parent_id = '') ";
		List list = getEm().createNativeQuery(sql).getResultList();
		String deptIds = "";
		if(list != null && list.size() > 0){
			for(int i=0;i<list.size();i++){
				String s = list.get(i).toString();
				deptIds += "'"+s+"',";
			}
			deptIds = deptIds.substring(0,deptIds.length()-1);
		}
		return deptIds;
	}
	
	public List<Department> findChildDepts(String deptids){
		String sql = "from Department where id in("+deptids+")";
		List<Department> list = getEm().createQuery(sql,Department.class).getResultList();
		return list;
	}
}
