package com.ucan.entity;

import java.io.Serializable;

import com.ucan.entity.page.PageParameter;

/**
 * @Description: 组织结构实体类
 * @author liming.cen
 * @date 2023年2月10日 下午4:06:06
 */
public class Organization implements Serializable {
    private static final long serialVersionUID = 2236703335251115201L;
    /**
     * 组织ID
     */
    private String orgId;
    /**
     * 组织名称
     */
    private String orgName;
    /**
     * 组织类型（1：总公司 2：分公司 3：部门），职位有另外一张表
     */
    private String orgType;
    /**
     * 组织编码
     */
    private String orgCode;
    /**
     * 上级组织ID
     */
    private String parentId;
    /**
     * 上级组织架构名称
     */
    private String parentName;
    /**
     * 节点排序，数值越小越靠前
     */
    private Integer position;

    /**
     * 组织字体图标
     */
    private String icon;
    /**
     * 是否是超级管理员组织节点（1 是 0 不是）
     */
    private Integer isSuper;

    /**
     * 分页参数对象（对象名称不要改，分页用到）
     */
    private PageParameter page;

    public String getOrgId() {
	return orgId;
    }

    public void setOrgId(String orgId) {
	this.orgId = orgId;
    }

    public String getOrgName() {
	return orgName;
    }

    public void setOrgName(String orgName) {
	this.orgName = orgName;
    }

    public String getOrgType() {
	return orgType;
    }

    public void setOrgType(String orgType) {
	this.orgType = orgType;
    }

    public String getOrgCode() {
	return orgCode;
    }

    public void setOrgCode(String orgCode) {
	this.orgCode = orgCode;
    }

    public String getParentId() {
	return parentId;
    }

    public void setParentId(String parentId) {
	this.parentId = parentId;
    }

    public Integer getPosition() {
	return position;
    }

    public void setPosition(Integer position) {
	this.position = position;
    }

    public PageParameter getPage() {
	return page;
    }

    public void setPage(PageParameter page) {
	this.page = page;
    }

    public String getIcon() {
	return icon;
    }

    public void setIcon(String icon) {
	this.icon = icon;
    }

    public String getParentName() {
	return parentName;
    }

    public void setParentName(String parentName) {
	this.parentName = parentName;
    }

    public Integer getIsSuper() {
	return isSuper;
    }

    public void setIsSuper(Integer isSuper) {
	this.isSuper = isSuper;
    }

}
