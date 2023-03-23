package com.ucan.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ucan.dao.PostMapper;
import com.ucan.dao.RolePostMapper;
import com.ucan.entity.Post;
import com.ucan.entity.RolePost;
import com.ucan.entity.tree.node.PostDTreeNode;
import com.ucan.entity.tree.response.DTreeResponse;
import com.ucan.entity.tree.response.Status;
import com.ucan.entity.tree.status.PostNodeStatus;
import com.ucan.service.IRolePostService;

/**
 * @Description::<角色-职位>映射信息服务实现
 * @author liming.cen
 * @date 2023年2月22日 下午6:34:15
 */
@Service
public class RolePostServiceImpl implements IRolePostService {
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private RolePostMapper rolePost;

    @Override
    public DTreeResponse getRoleToPostTree(String orgId, String roleId) {
	// 查询所有职位信息
	List<Post> posts = postMapper.queryPostsByOrgId(orgId);
	// 查询某个角色的<角色-职位>映射记录
	List<RolePost> rolePosts = rolePost.getPostByRoleId(roleId);
	List<PostDTreeNode> nodes = new ArrayList<>();
	List<String> postIds = new ArrayList<>();
	if (rolePosts.size() > 0) {
	    rolePosts.forEach(item -> {
		// 获取该角色的所有组织Id
		postIds.add(item.getPostId());
	    });
	}
	if (posts.size() > 0) {
	    for (Post post : posts) {
		if (post.getParentId().equals("-1")) {// 获取第一个节点
		    PostDTreeNode node = new PostDTreeNode();
		    node.setTypeAndChecked("3", "0");
		    node.setId(post.getPostId());
		    node.setTitle(post.getPostName());
		    node.setHide(false);
		    node.setDisabled(false);
		    node.setSpread(true);
		    node.setParentId(post.getParentId());
		    node.setPosition(post.getPosition());
//			node.setFiconClass("layui-icon-praise");
		    node.setIconClass(post.getIcon());
		    // 设置额外的数据
		    node.setBasicData(post);
//		    node.setIcon(post.getIcon()); 后续完善

		    nodes.add(node);
		}
	    }
	}
	for (PostDTreeNode parent : nodes) {
	    parent = getChildrenNodes(parent, posts);
	    // 子节点排序
	    parent.sortChildren();
	}
	DTreeResponse dtResponse = new DTreeResponse();
	Status status = new Status();
	if (nodes.size() > 0) {
	    // dtree固定返回码
	    status.setCode(200);
	    status.setMessage("数据加载成功！");
	    PostNodeStatus nodeStatus = new PostNodeStatus();
	    nodeStatus.judgeSelfStatusToShowTree(nodes, postIds);
	} else {
	    status.setCode(-1);
	    status.setMessage("没有查到数据！");
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
    private PostDTreeNode getChildrenNodes(PostDTreeNode parent, List<Post> posts) {
	for (Post post : posts) {
	    if (parent.getId().equals(post.getParentId())) {
		PostDTreeNode node = new PostDTreeNode();
		node.setTypeAndChecked("3", "0");
		node.setId(post.getPostId());
		node.setTitle(post.getPostName());
		node.setHide(false);
		node.setDisabled(false);
		node.setSpread(true);
		node.setParentId(post.getParentId());
//			node.setFiconClass("layui-icon-praise");
		node.setIconClass(post.getIcon());
		node.setPosition(post.getPosition());
		// 获取父节点名称
		String parentName = postMapper.getPostNameById(post.getParentId());
		post.setParentName(parentName);
		// 设置额外的数据
		node.setBasicData(post);
		parent.getChildren().add(node);
		node = getChildrenNodes(node, posts);
	    }
	}
	return parent;
    }

}
