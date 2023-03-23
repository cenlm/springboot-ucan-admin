package com.ucan.service;

import java.util.List;

import com.ucan.entity.MutexRole;
import com.ucan.entity.Role;
import com.ucan.entity.tree.response.DTreeResponse;

/**
 * @author liming.cen
 * @date 2022年12月24日 上午11:03:16
 */
public interface IRoleService {
    /**
     * 新增角色
     * 
     * @param role
     * @return
     */
    int insert(Role role) throws Exception;

    /**
     * 删除角色及其子孙角色，并解绑对应角色与组织架构、职位、（普通用户组：待办）、权限信息的映射关系
     * 
     * @param roleId
     * @return
     */
    int deleteRolesByRoleId(String roleId) throws Exception;

    /**
     * 更新角色信息
     * 
     * @param role
     * @return
     */
    int update(Role role) throws Exception;

    /**
     * 查询某个角色的信息
     * 
     * @param roleId
     * @return
     */
    Role queryRoleById(String roleId);

    /**
     * 查询基础角色（通过is_super进行查询，值为2）
     * 
     * @return
     */
    Role queryBasicRole();

    /**
     * 查询所有角色信息
     * 
     * @return
     */
    List<Role> queryAllRoles();

    /**
     * 查询角色节点树数据
     * 
     * @return
     */
    DTreeResponse queryRoleTreeNodes();

    /**
     * * <角色-组织>、<角色-职位>映射关系的修改
     * 
     * @param roleId         当前正在操作的roleId
     * @param orgId          当前正在操作的节点的组织Id
     * @param checkedOrgIds  待分配的组织Id
     * @param checkedPostIds 待分配的职位Id
     * @return
     * @throws Exception
     */
    int updateRoleOrgPostRelation(String roleId, String orgId, List<String> checkedOrgIds, List<String> checkedPostIds)
	    throws Exception;

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
     * 查询某角色的互斥角色
     * 
     * @param mutexRole
     * @return
     */
    List<MutexRole> getMutexRolesByPage(MutexRole mutexRole);

}
