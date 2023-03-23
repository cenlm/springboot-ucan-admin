package com.ucan.service;

import java.util.List;
import java.util.Map;

import com.ucan.entity.Organization;
import com.ucan.entity.User;

/**
 * @author liming.cen
 * @date 2022年12月23日 下午9:42:20
 */
public interface IUserService {
    int insert(User user);

    /**
     * 新增用户信息及<用户-职位>、<用户-用户组>映射关系
     * 
     * @param obj
     * @return
     */
    int insert(User user, Map<String, String> paramMap) throws Exception;

    /**
     * 添加<用户-职位>关系映射
     * 
     * @param map
     * @return
     */
    int addUserPostMapping(Map<String, String> map);

    int insertBatch(List<User> users);

    /**
     * 删除用户记录（用户基本信息+<用户-角色>映射记录）
     * 
     * @param id
     * @return
     */
    int deleteById(String id) throws Exception;

    int deleteByIds(List<String> ids) throws Exception;

    /**
     * 更新用户信息
     * 
     * @param obj
     * @return
     */
    int update(User user) throws Exception;

    /**
     * 更新用户状态
     * 
     * @param obj
     * @return
     */
    int updateUserStatus(User user);

    int updateBatch(List<User> users);

    /**
     * 查询用户个人信息
     * 
     * @param id
     * @return
     */
    User queryById(String id);

    User queryByName(String name);

    List<User> queryAll();

    List<User> queryAllByPage();

    /**
     * 分页查询用户
     * 
     * @param user
     * @return
     */
    List<User> queryUserByPage(User user);

    /**
     * 通过组织Id查询用户
     * 
     * @param org
     * @return
     */
    List<User> queryUsersByOrgIdsPageWithMap(Map<String, Object> map);

    /**
     * 通过组织Id查询用户总数
     * 
     * @param orgIds
     * @return
     */
    int queryUsersCountsByOrgIds(Map<String, Object> map);

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
     * 更新密码
     * 
     * @param paramMap
     * @return
     */
    Map<String, String> updatePassword(String userId, String oldPassword, String newPassword);

    /**
     * 个人设置页详情内容
     * 
     * @param userId
     * @return
     */
    List<User> queryUserDetail(String userId);
}
