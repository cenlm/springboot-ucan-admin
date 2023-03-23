package com.ucan.service;

import java.util.List;

import com.ucan.entity.Permission;
import com.ucan.entity.tree.node.PermissionTreeNode;
import com.ucan.entity.tree.response.DTreeResponse;

/**
 * @author liming.cen
 * @date 2022年12月24日 下午5:45:15
 */
public interface IPermissionService {
    int insert(Permission permission);

    int updatePermission(Permission permission);

    /**
     * 删除权限信息：删除id对应的权限信息，以及parentId为该id的节点（当前节点为父节点）及其所有后续节点（通过递归查找），
     * 找到所有节点后，去<role-permission> 映射表中解绑映射
     * 
     * @param id
     * @return
     */
    int deletePermissionById(String id) throws Exception;

    /**
     * 批量删除权限，然后去解绑<role-permission>映射。这里已直接遍历表格找到所有节点，不用进行递归查找节点。
     * 
     * @param ids
     * @return
     */
    int deleteByIds(List<String> ids);

    List<Permission> getPermissionsByPage(Permission permission);

    List<Permission> queryAllPermissions();

    Permission queryById(String id);

    /**
     * 根据父ID查询权限
     * 
     * @param parentId
     * @return
     */
    List<Permission> queryByParentId(String parentId);

    /**
     * 判断某个节点是否有子节点
     * 
     * @param parentId
     * @return
     */
    int queryCountByParentId(String parentId);

    /**
     * 获取所有权限节点数据（权限树形表格）
     * 
     * @return
     */
    List<PermissionTreeNode> getPermissionTreeNodes();

    /**
     * 获取角色管理页卡片对应角色的权限树形图，为某个角色的权限回显做准备
     * 
     * @param roleId
     * @param parentId
     * @return
     */
    DTreeResponse getPermissionTree4Role(String roleId, String parentId, String grandpaRoleId);

}
