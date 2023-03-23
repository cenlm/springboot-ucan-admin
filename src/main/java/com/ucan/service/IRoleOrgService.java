package com.ucan.service;

import com.ucan.entity.RoleOrganization;
import com.ucan.entity.tree.response.DTreeResponse;

/**
 * @Description:<角色-组织>映射信息服务接口
 * @author liming.cen
 * @date 2023年2月22日 下午3:31:05
 */
public interface IRoleOrgService {
    /**
     * 角色分配到组织结构时的组织树渲染
     * 
     * @param roleId
     * @return
     */
    DTreeResponse getRoleToOrgTree(String roleId);

    /**
     * 新增一条<角色-组织>记录
     * 
     * @param roleOrg
     * @return
     */
    int insertRoleOrgRelation(RoleOrganization roleOrg);
}
