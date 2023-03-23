package com.ucan.dao;

import java.util.List;

import com.ucan.base.BaseDao;
import com.ucan.entity.MutexRole;
import com.ucan.entity.Role;

/**
 * @author liming.cen
 * @date 2022年12月24日 上午10:55:27
 */
public interface RoleMapper extends BaseDao<Role> {
    /**
     * 查询所有角色信息
     * 
     * @return
     */
    List<Role> queryAllRoles();

    /**
     * 获取节点的子节点Id
     * 
     * @param parentId
     * @return
     */
    List<Role> getRoleIdByParentId(String parentId);

    /**
     * 查询基础角色（通过is_super进行查询，值为2）
     * 
     * @return
     */
    Role queryBasicRole();

    /**
     * 查询某个角色名称的信息
     * 
     * @param roleId
     * @return
     */
    String queryRoleNameById(String roleId);

    /**
     * 通过roleIds批量删除角色信息
     * 
     * @param roleIds
     * @return
     */
    int deleteRoleByRoleIds(List<String> roleIds);

    /**
     * 新增互斥角色
     * 
     * @param mutexRole
     * @return
     */
    int insertMutexRole(MutexRole mutexRole);

    /**
     * 删除互斥角色
     * 
     * @param mutexRole
     * @return
     */
    int deleteMutexRoleById(MutexRole mutexRole);

    /**
     * 删除角色的时候，连同已配置的互斥角色一起删除
     * 
     * @param roleIds
     * @return
     */
    int deleteMutexRoleByRoleIds(List<String> roleIds);

    /**
     * 通过roleId查询互斥角色（分页）
     * 
     * @param roleId
     * @return
     */
    List<MutexRole> getMutexRolesByPage(MutexRole mutexRole);

    /**
     * 通过roleId查询互斥角色
     * 
     * @param roleId
     * @return
     */
    List<MutexRole> getMutexRolesByRoleId(String roleId);

    /**
     * 通过roleId和mutexRoleId查询互斥角色
     * 
     * @param roleId
     * @return
     */
    List<MutexRole> queryByDubbleRoleId(MutexRole mutexRole);

}
