package com.ucan.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ucan.base.exception.CustomException;
import com.ucan.dao.OrganizationMapper;
import com.ucan.dao.PostMapper;
import com.ucan.dao.RoleMapper;
import com.ucan.dao.RoleOrganizationMapper;
import com.ucan.dao.UserOrgMapper;
import com.ucan.entity.Organization;
import com.ucan.entity.Post;
import com.ucan.entity.Role;
import com.ucan.entity.RoleOrganization;
import com.ucan.entity.UserOrganization;
import com.ucan.entity.tree.node.OrgDTreeNode;
import com.ucan.entity.tree.response.DTreeResponse;
import com.ucan.entity.tree.response.Status;
import com.ucan.entity.tree.status.OrgNodeStatus;
import com.ucan.service.IOrganizationService;
import com.ucan.utils.UUIDUtil;

/**
 * @Description: 组织信息服务类
 * @author liming.cen
 * @date 2023年2月21日 16:44:05
 */
@Service
public class OrganizationServiceImpl implements IOrganizationService {
    @Autowired
    private OrganizationMapper organizationMapper;
    @Autowired
    private UserOrgMapper userOrgMapper;
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private RoleOrganizationMapper roleOrganizationMapper;
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public int addOrganization(Organization org) throws CustomException {
	int result = 0;
	// 生成组织Id
	org.setOrgId(UUIDUtil.getUuid());
	org.setIsSuper(0);
	int orgUpdate = organizationMapper.insert(org);
	if (orgUpdate > 0) {// 分配基础角色
	    Role basicRole = roleMapper.queryBasicRole();
	    RoleOrganization roleOrg = new RoleOrganization();
	    roleOrg.setRoleId(basicRole.getRoleId());
	    roleOrg.setOrgId(org.getOrgId());
	    int roleOrgUpdate = roleOrganizationMapper.insert(roleOrg);
	    if (roleOrgUpdate > 0) {
		result = roleOrgUpdate;
	    } else {
		throw new CustomException("组织新增失败！原因：基础角色分配失败！");
	    }
	}
	return result;
    }

    @Override
    public List<String> getAllChildrenIdsByOrgId(String orgId) {
	List<String> orgIds = new ArrayList<>();
	getChildrenByParentId(orgId, orgIds);
	// 将当前节点orgId加入列表
	orgIds.add(orgId);
	return orgIds;
    }

    @Override
    public DTreeResponse getOrganizationTreeNodes() {
	List<Organization> orgs = organizationMapper.queryAllOrganizations();
	List<OrgDTreeNode> nodes = new ArrayList<>();
	if (orgs.size() > 0) {
	    for (Organization org : orgs) {
		if (org.getParentId().equals("-1")) {// 获取第一个节点
		    OrgDTreeNode node = new OrgDTreeNode();
		    node.setTypeAndChecked("0", "0");
		    node.setId(org.getOrgId());
		    node.setTitle(org.getOrgName());
		    node.setHide(false);
		    // 第一层级的角色节点，所有权限可选
		    node.setDisabled(false);
		    node.setSpread(true);
		    node.setParentId(org.getParentId());
		    node.setPosition(org.getPosition());
//			node.setFiconClass("layui-icon-praise");
		    node.setIconClass(org.getIcon());
		    // 设置额外的数据
		    node.setBasicData(org);
		    nodes.add(node);
		}
	    }
	}
	for (OrgDTreeNode parent : nodes) {
	    parent = getChildrenNodes(parent, orgs);
	    // 子节点排序
	    parent.sortChildren();
	}
	DTreeResponse dtResponse = new DTreeResponse();
	Status status = new Status();
	if (nodes.size() > 0) {
	    // dtree固定返回码
	    status.setCode(200);
	    status.setMessage("数据加载成功！");
	    dtResponse.setStatus(status);
	    dtResponse.setData(nodes);

	} else {
	    status.setCode(-1);
	    status.setMessage("数据加载失败！");
	}
	return dtResponse;
    }

    /**
     * 遍历查找某个节点的所有子节点
     * 
     * @param parentId
     * @param orgIds
     */
    public void getChildrenByParentId(String parentId, List<String> orgIds) {
	List<Organization> children = organizationMapper.queryOrgIdsByParentId(parentId);
	if (children.size() > 0) {// 如果节点有子节点，则继续遍历
	    for (Organization org : children) {
		orgIds.add(org.getOrgId());
		getChildrenByParentId(org.getOrgId(), orgIds);
	    }
	}
    }

