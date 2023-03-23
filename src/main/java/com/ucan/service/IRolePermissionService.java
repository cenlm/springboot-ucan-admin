package com.ucan.service;

import java.util.List;

import com.ucan.entity.RolePermission;

/**
 * @author liming.cen
 * @date 2022年12月24日 下午7:19:24
 */
public interface IRolePermissionService {
    /**
     * 批量新增<角色-权限>映射记录
     * 
     * @param list
     * @return
     */
    int insertBatch(List<RolePermission> list);

    /**
     * 批量删除<角色-权限>映射记录
     * 
     * @param list
     * @return
     */
    int deleteBatch(List<RolePermission> list);

    int insert(RolePermission rolePermission);

    List<RolePermission> queryPermissionsByRoleId(String roleId);

    List<RolePermission> queryByPage(String roleId);

    /**
     * 通过roleId查询权限Id
     * 
     * @param roleId
     * @return
     */
    List<RolePermission> queryPermissionsIdByRoleId(String roleId);

    /**
     * 根据roleId或permissionId 查询<role-permission> 映射记录数量
     * 
     * @param id
     * @return
     */
    int queryRolePermissionCounts(RolePermission rp);

    /**
     * 根据权限ID批量删除<角色-权限>映射记录
     * 
     * @param permissionIds
     * @return
     */
    int deleteByPermissionIds(List<String> permissionIds);

    /**
     * 更新<角色-权限>映射关系（新增或删除）
     * 
     * @param roleId        要修改权限的角色Id
     * @param permissionIds 待分配的权限Id
     * @return
     */
    int updateRolePermissionRelation(String roleId, List<String> permissionIds) throws Exception;

    

}
