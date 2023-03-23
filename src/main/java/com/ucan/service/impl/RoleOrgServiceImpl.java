package com.ucan.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ucan.dao.OrganizationMapper;
import com.ucan.dao.RoleOrganizationMapper;
import com.ucan.entity.Organization;
import com.ucan.entity.RoleOrganization;
import com.ucan.entity.tree.node.OrgDTreeNode;
import com.ucan.entity.tree.response.DTreeResponse;
import com.ucan.entity.tree.response.Status;
import com.ucan.entity.tree.status.OrgNodeStatus;
import com.ucan.service.IRoleOrgService;

/**
 * @Description: <角色-组织>映射信息服务
 * @author liming.cen
 * @date 2023年2月22日 下午3:29:42
 */
@Service
public class RoleOrgServiceImpl implements IRoleOrgService {
    @Autowired
    private RoleOrganizationMapper roleOrganizationMapper;
    @Autowired
    private OrganizationMapper organizationMapper;

    @Override
    public DTreeResponse getRoleToOrgTree(String roleId) {
	List<RoleOrganization> roleOrgs = roleOrganizationMapper.getOrgByRoleId(roleId);
	List<Organization> orgs = organizationMapper.queryAllOrganizations();
	List<OrgDTreeNode> nodes = new ArrayList<>();
	List<String> orgIds = new ArrayList<>();
	if (roleOrgs.size() > 0) {
	    roleOrgs.forEach(item -> {
		// 获取该角色的所有组织Id
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
		node.setTypeAndChecked("3", "0");
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
    public int insertRoleOrgRelation(RoleOrganization roleOrg) {
	return roleOrganizationMapper.insert(roleOrg);
    }
}
