package com.ucan.entity;

import java.io.Serializable;

/**
 * @Description: 职位实体类
 * @author liming.cen
 * @date 2023年2月10日 下午6:33:17
 */
public class Post implements Serializable {
    private static final long serialVersionUID = -1256403720282748631L;
    /**
     * 职位ID
     */
    private String postId;
    /**
     * 职位名称
     */
    private String postName;
    /**
     * 职位编码
     */
    private String postCode;
    /**
     * 上级职位ID
     */
    private String parentId;
    /**
     * 父节点名称
     */
    private String parentName;
    /**
     * 所属组织ID
     */
    private String orgId;
    /**
     * 职位字体图标
     */
    private String icon;
    /**
     * 节点位置，最小值1，越小越靠前
     */
    private Integer position;

    public String getPostId() {
	return postId;
    }

    public void setPostId(String postId) {
	this.postId = postId;
    }

    public String getPostName() {
	return postName;
    }

    public void setPostName(String postName) {
	this.postName = postName;
    }

    public String getPostCode() {
	return postCode;
    }

    public void setPostCode(String postCode) {
	this.postCode = postCode;
    }

    public String getParentId() {
	return parentId;
    }

    public void setParentId(String parentId) {
	this.parentId = parentId;
    }

    public String getOrgId() {
	return orgId;
    }

    public void setOrgId(String orgId) {
	this.orgId = orgId;
    }

    public String getIcon() {
	return icon;
    }

    public void setIcon(String icon) {
	this.icon = icon;
    }

    public Integer getPosition() {
	return position;
    }

    public void setPosition(Integer position) {
	this.position = position;
    }

    public String getParentName() {
	return parentName;
    }

    public void setParentName(String parentName) {
	this.parentName = parentName;
    }

}
