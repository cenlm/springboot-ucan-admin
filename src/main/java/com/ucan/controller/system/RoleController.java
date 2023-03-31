package com.ucan.controller.system;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson2.JSON;
import com.ucan.base.response.MsgEnum;
import com.ucan.base.response.Response;
import com.ucan.entity.MutexRole;
import com.ucan.entity.Role;
import com.ucan.entity.RolePermission;
import com.ucan.entity.User;
import com.ucan.entity.UserRole;
import com.ucan.entity.page.PageParameter;
import com.ucan.entity.tree.response.DTreeResponse;
import com.ucan.service.IPermissionService;
import com.ucan.service.IRolePermissionService;
import com.ucan.service.IRoleService;
import com.ucan.service.IUserRoleService;

/**
 * @author liming.cen
 * @date 2023年2月8日 下午19:04:15
 */
@Controller
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private IRoleService roleService;

    @Autowired
    private IPermissionService permissionService;

    @Autowired
    private IUserRoleService userRoleService;
    @Autowired
    private IRolePermissionService rolePermissionService;
//
//    @Autowired
//    private IUserService userService;

    @RequestMapping("/role_list")
    public String toRolePage() {
	return "role/role_list";
    }

    /**
     * 新增角色
     * 
     * @param role
     * @return
     * @throws Exception 
     */
    @RequestMapping("/addRole")
    @ResponseBody
    public String addRole(Role role) throws Exception {
	if (role.getPosition() == 0) {
	    role.setPosition(1);
	}
	role.setIsSuper(0);
	String msg;
	    int result = roleService.insert(role);
	    if (result > 0) {
		msg = JSON.toJSONString(Response.success("角色新增成功！"));
	    } else {
		msg = JSON.toJSONString(Response.fail("角色新增失败！"));
	    }
	return msg;
    }

    /**
     * 删除角色：删除角色及其子孙角色，并解绑对应角色与组织架构、职位、<br>
     * （普通用户组：待办）、权限信息的映射关系
     * 
     * @param role
     * @return
     * @throws Exception 
     */
    @RequestMapping("/deleteRole")
    @ResponseBody
    public String deleteRole(@RequestParam(name = "roleId") String roleId,
	    @RequestParam(name = "position", defaultValue = "0") String position) throws Exception {
	String jsonString = "";
	    Role role = roleService.queryRoleById(roleId);
	    if (role.getIsSuper() == 1) {
		return JSON.toJSONString(Response.fail("不能删除超级管理员！"));
	    } else if (role.getIsSuper() == 2) {
		return JSON.toJSONString(Response.fail("不能删除基础角色！"));
	    }
	    int result = roleService.deleteRolesByRoleId(roleId);
	    if (result > 0) {
		jsonString = JSON.toJSONString(Response.success("角色删除成功！"));
	    } else {
		jsonString = JSON.toJSONString(Response.fail("角色删除失败！"));
	    }
	return jsonString;
    }

    /**
     * 修改角色信息
     * 
     * @param role
     * @return
     * @throws Exception 
     */
    @RequestMapping("/updateRole")
    @ResponseBody
    public String updateRole(Role role) throws Exception {
	if (role.getPosition() == 0 && role.getIsSuper() != 1) {
	    role.setPosition(1);
	}
	String msg;
	    int result = roleService.update(role);
	    if (result > 0) {
		msg = JSON.toJSONString(Response.success("角色信息修改成功！"));
	    } else {
		msg = JSON.toJSONString(Response.fail("角色信息修改失败！"));
	    }
	return msg;
    }

    /**
     * 返回角色节点树数据
     * 
     * @return
     */
    @RequestMapping("/getRoleTreeNodes")
    @ResponseBody
    public String getRoleTreeNodes() {
	DTreeResponse response = roleService.queryRoleTreeNodes();
	return JSON.toJSONString(response);
    }

    /**
     * 获取某个角色拥有的所有权限ID
     * 
     * @param roleId
     * @return
     */
    @RequestMapping("/getPermissionIdsByRoleId")
    @ResponseBody
    public String getPermissionIdsByRoleId(@RequestParam(name = "roleId", defaultValue = "") String roleId) {
	List<RolePermission> rolePermission = rolePermissionService.queryPermissionsIdByRoleId(roleId);
	List<String> permissionIds = new ArrayList<>();
	if (rolePermission.size() > 0) {
	    rolePermission.forEach(item -> {
		// 该节点的子节点个数
		int childrens = permissionService.queryCountByParentId(item.getPermissionId());
		if (childrens <= 0) {// 筛选没有子节点的节点，因为layui节点树选择父节点的时候，
				     // 会全选其下的所有子节点，不符合需求
		    permissionIds.add(item.getPermissionId());
		}
	    });
	    return JSON.toJSONString(Response.success(permissionIds));
	} else {
	    return JSON
		    .toJSONString(Response.respCustomMsgWithData(MsgEnum.FAIL.getCode(), "没有查询到权限数据！", permissionIds));
	}
    }

    /**
     * 查询某个角色之下的用户
     * 
     * @param role
     * @param currentPage
     * @param pageSize
     * @return
     */
    @RequestMapping("/queryRoleUsersByPage")
    @ResponseBody
    public String queryRoleUsersByPage(UserRole userRole,
	    @RequestParam(name = "currentPage", defaultValue = "1") String currentPage,
	    @RequestParam(name = "pageSize", defaultValue = "5") String pageSize) {
	PageParameter page = new PageParameter(currentPage, pageSize);
	userRole.setPage(page);
	List<UserRole> userRoles = userRoleService.queryRoleUsersByPage(userRole);
	if (userRoles.size() > 0) {
	    return JSON.toJSONString(Response.respose(MsgEnum.SUCCESS, userRoles, page));
	} else {
	    return JSON.toJSONString(Response.respose(-1, "没有查询到数据", userRoles, page));
	}

    }

    /**
     * 查询某个角色的互斥角色
     * 
     * @param role
     * @param currentPage
     * @param pageSize
     * @return
     */
    @RequestMapping("/getMutexRolesByPage")
    @ResponseBody
    public String getMutexRolesByPage(MutexRole mutexRole,
	    @RequestParam(name = "currentPage", defaultValue = "1") String currentPage,
	    @RequestParam(name = "pageSize", defaultValue = "5") String pageSize) {
	PageParameter page = new PageParameter(currentPage, pageSize);
	mutexRole.setPage(page);
	List<MutexRole> mutexRoles = roleService.getMutexRolesByPage(mutexRole);
	if (mutexRoles.size() > 0) {
	    return JSON.toJSONString(Response.respose(MsgEnum.SUCCESS, mutexRoles, page));
	} else {
	    return JSON.toJSONString(Response.respose(-1, "没有查询到数据", mutexRoles, page));
	}

    }

    /**
     * 删除互斥角色记录
     * 
     * @param role
     * @param currentPage
     * @param pageSize
     * @return
     */
    @RequestMapping("/deleteMutexRole")
    @ResponseBody
    public String deleteMutexRole(MutexRole mutexRole) {
	int result = roleService.deleteMutexRoleById(mutexRole);
	if (result > 0) {
	    return JSON.toJSONString(Response.success("互斥记录删除成功！"));
	} else {
	    return JSON.toJSONString(Response.fail("互斥记录删除失败！"));
	}
    }

    /**
     * 新增互斥角色记录
     * 
     * @param mutexRole
     * @param currentPage
     * @param pageSize
     * @return
     */
    @RequestMapping("/addMutexRole")
    @ResponseBody
    public String addMutexRole(MutexRole mutexRole) {
	int result = roleService.insertMutexRole(mutexRole);
	if (result > 0) {
	    return JSON.toJSONString(Response.success("互斥记录新增成功！"));
	} else {
	    return JSON.toJSONString(Response.fail("互斥角色新增失败！原因：互斥角色已存在。"));
	}

    }

    /**
     * 更新<角色-权限>映射关系<br>
     * 1.父角色的权限分配会影响子角色权限的新增或减少（子角色不能越权，如副经理不能拥有经理没有的权限）<br>
     * 2.子角色新增权限，父角色要继承子角色权限（如：经理 将拥有 副经理的所有权限）。
     * 第二点功能实现说明：当子角色分配权限时，只能选择父角色已分配的权限（权限复选框动态禁用）
     * 
     * @param roleId     要修改权限的角色Id
     * @param checkedIds 前端传入的权限Id
     * @return
     * @throws Exception 
     */
    @RequestMapping("/updateRolePermissionRelation")
    @ResponseBody
    public String updateRolePermissionRelation(@RequestParam(name = "roleId", defaultValue = "") String roleId,
	    @RequestParam(name = "checkedIds[]", required = false, defaultValue = "") List<String> checkedIds) throws Exception {
	String msg;
	    int result = rolePermissionService.updateRolePermissionRelation(roleId, checkedIds);
	    if (result > 0) {
		msg = JSON.toJSONString(Response.success("权限分配成功!"));
	    } else {
		msg = JSON.toJSONString(Response.fail("权限分配失败！"));
	    }
	return msg;
    }

    /**
     * 更新<角色-组织>映射关系
     * 
     * @param roleId         当前角色Id
     * @param orgId          当前组织Id
     * @param checkedOrgIds  前端传入的组织Id
     * @param checkedPostIds 前端传入的职位Id
     * @return
     * @throws Exception 
     */
    @RequestMapping("/updateRoleOrgPostRelation")
    @ResponseBody
    public String updateRoleOrgPostRelation(@RequestParam(name = "roleId", defaultValue = "") String roleId,
	    @RequestParam(name = "orgId", defaultValue = "") String orgId,
	    @RequestParam(name = "checkedOrgIds[]", required = false, defaultValue = "") List<String> checkedOrgIds,
	    @RequestParam(name = "checkedPostIds[]", required = false, defaultValue = "") List<String> checkedPostIds) throws Exception {
	String msg;
	    int result = roleService.updateRoleOrgPostRelation(roleId, orgId, checkedOrgIds, checkedPostIds);
	    if (result > 0) {
		msg = JSON.toJSONString(Response.success("角色已成功分配组织/职位!"));
	    } else {
		msg = JSON.toJSONString(Response.fail("角色分配组织/职位失败！"));
	    }
	return msg;
    }

