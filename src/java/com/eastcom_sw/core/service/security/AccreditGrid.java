package com.eastcom_sw.core.service.security;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 授权列表,用于构造具有选中以及不可编辑选项的列表数据,使用makeAccreditGrid生成对象
 * 
 * @author JJF
 * @Date NOV 22 2012
 */
public class AccreditGrid{
	@SuppressWarnings("unused")
	private Map<String, Object> currentMap;// 当前被授权的对象拥有的权限,KEY值为对象ID，VALUE值为该对象
	@SuppressWarnings("unused")
	private Map<String, Object> allMap;// 当前操作对象拥有的权限,KEY值为对象ID，VALUE值为该对象
	@SuppressWarnings("unused")
	private int compareIndex;//若要按指定字段排序，则调用sort方法的时候传入字段index

	private void setCurrentMap(Map<String, Object> currentMap) {
		this.currentMap = currentMap;
	}

	private void setAllMap(Map<String, Object> allMap) {
		this.allMap = allMap;
	}

	private AccreditGrid() {// 不允许通过构造方法新建对象
	}
	
	/**
	 * resultMap为结果集,KEY值为对象ID+最后1位为对象对象状态，VALUE值为该对象 
	 * 状态描述:
	 * 0: (checked：true，disabled：false) 已选，可操作 
	 * 1：(checked：true，disabled：true)已选，不可操作 
	 * 2: (checked：false，disabled：false)未选，可操作
	 */
	private Map<String, Object> resultMap;
	
	
	public void setResultMap(Map<String, Object> resultMap) {
		this.resultMap = resultMap;
	}

	/**
	 * 获取当前列表json数据list
	 * @param attributeList		对象属性列表，如id,name,nameCn等
	 * @param sortIndex			排序字段index
	 * @return
	 */
	public List<JSONObject> getJsonObjectList(LinkedList<String> attributeList,int sortIndex) {
		List<JSONObject> returnList  = new ArrayList<JSONObject>();
		final String sortKey = attributeList.get(sortIndex);
		for (String key : resultMap.keySet()) {
			Object o = resultMap.get(key);
			JSONArray ja = JSONArray.fromObject(o);
			JSONObject jo =  new JSONObject();
			for(int i = 0;i<attributeList.size();i++){
				jo.put(attributeList.get(i), ja.getString(i));
			}
			int type = Integer.parseInt(key.charAt(key.length()-1) + "");
			switch (type) {//添加checked，disabled属性
			case 0:
				jo.put("checked", true);
				jo.put("disabled", false);
				break;
			case 1:
				jo.put("checked", true);
				jo.put("disabled", true);
				break;
			case 2:
				jo.put("checked", false);
				jo.put("disabled", false);
				break;
			}
			Collections.sort(returnList,new Comparator<JSONObject>(){  
	            public int compare(JSONObject arg0, JSONObject arg1) {
	                return arg0.getString(sortKey).compareTo(arg1.getString(sortKey));  
	            }
	        });
			returnList.add(jo);
		}
		
		return returnList;
	}
	
	/**
	 * 获取当前列表json数据Map
	 * @param attributeList		对象属性列表，如id,name,nameCn等
	 * @param sortIndex			排序字段index
	 * @return
	 */
	public Map<String, JSONObject> getJsonObjectMap(LinkedList<String> attributeList,int sortIndex) {
		List<JSONObject> list = getJsonObjectList(attributeList,sortIndex);
		Map<String, JSONObject> returnMap = new HashMap<String, JSONObject>();
		for(JSONObject jo : list){
			returnMap.put(jo.getString(attributeList.get(0)), jo);
		}
		return returnMap;
	}
	
	
	/**
	 * 生成一个授权列表
	 * 
	 * @param cMap
	 *            当前被授权的对象拥有的权限,KEY值为对象ID，VALUE值为该对象
	 * @param aMap
	 *            当前操作对象拥有的权限,KEY值为对象ID，VALUE值为该对象
	 * @return
	 */
	public static AccreditGrid makeAccreditGrid(Map<String, Object> cMap,
			Map<String, Object> aMap) {
		AccreditGrid ag = new AccreditGrid();
		ag.setAllMap(aMap);
		ag.setCurrentMap(cMap);
		Map<String, Object> result = new HashMap<String, Object>();
		for (String key : aMap.keySet()) {
			if (cMap.get(key) != null) {
				result.put(key + "0", aMap.get(key));
			} else {
				result.put(key + "2", aMap.get(key));
			}
		}
		for (String key : cMap.keySet()) {
			if (result.get(key+"0") == null && result.get(key+"2") == null) {
				result.put(key + "1", cMap.get(key));
			}
		}
		ag.setResultMap(result);
		return ag;
	}
}
