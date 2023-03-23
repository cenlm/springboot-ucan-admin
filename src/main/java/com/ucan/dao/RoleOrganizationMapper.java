package com.ucan.dao;

import java.util.List;

import com.ucan.base.BaseDao;
import com.ucan.entity.RoleOrganization;
import com.ucan.entity.RolePermission;

/**
 * <角色-组织>映射关系数据持久层
 * 
 * @author liming.cen
 * @date 2022年12月24日 下午5:16:19
 */
public interface RoleOrganizationMapper extends BaseDao<RoleOrganization> {
    /**
     * 通过roleIds获取<角色-组织>映射总记录数
     * 
     * @param roleIds
     * @return
     */
    int getRoleOrganizationCountsByRoleIds(List<String> roleIds);

    /**
     * 通过组批量织Id获取<角色-组织>信息
     * 
     * @param orgIds
     * @return
     */
    List<RoleOrganization> getRoleOrgByOrgIds(List<String> orgIds);

    /**
     * 通过orgId获取<角色-组织>记录
     * 
     * @param orgId
     * @return
     */
    List<RoleOrganization> getRoleOrgsByOrgId(String orgId);

    /**
     * 通过roleIds删除<角色-组织>映射记录
     * 
     * @param roleIds
     * @return
     */
    int deleteRoleOrgByRoleIds(List<String> roleIds);

    /**
     * 通过roleId从<角色-组织>映射表中获取权限Id
     * 
     * @param roleId
     * @return
     */
    List<RoleOrganization> getOrgByRoleId(String roleId);

}
