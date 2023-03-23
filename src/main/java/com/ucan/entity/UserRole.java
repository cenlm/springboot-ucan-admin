package com.ucan.entity;

import java.io.Serializable;

import com.ucan.entity.page.PageParameter;

/**
 * 用户-角色 实体类
 * 
 * @author liming.cen
 * @date 2022年12月23日 下午8:32:28
 */
public class UserRole implements Serializable {
    private static final long serialVersionUID = -1445116664474354173L;
    private String userId;
    private String roleId;

    /**
     * 组织
     */
    private Organization org;
    /**
     * 职位
     */
    private Post post;
    /**
     * 角色
     */
    private Role role;
    /**
     * 用户
     */
    private User user;

    private PageParameter page;

    public String getUserId() {
	return userId;
    }

    public void setUserId(String userId) {
	this.userId = userId;
    }

    public String getRoleId() {
	return roleId;
    }

    public void setRoleId(String roleId) {
	this.roleId = roleId;
    }

    public Role getRole() {
	return role;
    }

    public void setRole(Role role) {
	this.role = role;
    }

    public Organization getOrg() {
	return org;
    }

    public void setOrg(Organization org) {
	this.org = org;
    }

    public Post getPost() {
	return post;
    }

    public void setPost(Post post) {
	this.post = post;
    }

    public User getUser() {
	return user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    public PageParameter getPage() {
	return page;
    }

    public void setPage(PageParameter page) {
	this.page = page;
    }

}
