package com.eastcom_sw.core.service.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.eastcom_sw.common.service.BaseService;
import com.eastcom_sw.common.service.ServiceException;
import com.eastcom_sw.common.utils.CommonUtil;
import com.eastcom_sw.common.utils.ParseJSONObject;
import com.eastcom_sw.common.utils.SelectTree;
import com.eastcom_sw.core.dao.security.DepartmentDao;
import com.eastcom_sw.core.dao.security.DeptRedisDao;
import com.eastcom_sw.core.dao.security.UserDao;
import com.eastcom_sw.core.entity.security.Department;
import com.eastcom_sw.core.utils.security.DepartmentTreeNode;
import com.google.common.collect.Maps;

/**
 * 部门管理
 * 
 * @author SCM
 * @time 2012-7-31
 */
@Component
@Transactional(readOnly = true)
public class DepartmentService extends BaseService {

	@Autowired
	private DepartmentDao departmentDao;
	@Autowired
	private DeptRedisDao deptRedisDao;
	@Autowired
	private UserDao userDao;

	/**
	 * 通过部门ID查找部门
	 * 
	 * @param id
	 * @return
	 * @throws ServiceException
	 */
	public Department findDeptment(String id) throws ServiceException {
		return departmentDao.findOne(id);
	}

	/**
	 * 保存部门信息,同时更新部门上下级关系到redis
	 * 
	 * @param dept
	 * @throws ServiceException
	 */
	@Transactional(readOnly = false)
	public void saveDepartment(Department dept) throws ServiceException {
		departmentDao.save(dept);
		deptRedisDao.addDeptData(dept.getPid(), dept.getId()); // 新增维护部门与子部门关系
	}

	/**
	 * 修改部门信息
	 * 
	 * @param dept
	 * @throws ServiceException
	 */
	@Transactional(readOnly = false)
	public void updateDepartment(Department dept) throws ServiceException {
		departmentDao.update(dept);
	}

	/**
	 * 删除部门及其子部门
	 * 
	 * @param id
	 * @throws ServiceException
	 */
	public void deleteDepartment(String id) throws ServiceException {
		ArrayList<String> list = new ArrayList<String>();
		list.add(id);
		departmentDao.removeDepartments(deptRedisDao
				.getChildDeptByPid(id, list));
	}

	/**
	 * 获取所有的部门树数据
	 * 
	 * @param checkbox
	 *            true:checkbox树
	 * @param expanded
	 *            true:展开节点
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String findAllDeptTreeData(String name, boolean checkbox,
			boolean expanded) throws ServiceException {
		
		String userLevel = CommonUtil.getUser().getUserLevel();
		String userId = CommonUtil.getUser().getId();
		
		JSONArray records = new JSONArray();
		
		if("1".equals(userLevel)){//超级管理员看见全部部门
			List<Department> depts = new ArrayList<Department>();
			depts = departmentDao.findAll();
			if (name.isEmpty()) {
				for (Department d : depts) {
					if (d.getPid() == null || d.getPid().isEmpty()) {
						JSONObject root = d.getJSONdata(d, checkbox, expanded);
						getDepartmentChildren(root, depts, checkbox, expanded);
						records.add(root);
					}
				}
			} else {
				List<JSONObject> rsList = ParseJSONObject.parse(
						"id,name,nameCn,order,parentId,dsc",
						departmentDao.queryDepartmentByName(name));
				List<JSONObject> totalList = new ArrayList<JSONObject>();
				for (Department d : depts) {
					JSONObject jo = d.getJSONdata(d);
					jo.put("parentId", d.getPid() == null ? "" : d.getPid());
					totalList.add(jo);
				}
				List<JSONObject> rs = new SelectTree(totalList, rsList)
						.selectTreeNodes();
				List<Department> rsdepts = new ArrayList<Department>();
				for (JSONObject jo : rs) {
					rsdepts.add(Department.fromJSONObject(jo));
				}
				for (Department d : rsdepts) {
					if (d.getPid() == null || d.getPid().isEmpty()
							|| d.getPid().equals("null")) {
						JSONObject root = d.getJSONdata(d, checkbox, expanded);
						getDepartmentChildren(root, rsdepts, checkbox, expanded);
						records.add(root);
					}
				}
			}
			
		}else if("2".equals(userLevel)){//普通管理员只能看见自己部门以下的部门
			String departmentId = userDao.getUserDeptId(userId);
			String deptids = findAllChildrenIdById(departmentId);
			List<Department> childDepts = new ArrayList<Department>();
			childDepts = departmentDao.findChildDepts(deptids);
			
			if (name.isEmpty()) {
				for (Department d : childDepts) {
					if (d.getPid() == null || d.getPid().isEmpty()
							|| d.getPid().equals("null") || d.getId().equals(departmentId)) {
						JSONObject root = d.getJSONdata(d, checkbox, expanded);
						getDepartmentChildren(root, childDepts, checkbox, expanded);
						records.add(root);
					}
				}
			} else {
				List<JSONObject> rsList = ParseJSONObject.parse(
						"id,name,nameCn,order,parentId,dsc",
						departmentDao.queryDepartmentByNameAndDeptIds(name,deptids));
				List<JSONObject> totalList = new ArrayList<JSONObject>();
				for (Department d : childDepts) {
					JSONObject jo = d.getJSONdata(d);
					jo.put("parentId", d.getPid() == null ? "" : d.getPid());
					totalList.add(jo);
				}
				List<JSONObject> rs = new SelectTree(totalList, rsList)
						.selectTreeNodes();
				List<Department> rsdepts = new ArrayList<Department>();
				for (JSONObject jo : rs) {
					rsdepts.add(Department.fromJSONObject(jo));
				}
				for (Department d : rsdepts) {
					if (d.getPid() == null || d.getPid().isEmpty()
							|| d.getPid().equals("null") || d.getId().equals(departmentId)) {
						JSONObject root = d.getJSONdata(d, checkbox, expanded);
						getDepartmentChildren(root, rsdepts, checkbox, expanded);
						records.add(root);
					}
				}
			}
		}
		
		return records.toString();
	}

	/**
	 * 构造部门树
	 * 
	 * @param d
	 * @param depts
	 * @param checkbox
	 * @param expanded
	 */
	private JSONObject getDepartmentChildren(JSONObject root,
			List<Department> depts, boolean checkbox, boolean expanded) {
		JSONArray children = new JSONArray();
		for (Department dept : depts) {
			if (String.valueOf(root.get("id")).equals(dept.getPid())) {
				JSONObject child = dept.getJSONdata(dept, checkbox, expanded);
				getDepartmentChildren(child, depts, checkbox, expanded);
				children.add(child);
			}
		}
		if (children.size() > 0) {
			root.put("children", children);
			root.put("leaf", false);
		}
		return root;
	}

