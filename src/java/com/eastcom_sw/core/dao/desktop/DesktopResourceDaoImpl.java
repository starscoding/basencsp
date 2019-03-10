package com.eastcom_sw.core.dao.desktop;

import com.eastcom_sw.common.dao.jpa.DaoSupport;
import com.eastcom_sw.common.dao.jpa.MyBatchPreparedStatementSetter;
import com.eastcom_sw.common.utils.RandomGUID;
import com.eastcom_sw.core.entity.desktop.DesktopResource;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author SCM
 * 
 */
@Component
public class DesktopResourceDaoImpl extends DaoSupport<DesktopResource>
		implements DesktopResourceDao {

	@PersistenceContext(unitName = "defaultPU")
	public EntityManager em;

	private JdbcTemplate jdbcTemplate;

	@Resource(name = "dataSource")
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/**
	 * 删除资源菜单与个人桌面的级联关系
	 * 
	 * @param resourceid
	 */
	public void removeDesktopAndResourceRelation(String resourceid) {
		String sql = "delete from sys_desktop_resource  where resource_id = ?";
		getEm().createNativeQuery(sql).setParameter(1, resourceid)
				.executeUpdate();
	}

	/**
	 * 批量更新桌面的序号
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void sortUserDesktopIcons(String desktopId, String[] orders) {
		String sql = "UPDATE SYS_DESKTOP_RESOURCE SET ORDER_ = ? WHERE ID_ = ? AND DESKTOP_ID = ?";
		List params = new ArrayList();
		for (String vl : orders) {
			String[] ary = vl.split(":");
			String[][] p = new String[][] { { "int", ary[1] },
					{ "string", ary[0] }, { "string", desktopId } };
			params.add(p);
		}
		MyBatchPreparedStatementSetter setter = new MyBatchPreparedStatementSetter(
				params);
		jdbcTemplate.batchUpdate(sql, setter);
	}

	/**
	 * 添加当前屏幕的桌面图标
	 */
	public String addDesktopIcon(String desktopId, String resourceId, int order) {
		String id = new RandomGUID().getUUID32();
		String sql = "INSERT INTO SYS_DESKTOP_RESOURCE( ORDER_,RESOURCE_ID,DESKTOP_ID,ID_)VALUES(?,?,?,?)";
		getEm().createNativeQuery(sql).setParameter(1, order)
				.setParameter(2, resourceId).setParameter(3, desktopId)
				.setParameter(4, id).executeUpdate();
		return id;
	}

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
                               String drId2, int order) {
		RandomGUID r = new RandomGUID();
		String fId = r.getUUID32();
		String sql = "INSERT INTO SYS_DESKTOP_RESOURCE(ORDER_,RESOURCE_ID,DESKTOP_ID,ID_,NAME_,PARENT_ID)VALUES(?,?,?,?,?,?)";

		getEm().createNativeQuery(sql).setParameter(1, order)
				.setParameter(2, "").setParameter(3, desktopId)
				.setParameter(4, fId).setParameter(5, name).setParameter(6, "")
				.executeUpdate();

		addIconToFloder(drId1, fId, 0);
		addIconToFloder(drId2, fId, 1);

		return fId;
	}

	/**
	 * 文件夹重命名
	 * 
	 * @param id
	 * @param name
	 */
	public void renameFloder(String id, String name) {
		String sql = "UPDATE SYS_DESKTOP_RESOURCE SET NAME_ = ? WHERE ID_=?";
		getEm().createNativeQuery(sql).setParameter(1, name)
				.setParameter(2, id).executeUpdate();
	}

	/**
	 * 向文件夹添加资源
	 * 
	 * @param drId
	 * @param fId
	 * @param order
	 */
	public void addIconToFloder(String drId, String fId, int order) {
		String sql = "UPDATE SYS_DESKTOP_RESOURCE SET PARENT_ID = ?,ORDER_=? WHERE ID_=?";
		getEm().createNativeQuery(sql).setParameter(1, fId)
				.setParameter(2, order).setParameter(3, drId).executeUpdate();
	}
	
	public void saveUserShortCut(String userId, String menuId){
		String sql = "INSERT INTO SYS_USER_SHORTCUT(USER_ID,RESOURCE_ID) VALUES(?,?)";
		getEm().createNativeQuery(sql).setParameter(1, userId).setParameter(2, menuId).executeUpdate();
	}
	
	public String findMyShortcut(String userId){
		JSONArray records = new JSONArray();
		StringBuffer sb = new StringBuffer();
		List<JSONObject> js = new ArrayList<JSONObject>();
		sb.append("SELECT SR.ID_,SR.PARENT_ID,SR.NAME_,SR.NAME_CN,SR.LOCATION_,SR.STATUS_, ");
		sb.append("SR.ORDER_,SR.KIND_,SR.IS_SHOWDESKTOP,SR.IS_WEBPAGE,SR.IMAGE_ ");
		sb.append("FROM (SELECT RESOURCE_ID FROM SYS_USER_SHORTCUT WHERE USER_ID = ? ) SUS ");
		sb.append("LEFT JOIN SYS_RESCOURCE SR ON SUS.RESOURCE_ID = SR.ID_ ");
		List list = getEm().createNativeQuery(sb.toString()).setParameter(1, userId).getResultList();
		if(list != null && list.size()>0){
			for(int i=0;i<list.size();i++){
				JSONObject jo = new JSONObject();
				Object[] o = (Object[])list.get(i);
				Object[] os = new Object[]{};
				jo.put("id", o[0]);
				jo.put("name", o[2]);
				jo.put("nameCn", o[3]);
				jo.put("leaf", true);
				records.add(jo);
			}
		}
		return records.toString();
	}
	
	public void deleteMyShortcut(String userId, String resourceId){
		String sql = "delete from sys_user_shortcut where user_id = ? and resource_id = ?";
		getEm().createNativeQuery(sql).setParameter(1, userId).setParameter(2, resourceId)
				.executeUpdate();
	}
}
