package com.ucan.entity;

import java.io.Serializable;

/**
 * 角色-权限 实体类
 * 
 * @author liming.cen
 * @date 2022年12月23日 下午8:33:50
 */
public class RolePermission implements Serializable {
    private static final long serialVersionUID = -3384585364560847432L;
    private String roleId;
    private String permissionId;
    // 系统编码（用来区分分布式系统，如订单系统、库存系统等）
    private String sysCode;
    private Permission permission;

    public String getRoleId() {
	return roleId;
    }

    public void setRoleId(String roleId) {
	this.roleId = roleId;
    }

    public String getPermissionId() {
	return permissionId;
    }

    public void setPermissionId(String permissionId) {
	this.permissionId = permissionId;
    }

    public Permission getPermission() {
	return permission;
    }

    public void setPermission(Permission permission) {
	this.permission = permission;
    }

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }
    
    

}
