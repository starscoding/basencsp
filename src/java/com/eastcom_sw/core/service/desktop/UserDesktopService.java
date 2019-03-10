package com.eastcom_sw.core.service.desktop;

import com.eastcom_sw.common.service.BaseService;
import com.eastcom_sw.common.service.ServiceException;
import com.eastcom_sw.common.utils.DateUtil;
import com.eastcom_sw.common.utils.ParseJSONObject;
import com.eastcom_sw.core.dao.desktop.DesktopResourceDao;
import com.eastcom_sw.core.dao.desktop.UserDesktopDao;
import com.eastcom_sw.core.entity.desktop.UserDesktop;
import com.eastcom_sw.core.entity.security.User;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 用户个人桌面
 * 
 * @author SCM 2012-8-24
 */
@Component
@Transactional(readOnly = true)
public class UserDesktopService extends BaseService {

	@Autowired
	private UserDesktopDao userDesktopDao;
	@Autowired
	private DesktopResourceDao desktopResourceDao;

	@Transactional(readOnly = false)
	public void saveUserDesktop(UserDesktop userDesktop)
			throws ServiceException {
		userDesktopDao.save(userDesktop);
	}

	/**
	 * 获取当前用户个人桌面图标的信息
	 * 
	 * @param userid
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getDesktopResource(String userid) throws ServiceException {
		List list = userDesktopDao.getDesktopResource(userid);
		List<JSONObject> rs = new ArrayList<JSONObject>();
		if (list.size() > 0) {
			List<JSONObject> jsonList = ParseJSONObject
					.parse("deskId,deskOrder,drId,order,floderName,parentId,resId,name,nameCn,image,location",
							list);
			jsonList = sliceUnAuthRes(jsonList);
			LinkedHashMap<String, JSONObject> items = new LinkedHashMap<String, JSONObject>();

			List<JSONObject> floderItems = new ArrayList<JSONObject>();
			/** 由于要排序，先挑选出第一层app，即parentId为空的节点 */
			for (int i = 0; i < jsonList.size(); i++) {
				JSONObject jo = jsonList.get(i);
				String pId = jo.getString("parentId");
				if (StringUtils.isBlank(pId) || "null".equals(pId)) {
					String fn = jo.getString("floderName");
					if (StringUtils.isNotBlank(fn) && !"null".equals(fn)) {
						jo.put("isFloder", true);
						jo.put("nameCn", fn);// 如果是文件夹，将文件夹名复制给nameCn字段
					} else {
						jo.put("isFloder", false);
					}
					items.put(jo.getString("drId"), jo);
				} else {
					jo.put("isFloder", false);
					floderItems.add(jo);
				}
			}

			for (int i = 0; i < floderItems.size(); i++) {
				JSONObject jo = floderItems.get(i);
				JSONObject p = items.get(jo.getString("parentId"));
				JSONArray ja;
				if (p.get("items") == null) {
					ja = new JSONArray();
				} else {
					ja = p.getJSONArray("items");
				}
				ja.add(jo);
				p.put("items", ja);
				items.put(jo.getString("parentId"), p);
			}

