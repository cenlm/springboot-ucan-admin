package com.ucan.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import com.ucan.dao.UserMapper;
import com.ucan.dao.UserOrgMapper;
import com.ucan.entity.User;
import com.ucan.entity.UserOrganization;
import com.ucan.exception.CustomException;
import com.ucan.service.IUserService;
import com.ucan.shiro.util.EncryptionUtil;
import com.ucan.util.UUIDUtil;

/**
 * 用户信息服务
 * 
 * @author liming.cen
 * @date 2022年12月23日 下午9:44:27
 */
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserOrgMapper userOrgMapper;

    public int insert(User user) {
        int result = 0;
        String userName = user.getUserName();
        // 查询是否已存在该用户
        User userFromServer = queryByName(userName);
        if (null == userFromServer) {// 用户不存在，则新增用户
            // 表单接收到的未加密的明文密码
            String passwordStr = user.getPassword();
            if (passwordStr == "" || passwordStr == null) {// 如果前端不填写密码，则默认明文密码：123456
                user.setPassword(EncryptionUtil.md5Encode("123456"));
            } else {
                user.setPassword(EncryptionUtil.md5Encode(passwordStr));
            }
            user.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            user.setUserId(UUIDUtil.getUuid());
            result = userMapper.insert(user);
        }
        return result;
    }

    @Override
    public User queryByName(String name) {
        return userMapper.queryByName(name);
    }

    @Override
    public int insertBatch(List<User> objs) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int deleteById(String id) throws CustomException {
        // 查询该用户是否已分配至组织
        List<UserOrganization> userOrgs = userOrgMapper.getUserOrgByUserId(id);

        if (userOrgs.size() > 0) { // 用户已分配组织，要解除<用户-组织>关系
            int unBundleCounts = userOrgMapper.deleteBatch(userOrgs);
            if (unBundleCounts != userOrgs.size()) {
                throw new CustomException("用户删除失败！原因：用户与已分配组织解绑失败！");
            }
        }
        int userPostCounts = userMapper.queryUserPostCountByUserId(id);
        if (userPostCounts > 0) {// 用户与职位有绑定，需要解绑<用户-职位>映射
            int userPostUpdate = userMapper.deleteUserPostByUserId(id);
            if (userPostUpdate != userPostCounts) {
                throw new CustomException("用户删除失败！原因：用户与职位解绑失败！");
            }
        }

        return userMapper.deleteById(id);
    }

    @Override
    public int deleteByIds(List<String> ids) throws CustomException {
        int countsByUserIds = userOrgMapper.getCountsByUserIds(ids);
        if (countsByUserIds > 0) {// 已存在<用户-组织>映射关系，需要解绑
            int delUserOrgByIds = userOrgMapper.deleteByIds(ids);
            if (delUserOrgByIds != countsByUserIds) {
                throw new CustomException("删除失败！原因：用户与组织解绑失败！");
            }
        }
        int userPostCounts = userMapper.queryUserPostCountByUserIds(ids);
        if (userPostCounts > 0) {// 已存在<用户-职位>映射关系，需要解绑
            int delUserPosts = userMapper.deleteUserPostByUserIds(ids);
            if (delUserPosts != userPostCounts) {
                throw new CustomException("删除失败！原因：用户与职位解绑失败！");
            }
        }
        return userMapper.deleteByIds(ids);
    }

    @Override
    public int update(User user) throws CustomException {
        Subject subject = SecurityUtils.getSubject();
        String principal = (String) subject.getPrincipal();
        String currentUserId = (String) subject.getSession(false).getAttribute("currentUserId");
        // rememberMe情况下，不能从subject的session中获取到userId，需要通过用户名从数据库中查询
        if (Objects.isNull(currentUserId) && !Objects.isNull(principal) && user.getUserId().equals("null")) {
            User existingUser = userMapper.queryByName(principal);
            user.setUserId(existingUser.getUserId());
        }
        int result = userMapper.update(user);
        int userCount = userMapper.queryUsersCountByName(user.getUserName());
        if (userCount > 1) {// 更新后发现用户名冲突，抛异常回滚更新操作
            throw new CustomException("用户名已被占用，更新失败！");
        }
        return result;
    }

    @Override
    public int updateBatch(List<User> objs) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public User queryById(String id) {
        return userMapper.queryById(id);
    }

    @Override
    public List<User> queryAll() {
        return userMapper.queryAll();
    }

    @Override
    public List<User> queryAllByPage() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<User> queryUserByPage(User user) {
        return userMapper.queryUserByPage(user);
    }

    @Override
    public List<User> queryUsersByOrgIdsPageWithMap(Map<String, Object> map) {
        return userMapper.queryUsersByOrgIdsPageWithMap(map);
    }

    @Override
    public int queryUsersCountsByOrgIds(Map<String, Object> map) {
        return userMapper.queryUsersCountsByOrgIds(map);
    }

    @Override
    public int queryCountByPostId(Map<String, Object> map) {
        return userMapper.queryCountByPostId(map);
    }

    @Override
    public List<User> queryUsersByPostId(Map<String, Object> map) {
        return userMapper.queryUsersByPostId(map);
    }

    @Override
    public int addUserPostMapping(Map<String, String> map) {
        return userMapper.addUserPostMapping(map);
    }

    @Override
    public int insert(User user, Map<String, String> paramMap) throws CustomException {

        int result = 0;
        String userName = user.getUserName();
        // 查询是否已存在该用户
        User userFromServer = queryByName(userName);
        if (null == userFromServer) {// 用户不存在，则新增用户
            // 表单接收到的未加密的明文密码
            String passwordStr = user.getPassword();
            if (passwordStr == "" || passwordStr == null) {// 如果前端不填写密码，则默认明文密码：123456
                user.setPassword(EncryptionUtil.md5Encode("123456"));
            } else {
                user.setPassword(EncryptionUtil.md5Encode(passwordStr));
            }
            user.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            user.setUserId(UUIDUtil.getUuid());
            user.setIsSuper(0);
            int count = userMapper.insert(user);
            if (count > 0) {// 用户基本信息添加成功，继续添加<用户-职位>、<用户-用户组>映射关系
                paramMap.put("userId", user.getUserId());
                int mappingUpdate = 0;
                if (paramMap.get("type").equals("post")) {// <用户-职位>映射
                    mappingUpdate = userMapper.addUserPostMapping(paramMap);
                    if (mappingUpdate > 0) {
                        result = 1;
                    }
                } else {// TODO:<用户-用户组>映射

                }
                if (mappingUpdate <= 0) {// 映射关系添加失败
                    throw new CustomException("用户新增失败！原因：映射关系添加失败。");
                }
            }

        } else {
            throw new CustomException("用户新增失败！原因：已存在该用户。");
        }
        return result;

    }

    @Override
    public int updateUserStatus(User user) {
        return userMapper.update(user);
    }

    @Override
    public Map<String, String> updatePassword(String userId, String oldPassword, String newPassword) {
        Map<String, String> msgMap = new HashMap<>();
        User user = new User();
        Subject subject = SecurityUtils.getSubject();
        String principal = (String) subject.getPrincipal();
        String currentUserId = (String) subject.getSession(false).getAttribute("currentUserId");
        // rememberMe情况下需要通过用户名从数据库中查询userId
        if (Objects.isNull(currentUserId) && !Objects.isNull(principal)) {
            User existingUser = userMapper.queryByName(principal);
            user.setUserId(existingUser.getUserId());
        } else {
            user.setUserId(userId);
        }

        user.setPassword(EncryptionUtil.md5Encode(oldPassword));
        int count = userMapper.queryByPasswordAndUserId(user);
        if (count <= 0) {
            msgMap.put("code", "0");
            msgMap.put("msg", "旧密码不正确！");
        } else {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("userId", user.getUserId());
            paramMap.put("newPassword", EncryptionUtil.md5Encode(newPassword));
            int updatePassword = userMapper.updatePassword(paramMap);
            if (updatePassword > 0) {
                msgMap.put("code", "1");
                msgMap.put("msg", "密码修改成功！");
            } else {
                msgMap.put("code", "0");
                msgMap.put("msg", "密码修改失败！");
            }
        }
        return msgMap;
    }
   
    @Override
    public List<User> queryUserDetail(User user) {
        String userId = user.getUserId();
        if (userId.equals("null")) {
            // userMapper.xml中的queryUserDetail 动态判断语句需求
            user.setUserId("");
        }
        HashMap<String, String> paraMap = new HashMap<String, String>();
        paraMap.put("userId", user.getUserId());
        paraMap.put("userName", user.getUserName());
        List<User> queryUserDetail = userMapper.queryUserDetail(paraMap);
        return queryUserDetail;
    }

    @Override
    public int updatePasswordReset(User user) {
        return userMapper.updatePasswordReset(user);
    }

    @Override
    public int queryUsersCountByName(String userName) {
        return userMapper.queryUsersCountByName(userName);
    }
}
