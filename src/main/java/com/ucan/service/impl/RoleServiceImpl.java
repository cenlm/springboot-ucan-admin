package com.ucan.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ucan.base.exception.CustomException;
import com.ucan.dao.PostMapper;
import com.ucan.dao.RoleMapper;
import com.ucan.dao.RoleOrganizationMapper;
import com.ucan.dao.RolePermissionMapper;
import com.ucan.dao.RolePostMapper;
import com.ucan.entity.MutexRole;
import com.ucan.entity.Post;
import com.ucan.entity.Role;
import com.ucan.entity.RoleOrganization;
import com.ucan.entity.RolePost;
import com.ucan.entity.tree.node.RoleDTreeNode;
import com.ucan.entity.tree.response.DTreeResponse;
import com.ucan.entity.tree.response.Status;
import com.ucan.service.IRoleService;
import com.ucan.utils.UUIDUtil;

/**
 * 角色信息服务实现类
 * 
 * @author liming.cen
 * @date 2022年12月24日 上午11:04:34
 */
@Service
public class RoleServiceImpl implements IRoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private RoleOrganizationMapper roleOrganizationMapper;
    @Autowired
    private RolePostMapper rolePostMapper;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Override
    public int insert(Role role) throws CustomException {
	if (role.getRoleName() == null || role.getRoleName() == "") {
	    throw new CustomException("角色名称不能为空！");
	} else if (role.getRoleCode() == null || role.getRoleCode() == "") {
	    throw new CustomException("角色编码不能为空！");
	}
	// 生成角色Id
	role.setRoleId(UUIDUtil.getUuid());
	return roleMapper.insert(role);
    }

    @Override
    public List<Role> queryAllRoles() {
	return roleMapper.queryAllRoles();
    }

    @Override
    public DTreeResponse queryRoleTreeNodes() {
	List<Role> roles = roleMapper.queryAllRoles();
	List<RoleDTreeNode> nodes = new ArrayList<>();

	if (roles.size() > 0) {
	    for (Role role : roles) {
		if (role.getParentId().equals("-1")) {// 获取第一个节点
		    RoleDTreeNode node = new RoleDTreeNode();
		    node.setTypeAndChecked("0", "0");
		    node.setId(role.getRoleId());
		    node.setTitle(role.getRoleName());
		    node.setHide(false);
		    node.setDisabled(false);
		    node.setSpread(true);
		    node.setParentId(role.getParentId());
		    node.setPosition(role.getPosition());
//			node.setFiconClass("layui-icon-praise");
		    node.setIconClass(role.getIcon());
		    // 设置额外的数据
		    node.setBasicData(role);
		    nodes.add(node);
		}
	    }
	}
	for (RoleDTreeNode parent : nodes) {
	    parent = getChildrenNodes(parent, roles);
	    // 子节点排序
	    parent.sortChildren();
	}
	DTreeResponse dtResponse = new DTreeResponse();
	Status status = new Status();
	if (nodes.size() > 0) {
	    // dtree固定返回码
	    status.setCode(200);
	    status.setMessage("数据加载成功！");
	} else {
	    status.setCode(-1);
	    status.setMessage("数据加载失败！");
	}
	dtResponse.setStatus(status);
	dtResponse.setData(nodes);
	return dtResponse;
    }

    /**
     * 通过递归的方式获取子节点
     * 
     * @param parent
     * @param permissions
     * @return
     */
    private RoleDTreeNode getChildrenNodes(RoleDTreeNode parent, List<Role> roles) {
	for (Role role : roles) {
	    if (parent.getId().equals(role.getParentId())) {
		RoleDTreeNode node = new RoleDTreeNode();
		node.setTypeAndChecked("0", "0");
		node.setId(role.getRoleId());
		node.setTitle(role.getRoleName());
		node.setHide(false);
		node.setDisabled(false);
		node.setSpread(true);
		node.setParentId(role.getParentId());
		node.setPosition(role.getPosition());
//		node.setFiconClass("layui-icon-praise");
		node.setIconClass(role.getIcon());
		String parentName = roleMapper.queryRoleNameById(role.getParentId());
		role.setParentName(parentName);
		// 设置额外的数据
		node.setBasicData(role);
		parent.getChildren().add(node);
		node = getChildrenNodes(node, roles);
	    }
	}
	return parent;
    }

    @Override
    public int update(Role role) throws CustomException {
	if (role.getRoleName() == null || role.getRoleName() == "") {
	    throw new CustomException("角色名称不能为空！");
	} else if (role.getRoleCode() == null || role.getRoleCode() == "") {
	    throw new CustomException("角色编码不能为空！");
	}
	return roleMapper.update(role);
    }

    @Override
    public Role queryRoleById(String roleId) {
	return roleMapper.queryById(roleId);
    }

    @Override
    public int deleteRolesByRoleId(String roleId) throws CustomException {
	// 1.查询当前节点及其所有子孙节点的Id
	// 2.通过roleId去查询<角色-组织><角色-职位><角色-普通用户组><角色-权限>映射记录数
	// 3.通过查询到的roleIds到相应的映射表中删除记录，并判断是否完全删除成功
	// 4.从角色表中删除当前角色及其所有子孙角色

	// 当前角色的所有子孙角色Id(包含自身的roleId)
	List<String> children = new ArrayList<>();
	getChildrenRoles(roleId, children);
	// 将自身roleId添加进去
	children.add(roleId);
	int result = 0;
	// 要删除的<角色-组织>记录数
	int roleOrgNeedToDel = roleOrganizationMapper.getRoleOrganizationCountsByRoleIds(children);
	// 要删除的<角色-职位>记录数
	int rolePostNeedToDel = rolePostMapper.getRolePostCountsByRoleIds(children);
	// 要删除的<角色-权限>记录数
	int rolePermissionNeedToDel = rolePermissionMapper.getRolePermissionCountsByRoleIds(children);
	// 要删除的<角色-普通用户组>记录数（待办）
	if (rolePermissionNeedToDel > 0) {
	    // 删除<角色-权限>记录
	    int delRPCount = rolePermissionMapper.deleteRolePermissionByRoleIds(children);
	    if (rolePermissionNeedToDel != delRPCount) {// 记录没有完全删除，任务执行失败
		throw new CustomException("角色删除失败！原因：<角色-权限>映射删除失败。");
	    }
	}
	if (rolePostNeedToDel > 0) {
	    // 删除<角色-职位>记录
	    int delRPostCount = rolePostMapper.deleteRolePostByRoleIds(children);
	    if (rolePostNeedToDel != delRPostCount) {// 记录没有完全删除，任务执行失败
		throw new CustomException("角色删除失败！原因：<角色-职位>映射删除失败。");
	    }
	}
	if (roleOrgNeedToDel > 0) {
	    // 删除<角色-组织>记录
	    int delROCount = roleOrganizationMapper.deleteRoleOrgByRoleIds(children);
	    if (roleOrgNeedToDel != delROCount) {// 记录没有完全删除，任务执行失败
		throw new CustomException("角色删除失败！原因：<角色-组织>映射删除失败。");
	    }
	}
	// 删除角色信息记录
	int delRoleCount = roleMapper.deleteRoleByRoleIds(children);
	if (children.size() != delRoleCount) {
	    throw new CustomException("角色删除失败！原因：角色信息删除失败。");
	} else {
	    result = delRoleCount;
	}
	int deleteMutexRoleByRoleIds = roleMapper.deleteMutexRoleByRoleIds(children);
	if (deleteMutexRoleByRoleIds < 0) {
	    throw new CustomException("角色删除失败！原因：该角色已配置的互斥角色删除失败！");
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

    /**
     * 处理<角色-组织>映射关系
     * 
     * @param roleId            当前角色Id
     * @param knownMutexRoleIds 当前角色的互斥角色
     * @param checkedOrgIds     传入的组织Id
     * @return
     * @throws CustomException
     */

    private int updateRoleOrgRelation(String roleId, Set<String> knownMutexRoleIds, List<String> checkedOrgIds)
	    throws CustomException {

	// 当前角色已分配的<角色-组织>映射记录
	List<RoleOrganization> currentRoleOrgRelations = roleOrganizationMapper.getOrgByRoleId(roleId);

	// 当前角色拥有的组织Id
	List<String> orgIds = new ArrayList<>();

	// 需要去删除的<角色-组织>映射记录
	List<RoleOrganization> roRelationsNeedToDel = new ArrayList<>();
	// 需要去新增的<角色-组织>映射记录
	List<RoleOrganization> roRelationsNeedToAdd = new ArrayList<>();
	int finalResult = 0;
	// ===========开始处理<角色-组织>映射关系============================
	if (currentRoleOrgRelations.size() > 0) {// 当前角色已分配的组织信息
	    currentRoleOrgRelations.forEach(item -> {
		// 获得角色已分配的组织Id
		orgIds.add(item.getOrgId());
	    });
	    if (checkedOrgIds.size() <= 0) {
		// 角色原本就有组织，但传入的组织Id数量个数为0，说明角色已有的组织要被撤销。
		// 要删除当前角色已有的所有<角色-组织>映射记录
		int result = roleOrganizationMapper.deleteBatch(currentRoleOrgRelations);
		if (result != currentRoleOrgRelations.size()) {
		    throw new CustomException("组织分配失败！原因：<角色-组织>映射记录删除失败。");
		}
		return result;
	    } else {// 如果角色已分配组织记录和待分配组织记录都不为0，则要计算（已分配组织记录和待分配组织记录）交集和差集。
		    // 角色已有组织与待分配组织的交集
		List<String> intersection = orgIds.stream().filter(item -> checkedOrgIds.contains(item))
			.collect(Collectors.toList());
		if (intersection.size() <= 0) {// 如果角色已分配组织记录和待分配组织记录都不为0且交集为空，则要撤销已分配组织且分配新组织

		    orgIds.forEach(orgId -> {
			RoleOrganization ro = new RoleOrganization();
			ro.setRoleId(roleId);
			ro.setOrgId(orgId);
			roRelationsNeedToDel.add(ro);
		    });
		    int result = roleOrganizationMapper.deleteBatch(roRelationsNeedToDel);
		    if (result != currentRoleOrgRelations.size()) {
			throw new CustomException("组织分配失败！原因：<角色-组织>映射记录删除失败。");
		    }

		    // 分配新的组织
		    checkedOrgIds.forEach(orgId -> {
			RoleOrganization ro = new RoleOrganization();
			ro.setRoleId(roleId);
			ro.setOrgId(orgId);
			roRelationsNeedToAdd.add(ro);
		    });
//		    // 获取即将要被分配角色的组织已有的角色（角色分配至组织的时候不做角色互斥判断，只判断职位的角色互斥）
//		    List<RoleOrganization> roOfcheckedOrgs = roleOrganizationMapper.getRoleOrgByOrgIds(checkedOrgIds);
//		    StringBuilder msg = new StringBuilder();
//		    roOfcheckedOrgs.forEach(item -> {
//			if (knownMutexRoleIds.contains(item.getRoleId())) {
//			    msg.append("【");
//			    msg.append(item.getOrganization().getOrgName());
//			    msg.append("->");
//			    msg.append(item.getRole().getRoleName());
//			    msg.append("】");
//			}
//		    });
//		    if (msg.length() > 0) {// 说明当前角色与即将分配的组织有互斥角色
//			throw new CustomException(
//				"组织分配失败！原因：存在以下组织的角色与当前角色互斥:" + msg.toString() + "!请调整相应组织的角色分配或修改这些角色的互斥关系！");
//		    }
		    // 分配新组织
		    int countAdd = roleOrganizationMapper.insertBatch(roRelationsNeedToAdd);
		    if (countAdd != roRelationsNeedToAdd.size()) {// 没有完全新增成功，不算执行成功
			throw new CustomException("组织分配失败！原因：<角色-组织>映射记录新增失败。");
		    }
		    finalResult = 1;
		} else {// 交集不为空，则要删除已有组织与待分配组织的差集，新增待分配组织与已有组织的差集
			// 要删除的部分组织信息
		    List<String> reduceToDel = orgIds.stream().filter(item -> !checkedOrgIds.contains(item))
			    .collect(Collectors.toList());
		    // 要新增的部分组织信息
		    List<String> reduceToAdd = checkedOrgIds.stream().filter(item -> !orgIds.contains(item))
			    .collect(Collectors.toList());
		    if (reduceToDel.size() > 0) {// 有需要删除的部分组织信息
			reduceToDel.forEach(orgId -> {
			    RoleOrganization ro = new RoleOrganization();
			    ro.setRoleId(roleId);
			    ro.setOrgId(orgId);
			    roRelationsNeedToDel.add(ro);
			});

			int result = roleOrganizationMapper.deleteBatch(roRelationsNeedToDel);
			if (result != roRelationsNeedToDel.size()) {
			    throw new CustomException("组织分配失败！原因：<角色-组织>映射记录删除失败。");
			}
		    }
		    if (reduceToAdd.size() > 0) {// 有需要新增的部分组织信息
			reduceToAdd.forEach(orgId -> {
			    RoleOrganization ro = new RoleOrganization();
			    ro.setRoleId(roleId);
			    ro.setOrgId(orgId);
			    roRelationsNeedToAdd.add(ro);
			});
			// 获取即将要被分配角色的组织已有的角色
//			List<RoleOrganization> roOfcheckedOrgs = roleOrganizationMapper.getRoleOrgByOrgIds(reduceToAdd);
//			StringBuilder msg = new StringBuilder();
//			roOfcheckedOrgs.forEach(item -> {
//			    if (knownMutexRoleIds.contains(item.getRoleId())) {
//				msg.append("【");
//				msg.append(item.getOrganization().getOrgName());
//				msg.append("->");
//				msg.append(item.getRole().getRoleName());
//				msg.append("】");
//			    }
//			});
//			if (msg.length() > 0) {// 说明当前角色与即将分配的组织有互斥角色
//			    throw new CustomException(
//				    "组织分配失败！原因：存在以下组织的角色与当前角色互斥:" + msg.toString() + "!请调整相应组织的角色分配或修改这些角色的互斥关系！");
//			}
			// 分配新组织
			int countAdd = roleOrganizationMapper.insertBatch(roRelationsNeedToAdd);
			if (countAdd != roRelationsNeedToAdd.size()) {// 没有完全新增成功，不算执行成功
			    throw new CustomException("组织分配失败！原因：<角色-组织>映射记录新增失败。");
			}
		    }
		    finalResult = 1;
		}
	    }
	} else { // 当前角色还没有分配组织，则分配权限
	    if (checkedOrgIds.size() > 0) { // 有新的组织要分配
		checkedOrgIds.forEach(orgId -> {
		    RoleOrganization ro = new RoleOrganization();
		    ro.setRoleId(roleId);
		    ro.setOrgId(orgId);
		    roRelationsNeedToAdd.add(ro);
		});
		// 获取即将要被分配角色的组织已有的角色
//		List<RoleOrganization> roOfcheckedOrgs = roleOrganizationMapper.getRoleOrgByOrgIds(checkedOrgIds);
//		StringBuilder msg = new StringBuilder();
//		roOfcheckedOrgs.forEach(item -> {
//		    if (knownMutexRoleIds.contains(item.getRoleId())) {
//			msg.append("【");
//			msg.append(item.getOrganization().getOrgName());
//			msg.append("->");
//			msg.append(item.getRole().getRoleName());
//			msg.append("】");
//		    }
//		});
//		if (msg.length() > 0) {// 说明当前角色与即将分配的组织有互斥角色
//		    throw new CustomException(
//			    "组织分配失败！原因：存在以下组织的角色与当前角色互斥:" + msg.toString() + "!请调整相应组织的角色分配或修改这些角色的互斥关系！");
//		}
		// 分配新组织
		int countAdd = roleOrganizationMapper.insertBatch(roRelationsNeedToAdd);
		return countAdd;
	    } else {// 不需要处理，保持原状，返回成功标识为1
		finalResult = 1;
	    }

	}
	return finalResult;

    }

    /**
     * 处理<角色-职位>映射关系
     * 
     * @param roleId            当前角色Id
     * @param knownMutexRoleIds 当前角色的互斥角色
     * @param orgId             当前正在处理<角色-职位>映射关系的组织结构
     * @param checkedPostIds    传入的职位Id
     * @return
     * @throws CustomException
     */

    private int updateRolePostRelation(String roleId, Set<String> knownMutexRoleIds, String orgId,
	    List<String> checkedPostIds) throws CustomException {
	// 查询某个组织架构下的职位
	List<Post> posts = postMapper.queryPostsByOrgId(orgId);
	// 当前角色已分配的<角色-职位>映射记录
	List<RolePost> currentRolePostRelations = rolePostMapper.getPostByRoleId(roleId);
	List<RolePost> rpRelationsNeedToHandle = new ArrayList<>();
	List<String> currentRPRelationPostIds = new ArrayList<>();
	List<String> currentOrgPostIds = new ArrayList<>();

	// 取出当前角色已分配的职位Id
	currentRolePostRelations.forEach(item -> {
	    currentRPRelationPostIds.add(item.getPostId());
	});
	// 取出当前组织的职位Id
	posts.forEach(item -> {
	    currentOrgPostIds.add(item.getPostId());
	});
	// 通过计算交集，筛选出实际需要处理的postId
	List<String> postIdsNeedToHandle = currentRPRelationPostIds.stream()
		.filter(item -> currentOrgPostIds.contains(item)).collect(Collectors.toList());
	postIdsNeedToHandle.forEach(postId -> {
	    RolePost rolePost = new RolePost();
	    rolePost.setRoleId(roleId);
	    rolePost.setPostId(postId);
	    rpRelationsNeedToHandle.add(rolePost);
	});

	// 当前角色拥有的职位Id
	List<String> postIds = new ArrayList<>();

	// 需要去删除的<角色-职位>映射记录
	List<RolePost> rpRelationsNeedToDel = new ArrayList<>();
	// 需要去新增的<角色-职位>映射记录
	List<RolePost> rpRelationsNeedToAdd = new ArrayList<>();
	int finalResult = 0;
	// ===========开始处理<角色-职位>映射关系============================
	if (rpRelationsNeedToHandle.size() > 0) {// 当前角色已分配的职位信息
	    rpRelationsNeedToHandle.forEach(item -> {
		// 获得角色已分配的职位Id
		postIds.add(item.getPostId());
	    });
	    if (checkedPostIds.size() <= 0) {
		// 角色原本就有分配职位，但传入的职位Id数量个数为0，说明角色已有分配的职位要被撤销。
		// 要删除当前角色已有的所有<角色-职位>映射记录
		int result = rolePostMapper.deleteBatch(rpRelationsNeedToHandle);
		if (result != rpRelationsNeedToHandle.size()) {
		    throw new CustomException("职位分配失败！原因：<角色-职位>映射记录删除失败。");
		}
		return result;
	    } else {// 如果角色已分配职位记录和待分配职位记录都不为0，则要计算（已分配职位记录和待分配职位记录）交集和差集。
		    // 角色已有职位与待分配职位的交集
		List<String> intersection = postIds.stream().filter(item -> checkedPostIds.contains(item))
			.collect(Collectors.toList());
		if (intersection.size() <= 0) {// 如果角色已分配职位记录和待分配职位记录都不为0且交集为空，则要撤销已分配职位且分配新职位

		    postIds.forEach(postId -> {
			RolePost rp = new RolePost();
			rp.setRoleId(roleId);
			rp.setPostId(postId);
			rpRelationsNeedToDel.add(rp);
		    });
		    int result = rolePostMapper.deleteBatch(rpRelationsNeedToDel);
		    if (result != rpRelationsNeedToDel.size()) {
			throw new CustomException("职位分配失败！原因：<角色-职位>映射记录删除失败。");
		    }
		    // 分配新的职位
		    checkedPostIds.forEach(postId -> {
			RolePost rp = new RolePost();
			rp.setRoleId(roleId);
			rp.setPostId(postId);
			rpRelationsNeedToAdd.add(rp);
		    });

		    // 获取即将要被分配角色的职位已有的角色
		    List<RolePost> rpOfcheckedPosts = rolePostMapper.getRolePostByPostIds(checkedPostIds);
		    StringBuilder msg = new StringBuilder();
		    rpOfcheckedPosts.forEach(item -> {
			if (knownMutexRoleIds.contains(item.getRoleId())) {
			    msg.append("职位【");
			    msg.append(item.getPost().getPostName());
			    msg.append("】的【");
			    msg.append(item.getRole().getRoleName());
			    msg.append("】角色；");
			}
		    });
		    if (msg.length() > 0) {// 说明当前角色与即将分配的职位有互斥角色
			throw new CustomException(
				"分配失败！原因："+msg.toString()+"与当前角色互斥！请调整相应职位的角色分配或修改这些角色的互斥关系！");
		    }

		    // 分配新职位
		    int countAdd = rolePostMapper.insertBatch(rpRelationsNeedToAdd);
		    if (countAdd != rpRelationsNeedToAdd.size()) {// 没有完全新增成功，不算执行成功
			throw new CustomException("职位分配失败！原因：<角色-职位>映射记录新增失败。");
		    }
		    finalResult = 1;
		} else {// 交集不为空，则要删除已有职位与待分配职位的差集，新增待分配职位与已有职位的差集
			// 要删除的部分职位信息
		    List<String> reduceToDel = postIds.stream().filter(item -> !checkedPostIds.contains(item))
			    .collect(Collectors.toList());
		    // 要新增的部分职位信息
		    List<String> reduceToAdd = checkedPostIds.stream().filter(item -> !postIds.contains(item))
			    .collect(Collectors.toList());
		    if (reduceToDel.size() > 0) {// 有需要删除的部分职位信息
			reduceToDel.forEach(postId -> {
			    RolePost rp = new RolePost();
			    rp.setRoleId(roleId);
			    rp.setPostId(postId);
			    rpRelationsNeedToDel.add(rp);
			});

			int result = rolePostMapper.deleteBatch(rpRelationsNeedToDel);
			if (result != rpRelationsNeedToDel.size()) {
			    throw new CustomException("职位分配失败！原因：<角色-职位>映射记录删除失败。");
			}
		    }
		    if (reduceToAdd.size() > 0) {// 有需要新增的部分职位信息
			reduceToAdd.forEach(postId -> {
			    RolePost rp = new RolePost();
			    rp.setRoleId(roleId);
			    rp.setPostId(postId);
			    rpRelationsNeedToAdd.add(rp);
			});
			// 获取即将要被分配角色的职位已有的角色
			List<RolePost> rpOfcheckedPosts = rolePostMapper.getRolePostByPostIds(reduceToAdd);
			StringBuilder msg = new StringBuilder();
			rpOfcheckedPosts.forEach(item -> {
			    if (knownMutexRoleIds.contains(item.getRoleId())) {
				msg.append("职位【");
				msg.append(item.getPost().getPostName());
				msg.append("】的【");
				msg.append(item.getRole().getRoleName());
				msg.append("】角色；");
			    }
			});
			if (msg.length() > 0) {// 说明当前角色与即将分配的职位有互斥角色
			    throw new CustomException(
				    "分配失败！原因："+msg.toString()+"与当前角色互斥！请调整职位的角色分配或修改角色的互斥关系！");
			}
			// 分配新职位
			int countAdd = rolePostMapper.insertBatch(rpRelationsNeedToAdd);
			if (countAdd != rpRelationsNeedToAdd.size()) {// 没有完全新增成功，不算执行成功
			    throw new CustomException("职位分配失败！原因：<角色-职位>映射记录新增失败。");
			}
		    }
		    finalResult = 1;
		}
	    }
	} else { // 当前角色还没有分配职位，则分配职位
	    if (checkedPostIds.size() > 0) { // 有新的职位要分配
		checkedPostIds.forEach(postId -> {
		    RolePost rp = new RolePost();
		    rp.setRoleId(roleId);
		    rp.setPostId(postId);
		    rpRelationsNeedToAdd.add(rp);
		});

		// 获取即将要被分配角色的职位已有的角色
		List<RolePost> rpOfcheckedPosts = rolePostMapper.getRolePostByPostIds(checkedPostIds);
		StringBuilder msg = new StringBuilder();
		rpOfcheckedPosts.forEach(item -> {
		    if (knownMutexRoleIds.contains(item.getRoleId())) {
			msg.append("【");
			msg.append(item.getPost().getPostName());
			msg.append("->");
			msg.append(item.getRole().getRoleName());
			msg.append("】");
		    }
		});
		if (msg.length() > 0) {// 说明当前角色与即将分配的职位有互斥角色
		    throw new CustomException(
			    "分配失败！原因："+msg.toString()+"与当前角色互斥！请调整职位的角色分配或修改角色的互斥关系！");
		}
		// 分配新职位
		int countAdd = rolePostMapper.insertBatch(rpRelationsNeedToAdd);
		return countAdd;
	    } else {// 不需要分配职位，保持原状,返回处理成功标识
		finalResult = 1;
	    }

	}
	return finalResult;

    }

    @Override
    public int insertMutexRole(MutexRole mutexRole) {
	List<MutexRole> mrs = roleMapper.queryByDubbleRoleId(mutexRole);
	if (mrs.size() > 0) {// 说明记录已存在
	    return 0;
	}
	return roleMapper.insertMutexRole(mutexRole);
    }

    @Override
    public int deleteMutexRoleById(MutexRole mutexRole) {
	return roleMapper.deleteMutexRoleById(mutexRole);
    }

    @Override
    public List<MutexRole> getMutexRolesByPage(MutexRole mutexRole) {
	return roleMapper.getMutexRolesByPage(mutexRole);
    }

    @Override
    public int updateRoleOrgPostRelation(String roleId, String orgId, List<String> checkedOrgIds,
	    List<String> checkedPostIds) throws Exception {
	List<MutexRole> mutexRoles = roleMapper.getMutexRolesByRoleId(roleId);
	Set<String> knownMutexRoleIds = new HashSet<>();

	mutexRoles.forEach(item -> {
	    // 因为是互斥关系，所以要将roleId和mutexRoleId都取出来，然后剔除掉自身的roleId,就得到所有与当前角色互斥的角色Id
	    knownMutexRoleIds.add(item.getRoleId());
	    knownMutexRoleIds.add(item.getMutexRoleId());
	});
	knownMutexRoleIds.remove(roleId);
	int updRO = updateRoleOrgRelation(roleId, knownMutexRoleIds, checkedOrgIds);
	int updRP = updateRolePostRelation(roleId, knownMutexRoleIds, orgId, checkedPostIds);
	return (updRO > 0 && updRP > 0) ? 1 : 0;
    }

    @Override
    public Role queryBasicRole() {
	return roleMapper.queryBasicRole();
    }
}
