package com.eastcom_sw.core.dao.desktop;

import com.eastcom_sw.common.dao.jpa.Dao;
import com.eastcom_sw.core.entity.desktop.DesktopResource;

/**
 * 
 * @author SCM
 * 
 */
public interface DesktopResourceDao extends Dao<DesktopResource> {
	/** 删除资源与个人桌面的级联关系 */
	public void removeDesktopAndResourceRelation(String resourceid);

	public void sortUserDesktopIcons(String desktopId, String[] orders);

	public String addDesktopIcon(String desktopId, String resourceId, int order);

	/**
	 * 新建文件夹
	 * 
	 * @param name
	 * @param desktopId
	 * @param resourceId1
	 * @param resourceId2
	 * @param order
	 */
	public String createFloder(String name, String desktopId, String drId1,
                               String drId2, int order);
	
	/**
	 * 文件夹重命名
	 * 
	 * @param id
	 * @param name
	 */
	public void renameFloder(String id, String name);

	/**
	 * 向文件夹添加资源
	 * 
	 * @param drId
	 * @param fId
	 * @param order
	 */
	public void addIconToFloder(String drId, String fId, int order);

	public void saveUserShortCut(String userId, String menuId);

	public String findMyShortcut(String userId);

	public void deleteMyShortcut(String userId, String resourceId);
}
