/**
 * 
 */
package com.eastcom_sw.core.dao.desktop;

import com.eastcom_sw.common.dao.jpa.DaoSupport;
import com.eastcom_sw.common.dao.jpa.MyBatchPreparedStatementSetter;
import com.eastcom_sw.common.utils.RandomGUID;
import com.eastcom_sw.core.entity.desktop.UserDesktop;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author SCM
 * 
 */
@Component
public class UserDesktopDaoImpl extends DaoSupport<UserDesktop> implements
        UserDesktopDao {

	@PersistenceContext(unitName = "defaultPU")
	public EntityManager em;

	private JdbcTemplate jdbcTemplate;

	@Resource(name = "dataSource")
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/**
	 * 获取当前用户的个人桌面数据
	 * 
	 * @param userid
	 *            用户ID
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List getDesktopResource(String userid) {
		String sql = "SELECT UD.ID_ AS DESKTOPID,UD.ORDER_ DESKTOPORDER,DR.ID_,DR.ORDER_,DR.NAME_ AS FLODER_NAME,DR.PARENT_ID,R.ID_ RESOURCE_ID,R.NAME_,R.NAME_CN,R.IMAGE_,R.LOCATION_";
		sql += " FROM SYS_USERDESKTOP UD LEFT JOIN SYS_DESKTOP_RESOURCE DR ON DR.DESKTOP_ID = UD.ID_";
		sql += " LEFT JOIN SYS_RESCOURCE R ON R.ID_ = DR.RESOURCE_ID";
		sql += " WHERE UD.USER_ID = ? ORDER BY UD.ORDER_,DR.ORDER_";
		return getEm().createNativeQuery(sql).setParameter(1, userid)
				.getResultList();
	}

	/**
	 * 删除桌面的图标
	 * 
	 * @return
	 */
	public void deleteIconFromDesktop(String drId) {
		String sql = "DELETE FROM SYS_DESKTOP_RESOURCE WHERE ID_ = ? OR PARENT_ID = ?";
		getEm().createNativeQuery(sql).setParameter(1, drId)
				.setParameter(2, drId).executeUpdate();
	}

	/**
	 * 批量删除桌面的图标(只是删除单独资源，不会删除文件夹)
	 * 
	 * @return
	 */
	public void deleteIconFromDesktopBatch(List<String> drIds) {
		StringBuffer condition = new StringBuffer();
		for (int i = 0; i < drIds.size(); i++) {
			condition.append("?,");
		}
		if (condition.length() > 0) {
			condition.deleteCharAt(condition.length() - 1);
		}
		String sql = "DELETE FROM SYS_DESKTOP_RESOURCE WHERE ID_ IN("
				+ condition.toString() + ")";
		Query q = getEm().createNativeQuery(sql);
		for (int i = 0; i < drIds.size(); i++) {
			q.setParameter(i + 1, drIds.get(i));
		}
		q.executeUpdate();
	}

	/**
	 * 添加用户桌面
	 * 
	 * @param userid
	 *            用户ID
	 * @param createTime
	 *            创建时间
	 * @param order
	 *            序号
	 */
	public void addUserDesktop(String userid, String createTime, int order) {
		RandomGUID id = new RandomGUID();
		String sql = "INSERT INTO SYS_USERDESKTOP(ID_,CREATE_TIME,ORDER_,USER_ID) VALUES(?,?,?,?)";
		getEm().createNativeQuery(sql).setParameter(1, id)
				.setParameter(2, createTime).setParameter(3, order)
				.setParameter(4, userid).executeUpdate();
	}

	/**
	 * 删除桌面及其相关的快捷图标
	 * 
	 * @param desktopId
	 *            桌面的ID
	 */
	public void deleteDesktopAndResource(String desktopId) {
		String sql = "DELETE FROM SYS_USERDESKTOP WHERE ID_ = ?";
		String sql2 = "DELETE FROM SYS_DESKTOP_RESOURCE WHERE DESKTOP_ID = ?";

		getEm().createNativeQuery(sql2).setParameter(1, desktopId)
				.executeUpdate();
		getEm().createNativeQuery(sql).setParameter(1, desktopId)
				.executeUpdate();
	}

	/**
	 * 序列化屏幕
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void serializeScreen(String[] serializeIds) {
		String sql = "UPDATE SYS_USERDESKTOP SET ORDER_ = ? WHERE ID_ = ?";
		List params = new ArrayList();
		for (String vl : serializeIds) {
			String[] ary = vl.split(":");
			String[][] p = new String[][] { { "int", ary[1] },
					{ "string", ary[0] } };
			params.add(p);
		}
		MyBatchPreparedStatementSetter setter = new MyBatchPreparedStatementSetter(
				params);
		jdbcTemplate.batchUpdate(sql, setter);
	}

	/**
	 * 更新用户主题
	 * 
	 * @param userId
	 * @param theme
	 */
	public void updateUserTheme(String userId, String theme) {
		String sql = "UPDATE SYS_USER SET XTHEME_= ? WHERE ID_ = ?";
		Query q = getEm().createNativeQuery(sql).setParameter(1, theme)
				.setParameter(2, userId);
		q.executeUpdate();
	}
}
