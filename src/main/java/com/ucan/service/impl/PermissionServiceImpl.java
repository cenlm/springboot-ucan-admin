package com.ucan.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ucan.base.exception.CustomException;
import com.ucan.dao.PermissionMapper;
import com.ucan.dao.RolePermissionMapper;
import com.ucan.entity.Permission;
import com.ucan.entity.RolePermission;
import com.ucan.entity.tree.node.PermissionDTreeNode;
import com.ucan.entity.tree.node.PermissionTreeNode;
import com.ucan.entity.tree.response.DTreeResponse;
import com.ucan.entity.tree.response.Status;
import com.ucan.entity.tree.status.PermissionNodeStatus;
import com.ucan.service.IPermissionService;
import com.ucan.utils.UUIDUtil;

/**
 * 权限信息服务实现类
 * 
 * @author liming.cen
 * @date 2022年12月24日 下午5:46:17
 */
@Service
public class PermissionServiceImpl implements IPermissionService {
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Override
    public int insert(Permission permission) {
	permission.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	permission.setPermissionId(UUIDUtil.getUuid());
	return permissionMapper.insert(permission);
    }

    @Override
    public List<Permission> getPermissionsByPage(Permission permission) {
	return permissionMapper.getPermissionsByPage(permission);
    }

    @Override
    public List<Permission> queryAllPermissions() {
	return permissionMapper.queryAllPermissions();
    }

    @Override
    public Permission queryById(String id) {
	return permissionMapper.queryById(id);
    }

    @Override
    public int updatePermission(Permission permission) {
	return permissionMapper.update(permission);
    }

    @Override
    public int deletePermissionById(String id) throws CustomException {
	List<String> permissionIds = new ArrayList<>();
	getChildrenPermissions(id, permissionIds);
	// 加上触发删除操作的权限ID
	permissionIds.add(id);
	int updCount = permissionMapper.deleteByIds(permissionIds);
	// 权限信息删除成功后，删除与之相关的<角色-权限>映射记录
	if (updCount > 0) {
	    // 查找需要删除的<角色-权限>记录数量
	    int rolePermissionCount = rolePermissionMapper.queryCountByPermissionIds(permissionIds);
	    int update = rolePermissionMapper.deleteByPermissionIds(permissionIds);
	    if (update != rolePermissionCount) {// 删除的数量与预期的不一样，删除失败
		throw new CustomException("删除失败！原因：<角色-权限>映射关系解绑失败！");
	    }
	}

	return updCount;
    }

    /**
     * 获取某个权限节点的子节点（如果有）
     * 
     * @param permissionId
     * @return
     */
    public void getChildrenPermissions(String parentId, List<String> permissionIds) {
	List<Permission> children = permissionMapper.queryByParentId(parentId);
	if (children.size() > 0) {
	    for (Permission p : children) {
		permissionIds.add(p.getPermissionId());
		// 递归查找子节点
		getChildrenPermissions(p.getPermissionId(), permissionIds);
	    }
	}
    }

    @Override
    public int deleteByIds(List<String> ids) {
	return permissionMapper.deleteByIds(ids);
    }

    @Override
    public List<Permission> queryByParentId(String parentId) {
	return permissionMapper.queryByParentId(parentId);
    }

    @Override
    public List<PermissionTreeNode> getPermissionTreeNodes() {
	List<Permission> permissions = permissionMapper.queryAllPermissions();
	List<PermissionTreeNode> nodes = new ArrayList<>();
	if (permissions.size() > 0) {
	    for (Permission p : permissions) {
		if (p.getParentId().equals("-1")) {// 获取第一个节点
		    PermissionTreeNode node = new PermissionTreeNode();
		    node.setId(p.getPermissionId());
		    node.setTitle(p.getPermissionName());
		    node.setField(p.getPermissionName());
		    node.setChecked(false);
		    node.setDisabled(false);
		    node.setSpread(true);
		    node.setIcon(p.getIcon());
		    node.setPermissionId(p.getPermissionId());
		    node.setPermissionName(p.getPermissionName());
		    node.setPermissionType(p.getPermissionType());
		    node.setPermissionCode(p.getPermissionCode());
		    node.setParentId(p.getParentId());
		    node.setPosition(p.getPosition());
		    node.setRemarks(p.getRemarks());
		    node.setCreateTime(p.getCreateTime());
		    node.setModifyTime(p.getModifyTime());
		    node.setUrl(p.getUrl());
		    node.setSysCode(p.getSysCode());
		    nodes.add(node);
		}
	    }
	}
	for (PermissionTreeNode parent : nodes) {
	    parent = getChildrenTreeTableNodes(parent, permissions);
	    parent.sortChildren();
	}
	return nodes;
    }

