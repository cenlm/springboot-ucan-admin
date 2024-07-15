package com.ucan.service;

import java.util.List;

import com.ucan.entity.Organization;
import com.ucan.entity.tree.response.DTreeResponse;

/**
 * @Description: 组织结构服务
 * @author liming.cen
 * @date 2023年2月10日 下午4:28:04
 */
public interface IOrganizationService {
    /**
     * 新增一条记录
     * 
     * @param org
     * @return
     */
    int addOrganization(Organization org) throws Exception;

    /**
     * 修改组织名称
     * 
     * @param org
     * @return
     */
    int updateOrgNameById(Organization org);

    /**
     * 删除组织架构
     * 
     * @param org
     * @return
     */
    int deleteOrganization(Organization org) throws Exception;

    /**
     * 获取某个组织结构节点的所有子孙节点orgId（包括当前节点的orgId）
     * 
     * @param orgId
     * @return
     */
    List<String> getAllChildrenIdsByOrgId(String orgId);

    /**
     * 返回组织结构节点数据（封装后的数据）
     * 
     * @return
     */
    DTreeResponse getOrganizationTreeNodes();

    /**
     * 通过用户Id获取<用户-组织>映射数据，并对用户已有组织信息进行回显
     * 
     * @param userId
     * @return
     */
    DTreeResponse getOrgTree4User(String userId);

}
