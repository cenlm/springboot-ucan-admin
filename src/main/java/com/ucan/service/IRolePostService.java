package com.ucan.service;

import com.ucan.entity.tree.response.DTreeResponse;

/**
 * @Description:<角色-职位>映射信息服务接口
 * @author liming.cen
 * @date 2023年2月22日 下午6:31:05
 */
public interface IRolePostService {
    /**
     * 角色管理模块：<角色-职位>映射的职位树信息回显
     * 
     * @param roleId
     * @return
     */
    DTreeResponse getRoleToPostTree(String orgId, String roleId);
}
