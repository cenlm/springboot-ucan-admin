package com.ucan.controller.system;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson2.JSON;
import com.ucan.base.response.MsgEnum;
import com.ucan.base.response.Response;
import com.ucan.dao.PermissionMapper;
import com.ucan.entity.Permission;
import com.ucan.entity.page.PageParameter;
import com.ucan.entity.tree.node.PermissionTreeNode;
import com.ucan.entity.tree.response.DTreeResponse;
import com.ucan.service.IPermissionService;

/**
 * @author liming.cen
 * @date 2022年12月23日 下午10:58:47
 */
@Controller
@RequestMapping("/permission")
public class PermissionController {
    @Autowired
    private IPermissionService permissionService;

    @RequestMapping("/permission_list")
    public String toAddUserPage() {
	return "permission/permission_list";
    }

    @RequestMapping("/getPermissionsByPage")
    @ResponseBody
    public String getPermissionsByPage(Permission permission,
	    @RequestParam(name = "currentPage", defaultValue = "1") String currentPage,
	    @RequestParam(name = "pageSize", defaultValue = "5") String pageSize) {
	PageParameter page = new PageParameter(currentPage, pageSize);
	permission.setPage(page);
	List<Permission> permissions = permissionService.getPermissionsByPage(permission);
	if (permissions.size() > 0) {
	    return JSON.toJSONString(Response.respose(MsgEnum.SUCCESS, permissions, page));
	} else {
	    return JSON.toJSONString(Response.fail("查询失败！"));
	}
    }

    @RequestMapping("/addPermission")
    @ResponseBody
    public String addPermission(Permission permission) {
	String jsonDataString = "";
	if (permission.getPermissionName() == "" || null == permission.getPermissionName()) {
	    return JSON.toJSONString(Response.fail("权限名称不能为空！"));
	}
	if (permission.getPermissionCode() == "" || null == permission.getPermissionCode()) {
	    return JSON.toJSONString(Response.fail("权限编码不能为空！"));
	}
	int updCount = permissionService.insert(permission);
	if (updCount > 0) { // permission.getPermissionId() 为什么？因为permissionService.insert(permission)
			    // 已经为其赋值
	    Permission permissionFromServer = permissionService.queryById(permission.getPermissionId());
	    jsonDataString = JSON
		    .toJSONString(Response.respose(MsgEnum.SUCCESS.getCode(), "权限新增成功！", permissionFromServer, null));
	} else {
	    jsonDataString = JSON.toJSONString(Response.fail("权限新增失败！"));
	}

	return jsonDataString;
    }

    /**
     * 
     * @param user
     * @return
     */
    @RequestMapping("/updatePermission")
    @ResponseBody
    public String updatePermission(Permission permission) {
	if (permission.getPermissionName() == "" || null == permission.getPermissionName()) {
	    return JSON.toJSONString(Response.fail("权限名称不能为空！"));
	}
	if (permission.getPermissionCode() == "" || null == permission.getPermissionCode()) {
	    return JSON.toJSONString(Response.fail("权限编码不能为空！"));
	}
	String msg;
	permission.setModifyTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	int updateCount = permissionService.updatePermission(permission);
	if (updateCount > 0) {
	    msg = JSON.toJSONString(Response.success());
	} else {
	    msg = JSON.toJSONString(Response.fail());
	}
	return msg;
    }

    @RequestMapping("/delPermissionById")
    @ResponseBody
    public String delPermissionById(String permissionId) throws Exception {
	String result = "";
	int updCount = permissionService.deletePermissionById(permissionId);
	if (updCount > 0) {
	    result = JSON.toJSONString(Response.success("权限删除成功！"));
	} else {
	    result = JSON.toJSONString(Response.fail("权限删除失败！"));
	}
	return result;
    }

    @RequestMapping("/delPermissionByIds")
    @ResponseBody
    public String delPermissionByIds(@RequestParam(value = "permissionIds[]") List<String> permissionIds) {

	String result = "";
	int updPermissionCount = 0;
	if (permissionIds.size() > 0) {
	    updPermissionCount = permissionService.deleteByIds(permissionIds);
	    if (updPermissionCount > 0) {
		result = JSON.toJSONString(Response.success("成功删除 " + updPermissionCount + " 条记录！"));
	    } else {
		result = JSON.toJSONString(Response.fail());
	    }
	} else {
	    result = JSON.toJSONString(Response.fail("请选择要删除的权限！"));
	}

	return result;
    }

    @RequestMapping("/queryPermissionById")
    @ResponseBody
    public String queryPermissionById(@RequestParam(name = "permissionId") String permissionId) {
	String jsonDataString = "";
	Permission permission = permissionService.queryById(permissionId);
	if (permission != null) {// 如果有值，则继续查询该节点的父节点名称
	    Permission parent = permissionService.queryById(permission.getParentId());
	    permission.setParentName(null == parent ? "-1" : parent.getPermissionName());
	    jsonDataString = JSON.toJSONString(Response.respose(MsgEnum.SUCCESS, permission));
	} else {
	    jsonDataString = JSON.toJSONString(Response.respose(MsgEnum.FAIL, permission));
	}
	return jsonDataString;
    }

    /**
     * 获取所有权限节点数据
     * 
     * @return
     */
    @RequestMapping("/getPermissionTreeNodes")
    @ResponseBody
    public String getTreeTableNodes() {
	List<PermissionTreeNode> treeNodes = permissionService.getPermissionTreeNodes();
	if (treeNodes.size() > 0) {
	    return JSON.toJSONString(Response.success(treeNodes));
	}
	return JSON.toJSONString(Response.fail("查询失败"));
    }

    /**
     * 获取权限树，为某个角色的权限回显做准备
     * 
     * @param roleId        当前正在操作的角色节点的roleId
     * @param parentId      当前角色节点的父节点roleId
     * @param grandpaRoleId 当前节点的爷爷节点roleId
     * @return
     */
    @RequestMapping("/getPermissionTree4Role")
    @ResponseBody
    public String getPermissionTree4Role(@RequestParam(name = "roleId", defaultValue = "") String roleId,
	    @RequestParam(name = "parentId", defaultValue = "") String parentId,
	    @RequestParam(name = "grandpaRoleId", defaultValue = "") String grandpaRoleId) {
	DTreeResponse response = permissionService.getPermissionTree4Role(roleId, parentId, grandpaRoleId);
	return JSON.toJSONString(response);
    }
}
