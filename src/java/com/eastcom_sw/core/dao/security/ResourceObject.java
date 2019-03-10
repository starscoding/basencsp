/**
 * 用来数据格式转换（redis）
 */
package com.eastcom_sw.core.dao.security;

import java.util.UUID;

public class ResourceObject {

	private String id;
	private String pid; // 上级权限ID
	private String name; // 权限资源名称
	private String nameCn; // 权限资源中文名称
	private String location; // 权限资源链接地址
	private String status = "1"; // 菜单状态
	private int order = 0; // 顺序
	private String kind = "0"; // 权限资源类型 1:操作权限
	private String isShowDesktop = "0"; // 显示在个人桌面上
	private String isWebpage = "0"; // 是否是网页 0：否 1：是
	private String image; // 图片路径

	public ResourceObject() {
	}

	public ResourceObject(String id, String name, String pid) {
		this.setId(id);
		this.setName(name);
		this.setPid(pid);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameCn() {
		return nameCn;
	}

	public void setNameCn(String nameCn) {
		this.nameCn = nameCn;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getIsShowDesktop() {
		return isShowDesktop;
	}

	public void setIsShowDesktop(String isShowDesktop) {
		this.isShowDesktop = isShowDesktop;
	}

	public String getIsWebpage() {
		return isWebpage;
	}

	public void setIsWebpage(String isWebpage) {
		this.isWebpage = isWebpage;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Override
	public boolean equals(Object arg0) {
		if (arg0.getClass() == ResourceObject.class) {
			ResourceObject obj = (ResourceObject) arg0;
			if (obj.getId().equals(this.getId())) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		UUID uuid = UUID.nameUUIDFromBytes(this.id.getBytes());
		return uuid.hashCode();
	}
}
