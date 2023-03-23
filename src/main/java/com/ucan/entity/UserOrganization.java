package com.ucan.entity;

import java.io.Serializable;

import com.ucan.entity.page.PageParameter;

/**
 * @Description: <用户-组织>映射关系实体类
 * @author liming.cen
 * @date 2023年2月24日 上午10:58:00
 */
public class UserOrganization implements Serializable{
    private static final long serialVersionUID = -4276198357393292464L;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 组织id
     */
    private String orgId;
    /**
     * 组织类型
     */
    private String orgType;
    /**
     * 用户
     */
    private User user;
    /**
     * 组织
     */
    private Organization organization;
    /**
     * 分页参数，名称固定为page，不要改成其他的
     */
    private PageParameter page;

    public String getUserId() {
	return userId;
    }

    public void setUserId(String userId) {
	this.userId = userId;
    }

    public String getOrgId() {
	return orgId;
    }

    public void setOrgId(String orgId) {
	this.orgId = orgId;
    }

    public String getOrgType() {
	return orgType;
    }

    public void setOrgType(String orgType) {
	this.orgType = orgType;
    }

    public User getUser() {
	return user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    public Organization getOrganization() {
	return organization;
    }

    public void setOrganization(Organization organization) {
	this.organization = organization;
    }

    public PageParameter getPage() {
	return page;
    }

    public void setPage(PageParameter page) {
	this.page = page;
    }

}
