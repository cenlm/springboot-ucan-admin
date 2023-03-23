package com.ucan.entity;

import java.io.Serializable;

/**
 * @Description:
 * @author liming.cen
 * @date 2023年2月18日 上午10:25:48
 */
public class RoleOrganization implements Serializable{
    private static final long serialVersionUID = 2612123535922170253L;
    /**
     * 组织Id
     */
    private String orgId;
    /**
     * 角色Id
     */
    private String roleId;
    /**
     * 系统编码
     */
    private String sysCode;

    private Role role;

    private Organization organization;

    public String getOrgId() {
	return orgId;
    }

    public void setOrgId(String orgId) {
	this.orgId = orgId;
    }

    public String getRoleId() {
	return roleId;
    }

    public void setRoleId(String roleId) {
	this.roleId = roleId;
    }

    public String getSysCode() {
	return sysCode;
    }

    public void setSysCode(String sysCode) {
	this.sysCode = sysCode;
    }

    public Role getRole() {
	return role;
    }

    public void setRole(Role role) {
	this.role = role;
    }

    public Organization getOrganization() {
	return organization;
    }

    public void setOrganization(Organization organization) {
	this.organization = organization;
    }

}
