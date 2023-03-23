package com.ucan.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ucan.base.exception.CustomException;
import com.ucan.dao.PostMapper;
import com.ucan.dao.RoleMapper;
import com.ucan.dao.RolePostMapper;
import com.ucan.entity.Post;
import com.ucan.entity.Role;
import com.ucan.entity.RolePost;
import com.ucan.entity.tree.node.PostDTreeNode;
import com.ucan.entity.tree.response.DTreeResponse;
import com.ucan.entity.tree.response.Status;
import com.ucan.service.IPostService;
import com.ucan.utils.UUIDUtil;

/**
 * @Description:职位信息
 * @author liming.cen
 * @date 2023年2月10日 下午6:47:30
 */
@Service
public class PostServiceImpl implements IPostService {
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private RolePostMapper rolePostMapper;
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public int addPost(Post post) throws CustomException {
	int result = 0;
	// 生成postId
	post.setPostId(UUIDUtil.getUuid());
	int postUpdate = postMapper.insert(post);
	if (postUpdate > 0) {// 给职位分配基础角色
	    Role basicRole = roleMapper.queryBasicRole();
	    RolePost rolePost = new RolePost();
	    rolePost.setRoleId(basicRole.getRoleId());
	    rolePost.setPostId(post.getPostId());
	    // 分配基础角色
	    int rolePostUpdate = rolePostMapper.insert(rolePost);
	    if (rolePostUpdate > 0) {
		result = rolePostUpdate;
	    } else {
		throw new CustomException("职位新增失败！原因：基础角色分配失败！");
	    }
	}
	return result;
    }

    @Override
    public DTreeResponse getPostNodesByOrgId(String orgId) {
	List<Post> posts = postMapper.queryPostsByOrgId(orgId);
	List<PostDTreeNode> nodes = new ArrayList<>();
	if (posts.size() > 0) {
	    for (Post post : posts) {
		if (post.getParentId().equals("-1")) {// 获取第一个节点
		    PostDTreeNode node = new PostDTreeNode();
		    node.setTypeAndChecked("0", "0");
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
	} else {
	    status.setCode(-1);
	    status.setMessage("没有查到数据！");
	}
	dtResponse.setStatus(status);
	dtResponse.setData(nodes);
	return dtResponse;
    }

    @Override
    public int updatePost(Post post) {
	return postMapper.update(post);
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
		node.setTypeAndChecked("0", "0");
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

    @Override
    public String getPostNameById(String postId) {
	return postMapper.getPostNameById(postId);
    }

    @Override
    public int deletePostById(String parentId) throws CustomException {
	int children = postMapper.queryPostCountByParentId(parentId);
	List<RolePost> rolePosts = rolePostMapper.getRolePostByPostId(parentId);
	int userCount = postMapper.queryUserPostCountByPostId(parentId);
	if (children > 0) {
	    throw new CustomException("该节点存在子节点，请先删除子节点！");
	} else if (userCount > 0) {
	    throw new CustomException("该职位存在用户，请先删除用户！");
	} else if (rolePosts.size() > 0) {
	    StringBuilder sb = new StringBuilder();
	    rolePosts.forEach(item -> {
		sb.append(item.getRole().getRoleName() + "，");
	    });
	    if (sb.length() > 0) {
		throw new CustomException("该职位还分配着以下角色：" + sb.toString() + "请先解绑角色！");
	    }
	}
	Post post = new Post();
	post.setPostId(parentId);
	return postMapper.delete(post);
    }

}
