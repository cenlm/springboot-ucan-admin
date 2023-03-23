package com.ucan.entity;

import java.io.Serializable;
import java.util.List;

import com.ucan.entity.page.PageParameter;

/**
 * 用户实体类
 * 
 * @author liming.cen
 * @date 2022年12月23日 下午8:20:40
 */
public class User implements Serializable {

    private static final long serialVersionUID = 4513862602468058360L;
    private String userId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户别名
     */
    private String userAlias;
    /**
     * 用户性别 0：女 1：男
     */
    private Integer sex;
    /**
     * 用户手机号码
     */
    private String cellPhoneNumber;
    /**
     * 用户住址
     */
    private String address;
    /**
     * 用户登录时间
     */
    private String entryDate;
    /**
     * 用户上次登录时间
     */
    private String lastLogin;
    /**
     * 用户是否是超级管理员 1：是 0：否
     */
    private Integer isSuper;

    /**
     * 密码
     */
    private String password;
    /**
     * 邮箱地址
     */
    private String email;
    /**
     * 1:可用 0：不可用
     */
    private Integer isEnable;
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

    private Post post;
    /**
     * 用户角色列表
     */
    private List<Role> roles;
    /**
     * 分页参数对象（对象名称不要改，分页用到）
     */
    private PageParameter page;

    public String getUserId() {
	return userId;
    }

    public void setUserId(String userId) {
	this.userId = userId;
    }

    public String getUserName() {
	return userName;
    }

    public void setUserName(String userName) {
	this.userName = userName;
    }

    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getRemarks() {
	return remarks;
    }

    public void setRemarks(String remarks) {
	this.remarks = remarks;
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

    public String getUserAlias() {
	return userAlias;
    }

    public void setUserAlias(String userAlias) {
	this.userAlias = userAlias;
    }

    public String getCellPhoneNumber() {
	return cellPhoneNumber;
    }

    public void setCellPhoneNumber(String cellPhoneNumber) {
	this.cellPhoneNumber = cellPhoneNumber;
    }

    public String getAddress() {
	return address;
    }

    public void setAddress(String address) {
	this.address = address;
    }

    public String getEntryDate() {
	return entryDate;
    }

    public void setEntryDate(String entryDate) {
	this.entryDate = entryDate;
    }

    public String getLastLogin() {
	return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
	this.lastLogin = lastLogin;
    }

    public Integer getSex() {
	return sex;
    }

    public void setSex(Integer sex) {
	this.sex = sex;
    }

    public Integer getIsSuper() {
	return isSuper;
    }

    public void setIsSuper(Integer isSuper) {
	this.isSuper = isSuper;
    }

    public Integer getIsEnable() {
	return isEnable;
    }

    public void setIsEnable(Integer isEnable) {
	this.isEnable = isEnable;
    }

    public List<Role> getRoles() {
	return roles;
    }

    public void setRoles(List<Role> roles) {
	this.roles = roles;
    }

    public PageParameter getPage() {
	return page;
    }

    public void setPage(PageParameter page) {
	this.page = page;
    }

    public Post getPost() {
	return post;
    }

    public void setPost(Post post) {
	this.post = post;
    }

}
