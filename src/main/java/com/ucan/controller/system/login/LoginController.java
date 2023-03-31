package com.ucan.controller.system.login;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
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
import com.ucan.utils.EncryptionUtil;

/**
 * 用户登录控制器
 * 
 * @author liming.cen
 * @date 2022年12月27日 下午3:38:52
 */
@Controller
public class LoginController {
    @Autowired
    private IUserService userService;
    @Autowired
    private SessionDAO sessionDAO;
    private static Logger log = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping("/toLogin")
    public String toLogin() {
	return "login";
    }

    @RequestMapping("/login")
    @ResponseBody
    public String login(@RequestParam(name = "username", required = true, defaultValue = "") String username,
	    @RequestParam(name = "password", required = true, defaultValue = "") String password,
	    @RequestParam(name = "rememberMe", defaultValue = "false") String rememberMe) throws Exception {
	String msg = "";
	if (username == "" || password == "") {
	    msg = JSON.toJSONString(Response.fail("用户名|密码不能为空！"));
	}
	User user = userService.queryByName(username);
	if (user == null) {// 没有查询到用户
	    msg = JSON.toJSONString(Response.fail("用户名或密码错误！"));
	} else if (user.getIsEnable() == 0) {
	    msg = JSON.toJSONString(Response.fail("该用户已被禁用！"));
	} else {

	    Subject currentUser = SecurityUtils.getSubject();
	    if (!currentUser.isAuthenticated()) {/// IncorrectCredentialsException
		// 从缓存中获取还存活的session
		Collection<Session> activeSessions = sessionDAO.getActiveSessions();
		if (activeSessions.size() > 0) {
		    activeSessions.forEach(s -> {
			SimplePrincipalCollection principal = (SimplePrincipalCollection) s
				.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
			if (!Objects.isNull(principal)) {
			    String otherUser = (String) principal.getPrimaryPrincipal();
			    // 将在其他地方登录的相同的账号踢出系统
			    if (otherUser.equals(username)
				    && user.getPassword().equals(EncryptionUtil.md5Encode(password))) {
				s.setTimeout(100);
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
	}
	return msg;
    }

    @RequestMapping("/logout")
    public String logOut() {
	String url;
	Subject currentUser = SecurityUtils.getSubject();
	if (!currentUser.isAuthenticated()) {
	    log.warn("用户未登录！");
	    url = "redirect:/toLogin";
	} else {
	    currentUser.logout();
	    url = "redirect:/toLogin";
	}
	return url;
    }

}
