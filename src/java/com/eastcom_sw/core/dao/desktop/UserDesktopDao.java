/**
 * 
 */
package com.eastcom_sw.core.dao.desktop;

import com.eastcom_sw.common.dao.jpa.Dao;
import com.eastcom_sw.core.entity.desktop.UserDesktop;

import java.util.List;

/**
 * @author SCM
 * 
 */
public interface UserDesktopDao extends Dao<UserDesktop> {

	@SuppressWarnings("rawtypes")
	public List getDesktopResource(String userid);

	/**
	 * 删除桌面的图标
	 * 
	 * @return
	 */
	public void deleteIconFromDesktop(String drId);

	/**
	 * 批量删除桌面的图标(只是删除单独资源，不会删除文件夹)
	 * 
	 * @return
	 */
	public void deleteIconFromDesktopBatch(List<String> drIds);

	public void addUserDesktop(String userid, String createTime, int order);

	public void deleteDesktopAndResource(String desktopId);

	public void serializeScreen(String[] serializeIds);

	/**
	 * 更新用户主题
	 * 
	 * @param userId
	 * @param theme
	 */
	public void updateUserTheme(String userId, String theme);
}
