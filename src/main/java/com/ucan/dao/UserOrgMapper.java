package com.ucan.dao;

import java.util.List;

import com.ucan.base.BaseDao;
import com.ucan.entity.UserOrganization;

/**
 * @Description:<用户-组织>数据持久层
 * @author liming.cen
 * @date 2023年2月24日 上午10:55:08
 */
public interface UserOrgMapper extends BaseDao<UserOrganization> {
    /**
     * 通过用户id查询<用户-组织>映射记录
     * 
     * @param userId
     * @return
     */
    List<UserOrganization> getUserOrgByUserId(String userId);

    /**
     * 查询组织是否还分配着用户
     * 
     * @param orgId
     * @return
     */
    int getUserOrgCountByOrgId(String orgId);

    /**
     * 通过批量userId统计<用户-组织>数量
     * 
     * @param userIds
     * @return
     */
    int getCountsByUserIds(List<String> userIds);

    /**
     * 查找分组用户
     * 
     * @param userOrganization
     * @return
     */
    List<UserOrganization> getUserOrgByPage(UserOrganization userOrganization);
}
