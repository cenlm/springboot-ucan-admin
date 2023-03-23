package com.ucan.dao;

import java.util.List;

import com.ucan.base.BaseDao;
import com.ucan.entity.RolePost;

/**
 * @author liming.cen
 * @date 2022年12月24日 下午5:16:19
 */
public interface RolePostMapper extends BaseDao<RolePost> {
    /**
     * 通过多个roleId统计<角色-职位>映射记录数量
     * 
     * @param roleIds
     * @return
     */
    int getRolePostCountsByRoleIds(List<String> roleIds);

    /**
     * 获取<角色-职位>记录信息
     * 
     * @param roleId
     * @return
     */
    List<RolePost> getPostByRoleId(String roleId);

    /**
     * 通过批量职位id获取<角色-职位>记录信息
     * 
     * @param postIds
     * @return
     */
    List<RolePost> getRolePostByPostIds(List<String> postIds);

    /**
     * 通过职位Id查询<角色-职位>记录
     * 
     * @param postId
     * @return
     */
    List<RolePost> getRolePostByPostId(String postId);

    /**
     * 通过roleIds删除<角色-职位>映射记录数量
     * 
     * @param roleIds
     * @return
     */
    int deleteRolePostByRoleIds(List<String> roleIds);

}