//    /**
//     * 更新<角色-职位>映射关系
//     * 
//     * @param roleId         要修改权限的角色Id
//     * @param orgId          当前正在处理<角色-职位>映射关系的组织结构
//     * @param checkedPostIds 前端传入的职位Id
//     * @return
//     */
//    @RequestMapping("/updateRolePostRelation")
//    @ResponseBody
//    public String updateRolePostRelation(@RequestParam(name = "roleId", defaultValue = "") String roleId,
//	    @RequestParam(name = "orgId", defaultValue = "") String orgId,
//	    @RequestParam(name = "checkedPostIds[]", required = false, defaultValue = "") List<String> checkedPostIds) {
//	String msg;
//	try {
//	    int result = roleService.updateRolePostRelation(roleId, orgId, checkedPostIds);
//	    if (result > 0) {
//		msg = JSON.toJSONString(Response.success("角色已成功分配职位!"));
//	    } else {
//		msg = JSON.toJSONString(Response.fail("角色分配职位失败！"));
//	    }
//	} catch (Exception e) {
//	    msg = JSON.toJSONString(Response.fail(e.getMessage()));
//	    e.printStackTrace();
//	}
//	return msg;
//    }

    /**
     * 查询父角色的所有权限，用户限制子角色的权限选择 角色权限继承。 如：部门经理拥有部门副经理的所有权限，而部门副经理只能选择部门经理已有的权限，防止越权
     * 
     * @param parentRoleId
     * @return
     */
    @RequestMapping("/getParentRolePermissionId")
    @ResponseBody
    public String getPermissionIdByParentId(@RequestParam(name = "parentId", defaultValue = "") String parentId) {
	List<RolePermission> rolePermissions = rolePermissionService.queryPermissionsByRoleId(parentId);
	List<String> permissionIds = new ArrayList<>();
	if (rolePermissions.size() > 0) {
	    rolePermissions.forEach(item -> {
		permissionIds.add(item.getPermissionId());
	    });
	    return JSON.toJSONString(Response.success(permissionIds));
	}
	return JSON
		.toJSONString(Response.respCustomMsgWithData(MsgEnum.SUCCESS.getCode(), "查询成功，但没有数据。", permissionIds));
    }