    /**
     * 通过递归的方式获取子节点
     * 
     * @param parent
     * @param permissions
     * @return
     */
    private PermissionTreeNode getChildrenTreeTableNodes(PermissionTreeNode parent, List<Permission> permissions) {
	for (Permission p : permissions) {
	    if (parent.getPermissionId().equals(p.getParentId())) {
		PermissionTreeNode node = new PermissionTreeNode();
		node.setId(p.getPermissionId());
		node.setTitle(p.getPermissionName());
		node.setField(p.getPermissionName());
		node.setChecked(false);
		node.setDisabled(false);
		node.setSpread(true);
		node.setIcon(p.getIcon());
		node.setPermissionId(p.getPermissionId());
		node.setPermissionName(p.getPermissionName());
		node.setParentId(p.getParentId());
		node.setParentName(p.getParentName());
		node.setPosition(p.getPosition());
		node.setPermissionType(p.getPermissionType());
		node.setPermissionCode(p.getPermissionCode());
		node.setRemarks(p.getRemarks());
		node.setCreateTime(p.getCreateTime());
		node.setModifyTime(p.getModifyTime());
		node.setUrl(p.getUrl());
		node.setSysCode(p.getSysCode());
		parent.getChildren().add(node);
		node = getChildrenTreeTableNodes(node, permissions);

	    }
	}
	return parent;
    }

    @Override
    public int queryCountByParentId(String parentId) {
	return permissionMapper.queryCountByParentId(parentId);
    }

