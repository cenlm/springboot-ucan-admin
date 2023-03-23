package com.ucan.service;

import java.util.List;

import com.ucan.entity.UserOrganization;

/**
 * @Description: <用户-组织>映射关系处理接口
 * @author liming.cen
 * @date 2023年2月24日 上午10:47:28
 */
public interface IUserOrgService {
    /**
     * 更新<用户-组织>映射关系
     * 
     * @param userId
     * @param checkedOrgIds
     * @return
     */
    int updateUserOrgRelation(String userId, String isSuper, List<String> checkedOrgIds) throws Exception;

    /**
     * 查找分组用户
     * 
     * @param userOrganization
     * @return
     */
    List<UserOrganization> getUserOrgByPage(UserOrganization userOrganization);
}
