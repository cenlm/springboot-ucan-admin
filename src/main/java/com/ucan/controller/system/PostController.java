package com.ucan.controller.system;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson2.JSON;
import com.ucan.base.response.Response;
import com.ucan.entity.Post;
import com.ucan.entity.tree.node.PostTreeNode;
import com.ucan.entity.tree.node.TreeNode;
import com.ucan.entity.tree.response.DTreeResponse;
import com.ucan.service.IPostService;
import com.ucan.service.IRolePostService;

/**
 * 职位信息控制器
 * 
 * @author liming.cen
 *
 */
@Controller
@RequestMapping("/post")
public class PostController {
    @Autowired
    private IPostService postService;
    @Autowired
    private IRolePostService rolePostService;

    @RequestMapping("/getPostTreeNodes")
    @ResponseBody
    public String getPostTreeNodes(@RequestParam(name = "orgId", defaultValue = "0") String orgId) {
	DTreeResponse response = postService.getPostNodesByOrgId(orgId);
	return JSON.toJSONString(response);
    }

    /**
     * 角色已分配的职位信息查询（职位信息树）
     * 
     * @param orgId
     * @return
     */
    @RequestMapping("/getRoleToPostTree")
    @ResponseBody
    public String getRoleToPostTree(@RequestParam(name = "orgId", defaultValue = "0") String orgId,
	    @RequestParam(name = "roleId", defaultValue = "0") String roleId) {
	DTreeResponse response = rolePostService.getRoleToPostTree(orgId, roleId);
	return JSON.toJSONString(response);
    }

    @RequestMapping("/addPost")
    @ResponseBody
    public String addPost(Post post) throws Exception {
	String msg = "";
	    int result = postService.addPost(post);
	    if (result > 0) {
		return JSON.toJSONString(Response.success("成功新增职位：【" + post.getPostName() + "】，并分配了基础角色！"));
	    } else {
		return JSON.toJSONString(Response.fail("职位新增失败！"));
	    }
    }

    @RequestMapping("/updatePost")
    @ResponseBody
    public String updatePost(Post post) {

	int result = postService.updatePost(post);
	if (result <= 0) {
	    return JSON.toJSONString(Response.fail("职位信息更新失败！"));
	}
	return JSON.toJSONString(Response.success("职位信息更新成功！"));
    }

    @RequestMapping("/deletePost")
    @ResponseBody
    public String deletePost(@RequestParam(name = "postId", defaultValue = "") String postId) {

	String msg = "";
	try {
	    int result = postService.deletePostById(postId);
	    if (result <= 0) {
		msg = JSON.toJSONString(Response.fail("职位删除失败！"));
	    } else {
		msg = JSON.toJSONString(Response.success("职位删除成功！"));
	    }

	} catch (Exception e) {
	    msg = JSON.toJSONString(Response.fail(e.getMessage()));
	    e.printStackTrace();
	}
	return msg;
    }
}
