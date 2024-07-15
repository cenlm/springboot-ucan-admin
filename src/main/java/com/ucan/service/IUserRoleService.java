package com.ucan.service;

import java.util.List;

import com.ucan.entity.UserRole;

/**
 * @author liming.cen
 * @date 2022年12月24日 下午5:56:36
 */
public interface IUserRoleService {
    /**
     * 查询用户角色
     * 
     * @param userId
     * @return
     */
    List<UserRole> queryRolesByUserId(String userId);

    /**
     * 查询所有roleId，主要用于超级管理员授权
     * 
     * @return
     */
    List<UserRole> queryAllRoleIds();

    /**
     * 查询角色成员
     * 
     * @param role
     * @return
     */
    List<UserRole> queryRoleUsersByPage(UserRole userRole);
}
