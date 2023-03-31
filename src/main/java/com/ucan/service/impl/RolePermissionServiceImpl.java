package com.ucan.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ucan.dao.RoleMapper;
import com.ucan.dao.RolePermissionMapper;
import com.ucan.entity.Role;
import com.ucan.entity.RolePermission;
import com.ucan.exception.CustomException;
import com.ucan.service.IRolePermissionService;

/**
 * @author liming.cen
 * @date 2022年12月24日 下午7:23:38
 */
@Service
public class RolePermissionServiceImpl implements IRolePermissionService {
    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<RolePermission> queryPermissionsByRoleId(String roleId) {
	return rolePermissionMapper.queryPermissionsByRoleId(roleId);
    }

    @Override
    public List<RolePermission> queryByPage(String roleId) {
	return rolePermissionMapper.queryByPage(roleId);
    }

    @Override
    public int insert(RolePermission rolePermission) {
	return rolePermissionMapper.insert(rolePermission);
    }

    @Override
    public int queryRolePermissionCounts(RolePermission rp) {
	return rolePermissionMapper.queryRolePermissionCounts(rp);
    }

    @Override
    public int deleteByPermissionIds(List<String> permissionIds) {
	return rolePermissionMapper.deleteByPermissionIds(permissionIds);
    }

    @Override
    public List<RolePermission> queryPermissionsIdByRoleId(String roleId) {
	return rolePermissionMapper.queryPermissionsIdByRoleId(roleId);
    }

    @Override
    public int insertBatch(List<RolePermission> list) {
	return rolePermissionMapper.insertBatch(list);
    }

    @Override
    public int deleteBatch(List<RolePermission> list) {
	return rolePermissionMapper.deleteBatch(list);
    }

    /**
     * 更新<角色-权限>映射关系（新增或删除）<br>
     * 删除逻辑：通过交集计算出当前节点与所有子孙节点需要删除的<角色-权限>记录，最后一并删除 父角色与子角色的权限分配的相互影响：<br>
     * 1.父角色的权限分配会影响子角色权限的新增或减少（子角色不能越权，如副经理不能拥有经理没有的权限）<br>
     * 2.子角色新增权限，父角色要继承子角色权限（如：经理 将拥有 副经理的所有权限）。<br>
     * 第二点功能实现说明：当子角色分配权限时，只能选择父角色已分配的权限（权限复选框动态禁用）。
     */
    @Override
    public int updateRolePermissionRelation(String roleId, List<String> checkedIds) throws CustomException {
	// 查询当前节点已有权限
	List<RolePermission> currentNodeRelations = rolePermissionMapper.queryPermissionsIdByRoleId(roleId);
	// 获取当前节点及其子节点Id
	List<String> roleIds = new ArrayList<>();
	// 获取子节点Id
	getChildrenRoles(roleId, roleIds);
	// 将当前roleId添加进去
	roleIds.add(roleId);
	// 需要去删除的<角色-权限>映射记录
	List<RolePermission> rpRelationsNeedToDel = new ArrayList<>();
	// 需要去新增的<角色-权限>映射记录
	List<RolePermission> rpRelationsNeedToAdd = new ArrayList<>();
	List<String> originRpIds = new ArrayList<>();
	int finalResult = 0;
	if (currentNodeRelations.size() > 0) {// 当前角色已拥有权限
	    currentNodeRelations.forEach(item -> {
		// 获得已有的权限Id
		originRpIds.add(item.getPermissionId());
	    });
	    if (checkedIds.size() <= 0) {
		// 角色原本就有权限，但传入的权限Id数量个数为0，说明角色已有的权限要被撤销。
		// 要删除当前节点和其所有子孙节点的已匹配的部分权限（或全部）
		int result = deleteCurrentAndChildrenRolesPermission(roleIds, currentNodeRelations);
		return result;
	    } else {// 如果角色已有权限和待分配权限个数都不为0，则要计算（已有权限与待分配权限的）交集和差集。
		    // 角色已有权限与待分配权限的交集
		List<String> intersection = originRpIds.stream().filter(item -> checkedIds.contains(item))
			.collect(Collectors.toList());
		if (intersection.size() <= 0) {// 角色已有权限和待分配权限个数都不为0且交集为空，则要撤销已有权限且分配新权限

		    originRpIds.forEach(id -> {
			RolePermission rp = new RolePermission();
			rp.setRoleId(roleId);
			rp.setPermissionId(id);
			rpRelationsNeedToDel.add(rp);
		    });
		    // 删除当前节点和其所有子孙节点匹配到的部分权限（或全部）
		    int delCount = deleteCurrentAndChildrenRolesPermission(roleIds, rpRelationsNeedToDel);
		    if (delCount < 0) {// 删除失败
			throw new CustomException("权限分配失败！原因：部分权限撤销失败。");
		    }
		    // 分配新权限
		    checkedIds.forEach(id -> {
			RolePermission rp = new RolePermission();
			rp.setRoleId(roleId);
			rp.setPermissionId(id);
			rpRelationsNeedToAdd.add(rp);
		    });
		    // 分配新权限
		    int countAdd = rolePermissionMapper.insertBatch(rpRelationsNeedToAdd);
		    if (countAdd != rpRelationsNeedToAdd.size()) {// 没有完全新增成功，不算执行成功
			throw new CustomException("权限分配失败！原因：部分权限新增失败。");
		    }
		    finalResult = 2;
		} else {// 交集不为空，则要删除已有权限与待分配权限的差集，新增待分配权限与已有权限的差集
			// 要删除的部分权限
		    List<String> reduceToDel = originRpIds.stream().filter(item -> !checkedIds.contains(item))
			    .collect(Collectors.toList());
		    // 要新增的部分权限
		    List<String> reduceToAdd = checkedIds.stream().filter(item -> !originRpIds.contains(item))
			    .collect(Collectors.toList());
		    if (reduceToDel.size() > 0) {// 有需要删除的部分权限
			reduceToDel.forEach(id -> {
			    RolePermission rp = new RolePermission();
			    rp.setRoleId(roleId);
			    rp.setPermissionId(id);
			    rpRelationsNeedToDel.add(rp);
			});

			// 删除当前节点和其所有子孙节点的部分权限（或全部）
			int delCount = deleteCurrentAndChildrenRolesPermission(roleIds, rpRelationsNeedToDel);
			if (delCount < 0) {// 删除失败
			    throw new CustomException("权限分配失败！原因：部分权限撤销失败。");
			}
		    }
		    if (reduceToAdd.size() > 0) {// 有需要新增的部分权限
			reduceToAdd.forEach(id -> {
			    RolePermission rp = new RolePermission();
			    rp.setRoleId(roleId);
			    rp.setPermissionId(id);
			    rpRelationsNeedToAdd.add(rp);
			});

			// 新增部分权限
			int countAdd = rolePermissionMapper.insertBatch(rpRelationsNeedToAdd);
			if (countAdd != rpRelationsNeedToAdd.size()) {// 没有完全执行完成
			    throw new CustomException("权限分配失败！原因：部分权限新增失败。");
			}
		    }
		    finalResult = 2;
		}
	    }
	} else { // 当前角色还没有分配权限，则分配权限
	    if (checkedIds.size() > 0) { // 有新的权限要分配
		checkedIds.forEach(id -> {
		    RolePermission rp = new RolePermission();
		    rp.setRoleId(roleId);
		    rp.setPermissionId(id);
		    rpRelationsNeedToAdd.add(rp);
		});
		// 分配新权限
		int result = rolePermissionMapper.insertBatch(rpRelationsNeedToAdd);
		return result;
	    } else {
		finalResult = 1;
	    }

	}
	return finalResult;
    }

