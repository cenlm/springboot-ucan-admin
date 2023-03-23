package com.ucan.service;

import java.util.List;

import com.ucan.entity.UserRole;

/**
 * @author liming.cen
 * @date 2022年12月24日 下午5:56:36
 */
public interface IUserRoleService {
//    /**
//     * 新增 <用户-角色> 映射记录
//     * 
//     * @param ur
//     * @return
//     */
//    int insert(UserRole ur);
//
//    /**
//     * 查询用户已分配的 <用户ID-角色ID> 映射记录
//     * 
//     * @param userId
//     * @return
//     */
//    List<UserRole> queryRolesByUserId(String userId);
//
//    List<UserRole> queryByPage(String userId);
//
//    /**
//     * 按userId删除 <用户ID-角色ID> 映射记录
//     * 
//     * @param userId
//     * @return
//     */
//    int deleteUserRoleByUserId(String userId);
//
//    /**
//     * 查询用户已分配的角色数量
//     * 
//     * @param userId
//     * @return
//     */
//    int queryUserRoleCounts(String userId);
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
