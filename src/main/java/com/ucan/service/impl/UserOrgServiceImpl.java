package com.ucan.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ucan.base.exception.CustomException;
import com.ucan.dao.PostMapper;
import com.ucan.dao.RoleMapper;
import com.ucan.dao.RoleOrganizationMapper;
import com.ucan.dao.RolePostMapper;
import com.ucan.dao.UserOrgMapper;
import com.ucan.entity.MutexRole;
import com.ucan.entity.RoleOrganization;
import com.ucan.entity.RolePost;
import com.ucan.entity.UserOrganization;
import com.ucan.entity.UserPost;
import com.ucan.service.IUserOrgService;

/**
 * @Description:<用户-组织>映射关系处理服务实现
 * @author liming.cen
 * @date 2023年2月24日 上午10:50:33
 */
@Service
public class UserOrgServiceImpl implements IUserOrgService {
    @Autowired
    private UserOrgMapper userOrgMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleOrganizationMapper roleOrganizationMapper;
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private RolePostMapper rolePostMapper;

    @Override
    public int updateUserOrgRelation(String userId, String isSuper, List<String> checkedOrgIds) throws CustomException {
	int finalResult = 0;
	if (checkedOrgIds.size() > 0 && !"1".equals(isSuper)) {// 如果不是超级管理员，才进行互斥判断
	    // 判断待分配的组织是否有互斥角色，以及当前用户的职位的角色是否有跟待分配的组织角色互斥
	    isOrgRoleMutex(checkedOrgIds, userId);
	}

	// 查询当前角色已分配的组织
	List<UserOrganization> currentUserOrgRelations = userOrgMapper.getUserOrgByUserId(userId);
	// 获取当前用户拥有的组织Id
	List<String> originUoIds = new ArrayList<>();
	// 需要去删除的<用户-组织>映射记录
	List<UserOrganization> rpRelationsNeedToDel = new ArrayList<>();
	// 需要去新增的<用户-组织>映射记录
	List<UserOrganization> rpRelationsNeedToAdd = new ArrayList<>();

	if (currentUserOrgRelations.size() > 0) {// 当前用户已拥有组织
	    currentUserOrgRelations.forEach(item -> {
		// 获得已有的组织Id
		originUoIds.add(item.getOrgId());
	    });
	    if (checkedOrgIds.size() <= 0) {
		// 用户原本就已经分配组织，但传入的组织Id数量个数为0，说明用户要被撤销原有的组织分配。
		int result = userOrgMapper.deleteBatch(currentUserOrgRelations);
		return result;
	    } else {// 如果用户已分配和待分配组织个数都不为0，则要计算（已分配和待分配组织的）交集和差集。
		    // 交集
		List<String> intersection = originUoIds.stream().filter(item -> checkedOrgIds.contains(item))
			.collect(Collectors.toList());
		if (intersection.size() <= 0) {// 用户已分配和待分配组织个数都不为0且交集为空，则要撤销已分配组织且分配组织

		    originUoIds.forEach(orgId -> {
			UserOrganization uo = new UserOrganization();
			uo.setUserId(userId);
			uo.setOrgId(orgId);
			rpRelationsNeedToDel.add(uo);
		    });
		    // 删除当前节点和其所有子孙节点匹配到的部分权限（或全部）
		    int delCount = userOrgMapper.deleteBatch(rpRelationsNeedToDel);
		    if (delCount < 0) {// 删除失败
			throw new CustomException("删除失败！原因：部分<用户-组织>关系撤销失败。");
		    }
		    // 分配新权限
		    checkedOrgIds.forEach(orgId -> {
			UserOrganization uo = new UserOrganization();
			uo.setUserId(userId);
			uo.setOrgId(orgId);
			rpRelationsNeedToAdd.add(uo);
		    });
		    // 分配新权限
		    int countAdd = userOrgMapper.insertBatch(rpRelationsNeedToAdd);
		    if (countAdd != rpRelationsNeedToAdd.size()) {// 没有完全新增成功，不算执行成功
			throw new CustomException("删除失败！原因：部分<用户-组织>关系新增失败。");
		    }
		    finalResult = 2;
		} else {// 交集不为空，则要删除已有权限与待分配权限的差集，新增待分配权限与已有权限的差集
			// 要删除的部分权限
		    List<String> reduceToDel = originUoIds.stream().filter(item -> !checkedOrgIds.contains(item))
			    .collect(Collectors.toList());
		    // 要新增的部分权限
		    List<String> reduceToAdd = checkedOrgIds.stream().filter(item -> !originUoIds.contains(item))
			    .collect(Collectors.toList());
		    if (reduceToDel.size() > 0) {// 有需要删除的部分权限
			reduceToDel.forEach(orgId -> {
			    UserOrganization uo = new UserOrganization();
			    uo.setUserId(userId);
			    uo.setOrgId(orgId);
			    rpRelationsNeedToDel.add(uo);
			});

			// 删除当前节点和其所有子孙节点的部分权限（或全部）
			int delCount = userOrgMapper.deleteBatch(rpRelationsNeedToDel);
			if (delCount < 0) {// 删除失败
			    throw new CustomException("删除失败！原因：部分<用户-组织>关系撤销失败。");
			}
		    }
		    if (reduceToAdd.size() > 0) {// 有需要新增的部分权限
			reduceToAdd.forEach(orgId -> {
			    UserOrganization uo = new UserOrganization();
			    uo.setUserId(userId);
			    uo.setOrgId(orgId);
			    rpRelationsNeedToAdd.add(uo);
			});

			// 新增部分权限
			int countAdd = userOrgMapper.insertBatch(rpRelationsNeedToAdd);
			if (countAdd != rpRelationsNeedToAdd.size()) {// 没有完全执行完成
			    throw new CustomException("删除失败！原因：部分<用户-组织>关系撤销失败。");
			}
		    }
		    finalResult = 2;
		}
	    }
	} else { // 当前角色还没有分配权限，则分配权限
	    if (checkedOrgIds.size() > 0) { // 有新的权限要分配
		checkedOrgIds.forEach(orgId -> {
		    UserOrganization uo = new UserOrganization();
		    uo.setUserId(userId);
		    uo.setOrgId(orgId);
		    rpRelationsNeedToAdd.add(uo);
		});

		// 分配新权限
		int result = userOrgMapper.insertBatch(rpRelationsNeedToAdd);
		return result;
	    } else {
		finalResult = 1;
	    }

	}
	return finalResult;

    }

