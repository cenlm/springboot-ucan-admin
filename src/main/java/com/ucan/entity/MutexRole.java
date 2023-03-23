package com.ucan.entity;

import java.io.Serializable;

import com.ucan.entity.page.PageParameter;

/**
 * @Description:互斥角色实体
 * @author liming.cen
 * @date 2023年2月23日 下午3:08:20
 */
public class MutexRole implements Serializable {
    private static final long serialVersionUID = -8541426661216782581L;
    /**
     * 角色id
     */
    private String roleId;
    /**
     * 互斥角色Id
     */
    private String mutexRoleId;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 互斥角色名称
     */
    private String mutexRoleName;
    /**
     * 系统编码
     */
    private String sysCode;
    /**
     * 分页参数
     */
    private PageParameter page;

    public String getRoleId() {
	return roleId;
    }

    public void setRoleId(String roleId) {
	this.roleId = roleId;
    }

    public String getMutexRoleId() {
	return mutexRoleId;
    }

    public void setMutexRoleId(String mutexRoleId) {
	this.mutexRoleId = mutexRoleId;
    }

    public String getSysCode() {
	return sysCode;
    }

    public void setSysCode(String sysCode) {
	this.sysCode = sysCode;
    }

    public PageParameter getPage() {
	return page;
    }

    public String getRoleName() {
	return roleName;
    }

    public void setRoleName(String roleName) {
	this.roleName = roleName;
    }

    public String getMutexRoleName() {
	return mutexRoleName;
    }

    public void setMutexRoleName(String mutexRoleName) {
	this.mutexRoleName = mutexRoleName;
    }

    public void setPage(PageParameter page) {
	this.page = page;
    }

}
