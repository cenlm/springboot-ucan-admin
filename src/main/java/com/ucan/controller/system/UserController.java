package com.ucan.controller.system;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson2.JSON;
import com.ucan.base.response.MsgEnum;
import com.ucan.base.response.Response;
import com.ucan.entity.Organization;
import com.ucan.entity.Post;
import com.ucan.entity.User;
import com.ucan.entity.UserOrganization;
import com.ucan.entity.page.PageParameter;
import com.ucan.service.IOrganizationService;
import com.ucan.service.IUserOrgService;
import com.ucan.service.IUserService;
import com.ucan.shiro.util.EncryptionUtil;
import com.ucan.util.page.PageUtil;

/**
 * @author liming.cen
 * @date 2022年12月23日 下午10:58:47
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IOrganizationService organizationService;
    @Autowired
    private IUserOrgService userOrgService;

    @Autowired
    private EhCacheManager ehCacheManager;

    @RequestMapping("/user_list")
    public String toAddUserPage() {
        return "user/user_list";
    }

    @RequestMapping("/user_setting")
    public String toUserSetting() {
        return "user/user_setting";
    }

    @RequestMapping("/queryUserByPage")
    @ResponseBody
    public String queryUserByPage(User user, @RequestParam(name = "currentPage", defaultValue = "1") String currentPage,
            @RequestParam(name = "pageSize", defaultValue = "5") String pageSize) {
        PageParameter page = new PageParameter(currentPage, pageSize);
        user.setPage(page);
        List<User> users = userService.queryUserByPage(user);
        if (users.size() > 0) {
            return JSON.toJSONString(Response.respose(MsgEnum.SUCCESS, users, page));
        } else {
            return JSON.toJSONString(Response.respose(MsgEnum.FAIL.getCode(), "没有查询到数据", users, page));
        }

    }

    /**
     * 查找分组用户
     * 
     * @param userOrganization
     * @param currentPage
     * @param pageSize
     * @return
     */
    @RequestMapping("/getUserOrgByPage")
    @ResponseBody
    public String getUserOrgByPage(UserOrganization userOrganization,
            @RequestParam(name = "currentPage", defaultValue = "1") String currentPage,
            @RequestParam(name = "pageSize", defaultValue = "5") String pageSize) {
        PageParameter page = new PageParameter(currentPage, pageSize);
        userOrganization.setPage(page);
        List<UserOrganization> userOrgs = userOrgService.getUserOrgByPage(userOrganization);
        if (userOrgs.size() > 0) {
            return JSON.toJSONString(Response.respose(MsgEnum.SUCCESS, userOrgs, page));
        } else {
            return JSON.toJSONString(Response.respose(MsgEnum.FAIL.getCode(), "没有查询到数据", userOrgs, page));
        }

    }

    /**
     * 通过组织ID、用户名、用户状态查询用户信息
     * 
     * @param org         组织信息
     * @param username    用户名
     * @param isEnable    用户状态
     * @param currentPage
     * @param pageSize
     * @return
     */
    @RequestMapping("/queryUserByOrgIdPage")
    @ResponseBody
    public String queryUserByOrgIdPage(Organization org,
            @RequestParam(name = "username", defaultValue = "") String username,
            @RequestParam(name = "isEnable", defaultValue = "") String isEnable,
            @RequestParam(name = "currentPage", defaultValue = "1") String currentPage,
            @RequestParam(name = "pageSize", defaultValue = "5") String pageSize) {
        List<String> orgIds = organizationService.getAllChildrenIdsByOrgId(org.getOrgId());
        PageParameter page = new PageParameter(currentPage, pageSize);
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("isEnable", isEnable);
        map.put("page", page);
        map.put("orgIds", orgIds);
        // 查询总记录数
        int totalCount = userService.queryUsersCountsByOrgIds(map);
        // 设置分页参数和总记录数
        PageUtil.setPageParameter(page, totalCount);
        List<User> users = userService.queryUsersByOrgIdsPageWithMap(map);
        if (users.size() > 0) {
            return JSON.toJSONString(Response.respose(MsgEnum.SUCCESS, users, page));
        } else {
            return JSON.toJSONString(Response.respose(MsgEnum.FAIL.getCode(), "没有查询到数据", users, page));
        }

    }

    /**
     * 通过组织ID、用户名、用户状态查询用户信息
     * 
     * @param org         组织信息
     * @param username    用户名
     * @param isEnable    用户状态
     * @param currentPage
     * @param pageSize
     * @return
     */
    @RequestMapping("/queryUserByPostIdPage")
    @ResponseBody
    public String queryUserByPostIdPage(Post post, @RequestParam(name = "username", defaultValue = "") String username,
            @RequestParam(name = "isEnable", defaultValue = "") String isEnable,
            @RequestParam(name = "currentPage", defaultValue = "1") String currentPage,
            @RequestParam(name = "pageSize", defaultValue = "5") String pageSize) {
        PageParameter page = new PageParameter(currentPage, pageSize);
        Map<String, Object> map = new HashMap<>();
        map.put("postId", post.getPostId());
        map.put("username", username);
        map.put("isEnable", isEnable);
        map.put("page", page);
        // 查询总记录数
        int totalCount = userService.queryCountByPostId(map);
        // 设置分页参数和总记录数
        PageUtil.setPageParameter(page, totalCount);
        List<User> users = userService.queryUsersByPostId(map);
        if (users.size() > 0) {
            return JSON.toJSONString(Response.respose(MsgEnum.SUCCESS, users, page));
        } else {
            return JSON.toJSONString(Response.respose(MsgEnum.FAIL.getCode(), "没有查询到数据", users, page));
        }

    }

    /**
     * 新增用户：只能跟职位（Post）或用户组（Group）直接产生映射关系，跟组织结构的映射关系只用于角色分配与授权
     * 
     * @param user    用户信息
     * @param postId  职位Id
     * @param groupId 用户组Id
     * @param type    目标映射类型 post:职位 group:用户组
     * @return
     * @throws Exception
     */
    @RequestMapping("/addUser")
    @ResponseBody
    public String addUser(User user, @RequestParam(name = "postId", defaultValue = "") String postId,
            @RequestParam(name = "groupId", defaultValue = "") String groupId,
            @RequestParam(name = "type", defaultValue = "") String type) throws Exception {
        String jsonDataString = "";
        if (user.getUserName() == "" || user.getUserName() == null) {
            jsonDataString = JSON.toJSONString(Response.fail("用户名不能为空！"));
        }
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("postId", postId);
        paramMap.put("groupId", groupId);
        paramMap.put("type", type);
        int updCount = 0;
        updCount = userService.insert(user, paramMap);
        if (updCount > 0) {
            User userFromServer = userService.queryById(user.getUserId());
            jsonDataString = JSON.toJSONString(Response.respose(MsgEnum.SUCCESS, userFromServer));
        } else {
            jsonDataString = JSON.toJSONString(Response.fail());
        }

        return jsonDataString;
    }

    @RequestMapping("/updateUser")
    @ResponseBody
    public String updateUser(@RequestBody User user) throws Exception {
        String msg;
        user.setModifyTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        int updateCount = userService.update(user);
        if (updateCount > 0) {
            msg = JSON.toJSONString(Response.success("用户信息更新成功！"));
        } else {
            msg = JSON.toJSONString(Response.fail("用户信息更新失败！"));
        }

        return msg;
    }

    /**
     * （user_setting.ftl页面）进行文件上传测试
     * 
     * @param request
     * @param response
     * @throws Exception
     */
    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file, @RequestParam("description") String description,
            Model model, HttpServletRequest request) {
        if (!file.isEmpty()) {
            try {
                // 获取文件名，防止路径遍历攻击（如 ../../etc/passwd）
                String fileName = file.getOriginalFilename().replaceAll("[^a-zA-Z0-9.-]", "_");
                //获取根目录绝对路径
                String appRoot = request.getServletContext().getRealPath("/");
                // 保存文件到服务器
                String uploadDir = "/uploads/";
                File dest = new File(appRoot + uploadDir + fileName);
                File parentDir = dest.getParentFile();
                if (!parentDir.exists()) {// 目录不存在，先创建目录
                    boolean dirCreated = parentDir.mkdir();
                    if (!dirCreated) {
                        throw new IOException("Failed to create directory: " + parentDir.getAbsolutePath());
                    }
                }
                file.transferTo(dest);
                System.out.println("文件上传路径：" + parentDir.getAbsolutePath());
                model.addAttribute("message", "文件上传成功：" + fileName);
            } catch (IOException e) {
                model.addAttribute("message", "文件上传失败：" + e.getMessage());
            }
        } else {
            model.addAttribute("message", "文件为空");
        }
        return "result";
    }

    @RequestMapping("/updatePassword")
    @ResponseBody
    public String updatePassword(@RequestParam(name = "userId", required = true) String userId,
            @RequestParam(name = "password", required = true) String password,
            @RequestParam(name = "newPassword", required = true) String newPassword) {
        Map<String, String> result = userService.updatePassword(userId, password, newPassword);
        if (result.get("code").equals("1")) {
            return JSON.toJSONString(Response.success(result.get("msg")));
        } else {
            return JSON.toJSONString(Response.fail(result.get("msg")));
        }
    }

    /**
     * 重置密码，清除用户登录限制记录信息
     * 
     * @param userId
     * @param userName
     * @return
     */
    @RequestMapping("/resetPassword")
    @ResponseBody
    public String resetPassword(@RequestParam(name = "userId", required = true) String userId,
            @RequestParam(name = "userName", required = true) String userName) {
        User user = new User();
        user.setUserId(userId);
        user.setPassword(EncryptionUtil.md5Encode("88888888"));
        int result = userService.updatePasswordReset(user);
        if (result > 0) {
            // 失败登录次数计数缓存
            Cache<String, AtomicInteger> attemptsCache = ehCacheManager.getCache("failLoginCount");
            // 限制登录时长计数缓存
            Cache<String, Date> limitTimer = ehCacheManager.getCache("limitTimer");
            String failLoginCountKey = "fail_login_attempts_" + userName;
            String limitTimerKey = "limit_login_timer_" + userName;
            // 密码重置成功，清除之前用户的登录失败信息记录
            attemptsCache.remove(failLoginCountKey);
            limitTimer.remove(limitTimerKey);
            return JSON.toJSONString(Response.success("密码已重置为：88888888，请通知【" + userName + "】尽快登录系统并修改密码！"));
        } else {
            return JSON.toJSONString(Response.fail("密码重置失败！"));
        }
    }

    /**
     * 更新用户可用状态
     * 
     * @param user
     * @return
     */

    @RequestMapping("/updateUserStatus")
    @ResponseBody
    public String updateUserStatus(@RequestBody User user) {
        String msg;
        user.setModifyTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        int updateCount = userService.updateUserStatus(user);
        if (updateCount > 0) {
            msg = JSON.toJSONString(Response.success());
        } else {
            msg = JSON.toJSONString(Response.fail());
        }
        return msg;
    }

    @RequestMapping("/delUserById")
    @ResponseBody
    public String delUserById(String userId) {
        String result = "";
        try {
            int count = userService.deleteById(userId);
            if (count > 0) {
                result = JSON.toJSONString(Response.success());
            } else {
                result = JSON.toJSONString(Response.fail());
            }
        } catch (Exception e) {
            result = JSON.toJSONString(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("/delUsersById")
    @ResponseBody
    public String delUsersById(@RequestParam(value = "userIds[]", defaultValue = "") List<String> userIds) {

        String result = "";
        int updUserCount = 0;
        if (userIds.size() > 0) {
            try {
                updUserCount = userService.deleteByIds(userIds);
                if (updUserCount > 0) {
                    result = JSON.toJSONString(Response.success("成功删除 " + updUserCount + " 条记录！"));
                } else {
                    result = JSON.toJSONString(Response.fail("删除失败！"));
                }
            } catch (Exception e) {
                result = JSON.toJSONString(Response.fail(e.getMessage()));
                e.printStackTrace();
            }

        } else {
            result = JSON.toJSONString(Response.fail("没有选择用户 或者 超级管理员不能删除！"));
        }

        return result;
    }

    @RequestMapping("/queryUserById")
    @ResponseBody
    public String queryUserById(String userId) {
        String jsonDataString = "";
        User user = userService.queryById(userId);
        if (user != null) {
            jsonDataString = JSON.toJSONString(Response.respose(MsgEnum.SUCCESS, user));
        } else {
            jsonDataString = JSON.toJSONString(Response.respose(MsgEnum.FAIL, user));
        }
        return jsonDataString;
    }

    @RequestMapping("/addUserPostMapping")
    @ResponseBody
    public String addUserPostMapping(@RequestParam(name = "userId", defaultValue = "") String userId,
            @RequestParam(name = "postId", defaultValue = "") String postId) {
        String jsonDataString = "";
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("userId", userId);
        paramMap.put("postId", postId);
        int count = userService.addUserPostMapping(paramMap);
        if (count > 0) {
            jsonDataString = JSON.toJSONString(Response.success("成功新增一条<用户-职位>映射记录！"));
        } else {
            jsonDataString = JSON.toJSONString(Response.fail("<用户-职位>映射记录添加失败！"));
        }
        return jsonDataString;
    }

//    @RequiresPermissions("document:read") //shiro注解测试
    @RequestMapping("/queryUserDetail")
    @ResponseBody
    public String queryUserDetail(User user) {
        List<User> queryUserDetail = userService.queryUserDetail(user);
        if (queryUserDetail.size() > 0) {
            return JSON.toJSONString(Response.success(queryUserDetail));
        } else {
            return JSON.toJSONString(Response.fail(queryUserDetail));
        }
    }

}