			LinkedHashMap<String, JSONObject> desks = new LinkedHashMap<String, JSONObject>();
			for (Iterator<String> it = items.keySet().iterator(); it.hasNext();) {
				JSONObject item = items.get(it.next());
				JSONObject desk = desks.get(item.getString("deskId"));
				if (desk == null) {
					desk = new JSONObject();
					desk.put("id", item.getString("deskId"));
					desk.put("order", item.getInt("deskOrder"));
					JSONArray ja = new JSONArray();
					if (StringUtils.isNotBlank(item.getString("drId"))
							&& !item.getString("drId").equals("null")) {
						ja.add(item);
					}
					desk.put("items", ja);
				} else {
					JSONArray ja = JSONArray.fromObject(desk.get("items"));
					if (StringUtils.isNotBlank(item.getString("drId"))
							&& !item.getString("drId").equals("null")) {
						ja.add(item);
					}
					desk.put("items", ja);
				}
				desks.put(item.getString("deskId"), desk);
			}
			for (Iterator<String> it = desks.keySet().iterator(); it.hasNext();) {
				rs.add(desks.get(it.next()));
			}
		} else {// 首次登陆没有桌面图标信息
			UserDesktop ud = new UserDesktop();
			ud.setCreateTime(DateUtil.getCurrentDatetime());
			ud.setOrder(0);
			User fkUser = new User();
			fkUser.setId(userid);
			ud.setUser(fkUser);
			saveUserDesktop(ud);
			JSONObject jo = new JSONObject();
			jo.put("id", ud.getId());
			jo.put("order", 0);
			jo.put("items", new JSONArray());
			rs.add(jo);
		}
		return rs;
	}

	private List<JSONObject> sliceUnAuthRes(List<JSONObject> list) {
		Subject subject = SecurityUtils.getSubject();
		List<JSONObject> rs = new ArrayList<JSONObject>();
		List<String> idsDel = new ArrayList<String>();
		for (JSONObject jo : list) {
			String name = jo.getString("name");
			if (StringUtils.isNotBlank(name) && !"null".equals(name)) {
				if (subject.isPermitted(name + ":" + name)) {
					rs.add(jo);
				} else {
					idsDel.add(jo.getString("drId"));
				}
			} else {
				rs.add(jo);
			}
		}
		if (idsDel.size() > 0) {
			userDesktopDao.deleteIconFromDesktopBatch(idsDel);
		}
		return rs;
	}

	/**
	 * 删除桌面的图标
	 * 
	 * @return
	 */
	public void deleteIconFromDesktop(String drId) throws ServiceException {
		userDesktopDao.deleteIconFromDesktop(drId);
	}

	/**
	 * 批量更新用户个人桌面图标的序号
	 * 
	 * @param desktopId
	 *            桌面的ID
	 * @param orders
	 *            {菜单：序号} 格式的数组
	 */
	public void sortUserDesktopIcons(String desktopId, String[] orders)
			throws ServiceException {
		desktopResourceDao.sortUserDesktopIcons(desktopId, orders);
	}

	/**
	 * 添加用户屏幕
	 * 
	 * @param userid
	 * @param createTime
	 * @param order
	 * @throws ServiceException
	 */
	public void addUserDesktop(String userid, String createTime, int order)
			throws ServiceException {
		userDesktopDao.addUserDesktop(userid, createTime, order);
	}

	/**
	 * 删除用户屏幕及其图标
	 * 
	 * @param desktopId
	 * @throws ServiceException
	 */
	public void deleteDesktopAndResource(String desktopId)
			throws ServiceException {
		userDesktopDao.deleteDesktopAndResource(desktopId);
	}

	/**
	 * 添加当前屏幕的桌面图标
	 * 
	 * @param desktopId
	 * @param resourceId
	 * @param order
	 * @throws ServiceException
	 */
	public String addDesktopIcon(String desktopId, String resourceId, int order)
			throws ServiceException {
		return desktopResourceDao.addDesktopIcon(desktopId, resourceId, order);
	}

	/**
	 * 序列化屏幕
	 * 
	 * @param serializeIds
	 */
	public void serializeScreen(String[] serializeIds) throws ServiceException {
		userDesktopDao.serializeScreen(serializeIds);
	}

	/**
	 * 更新用户主题
	 * 
	 * @param userId
	 * @param theme
	 */
	public void updateUserTheme(String userId, String theme) {
		userDesktopDao.updateUserTheme(userId, theme);
	}

	/**
	 * 新建文件夹
	 * 
	 * @return
	 */
	public String createFloder(String jsonData) {
		JSONObject jo = JSONObject.fromObject(jsonData);
		return desktopResourceDao.createFloder(jo.getString("name"),
				jo.getString("desktopId"), jo.getString("drId1"),
				jo.getString("drId2"), Integer.parseInt(jo.getString("order")));
	}

	/**
	 * 文件夹重命名
	 * 
	 * @param id
	 * @param name
	 */
	public void renameFloder(String jsonData) {
		JSONObject jo = JSONObject.fromObject(jsonData);
		desktopResourceDao.renameFloder(jo.getString("id"),
				jo.getString("name"));
	}

	/**
	 * 向文件夹添加资源
	 */
	public void addIconToFloder(String jsonData) {
		JSONObject jo = JSONObject.fromObject(jsonData);
		desktopResourceDao.addIconToFloder(jo.getString("drId"),
				jo.getString("fId"), Integer.parseInt(jo.getString("order")));
	}

	public void saveUserShortCut(String userId, String menuId) {
		desktopResourceDao.saveUserShortCut(userId,menuId);
		
	}

	public String findMyShortcut(String userId) {
		return desktopResourceDao.findMyShortcut(userId);
	}

	public void deleteMyShortcut(String userId, String resourceId) {
		desktopResourceDao.deleteMyShortcut(userId,resourceId);
		
	}
}