    /**
     * 通过递归的方式获取子节点
     * 
     * @param parent
     * @param permissions
     * @return
     */
    private OrgDTreeNode getChildrenNodes(OrgDTreeNode parent, List<Organization> orgs) {
	for (Organization org : orgs) {
	    if (parent.getId().equals(org.getParentId())) {
		OrgDTreeNode node = new OrgDTreeNode();
		node.setTypeAndChecked("0", "0");
		node.setId(org.getOrgId());
		node.setTitle(org.getOrgName());
		node.setHide(false);
		node.setSpread(true);
		node.setParentId(org.getParentId());
		node.setPosition(org.getPosition());
//			node.setFiconClass("layui-icon-praise");
		node.setIconClass(org.getIcon());
		String parentName = organizationMapper.getOrgNameById(org.getParentId());
		org.setParentName(parentName);
		// 设置额外的数据
		node.setBasicData(org);
		parent.getChildren().add(node);
		node = getChildrenNodes(node, orgs);
	    }
	}

	return parent;
    }

    @Override
    public int updateOrgNameById(Organization org) {
	return organizationMapper.update(org);
    }

    @Override
    public DTreeResponse getOrgTree4User(String userId) {

	List<UserOrganization> userOrgs = userOrgMapper.getUserOrgByUserId(userId);
	List<Organization> orgs = organizationMapper.queryAllOrganizations();
	List<OrgDTreeNode> nodes = new ArrayList<>();
	List<String> orgIds = new ArrayList<>();
	if (userOrgs.size() > 0) {
	    userOrgs.forEach(item -> {
		// 获取该用户的所有组织Id
		orgIds.add(item.getOrgId());
	    });
	}
	if (orgs.size() > 0) {
	    for (Organization org : orgs) {
		if (org.getParentId().equals("-1")) {// 获取第一个节点
		    OrgDTreeNode node = new OrgDTreeNode();
		    node.setTypeAndChecked("3", "0");
		    node.setId(org.getOrgId());
		    node.setTitle(org.getOrgName());
		    node.setHide(false);
		    // 第一层级的角色节点，所有权限可选
		    node.setDisabled(false);
		    node.setSpread(true);
		    node.setParentId(org.getParentId());
		    node.setPosition(org.getPosition());
//			node.setFiconClass("layui-icon-praise");
		    node.setIconClass(org.getIcon());
		    // 设置额外的数据
		    node.setBasicData(org);
		    nodes.add(node);
		}
	    }
	}
	for (OrgDTreeNode parent : nodes) {
	    parent = getChildrenNodes(parent, orgs);
	    // 子节点排序
	    parent.sortChildren();
	}
	DTreeResponse dtResponse = new DTreeResponse();
	Status status = new Status();
	if (nodes.size() > 0) {
	    // dtree固定返回码
	    status.setCode(200);
	    status.setMessage("数据加载成功！");
	    OrgNodeStatus nodeStatus = new OrgNodeStatus();
	    nodeStatus.judgeSelfStatusToShowTree(nodes, orgIds);
	    dtResponse.setStatus(status);
	    dtResponse.setData(nodes);

	} else {
	    status.setCode(-1);
	    status.setMessage("数据加载失败！");
	}
	return dtResponse;

    }

    @Override
    public int deleteOrganization(Organization org) throws CustomException {
	if (org.getIsSuper() == 1) {
	    throw new CustomException("超级管理员组织节点不能删除！");
	}
	List<Organization> children = organizationMapper.queryOrgIdsByParentId(org.getOrgId());
	if (children.size() > 0) {
	    throw new CustomException("该节点还存在子节点，请先删除子节点！");
	} else {// 没有子节点，则要判断是否已分配给用户，是否有职位，是否已分配角色
	    int count = userOrgMapper.getUserOrgCountByOrgId(org.getOrgId());
	    List<Post> posts = postMapper.queryPostsByOrgId(org.getOrgId());
	    List<RoleOrganization> roleOrgs = roleOrganizationMapper.getRoleOrgsByOrgId(org.getOrgId());
	    if (count > 0) {
		throw new CustomException("该节点还存在分配着用户，请先从用户列表中解除用户分配！");
	    } else if (posts.size() > 0) {
		throw new CustomException("该节点还分配着职位，请先删除职位！");
	    } else if (roleOrgs.size() > 0) {
		StringBuilder sb = new StringBuilder();
		roleOrgs.forEach(item -> {
		    sb.append(item.getRole().getRoleName() + "，");
		});
		throw new CustomException("该节点还分配角色：" + sb.toString() + "请先解除角色分配！");
	    }

	}
	return organizationMapper.delete(org);
    }

}
