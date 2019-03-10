package com.eastcom_sw.core.utils.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;

/**
 * 部门树节点以及子部门(也可用于其他具有相同字段的实体) 单条数据顺序
 * id,desc,name,nameCn,order,parentId(不需要可传入空值)
 * 
 * @author JJF
 * @date OCT 17 2012
 */
public class DepartmentTreeNode implements Comparable<DepartmentTreeNode> {
	private String id;// 当前节点部门id
	private String name;// 部门名称
	private String nameCn;// 部门中文名
	private String order = "0";// 排序号
	private String desc;// 描述
	private int nodeStatus = 2; // 0: (checked：true，disabled：false) 已选，可操作
								// 1： (checked：true，disabled：true) 已选，不可操作
								// 2: (checked：false，disabled：false) 未选，可操作
	private LinkedHashSet<DepartmentTreeNode> childNodes;// 子部门节点集合

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNameCn() {
		return nameCn;
	}

	public void setNameCn(String nameCn) {
		this.nameCn = nameCn;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNodeStatus() {
		return nodeStatus;
	}

	public void setNodeStatus(int nodeStatus) {
		this.nodeStatus = nodeStatus;
	}

	public LinkedHashSet<DepartmentTreeNode> getChildNodes() {
		return childNodes;
	}

	public void setChildNodes(LinkedHashSet<DepartmentTreeNode> childNodes) {
		this.childNodes = childNodes;
	}

	// /**
	// * 根据order排序
	// */
	// private LinkedHashSet<DepartmentTreeNode> sortTree(DepartmentTreeNode
	// dtn) {
	// LinkedHashSet<DepartmentTreeNode> sortedChilds = new
	// LinkedHashSet<DepartmentTreeNode>();
	// if (dtn.getChildNodes().size() > 0) {
	// Object[] childsArray = dtn.getChildNodes().toArray();
	// Arrays.sort(childsArray);
	// for (int i = 0; i < childsArray.length; i++) {
	// sortedChilds.add((DepartmentTreeNode) childsArray[i]);
	// }
	// }
	// return sortedChilds;
	// }

	/**
	 * 获取json字符串(若为普通树，则使用该方法,节点不带disabled属性)
	 * 
	 * @return
	 */
	public String getTreeJson(boolean checkedFlag) {
		return getChildJson(this, checkedFlag, false);
	}

	/**
	 * 获取部门授权树json字符串(若为授权树，则使用该方法,节点带disabled属性)
	 * 
	 * @return
	 */
	public String getAccreditTreeJson() {
		this.sort();
		return getChildJson(this, true, true);
	}

	private void sort() {
		LinkedHashSet<DepartmentTreeNode> childs = this.getChildNodes();
		this.setChildNodes(sort(childs));
		for (DepartmentTreeNode dtn : childs) {
			if (dtn.getChildNodes().size() > 0) {
				dtn.sort();
			}
		}
	}

	private LinkedHashSet<DepartmentTreeNode> sort(
			LinkedHashSet<DepartmentTreeNode> list) {
		LinkedHashSet<DepartmentTreeNode> rs = new LinkedHashSet<DepartmentTreeNode>();
		Object[] ls = list.toArray();
		Arrays.sort(ls, new Comparator<Object>() {
			public int compare(Object o1, Object o2) {
				DepartmentTreeNode d1 = (DepartmentTreeNode) o1;
				DepartmentTreeNode d2 = (DepartmentTreeNode) o2;
				int or1 = Integer.parseInt(d1.getOrder());
				int or2 = Integer.parseInt(d2.getOrder());
				if (or1 == or2) {
					return 0;
				} else if (or1 > or2) {
					return 1;
				} else {
					return -1;
				}
			}
		});
		for (Object o : ls) {
			DepartmentTreeNode dtn = (DepartmentTreeNode) o;
			rs.add(dtn);
		}
		return rs;
	}

	/**
	 * 构造子部门json字符串
	 * 
	 * @param dtn
	 * @return
	 */
	private String getChildJson(DepartmentTreeNode dtn, boolean checkedFlag,
			boolean addDisabled) {
		String jsonStr = "[";
		LinkedHashSet<DepartmentTreeNode> childs = dtn.getChildNodes();
		// LinkedHashSet<DepartmentTreeNode> childs = sortTree(dtn);
		// dtn.getChildNodes();
		for (DepartmentTreeNode child : childs) {
			jsonStr += "{";
			jsonStr += "'id':'" + child.getId() + "',";
			jsonStr += "'name':'" + child.getName() + "',";
			jsonStr += "'nameCn':'" + child.getNameCn() + "',";
			jsonStr += "'text':'" + child.getNameCn() + "',";
			jsonStr += "'order':'" + child.getOrder() + "',";
			if (addDisabled) {
				switch (child.getNodeStatus()) {
				case 0:
					jsonStr += "'checked':true,";
					jsonStr += "'disabled':false,";
					break;
				case 1:
					jsonStr += "'checked':true,";
					jsonStr += "'disabled':true,";
					break;
				case 2:
					jsonStr += "'checked':false,";
					jsonStr += "'disabled':false,";
					break;
				}
			} else {
				if (checkedFlag) {
					jsonStr += "'checked':false,";
				}
			}
			jsonStr += "'desc':'" + child.getDesc() + "'";
			if (child.getChildNodes().size() > 0) {
				jsonStr += ",'children':";
				jsonStr += getChildJson(child, checkedFlag, addDisabled);
			} else {
				jsonStr += ",'leaf':'true'";
			}
			jsonStr += "},";
		}
		jsonStr = jsonStr.substring(0, jsonStr.length() - 1);
		jsonStr += "]";
		return jsonStr;
	}

	/**
	 * 根据数据库查询结果部门信息集合构造整个部门树
	 * 
	 * @param resultList
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static DepartmentTreeNode makeTree(List resultList) {
		LinkedHashSet<DepartmentTreeNode> parents = new LinkedHashSet<DepartmentTreeNode>();
		List<String> deptIds = getDeptIds(resultList);
		for (Object o : resultList) {
			JSONArray ja = JSONArray.fromObject(o);
			String parentId = ja.getString(5);
			if (parentId == null || parentId.isEmpty()
					|| deptIds.contains(parentId) == false) {
				DepartmentTreeNode dtn = new DepartmentTreeNode();
				dtn.setId(ja.getString(0));
				dtn.setDesc(ja.getString(1));
				dtn.setName(ja.getString(2));
				dtn.setNameCn(ja.getString(3));
				dtn.setOrder(ja.getString(4));
				parents.add(dtn);
				// resultList.remove(o);
			}
		}
		parents = findChilds(parents, resultList);
		DepartmentTreeNode root = new DepartmentTreeNode();
		root.setId("root");
		root.setName("root");
		root.setNameCn("所有");
		root.setOrder("0");
		root.setDesc("根目录");
		root.setChildNodes(parents);
		return root;
	}

	/**
	 * 构造用户授予权限部门树(单条记录中的数据顺序为id,desc,name,nameCn,order,parentId)
	 * 
	 * @param userDeptList
	 *            当前需要编辑的用户所拥有的权限
	 * @param myDeptList
	 *            当前登录用户所拥有的权限
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static DepartmentTreeNode makeAccreditTree(List userDeptList,
			List myDeptList) {
		Map<String, Map<String, String>> deptIdsMap = getAccreditDeptIds(
				userDeptList, myDeptList);
		Map<String, String> ttIds = deptIdsMap.get("tt");
		Map<String, String> cdIds = deptIdsMap.get("cd");
		Map<String, String> ceIds = deptIdsMap.get("ce");
		Map<String, String> ueIds = deptIdsMap.get("ue");
		List totalList = userDeptList;
		totalList.addAll(myDeptList);
		// totalList = new ArrayList<Object>(new HashSet<Object>(totalList));
		totalList = removeDuplicateById(totalList);
		LinkedHashSet<DepartmentTreeNode> parents = new LinkedHashSet<DepartmentTreeNode>();
		for (Object o : totalList) {
			JSONArray ja = JSONArray.fromObject(o);
			String parentId = ja.getString(5);
			if (parentId == null || parentId.isEmpty()
					|| ttIds.get(parentId) == null) {
				DepartmentTreeNode dtn = new DepartmentTreeNode();
				dtn.setId(ja.getString(0));
				dtn.setDesc(ja.getString(1));
				dtn.setName(ja.getString(2));
				dtn.setNameCn(ja.getString(3));
				dtn.setOrder(ja.getString(4));
				if (cdIds.get(ja.getString(0)) != null) {
					dtn.setNodeStatus(1);
				} else if (ceIds.get(ja.getString(0)) != null) {
					dtn.setNodeStatus(0);
				} else if (ueIds.get(ja.getString(0)) != null) {
					dtn.setNodeStatus(2);
				}
				parents.add(dtn);
			}
		}
		parents = findAccreditChilds(parents, totalList, cdIds, ceIds, ueIds);
		DepartmentTreeNode root = new DepartmentTreeNode();
		root.setId("root");
		root.setName("root");
		root.setNameCn("所有部门");
		root.setOrder("0");
		root.setDesc("根目录");
		root.setChildNodes(parents);
		return root;
	}

	/**
	 * 构造子部门树
	 * 
	 * @param parents
	 * @param resultList
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static LinkedHashSet<DepartmentTreeNode> findChilds(
			LinkedHashSet<DepartmentTreeNode> parents, List resultList) {
		for (DepartmentTreeNode pdtn : parents) {
			String pId = pdtn.getId();
			LinkedHashSet<DepartmentTreeNode> childs = new LinkedHashSet<DepartmentTreeNode>();
			for (Object o : resultList) {
				JSONArray ja = JSONArray.fromObject(o);
				String parentId = ja.getString(5);
				if (pId.equals(parentId)) {
					DepartmentTreeNode dtn = new DepartmentTreeNode();
					dtn.setId(ja.getString(0));
					dtn.setDesc(ja.getString(1));
					dtn.setName(ja.getString(2));
					dtn.setNameCn(ja.getString(3));
					dtn.setOrder(ja.getString(4));
					childs.add(dtn);
					// resultList.remove(o);
				}
			}
			if (childs.size() > 0) {// 循环查找子节点
				findChilds(childs, resultList);
			}
			pdtn.setChildNodes(childs);
		}
		return parents;
	}

	/**
	 * 构造部门授权子部门树
	 * 
	 * @param parents
	 * @param resultList
	 * @param cdIds
	 * @param ceIds
	 * @param ueIds
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static LinkedHashSet<DepartmentTreeNode> findAccreditChilds(
			LinkedHashSet<DepartmentTreeNode> parents, List resultList,
			Map<String, String> cdIds, Map<String, String> ceIds,
			Map<String, String> ueIds) {
		for (DepartmentTreeNode pdtn : parents) {
			String pId = pdtn.getId();
			LinkedHashSet<DepartmentTreeNode> childs = new LinkedHashSet<DepartmentTreeNode>();
			for (Object o : resultList) {
				JSONArray ja = JSONArray.fromObject(o);
				String parentId = ja.getString(5);
				if (pId.equals(parentId)) {
					DepartmentTreeNode dtn = new DepartmentTreeNode();
					dtn.setId(ja.getString(0));
					dtn.setDesc(ja.getString(1));
					dtn.setName(ja.getString(2));
					dtn.setNameCn(ja.getString(3));
					dtn.setOrder(ja.getString(4));
					if (cdIds.get(ja.getString(0)) != null) {
						dtn.setNodeStatus(1);
					} else if (ceIds.get(ja.getString(0)) != null) {
						dtn.setNodeStatus(0);
					} else if (ueIds.get(ja.getString(0)) != null) {
						dtn.setNodeStatus(2);
					}
					childs.add(dtn);
				}
			}
			if (childs.size() > 0) {// 循环查找子节点
				findAccreditChilds(childs, resultList, cdIds, ceIds, ueIds);
			}
			pdtn.setChildNodes(childs);
		}
		return parents;
	}

	/**
	 * 获取所有部门的id集合
	 * 
	 * @param deptList
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static List<String> getDeptIds(List deptList) {
		List<String> deptIds = new ArrayList<String>();
		for (Object o : deptList) {
			JSONArray ja = JSONArray.fromObject(o);
			deptIds.add((String) ja.get(0));
		}
		return deptIds;
	}

	/**
	 * 获取授权部门id集合(此处返回的集合是为了遍历时判断是否存在用，故使用Map返回提高性能)
	 * 
	 * @param userDeptList
	 *            当前需要编辑的用户所拥有的权限
	 * @param myDeptList
	 *            当前登录用户所拥有的权限
	 * 
	 * @return set : key="cd":已选，不可操作 的deptList key="ce":已选，可操作 的deptList
	 *         key="ue":未选，可操作 的deptList key="tt":所有deptList
	 */
	@SuppressWarnings("rawtypes")
	private static Map<String, Map<String, String>> getAccreditDeptIds(
			List userDeptList, List myDeptList) {
		List<String> userDeptIds = new ArrayList<String>();// 所编辑的用户拥有权限的部门id集合
		List<String> myDeptIds = new ArrayList<String>();// 当前登录用户拥有权限部门id集合
		// List<String> ttIds = new ArrayList<String>();
		// List<String> cdIds = new ArrayList<String>();
		Map<String, String> ttIds = new HashMap<String, String>();
		Map<String, String> cdIds = new HashMap<String, String>();
		Map<String, String> ceIds = new HashMap<String, String>();
		Map<String, String> ueIds = new HashMap<String, String>();
		// List<String> ceIds = new ArrayList<String>();
		// List<String> ueIds = new ArrayList<String>();
		for (Object o : userDeptList) {// userDeptIds赋值
			JSONArray ja = JSONArray.fromObject(o);
			userDeptIds.add((String) ja.get(0));
		}
		for (Object o : myDeptList) {// myDeptIds赋值
			JSONArray ja = JSONArray.fromObject(o);
			myDeptIds.add((String) ja.get(0));
		}

		for (String id : userDeptIds) {
			if (myDeptIds.contains(id)) {// myDeptIds存在这个id则表示应该是已选择可编辑节点
				// ceIds.add(id);
				ceIds.put(id, "exsist");
				myDeptIds.remove(id);
			} else {// 不存在则为已选择不可编辑节点
				// cdIds.add(id);
				cdIds.put(id, "exsist");
			}
			ttIds.put(id, "exsist");
		}
		for (String id : myDeptIds) {// 将余下的myDeptIds（未选择，可编辑）已选择的已经被移除出list
			// ueIds.add(id);
			ueIds.put(id, "exsist");
			// ttIds.add(id);
			ttIds.put(id, "exsist");
		}
		Map<String, Map<String, String>> returnMap = new HashMap<String, Map<String, String>>();
		returnMap.put("tt", ttIds);
		returnMap.put("cd", cdIds);
		returnMap.put("ce", ceIds);
		returnMap.put("ue", ueIds);
		return returnMap;
	}

	/**
	 * 去重
	 * 
	 * @param totalList
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List removeDuplicateById(List totalList) {
		Set<String> idSet = new HashSet<String>();
		List newList = new ArrayList();
		for (Object o : totalList) {
			JSONArray ja = JSONArray.fromObject(o);
			if (idSet.add(ja.getString(0))) {
				newList.add(o);
			}
		}
		return newList;
	}

	@Override
	public int compareTo(DepartmentTreeNode o) {
		int order1 = Integer.parseInt(this.getOrder());
		int order2 = Integer.parseInt(o.getOrder());
		if (order1 > order2) {
			return 1;
		} else {
			return 0;
		}
	}
}
