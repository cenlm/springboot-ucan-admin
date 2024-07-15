package com.ucan.shiro.listener;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationListener;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @Description: 用户登录、退出监听器
 * @author liming.cen
 * @date 2024-07-09 21:21:10
 * 
 */
@Component("authenticationListener")
public class ShiroAuthenticationListener implements AuthenticationListener {
    private static Logger log = LoggerFactory.getLogger(ShiroAuthenticationListener.class);
    @Autowired
    @Qualifier("sessionDAO")
    private CachingSessionDAO shiroSessionDao;

    private Session session;

    Map<String, Session> activeSessionsMap = new ConcurrentHashMap<String, Session>();

    @Override
    public void onSuccess(AuthenticationToken token, AuthenticationInfo info) {
        // 认证成功后，获取已经分配给当前subject的session
        Subject subject = SecurityUtils.getSubject();
        boolean isAuthenticated = subject.isAuthenticated();
        boolean isRemembered = subject.isRemembered();
        session = subject.getSession(false);
        // 到onSuccess时间点的时候，用户已经认证成功，但还未解析session、principal、认证信息，需要后续创建WebDelegatingSubject及更新session中的状态
        if (!isAuthenticated && !isRemembered && !Objects.isNull(session)) {// 未认证且为处于非rememberMe状态的用户才纪录该session
            activeSessionsMap.put((String) token.getPrincipal(), session);
            log.info("【" + token.getPrincipal() + "】认证成功！sessionId:" + session.getId());
        } else {
            log.info("【" + token.getPrincipal() + "】认证成功！");
        }
    }

    @Override
    public void onFailure(AuthenticationToken token, AuthenticationException ae) {
        log.info("【" + token.getPrincipal() + "】认证失败！");
    }

    /**
     * 系统默认的退出行为只会删掉浏览器的rememberMe Cookie和移除掉session中纪录的principal和认证状态，<br>
     * 不会删除session，需要在onLogout回调方法中定义删除session的逻辑
     */
    @Override
    public void onLogout(PrincipalCollection principals) {
        Session session = activeSessionsMap.get(principals.getPrimaryPrincipal());
        if (!Objects.isNull(session)) {
            // 用户退出，清除当前已认证用户的session
            shiroSessionDao.delete(session);
        }
        log.info("【" + principals.getPrimaryPrincipal() + "】已退出系统！");

        Collection<Session> activeSessions = shiroSessionDao.getActiveSessions();
        if (activeSessions.size() > 0) {
            log.info("未过期Session（1.已认证登录，但未退出系统，可能只是关闭了浏览器；2.匿名访问）：");
            activeSessions.forEach(activeSession -> {
                Object principal = activeSession.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
                log.info("用户：" + (Objects.isNull(principal) ? "匿名访问" : principal) + "，sessionId:"
                        + activeSession.getId());
            });
        }

    }

}
