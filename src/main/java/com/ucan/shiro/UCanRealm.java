package com.ucan.shiro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.ucan.entity.Permission;
import com.ucan.entity.Role;
import com.ucan.entity.RolePermission;
import com.ucan.entity.User;
import com.ucan.entity.UserRole;
import com.ucan.service.IPermissionService;
import com.ucan.service.IRolePermissionService;
import com.ucan.service.IUserRoleService;
import com.ucan.service.IUserService;

/**
 * @author liming.cen
 * @date 2022年12月25日 上午10:36:08
 */
public class UCanRealm extends AuthorizingRealm {
    @Autowired
    private IUserService userService;
    @Autowired
    private IUserRoleService userRoleService;
    @Autowired
    private IRolePermissionService rolePermissionService;
    @Autowired
    private IPermissionService permissionService;

    /**
     * 认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
	String name = (String) token.getPrincipal();
	SimpleAuthenticationInfo info = null;
	try {
	    User user = userService.queryByName(name);
	    if (null == user) {
		throw new UnknownAccountException("抱歉，没有查询到账号！");
	    } else {
		info = new SimpleAuthenticationInfo(user.getUserName(), user.getPassword(), getName());
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}

	return info;
    }

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
	String name = (String) principals.getPrimaryPrincipal();
	Map<String, User> map = new HashMap<>();

	Set<String> roleIds = new HashSet<>();
	Set<String> roleCodes = new HashSet<>();
	List<UserRole> userRoles = getRoles(name, map);
	if (userRoles.size() > 0) {
	    userRoles.forEach(item -> {
		roleIds.add(item.getRoleId());
		Role role = item.getRole();
		if (null != role) {
		    roleCodes.add(role.getRoleCode());
		}
	    });

	}
	Set<String> permissionCodes = getPermissions(roleIds, map);
	SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
	info.setRoles(roleCodes);
	info.setStringPermissions(permissionCodes);
	return info;
    }

    /**
     * 获取某用户的所有角色ID和角色编码
     * 
     * @param userName
     * @return
     */
    private List<UserRole> getRoles(String userName, Map<String, User> map) {
	User user = userService.queryByName(userName);
	List<UserRole> userRoles = new ArrayList<>();
	try {
	    if (null == user) {
		map.put("user", user);
		throw new UnknownAccountException("抱歉，没有查询到该用户！");
	    } else {
		map.put("user", user);
		if (user.getIsSuper() == 1) {// 如果是超级管理员，则直接获取所有roleId
		    userRoles.addAll(userRoleService.queryAllRoleIds());
		} else {
		    userRoles.addAll(userRoleService.queryRolesByUserId(user.getUserId()));
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return userRoles;
    }

    /**
     * 获取某用户的所有权限ID和权限编码
     * 
     * @param roleIds
     * @return
     */
    private Set<String> getPermissions(Set<String> roleIds, Map<String, User> map) {
	List<RolePermission> rolePermissions = null;
	Set<String> permissionSet = new HashSet<String>();
	User user = map.get("user");
	if (null != user) {
	    if (user.getIsSuper() == 1) {// 如果是超级管理员，则直接获取所有权限id
		List<Permission> allPermissions = permissionService.queryAllPermissions();
		if (allPermissions.size() > 0) {
		    allPermissions.forEach(item -> {
			permissionSet.add(item.getPermissionCode());
		    });
		}
	    } else {
		for (String roleId : roleIds) {
		    rolePermissions = rolePermissionService.queryPermissionsByRoleId(roleId);
		    if (rolePermissions.size() > 0) {
			for (RolePermission rolePermission : rolePermissions) {
			    // 以权限ID作为权限标识
			    // permissionSet.add(rolePermission.getPermissionId());
			    // 以权限编码作为权限标识
			    permissionSet.add(rolePermission.getPermission().getPermissionCode());
			}
		    }
		}
	    }
	} else {
	    throw new UnknownAccountException("抱歉，没有查询到该用户！");
	}

	return permissionSet;
    }

}
