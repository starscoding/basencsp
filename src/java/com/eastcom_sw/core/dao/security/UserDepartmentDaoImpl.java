package com.eastcom_sw.core.dao.security;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;

import com.eastcom_sw.common.dao.jpa.DaoSupport;
import com.eastcom_sw.core.entity.security.Department;

/**
 * 用户部门相关查询
 * @author JJF
 * @Date NOV 22 2012
 */
@Component
public class UserDepartmentDaoImpl extends DaoSupport<Department> implements UserDepartmentDao {
	
	@PersistenceContext(unitName="defaultPU")
	public EntityManager em;
	
	/**
	 * 获取用户所有权限部门信息
	 * @param userId	"":获取所有部门	userId:获取该用户id的权限部门
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List getAllAccreditDeptByUser(String userId){
		String sql = "";
		Query query = null;
		if(userId.isEmpty()){
			sql = "select d.id_,d.desc_,d.name_,d.name_cn,d.order_,d.parent_id from sys_department d order by d.order_ ASC";
			query = getEm().createNativeQuery(sql);
		}
		else{
			sql = "select d.id_,d.desc_,d.name_,d.name_cn,d.order_,d.parent_id from sys_department d,sys_user_depart_range udr  where udr.user_id = ?0 and d.id_ = udr.dept_id  order by d.order_ ASC";
//			sql = "select d.id_,d.desc_,d.name_,d.name_cn,d.order_,d.parent_id from sys_department d,sys_userdepartment udr where udr.user_id = ?0 and d.id_ = udr.dept_id  order by d.order_ ASC";
			query = getEm().createNativeQuery(sql);
			query.setParameter(0, userId);
		}
		return query.getResultList();
	}
	
	/**
	 * 获取用户所有所属部门信息
	 * @param userId		"":获取所有部门	userId:获取该用户id的权限部门
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List getAllDeptByUser(String userId){
		String sql = "";
		Query query = null;
		if(userId.isEmpty()){
			return new ArrayList();
		}
		else{
			sql = "select d.id_,d.desc_,d.name_,d.name_cn,d.order_,d.parent_id from sys_department d,sys_userdepartment ud  where ud.user_id = ?0 and d.id_ = ud.dept_id";
			query = getEm().createNativeQuery(sql);
			query.setParameter(0, userId);
			return query.getResultList();
		}
	}
	
	/**根据部门id获取所有关联用户id*/
	@SuppressWarnings("rawtypes")
	public List queryUsersById(List ids){
		StringBuffer idsStr = new StringBuffer();
		idsStr.append('?').append(0);
		for(int i=1;i<ids.size();i++){
			idsStr.append(',').append('?').append(i);
		}
		String sql = "select ud.user_id from sys_userdepartment ud where ud.dept_id in("+idsStr+")";
		Query query = getEm().createNativeQuery(sql);
		for(int i=0;i<ids.size();i++){
			query.setParameter(i,ids.get(i));
		}
		return query.getResultList();
	}
	
	/**
	 * 获取当前部门下用户基本信息
	 * @param did	部门id
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List queryDeptUsers(String did){
		String sql = "select U.ID_,U.FULLNAME from SYS_USERDEPARTMENT ud inner join SYS_USER u on (UD.USER_ID = U.ID_) where UD.DEPT_ID = ?0";
		return getEm().createNativeQuery(sql).setParameter(0, did).getResultList();
	}
}