    /**
     * 判断待分配组织是否存在互斥角色,以及当前用户的职位的角色是否有跟待分配的组织角色互斥
     * 
     * @param checkedOrgIds 待分配的组织Ids
     * @param 当前userId
     * @throws CustomException
     */
    private void isOrgRoleMutex(List<String> checkedOrgIds, String userId) throws CustomException {
	if (checkedOrgIds.size() > 0) {
	    // 从互斥角色表中查询所有互斥角色
	    List<MutexRole> mutexRoles = roleMapper.getMutexRolesByRoleId("");
	    // 互斥表中的所有互斥角色id
	    Set<String> knownMutexRoleIds = new HashSet<String>();
	    Map<String, Set<String>> mutexRoleMap = new HashMap<>();
	    Map<String, String> mutexRoleNameMap = new HashMap<>();

	    mutexRoles.forEach(item -> {
		// 因为是互斥关系，所以要将roleId和mutexRoleId都取出来，然后剔除掉自身的roleId,就得到所有与当前角色互斥的角色Id
		knownMutexRoleIds.add(item.getRoleId());
		knownMutexRoleIds.add(item.getMutexRoleId());
	    });

	    knownMutexRoleIds.forEach(knownMutexRoleId -> {// 第一轮遍历，通过roleId获取与roleId互斥的mutexRole集合
		Set<String> mutexRoleIdSet = new HashSet<String>();
		mutexRoles.forEach(mrItem -> {
		    if (mrItem.getRoleId().equals(knownMutexRoleId)) {
			mutexRoleIdSet.add(mrItem.getMutexRoleId());
			mutexRoleMap.put(knownMutexRoleId, mutexRoleIdSet);
			mutexRoleNameMap.put(knownMutexRoleId, mrItem.getRoleName());
		    } else {
			mutexRoleMap.put(knownMutexRoleId, mutexRoleIdSet);
		    }
		});
	    });

	    knownMutexRoleIds.forEach(knownMutexRoleId -> {// 第二轮遍历，通过mutexRole获取roleId集合
		mutexRoles.forEach(mrItem -> {
		    if (mrItem.getMutexRoleId().equals(knownMutexRoleId)) {
			// 第一轮遍历已经生成map，第二次遍历只需通过map.get()获取set集合，然后进行add操作即可。
			mutexRoleMap.get(knownMutexRoleId).add(mrItem.getRoleId());
			mutexRoleNameMap.put(knownMutexRoleId, mrItem.getMutexRoleName());
		    }
		});
	    });
	    // 获取待分配的组织的所有角色
	    List<RoleOrganization> roRelations = roleOrganizationMapper.getRoleOrgByOrgIds(checkedOrgIds);
	    // 到这一步，就已经把互斥角色表中的每个角色的互斥关系用以下形式记录下来了：
	    // 伪代码：map.put("roleId",new HashSet().add('所有与指定roleId互斥的roleId'))
	    // 获取即将要被分配角色的职位已有的角色
	    // 获取当前用户的职位及其该职位的所有角色
	    List<UserPost> userPosts = postMapper.queryUserPostByUserId(userId);
	    if (userPosts.size() > 0) {
		List<String> postIds = new ArrayList<>();
		// 获取该用户所有职位id
		userPosts.forEach(item -> {
		    postIds.add(item.getPostId());
		});
		// 获取用户职位的所有角色
		List<RolePost> rolePosts = rolePostMapper.getRolePostByPostIds(postIds);
		if (rolePosts.size() > 0) {
		    List<String> roleIds = new ArrayList<>();
		    Map<String, String> roleNameMap = new HashMap<>();
		    Map<String, String> postNameMap = new HashMap<>();
		    // 获取职位的所有角色
		    rolePosts.forEach(item -> {
			roleIds.add(item.getRoleId());
			roleNameMap.put(item.getRoleId(), item.getRole().getRoleName());
			postNameMap.put(item.getRoleId(), item.getPost().getPostName());
		    });
		    // 计算职位的角色是否能匹配到互斥表中的角色以判断是否存在互斥角色
		    List<String> hasMutexRoles = roleIds.stream().filter(roleId -> knownMutexRoleIds.contains(roleId))
			    .collect(Collectors.toList());
		    if (hasMutexRoles.size() > 0) {// 存在互斥角色
			StringBuilder resultSb = new StringBuilder();
			roRelations.forEach(roleOrg -> {
			    hasMutexRoles.forEach(mutexId -> {
				// mutexRoleMap.get(mutexId)是一个HashSet
				if (mutexRoleMap.get(mutexId).contains(roleOrg.getRoleId())) {
				    resultSb.append("职位【" + postNameMap.get(mutexId) + "】的角色【"
					    + roleNameMap.get(mutexId) + "】与组织【" + roleOrg.getOrganization().getOrgName() + "】的角色【"
					    + roleOrg.getRole().getRoleName() + "】互斥；");
				}
			    });
			});
			if (resultSb.length() > 0) {
			    throw new CustomException("分配失败！用户职位的角色与待分配组织的角色互斥：" + resultSb);
			}
		    }
		}
	    }

	    StringBuilder msg = new StringBuilder();
	    roRelations.forEach(item -> {
		Set<String> mutexRoleSet = mutexRoleMap.get(item.getRoleId());
		roRelations.forEach(item1 -> {
		    if (null != mutexRoleSet && !mutexRoleSet.isEmpty()) {
			mutexRoleSet.forEach(mutexId -> {
			    if (item1.getRoleId().equals(mutexId)) {
				msg.append("【" + item.getOrganization().getOrgName() + "—"
					+ item.getRole().getRoleName() + "】与【" + item1.getOrganization().getOrgName()
					+ "—" + item1.getRole().getRoleName() + "】，");
			    }
			});
		    }
		});

	    });
	    if (msg.length() > 0) {
		throw new CustomException("分配失败！存在互斥角色：" + msg + "请到角色管理模块进行调整！");
	    }
	}

//	for (Map.Entry<String, Set<String>> entry : mutexRoleMap.entrySet()) {
//	    System.out.println(entry.getKey() + "--->" + entry.getValue());
//	}
    }

    @Override
    public List<UserOrganization> getUserOrgByPage(UserOrganization userOrganization) {
	return userOrgMapper.getUserOrgByPage(userOrganization);
    }

}
