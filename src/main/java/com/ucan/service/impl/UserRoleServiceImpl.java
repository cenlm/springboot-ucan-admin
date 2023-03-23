package com.ucan.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ucan.dao.UserRoleMapper;
import com.ucan.entity.UserRole;
import com.ucan.service.IUserRoleService;

/**
 * @author liming.cen
 * @date 2022年12月24日 下午5:57:36
 */
@Service
public class UserRoleServiceImpl implements IUserRoleService {
    @Autowired
    private UserRoleMapper userRoleMapper;
//
//    @Override
//    public int insert(UserRole ur) {
//	return userRoleMapper.insert(ur);
//    }
//
//    @Override
//    public List<UserRole> queryRolesByUserId(String userId) {
//	return userRoleMapper.queryRolesByUserId(userId);
//    }
//
//    @Override
//    public List<UserRole> queryByPage(String userId) {
//	return userRoleMapper.queryByPage(userId);
//    }
//
//  
//    @Override
//    public int deleteUserRoleByUserId(String userId) {
//	return userRoleMapper.deleteById(userId);
//    }
//
//    @Override
//    public int queryUserRoleCounts(String userId) {
//	return userRoleMapper.queryUserRoleCounts(userId);
//    }

    @Override
    public List<UserRole> queryRoleUsersByPage(UserRole userRole) {
	return userRoleMapper.queryRoleUsersByPage(userRole);
    }

    @Override
    public List<UserRole> queryRolesByUserId(String userId) {
	return userRoleMapper.queryRolesByUserId(userId);
    }

    @Override
    public List<UserRole> queryAllRoleIds() {
	return userRoleMapper.queryAllRoleIds();
    }

}
