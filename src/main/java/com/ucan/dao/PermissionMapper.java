package com.ucan.dao;

import java.util.List;

import com.ucan.base.BaseDao;
import com.ucan.entity.Permission;

/**
 * 权限信息持久层
 * 
 * @author liming.cen
 * @date 2022年12月24日 下午1:24:04
 */
public interface PermissionMapper extends BaseDao<Permission> {
    List<Permission> getPermissionsByPage(Permission permission);

    List<Permission> queryAllPermissions();

    /**
     * 按父权限ID查询信息
     * 
     * @param parentId
     * @return
     */
    List<Permission> queryByParentId(String parentId);

    int deleteByParentId(String parentId);

    /**
     * 查询某个节点是否有子节点
     * 
     * @param parentId
     * @return
     */
    int queryCountByParentId(String parentId);

}
