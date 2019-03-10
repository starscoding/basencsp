package com.eastcom_sw.core.utils.security;

import java.util.HashSet;
import java.util.Set;

import com.eastcom_sw.core.dao.security.ResourceObject;
import com.eastcom_sw.core.entity.security.Resource;

/**
 * 生成资源对象
 * 
 * @author JJF
 * @date APR 28 2013
 * 
 */
public class GenerateResource {
	@SuppressWarnings("unused")
	public static Resource generate(Set<ResourceObject> totalRs, String rsName) {
		ResourceObject parent = new ResourceObject();

		for (ResourceObject rs : totalRs) {
			if (rs.getName().equals(rsName)) {
				parent = rs;
				break;
			}
		}

		Resource r = parseFromBaseInfo(parent);

		if (parent == null) {
			String statusInfo = "用户权限资源列表中不存在名称为：" + rsName + "的资源!";
			System.out.println(statusInfo);
		} else {
			Set<Resource> me = new HashSet<Resource>();
			me.add(r);
			Set<Resource> tp = ganerateChildren(me, totalRs);
			for (Resource p : tp) {// 只有一条
				r = p;
			}
		}
		return r;
	}

	private static Set<Resource> ganerateChildren(Set<Resource> parents,
			Set<ResourceObject> totalRs) {
		for (Resource p : parents) {
			String pId = p.getId();
			Set<ResourceObject> _leftRs = new HashSet<ResourceObject>();
			Set<Resource> _childNodes = new HashSet<Resource>();
			for (ResourceObject rs : totalRs) {
				if (rs.getPid().equals(pId)) {
					_childNodes.add(parseFromBaseInfo(rs));
				} else {
					_leftRs.add(rs);
				}
			}
			if (_childNodes.size() > 0) {
				ganerateChildren(_childNodes, _leftRs);
			}
			p.setChildResources(_childNodes);
		}
		return parents;
	}

	/**
	 * 从基本信息生成resource对象
	 * 
	 * @param ro
	 * @return
	 */
	private static Resource parseFromBaseInfo(ResourceObject ro) {
		Resource r = new Resource();
		r.setId(ro.getId());
		r.setName(ro.getName());
		r.setNameCn(ro.getNameCn());
		r.setLocation(ro.getLocation());
		r.setStatus(ro.getStatus());
		r.setOrder(ro.getOrder());
		r.setKind(ro.getKind());
		r.setIsShowDesktop(ro.getIsShowDesktop());
		r.setIsWebpage(ro.getIsWebpage());
		r.setImage(ro.getImage());
		return r;
	}
}