//    @RequestMapping("/addUser")
//    @ResponseBody
//    public String addUser(User user) {
//	String jsonDataString = "";
//	if (user.getUserName() == "" || user.getUserName() == null) {
//	    jsonDataString = JSON.toJSONString(Response.fail("用户名不能为空！"));
//	}
//
//	int updCount = userService.insert(user);
//	if (updCount > 0) {
//	    User userFromServer = userService.queryById(user.getUserId());
//	    jsonDataString = JSON.toJSONString(Response.respose(MsgEnum.SUCCESS, userFromServer));
//	} else {
//	    jsonDataString = JSON.toJSONString(Response.fail());
//	}
//
//	return jsonDataString;
//    }

//    @RequestMapping("/updateUser")
//    @ResponseBody
//    public String updateUser(@RequestBody User user) {
//	String msg;
//	user.setModifyTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//	int updateCount = userService.update(user);
//	if (updateCount > 0) {
//	    msg = JSON.toJSONString(Response.success());
//	} else {
//	    msg = JSON.toJSONString(Response.fail());
//	}
//	return msg;
//    }

//    /**
//     * 更新用户可用状态
//     * 
//     * @param user
//     * @return
//     */
//
//    @RequestMapping("/updateUserStatus")
//    @ResponseBody
//    public String updateUserStatus(@RequestBody User user) {
//	String msg;
//	user.setModifyTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//	int updateCount = userService.update(user);
//	if (updateCount > 0) {
//	    msg = JSON.toJSONString(Response.success());
//	} else {
//	    msg = JSON.toJSONString(Response.fail());
//	}
//	return msg;
//    }