    /**
     * 计算当前节点和它的所有子孙节点要删除的记录，最后才一并删除
     * 
     * @param roleIds           当前节点和其所有子孙节点roleId
     * @param relationNeedToDel 需要子节点进行删除处理的权限
     * @return
     * @throws CustomException
     */
    public int deleteCurrentAndChildrenRolesPermission(List<String> roleIds, List<RolePermission> relationNeedToDel)
	    throws CustomException {
	int result = 0;
	// 如果当前节点有子孙节点
	if (roleIds.size() > 0) {
	    // 最终的请求参数的组装
	    List<RolePermission> paramForRoleChildren = new ArrayList<>();
	    // 需要进行权限删除的权限Id
	    List<String> permissionIds = new ArrayList<>();
	    relationNeedToDel.forEach(item -> {
		permissionIds.add(item.getPermissionId());
	    });
	    // 处理子节点权限：可能需要子节点去删除的权限（需要做交集进行判断）
	    List<RolePermission> roleIdNeedToHandle = rolePermissionMapper.queryRoleIdByPermissionIds(permissionIds);
	    if (roleIdNeedToHandle.size() > 0) {
		List<String> roleIdsNeedToHandle = new ArrayList<>();
		roleIdNeedToHandle.forEach(item -> {
		    roleIdsNeedToHandle.add(item.getRoleId());
		});
		// 求交集，算出实际需要进行权限删除的子角色Id
		List<String> intersectionRoleIds = roleIds.stream().filter(item -> roleIdsNeedToHandle.contains(item))
			.collect(Collectors.toList());
		if (intersectionRoleIds.size() > 0) {
		    // 计算出需要进行权限删除的roleId后，还要计算这些roleId的哪些权限要删除，要再算一次权限ID交集
		    List<RolePermission> permissions = rolePermissionMapper
			    .queryPermissionIdsByRoleIds(intersectionRoleIds);
		    List<String> childNodesPerIdNeedToMatch = new ArrayList<>();
		    if (permissions.size() > 0) {
			permissions.forEach(item -> {
			    childNodesPerIdNeedToMatch.add(item.getPermissionId());
			});
			List<String> finalPerIdsNeedToDel = childNodesPerIdNeedToMatch.stream()
				.filter(perId -> permissionIds.contains(perId)).collect(Collectors.toList());
			// 遍历需要进行删除的权限Id
			finalPerIdsNeedToDel.forEach(perId -> {
			    // 遍历真正需要进行权限删除的子节点权限Id，进行权限删除的参数组装
			    intersectionRoleIds.forEach(roleId -> {
				RolePermission rp = new RolePermission();
				rp.setRoleId(roleId);
				rp.setPermissionId(perId);
				paramForRoleChildren.add(rp);
			    });
			});
		    }
		    result = rolePermissionMapper.deleteBatch(paramForRoleChildren);
		}
	    }
	}
	return result;
    }

    /**
     * 递归获取子孙节点Id
     * 
     * @param parentId
     * @param roleIds
     */
    public void getChildrenRoles(String parentId, List<String> roleIds) {
	List<Role> children = roleMapper.getRoleIdByParentId(parentId);
	if (children.size() > 0) {
	    children.forEach(item -> {
		roleIds.add(item.getRoleId());
		getChildrenRoles(item.getRoleId(), roleIds);
	    });
	}
    }

}
