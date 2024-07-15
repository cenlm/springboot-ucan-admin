package com.ucan.shiro.util;

import java.util.Collection;

import org.apache.shiro.session.ExpiredSessionException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.StoppedSessionException;
import org.apache.shiro.session.mgt.AbstractValidatingSessionManager;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.ImmutableProxiedSession;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description: Session 校验工具
 * @author liming.cen
 * @date 2024-07-11 08:57:22
 * 
 */
@Component
public class SessionVerificationUtil {
    @Autowired
    private DefaultSessionManager sessionManager;
    @Autowired
    private SessionDAO sessionDAO;

    /**
     * 校验指定session，如果已停用，则设置立即超时过期,<br>
     * subject执行logout操作后会从服务端删除该session,<br>
     * 请看{@ShiroAuthenticationListener}的onLogout方法
     * 
     * @param s
     */
    public void validateSpecifiedSession(Session s) {
        SessionKey key = new DefaultSessionKey(s.getId());
        validate(s, key);
    }

    /**
     * 校验所有session
     */
    public void validateSessions() {
        Collection<Session> activeSessions = sessionDAO.getActiveSessions();

        if (activeSessions != null && !activeSessions.isEmpty()) {
            activeSessions.forEach(s -> {
                SessionKey key = new DefaultSessionKey(s.getId());
                validate(s, key);
            });

        }
    }

    private void validate(Session session, SessionKey key) throws InvalidSessionException {
        try {
            doValidate(session);
        } catch (ExpiredSessionException ese) {
            notifyExpiration(session);
//            ese.printStackTrace();
        } catch (StoppedSessionException sse) {
            notifyStop(session);
            // 设置session立即过期
            session.setTimeout(0);
        } catch (InvalidSessionException ise) {
            throw ise;
        }
    }

    private void doValidate(Session session) throws InvalidSessionException {
        if (session instanceof ValidatingSession) {
            ((ValidatingSession) session).validate();
        } else {
            String msg = "The " + getClass().getName() + " implementation only supports validating "
                    + "Session implementations of the " + ValidatingSession.class.getName() + " interface.  "
                    + "Please either implement this interface in your session implementation or override the "
                    + AbstractValidatingSessionManager.class.getName()
                    + ".doValidate(Session) method to perform validation.";
            throw new IllegalStateException(msg);
        }
    }

    /**
     * session过期监听
     * 
     * @param session
     */
    private void notifyExpiration(Session session) {
        for (SessionListener listener : sessionManager.getSessionListeners()) {
            listener.onExpiration(new ImmutableProxiedSession(session));
        }
    }

    /**
     * session停用监听
     * 
     * @param session
     */
    private void notifyStop(Session session) {
        for (SessionListener listener : sessionManager.getSessionListeners()) {
            listener.onStop(new ImmutableProxiedSession(session));
        }
    }

//    public DefaultSessionManager getSessionManager() {
//        return sessionManager;
//    }
//
//    public void setSessionManager(DefaultSessionManager sessionManager) {
//        this.sessionManager = sessionManager;
//    }
//
//    public SessionDAO getSessionDAO() {
//        return sessionDAO;
//    }
//
//    public void setSessionDAO(SessionDAO sessionDAO) {
//        this.sessionDAO = sessionDAO;
//    }

}