//    @RequestMapping("/delUserById")
//    @ResponseBody
//    public String deleteUserById(String userId) {
//	String result = "";
//	int updUserCount = userService.deleteById(userId);
//	if (updUserCount > 0) {
//	    result = JSON.toJSONString(Response.success());
//	} else {
//	    result = JSON.toJSONString(Response.fail());
//	}
//
//	return result;
//    }
//
//    @RequestMapping("/delUsersById")
//    @ResponseBody
//    public String delUsersById(@RequestParam(value = "userIds[]") List<String> userIds) {
//
//	String result = "";
//	int updUserCount = 0;
//	if (userIds.size() > 0) {
//	    updUserCount = userService.deleteByIds(userIds);
//	    if (updUserCount > 0) {
//		result = JSON.toJSONString(Response.success("成功删除 " + updUserCount + " 条记录！"));
//	    } else {
//		result = JSON.toJSONString(Response.fail());
//	    }
//	} else {
//	    result = JSON.toJSONString(Response.fail("请选中用户！"));
//	}
//
//	return result;
//    }
//
//    @RequestMapping("/queryUserById")
//    @ResponseBody
//    public String queryUserById(String userId) {
//	String jsonDataString = "";
//	User user = userService.queryById(userId);
//	if (user != null) {
//	    jsonDataString = JSON.toJSONString(Response.respose(MsgEnum.SUCCESS, user));
//	} else {
//	    jsonDataString = JSON.toJSONString(Response.respose(MsgEnum.FAIL, user));
//	}
//	return jsonDataString;
//    }

}
