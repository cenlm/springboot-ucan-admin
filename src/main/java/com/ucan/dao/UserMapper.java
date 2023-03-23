package com.ucan.dao;

import java.util.List;
import java.util.Map;

import com.ucan.base.BaseDao;
import com.ucan.entity.User;

/**
 * @author liming.cen
 * @date 2022年12月23日 下午8:36:11
 */

public interface UserMapper extends BaseDao<User> {
    int updatePassword(User user);

    /**
     * 添加<用户-职位>关系映射
     * 
     * @param map
     * @return
     */
    int addUserPostMapping(Map<String, String> map);

    List<User> queryAll();

    /**
     * 分页查询用户
     * 
     * @param user
     * @return
     */
    List<User> queryUserByPage(User user);

    /**
     * 通过组织Id查询用户总数
     * 
     * @param orgIds
     * @return
     */
    int queryUsersCountsByOrgIds(Map<String, Object> map);

    /**
     * 通过组织Id查询用户
     * 
     * @param map
     * @return
     */
    List<User> queryUsersByOrgIdsPageWithMap(Map<String, Object> map);

    /**
     * 通过职位Id查询用户总数
     * 
     * @param orgIds
     * @return
     */
    int queryCountByPostId(Map<String, Object> map);

    /**
     * 通过职位Id查询用户（分页查询）
     * 
     * @param map
     * @return
     */
    List<User> queryUsersByPostId(Map<String, Object> map);

    /**
     * 通过用户id删除<用户-职位>记录
     * 
     * @param userId
     * @return
     */
    int deleteUserPostByUserId(String userId);

    /**
     * 通过批量userId删除<用户-职位>记录
     * 
     * @param userIds
     * @return
     */
    int deleteUserPostByUserIds(List<String> userIds);

    /**
     * 通过userId统计<用户-职位>记录数
     * 
     * @param userId
     * @return
     */
    int queryUserPostCountByUserId(String userId);

    /**
     * 通过批量userId统计<用户-职位>记录数
     * 
     * @param userIds
     * @return
     */
    int queryUserPostCountByUserIds(List<String> userIds);

    /**
     * 通过用户Id和密码查询用户（修改密码时，用于校验）
     * 
     * @param user
     * @return
     */
    int queryByPasswordAndUserId(User user);

    /**
     * 修改密码
     * 
     * @param paramMap
     * @return
     */
    int updatePassword(Map<String, String> paramMap);

    /**
     * 个人设置页详情内容
     * 
     * @param userId
     * @return
     */
    List<User> queryUserDetail(String userId);

   

}
