package com.ucan.dao;

import java.util.List;

import com.ucan.base.BaseDao;
import com.ucan.entity.Role;
import com.ucan.entity.User;
import com.ucan.entity.UserRole;

/**
 * @author liming.cen
 * @date 2022年12月24日 下午1:38:22
 */
public interface UserRoleMapper extends BaseDao<UserRole> {

//    List<UserRole> queryByPage(String userId);
//
//    int queryUserRoleCounts(String userId);
    /**
     * 通过用户Id查询角色
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
     * 查询角色成员（多表联查）
     * 
     * @param roleId
     * @return
     */
    List<UserRole> queryRoleUsersByPage(UserRole userRole);
}
