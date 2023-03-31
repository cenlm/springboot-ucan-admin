package com.ucan.controller.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson2.JSON;
import com.ucan.base.response.Response;
import com.ucan.entity.Organization;
import com.ucan.entity.tree.response.DTreeResponse;
import com.ucan.service.IOrganizationService;
import com.ucan.service.IRoleOrgService;
import com.ucan.service.IUserOrgService;

/**
 * 组织结构控制器
 * 
 * @author liming.cen
 * @date 2023年2月8日 下午19:04:15
 */
@Controller
@RequestMapping("/organization")
public class OrganizationController {
    @Autowired
    private IOrganizationService organizationService;
    @Autowired
    private IRoleOrgService roleOrgService;
    @Autowired
    private IUserOrgService userOrgService;

    @RequestMapping("/getOrganizationTreeNodes")
    @ResponseBody
    public String getOrganizationTreeNodes() {
	DTreeResponse response = organizationService.getOrganizationTreeNodes();
	return JSON.toJSONString(response);
    }

    /**
     * 用户组织树查询（用户模块-用户列表-分配组织）
     * 
     * @return
     */
    @RequestMapping("/getOrgTree4User")
    @ResponseBody
    public String getOrgTree4User(@RequestParam(name = "userId", defaultValue = "") String userId) {
	DTreeResponse response = organizationService.getOrgTree4User(userId);
	return JSON.toJSONString(response);
    }

    /**
     * <角色-组织>树回显
     * 
     * @param roleId
     * @return
     */
    @RequestMapping("/getRoleToOrgTree")
    @ResponseBody
    public String getRoleToOrgTree(@RequestParam(name = "roleId", defaultValue = "") String roleId) {
	DTreeResponse response = roleOrgService.getRoleToOrgTree(roleId);
	return JSON.toJSONString(response);
    }

    @RequestMapping("/updateUserOrgRelation")
    @ResponseBody
    public String updateUserOrgRelation(@RequestParam(name = "") String userId,
	    @RequestParam(name = "isSuper", defaultValue = "0") String isSuper,
	    @RequestParam(name = "checkedOrgIds[]", defaultValue = "") List<String> checkedOrgIds) throws Exception {
	String msg = "";
	int result = 0;

	    if (isSuper.equals("1")) {
		return JSON.toJSONString(Response.fail("不允许给超级管理员再分配组织！"));
	    }
	    result = userOrgService.updateUserOrgRelation(userId, isSuper, checkedOrgIds);

	    if (result > 0) {
		msg = JSON.toJSONString(Response.success("用户已成功分配组织!"));
	    } else {
		msg = JSON.toJSONString(Response.fail("组织分配失败！"));
	    }
	return msg;
    }

    /**
     * 新增组织结构
     * 
     * @param org
     * @return
     */
    @RequestMapping("/addOrganization")
    @ResponseBody
    public String addOrganization(Organization org) {
	String jsonString = "";
	int result;
	try {
	    result = organizationService.addOrganization(org);
	    if (result > 0) {
		jsonString = JSON.toJSONString(Response.success("成功新增组织：" + org.getOrgName() + "，并分配了基础角色！"));
	    } else {
		jsonString = JSON.toJSONString(Response.fail("组织结构数据新增失败！"));
	    }
	} catch (Exception e) {
	    JSON.toJSONString(Response.fail(e.getMessage()));
	    e.printStackTrace();
	}

	return jsonString;
    }

    /**
     * 删除组织结构
     * 
     * @param org
     * @return
     */
    @RequestMapping("/deleteOrganization")
    @ResponseBody
    public String deleteOrganization(Organization org) {
	String jsonString = "";
	int result;
	try {
	    result = organizationService.deleteOrganization(org);
	    if (result > 0) {
		jsonString = JSON.toJSONString(Response.success("删除成功！"));
	    } else {
		jsonString = JSON.toJSONString(Response.fail("删除失败！"));
	    }
	} catch (Exception e) {
	    jsonString = JSON.toJSONString(Response.fail(e.getMessage()));
	    e.printStackTrace();
	}

	return jsonString;
    }

    /**
     * 修改组织信息
     * 
     * @param org
     * @return
     */
    @RequestMapping("/updateOrganization")
    @ResponseBody
    public String updateOrganization(Organization org) {
	String jsonString = "";
	int result = organizationService.updateOrgNameById(org);
	if (result > 0) {
	    jsonString = JSON.toJSONString(Response.success("修改成功！"));
	} else {
	    jsonString = JSON.toJSONString(Response.fail("修改失败！"));
	}
	return jsonString;
    }
}
