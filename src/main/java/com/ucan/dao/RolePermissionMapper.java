package com.ucan.dao;

import java.util.List;

import com.ucan.base.BaseDao;
import com.ucan.entity.Role;
import com.ucan.entity.RolePermission;
import com.ucan.entity.User;

/**
 * @author liming.cen
 * @date 2022年12月24日 下午5:16:19
 */
public interface RolePermissionMapper extends BaseDao<RolePermission> {
    List<RolePermission> queryByPage(String roleId);

    List<RolePermission> queryPermissionsByRoleId(String roleId);

    /**
     * 通过roleId查询权限Id
     * 
     * @param roleId
     * @return
     */
    List<RolePermission> queryPermissionsIdByRoleId(String roleId);

    /**
     * 通过权限Id查询角色Id
     * 
     * @param permissionIds
     * @return
     */
    List<RolePermission> queryRoleIdByPermissionIds(List<String> permissionIds);

    /**
     * 通过roleId查询权限id
     * 
     * @param roleIds
     * @return
     */
    List<RolePermission> queryPermissionIdsByRoleIds(List<String> roleIds);

    /**
     * 根据roleId或permissionId 查询<role-permission> 映射记录数量
     * 
     * @param id
     * @return
     */
    int queryRolePermissionCounts(RolePermission rp);

    /**
     * 通过多个roleId统计<角色-权限>映射记录数量
     * 
     * @param roleIds
     * @return
     */
    int getRolePermissionCountsByRoleIds(List<String> roleIds);

    /**
     * 通过多个permissionId统计<角色-权限>映射记录总数
     * 
     * @param permissionIds
     * @return
     */
    int queryCountByPermissionIds(List<String> permissionIds);

    /**
     * 通过权限ID删除<角色-权限>映射记录
     * 
     * @param permissionId
     * @return
     */
    int deleteByPermissionId(String permissionId);

    /**
     * 通过角色ID删除<角色-权限>映射记录
     * 
     * @param roleId
     * @return
     */
    int deleteByRoleId(String roleId);

    /**
     * 通过角色ID批量删除<角色-权限>映射记录
     * 
     * @param roleIds
     * @return
     */
    int deleteByRoleIds(List<String> roleIds);

    /**
     * 通过权限ID批量删除<角色-权限>映射记录
     * 
     * @param permissionIds
     * @return
     */
    int deleteByPermissionIds(List<String> permissionIds);

    /**
     * 通过roleIds删除<角色-权限>映射记录
     * 
     * @param roleIds
     * @return
     */
    int deleteRolePermissionByRoleIds(List<String> roleIds);
}
