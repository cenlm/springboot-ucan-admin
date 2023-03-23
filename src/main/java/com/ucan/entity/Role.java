package com.ucan.entity;

import java.io.Serializable;
import java.util.List;

import com.ucan.entity.page.PageParameter;

/**
 * 角色实体类
 * 
 * @author liming.cen
 * @date 2022年12月23日 下午8:27:13
 */
public class Role implements Serializable {
    private static final long serialVersionUID = 3219888776861578374L;
    private String roleId;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 父角色Id
     */
    private String parentId;
    /**
     * 角色编码
     */
    private String roleCode;
    /**
     * 父节点名称
     */
    private String parentName;
    /**
     * 节点位置（超级管理员：0，其他角色最小为1）
     */
    private Integer position;

    /***
     * 创建时间
     */
    private String createTime;
    /**
     * 修改时间
     */
    private String modifyTime;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 角色字体图标
     */
    private String icon;
    /**
     * 系统代码
     */
    private String sysCode;
    /**
     * 是否是超级角色（1 是，0 不是 2 基础角色）
     */
    private Integer isSuper;
    /**
     * 角色分配的权限列表
     */
    private List<Permission> permissions;
    /**
     * 分页参数对象（对象名称不要改，分页用到）
     */
    private PageParameter page;

    public String getRoleId() {
	return roleId;
    }

    public void setRoleId(String roleId) {
	this.roleId = roleId;
    }

    public String getRoleName() {
	return roleName;
    }

    public void setRoleName(String roleName) {
	this.roleName = roleName;
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

    public String getCreateTime() {
	return createTime;
    }

    public void setCreateTime(String createTime) {
	this.createTime = createTime;
    }

    public String getModifyTime() {
	return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
	this.modifyTime = modifyTime;
    }

    public String getRemarks() {
	return remarks;
    }

    public void setRemarks(String remarks) {
	this.remarks = remarks;
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

    public String getSysCode() {
	return sysCode;
    }

    public void setSysCode(String sysCode) {
	this.sysCode = sysCode;
    }

    public List<Permission> getPermissions() {
	return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
	this.permissions = permissions;
    }

    public PageParameter getPage() {
	return page;
    }

    public void setPage(PageParameter page) {
	this.page = page;
    }

    public Integer getIsSuper() {
	return isSuper;
    }

    public void setIsSuper(Integer isSuper) {
	this.isSuper = isSuper;
    }

    public String getRoleCode() {
	return roleCode;
    }

    public void setRoleCode(String roleCode) {
	this.roleCode = roleCode;
    }

}