    @Override
    public DTreeResponse getPermissionTree4Role(String roleId, String parentId, String grandpaRoleId) {
	List<RolePermission> rolePermission = rolePermissionMapper.queryPermissionsIdByRoleId(roleId);
	// 获取父节点的所有<角色-权限>映射记录
	List<RolePermission> pIds = rolePermissionMapper.queryPermissionsByRoleId(parentId);
	List<String> parentPerIds = new ArrayList<>();
	if (pIds.size() > 0) {// 获取父节点的所有权限Id
	    pIds.forEach(item -> {
		parentPerIds.add(item.getPermissionId());
	    });
	}

	List<String> permissionIds = new ArrayList<>();
	if (rolePermission.size() > 0) {
	    rolePermission.forEach(item -> {
		// 该节点的子节点个数
//		int childrens = permissionMapper.queryCountByParentId(item.getPermissionId());
//		if (childrens <= 0) {// 剔除掉父节点,
		// 筛选没有子节点的节点，因为layui节点树选择父节点的时候，
		// 会全选其下的所有子节点，不符合需求
//		permissionIds.add(item.getPermissionId());
//		}
		// 获取该角色的所有权限Id
		permissionIds.add(item.getPermissionId());
	    });
	}
	List<Permission> permissions = permissionMapper.queryAllPermissions();
	List<PermissionDTreeNode> nodes = new ArrayList<>();
	if (permissions.size() > 0) {// 获取权限树数据，生成完整的权限树
	    for (Permission p : permissions) {
		if (p.getParentId().equals("-1")) {// 获取第一个节点
		    PermissionDTreeNode node = new PermissionDTreeNode();
//		    node.setTypeAndChecked("0", "0");
		    node.setTypeAndChecked("3", "0");
		    node.setId(p.getPermissionId());
		    node.setTitle(p.getPermissionName());
		    node.setHide(false);
		    // 第一层级的角色节点，所有权限可选
		    node.setDisabled(false);
		    node.setSpread(true);
		    node.setParentId(p.getParentId());
		    node.setPosition(p.getPosition());
//			node.setFiconClass("layui-icon-praise");
		    node.setIconClass(p.getIcon());
		    // 设置额外的数据
		    node.setBasicData(p);
		    nodes.add(node);
		}
	    }
	}

	for (PermissionDTreeNode parent : nodes) {
	    parent = getPermissionChildrenNodes(parent, permissions, parentPerIds, parentId, grandpaRoleId);
	    // 子节点排序
	    parent.sortChildren();
	    
	}
	DTreeResponse dtResponse = new DTreeResponse();
	Status status = new Status();
	if (nodes.size() > 0) {
	    // dtree固定返回码
	    status.setCode(200);
	    status.setMessage("数据加载成功！");
	    PermissionNodeStatus nodeStatus = new PermissionNodeStatus();
//	    nodeStatus.judgeAllStatusToShowTree(nodes, permissionIds);
	    nodeStatus.judgeSelfStatusToShowTree(nodes, permissionIds);
	    // 处理根节点复选框状态，子节点在递归方法中进行处理
	    if (parentPerIds.size() <= 0 && "-1".equals(grandpaRoleId)) {// 若当前节点的父节点无权限信息且是根节点，则要禁用所有节点复选框
		nodes.forEach(item -> {
		    item.setDisabled(true);
		});
	    } else if (!"-1".equals(parentId) && parentPerIds.size() <= 0) {// 如果当前节点的父节点不是根节点，且父节点权限为空，则要禁用所有节点复选框
		nodes.forEach(item -> {
		    item.setDisabled(true);
		});
	    } else if (parentPerIds.size() > 0) {// 当前节点的父节点是根节点且有权限信息
		nodes.forEach(item -> {
		    if (!parentPerIds.contains(item.getId())) { // 通过父节点权限信息，处理根节点复选框状态
			item.setDisabled(true);
		    }
		});
	    }
	} else {
	    status.setCode(-1);
	    status.setMessage("数据加载失败！");
	}

	dtResponse.setStatus(status);
	dtResponse.setData(nodes);
	return dtResponse;

    }

    /**
     * 递归处理子节点
     * 
     * @param parent           要处理的子节点
     * @param permissions      权限信息列表
     * @param parentPerIds     父节点已有的权限
     * @param parentId         父节点Id
     * @param grandpaRoleId    祖父节点id
     * @param parentIsRootNode 父节点是否是根节点（第一层级节点）
     * @return
     */
    private PermissionDTreeNode getPermissionChildrenNodes(PermissionDTreeNode parent, List<Permission> permissions,
	    List<String> parentPerIds, String parentId, String grandpaRoleId) {

	for (Permission p : permissions) {
	    if (parent.getId().equals(p.getParentId())) {
		PermissionDTreeNode node = new PermissionDTreeNode();
//		node.setTypeAndChecked("0", "0");
		node.setTypeAndChecked("3", "0");
		node.setId(p.getPermissionId());
		node.setTitle(p.getPermissionName());
		node.setHide(false);
		if (parentId.equals("-1")) {// 如果当前点击的是第一层级角色，则所有子权限节点可选择，权限根节点在第一次迭代中已经进行处理
		    node.setDisabled(false);
		} else {
		    if (parentPerIds.size() > 0) {// 父节点有权限
			if (!parentPerIds.contains(p.getPermissionId())) {
			    node.setDisabled(true);
			} else {// 匹配父节点权限的节点设置为可选
			    node.setDisabled(false);
			}

		    } else {// 父节点无权限，子节点设置为禁用
			node.setDisabled(true);

		    }
		}

		node.setSpread(true);
		node.setParentId(p.getParentId());
		node.setPosition(p.getPosition());
//			node.setFiconClass("layui-icon-praise");
		node.setIconClass(p.getIcon());
		// 设置额外的数据
		node.setBasicData(p);
		parent.getChildren().add(node);
		node = getPermissionChildrenNodes(node, permissions, parentPerIds, parentId, grandpaRoleId);

	    }
	}
	return parent;
    }

}