	/**
	 * 根据部门名称查找部门
	 * 
	 * @param name
	 * @throws ServiceException
	 */
	@SuppressWarnings("rawtypes")
	public String searchDepartment(String name) throws ServiceException {
		List baseDatas = departmentDao.searchDepartment(name);
		DepartmentTreeNode dtn = DepartmentTreeNode.makeTree(baseDatas);
		return dtn.getTreeJson(false);
	}

	/**
	 * 检查是否存在同名部门，名称或中文名称
	 * 
	 * @param name
	 * @param type
	 * @return
	 */
	public boolean nameExsistCheck(String name, String type) {
		long size = departmentDao.queryDeptByName(name, type);
		if (size > 0) {
			return true;// 存在
		} else {
			return false;// 不存在
		}
	}

	/**
	 * 更改部门的父部门以及顺序
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
		departmentDao.changeParentAndOrder(params);
		deptRedisDao.updateDeptParentAndOrder(params);
	}

	/**
	 * 加载所有部门与其父部门的关系到redis
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void loadDeptRelationshipToRedis() throws ServiceException {
		List list = departmentDao.findAllDept();
		if (list != null) {
			Map<String, String> map = Maps.newIdentityHashMap();
			for (Object[] o : (List<Object[]>) list) {
				map.put((String) o[0], (String) o[1]);
			}

			if (!map.isEmpty())
				deptRedisDao.initDeptRelation(map);
		}
	}

	/**
	 * 根据该节点Id,得到该节点的所有子节点Id
	 * @param id
	 * @return
	 */
	public String findAllChildrenIdById(String id) {
		List<Department> depts = new ArrayList<Department>();
		depts = departmentDao.findAll();
		List list = new ArrayList();
		list.add(id);
		List childrenIds = getChildrenIds(id,depts,list);
		
		String deptIds = "";
		if(childrenIds != null && childrenIds.size()>0){
			for(int i=0;i<childrenIds.size();i++){
				deptIds += "'"+childrenIds.get(i).toString()+"',";
			}
			deptIds = deptIds.substring(0,deptIds.length()-1);
		}
		return deptIds;
	}

	private List getChildrenIds(String id, List<Department> depts, List list) {
		for(Department o : depts){
			if(id.equals(o.getPid())){
				list.add(o.getId());
				getChildrenIds(o.getId(), depts, list);
			}
		}
		return list;
	}

	public List getDeptInfoByIds(String ids) {
		return departmentDao.getDeptInfoByIds(ids);
	}

	public String findDepartmentHavaNoParent() {
		return departmentDao.findDepartmentHavaNoParent();
	}
}
