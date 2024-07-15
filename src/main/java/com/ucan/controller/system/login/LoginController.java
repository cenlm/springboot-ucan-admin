package com.ucan.controller.system.login;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.java_websocket.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson2.JSON;
import com.ucan.base.response.Response;
import com.ucan.entity.User;
import com.ucan.service.IUserService;
import com.ucan.shiro.util.EncryptionUtil;
import com.ucan.shiro.util.SessionVerificationUtil;
import com.ucan.websocket.SocketServer;
import com.ucan.websocket.SocketServerManager;

/**
 * 用户登录控制器
 * 
 * @author liming.cen
 * @date 2022年12月27日 下午3:38:52
 */
@Controller
public class LoginController {
    private static Logger log = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private IUserService userService;
    @Autowired
    private SessionDAO sessionDAO;

    @Autowired
    private SessionVerificationUtil sessionVerification;
    @Autowired
    private SocketServerManager socketServerManager;

    @RequestMapping("/toLogin")
    public String toLogin() {
        Subject currentUser = SecurityUtils.getSubject();
        if (!currentUser.isAuthenticated()) {
            return "login";
        } else {// 如果用户已经登录，则直接跳到主页
            return "home/index";
        }

    }
   
    @RequestMapping("/login")
    @ResponseBody
    public String login(@RequestParam(name = "username", required = true, defaultValue = "") String username,
            @RequestParam(name = "password", required = true, defaultValue = "") String password,
            @RequestParam(name = "rememberMe", defaultValue = "false") String rememberMe) throws Exception {
        String msg = "";
        if (username == "" || password == "") {
            return JSON.toJSONString(Response.fail("用户名|密码不能为空！"));
        }
        Subject currentUser = SecurityUtils.getSubject();
        if (!currentUser.isAuthenticated() && !currentUser.isRemembered()) {/// IncorrectCredentialsException

            User user = userService.queryByName(username);
            if (user == null) {// 没有查询到用户
                return JSON.toJSONString(Response.fail("用户名或密码错误！"));
            } else if (user.getIsEnable() == 0) {
                return JSON.toJSONString(Response.fail("该用户已被禁用！"));
            }
            // 从缓存中获取还存活的session
            Collection<Session> activeSessions = sessionDAO.getActiveSessions();
            if (activeSessions.size() > 0) {
                activeSessions.forEach(s -> {
                    SimplePrincipalCollection principal = (SimplePrincipalCollection) s
                            .getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
                    if (!Objects.isNull(principal)) {
                        String otherUser = (String) principal.getPrimaryPrincipal();
                        // 将异地登录的账号踢出系统
                        if (otherUser.equals(username)
                                && user.getPassword().equals(EncryptionUtil.md5Encode(password))) {
                            // 设置session立即超时
                            s.setTimeout(0);
                            sessionVerification.validateSpecifiedSession(s);
                            SocketServer socketServer = socketServerManager.getSocketServer();
                            WebSocket webSocket = socketServer.getSocket(otherUser + "_index");
                            /**
                             * 说明之前已登录系统的用户A的浏览器已关闭，已断开socket连接，<br>
                             * 此时仅需要从服务器中删除用户A的session即可
                             */
                            if (Objects.isNull(webSocket)) {
                                sessionDAO.delete(s);
                            } else {// 之前已登录系统的用户A的浏览器未关闭，执行用户踢出流程
                                webSocket.send("kickout");
                                WebSocket socketFromLoginPage = socketServer.getSocket(username + "_login");
                                socketFromLoginPage.send("正在踢出异地登录账号，登录操作6秒后进行");
                                try {
                                    Thread.sleep(6000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                });
            }
            UsernamePasswordToken token = new UsernamePasswordToken(username, EncryptionUtil.md5Encode(password));
            // 要不要自定义过滤器来解决rememberMe失效的问题呢？
            // https://blog.csdn.net/nsrainbow/article/details/36945267/
            token.setRememberMe(rememberMe.equals("true") ? true : false);
            currentUser.login(token);
            User userObjUser = new User();
            userObjUser.setUserId(user.getUserId());
            // 设置登录时间
            userObjUser.setEntryDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            // 设置上次登录时间
            userObjUser.setLastLogin(user.getEntryDate());

            // 更新登录时间和上次登录时间
            userService.update(userObjUser);
            Session session = currentUser.getSession(false);
            session.setAttribute("currentUserId", user.getUserId());
            msg = JSON.toJSONString(Response.success("用户登录成功！"));
        } else {
            msg = JSON.toJSONString(Response.success("用户已登录！"));
        }
        return msg;
    }

    /**
     * 系统默认的退出行为只会删掉浏览器的rememberMe Cookie和移除掉session中纪录的principal和认证状态，<br>
     * 不会删除session，需要在{@ShiroAuthenticationListener}{@link #onLogout(PrincipalCollection principals)}方法中定义session删除逻辑
     */
    @RequestMapping("/logout")
    public String logOut() {
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout();
        return "redirect:/toLogin";
    }

}
