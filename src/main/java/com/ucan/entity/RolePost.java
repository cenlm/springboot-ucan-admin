package com.ucan.entity;

import java.io.Serializable;

/**
 * 角色-职位 实体类
 * 
 * @author liming.cen
 * @date 2023年2月18日 10:44:26
 */
public class RolePost implements Serializable {
    private static final long serialVersionUID = 6193795620176981765L;
    /**
     * 职位Id
     */
    private String postId;
    /**
     * 角色Id
     */
    private String roleId;
    /**
     * 系统编码
     */
    private String sysCode;
    /**
     * 角色
     */
    private Role role;
    /**
     * 职位
     */
    private Post post;

    public String getPostId() {
	return postId;
    }

    public void setPostId(String postId) {
	this.postId = postId;
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

    public Post getPost() {
	return post;
    }

    public void setPost(Post post) {
	this.post = post;
    }

}
