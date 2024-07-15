package com.ucan.service;

import com.ucan.entity.Post;
import com.ucan.entity.tree.response.DTreeResponse;

/**
 * @Description: 职位信息服务
 * @author liming.cen
 * @date 2023年2月10日 下午4:28:04
 */
public interface IPostService {
    /**
     * 新增一条记录
     * 
     * @param org
     * @return
     */
    int addPost(Post post) throws Exception;

    /**
     * 更新职位信息
     * 
     * @param post
     * @return
     */
    int updatePost(Post post);

    /**
     * 通过组织ID查询职位信息并返回节点树数据
     * 
     * @param orgId
     * @return
     */
    DTreeResponse getPostNodesByOrgId(String orgId);

    /**
     * 获取职位名称
     * 
     * @param postId
     * @return
     */
    String getPostNameById(String postId);

    /**
     * 通过职位Id删除职位
     * 
     * @param postId
     * @return
     */
    int deletePostById(String postId) throws Exception;
}
