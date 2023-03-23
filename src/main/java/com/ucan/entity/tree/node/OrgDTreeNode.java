package com.ucan.entity.tree.node;

import com.ucan.entity.Organization;

/**
 * @Description: 组织架构DTree节点模型
 * @author liming.cen
 * @date 2023年2月21日 下午4:30:18
 */
public class OrgDTreeNode extends DTreeNode<OrgDTreeNode, Organization> {
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
     * 上级组织名称
     */
    private String parentName;
    /**
     * 节点排序，数值越小越靠前
     */
    private Integer position;
    /**
     * 是否是超级管理员组织节点（1 是 0 不是）
     */
    private Integer isSuper;

    /**
     * 字体图标
     */
    private String icon;

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

    public String getParentName() {
	return parentName;
    }

    public void setParentName(String parentName) {
	this.parentName = parentName;
    }

    public Integer getPosition() {
	return position;
    }

    public void setPosition(Integer position) {
	this.position = position;
    }

    public String getIcon() {
	return icon;
    }

    public void setIcon(String icon) {
	this.icon = icon;
    }

    public Integer getIsSuper() {
	return isSuper;
    }

    public void setIsSuper(Integer isSuper) {
	this.isSuper = isSuper;
    }

}
