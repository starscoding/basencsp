/**
 * 
 */
package com.eastcom_sw.core.service.security;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.eastcom_sw.common.service.BaseService;
import com.eastcom_sw.common.service.ServiceException;
import com.eastcom_sw.core.dao.security.MenuVisitedRedisDao;
import com.eastcom_sw.core.dao.security.OnlineUserRedisDao;

/**
 * @author SCM 2012-11-5
 */
@Component
@Transactional(readOnly = true)
public class MenuVisitedService extends BaseService {

	@Autowired
	private MenuVisitedRedisDao menuRedisDao;
	@Autowired
	private OnlineUserRedisDao onlineUserRedisDao;

	/**
	 * 存储用户访问菜单的次数
	 * 
	 * @param userId
	 * @param menuId
	 */
	public boolean addUserMenuVisitTime(String userId, String menuId)
			throws ServiceException {
		return menuRedisDao.addUserCommonlyuserdMenu(userId, menuId);
	}

	/**
	 * 获取用户访问菜单量的topn
	 * 
	 * @param userId
	 * @param topn
	 * @return
	 * @throws ServiceException
	 */
	public Set<String> getUserVisitedTopN(String userId, int topn)
			throws ServiceException {
		return menuRedisDao.getUserVisitedTopN(userId, topn);
	}

	/**
	 * 从用户常用菜单中删除某菜单
	 * 
	 * @param userId
	 * @param menuId
	 * @throws ServiceException
	 */
	public void delMenuFromUsermenus(String userId, String menuId)
			throws ServiceException {
		menuRedisDao.delMenuFromUsermenus(userId, menuId);
	}

	/**
	 * 组装用户访问远程模块随机码信息
	 * 
	 * @param menuId
	 * @param userName
	 * @return
	 */
	public Map<String, String> getRandomUUIDForMenuVisited(String userName,
			String sessionId, String menuId, String projectName)
			throws ServiceException {
		Map<String, String> params = new HashMap<String, String>();
		String randomId = menuRedisDao.logRandomUUIDForMenuVisited(menuId,
				userName);
		String pAddress = getCommonFieldValueByName(projectName, "value");
		params.put("random", randomId);
		params.put("ip", pAddress);
		params.put("sessionid", sessionId);
		return params;
	}

	/**
	 * 删除用户访问页面生成的随机码
	 * 
	 * @param username
	 * @param pageId
	 */
	public void deleteRandomByPageId(String username, String pageId) {
		menuRedisDao.deleteRandomByPageId(username, pageId);
	}
}
