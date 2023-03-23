package com.ucan.config.shiro;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.ExecutorServiceSessionValidationScheduler;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.SessionValidationScheduler;
import org.apache.shiro.session.mgt.ValidatingSessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import com.ucan.shiro.UCanRealm;
import com.ucan.shiro.UcanSessionIdGenerator;
import com.ucan.shiro.listener.ShiroSessionListener;

/**
 * @Description: shiro配置类
 * @author liming.cen
 * @date 2023-03-21 20:15:51
 * 
 */
@Configuration
public class ShiroConfig {

    @Bean("securityManager")
    public DefaultWebSecurityManager securityManager(@Qualifier("uCanRealm") Realm uCanRealm,
	    @Qualifier("sessionManager") DefaultWebSessionManager sessionManager,
	    @Qualifier("ehCacheManager") EhCacheManager cacheManager) {
	DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
	securityManager.setRealm(uCanRealm);
	securityManager.setSessionManager(sessionManager);
	securityManager.setCacheManager(cacheManager);
	return securityManager;

    }

    @Bean
    public EhCacheManager ehCacheManager(EhCacheManagerFactoryBean ehcacheManagerFactory) {
	EhCacheManager ehCacheManager = new EhCacheManager();
	ehCacheManager.setCacheManager(ehcacheManagerFactory.getObject());
	return ehCacheManager;
    }

    @Bean
    public EhCacheManagerFactoryBean ehcacheManagerFactory() {
	EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();
	ehCacheManagerFactoryBean.setShared(true);
	return ehCacheManagerFactoryBean;
    }

    /**
     * 创建realm
     * 
     * @return
     */
    @Bean("uCanRealm")
    public Realm uCanRealm() {
	UCanRealm realm = new UCanRealm();
	realm.setCachingEnabled(true);
	realm.setAuthorizationCachingEnabled(true);
	realm.setAuthorizationCacheName("shiroAuthzCache");

	return (Realm) realm;
    }

//    @Bean
//    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
//	DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
//
//	chainDefinition.addPathDefinition("/toLogin", "anon");
//	chainDefinition.addPathDefinition("/toLogin.do", "anon");
//	chainDefinition.addPathDefinition("/login", "anon");
//	chainDefinition.addPathDefinition("/login.do", "anon");
//	chainDefinition.addPathDefinition("/css/**", "anon");
//	chainDefinition.addPathDefinition("/js/**", "anon");
//	chainDefinition.addPathDefinition("/imgs/**", "anon");
//	chainDefinition.addPathDefinition("/**", "authc");
//	return chainDefinition;
//    }

    /**
     * shiro session 状态（检查）校验调度器，基于jdk的ExecutorService实现
     * 
     * @return
     */
    @Bean("sessionValidationScheduler")
    public ExecutorServiceSessionValidationScheduler shiroSessionValidationScheduler(
	    @Qualifier("sessionManager") SessionManager sessionManager) {
	ExecutorServiceSessionValidationScheduler essvScheduler = new ExecutorServiceSessionValidationScheduler();
	essvScheduler.setInterval(3600000);
	essvScheduler.setSessionManager((ValidatingSessionManager) sessionManager);
	return essvScheduler;
    }

    /**
     * shiro native session管理器
     * 
     * @param sessionDao
     * @param sessionValidationScheduler
     * @param shiroSessionListener
     * @param sessionIdCookie
     * @return
     */
    @Bean("sessionManager")
    public DefaultWebSessionManager sessionManager(@Qualifier("sessionDAO") SessionDAO sessionDao,
	    @Lazy SessionValidationScheduler sessionValidationScheduler,
	    @Qualifier("shiroSessionListener") SessionListener shiroSessionListener,
	    @Qualifier("uCanCookie") SimpleCookie sessionIdCookie) {
	DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
	sessionManager.setSessionDAO(sessionDao);
	sessionManager.setSessionValidationScheduler(sessionValidationScheduler);
	sessionManager.setDeleteInvalidSessions(true);
	sessionManager.setSessionIdCookieEnabled(true);
	sessionManager.setSessionListeners(Collections.singletonList(shiroSessionListener));
	sessionManager.setGlobalSessionTimeout(-1);
	sessionManager.setSessionIdCookie(sessionIdCookie);
	return sessionManager;

    }

    @Bean
    public ShiroSessionListener shiroSessionListener() {
	return new ShiroSessionListener();
    }

    /**
     * 配置cookie模板
     * 
     * @return
     */
    @Bean("uCanCookie")
    public SimpleCookie uCanCookie() {
	SimpleCookie cookie = new SimpleCookie("uCanCookie");
	cookie.setMaxAge(-1);
	cookie.setHttpOnly(true);
	return cookie;
    }

    /**
     * 自定义sessionId生成器
     * 
     * @return
     */
    @Bean("uCanSessionIdGenerator")
    public UcanSessionIdGenerator uCanSessionIdGenerator() {
	return new UcanSessionIdGenerator();
    }

    @Bean("lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
	return new LifecycleBeanPostProcessor();
    }

    @Bean("shiroFilterFactoryBean")
    public ShiroFilterFactoryBean shiroFilterFactory(DefaultWebSecurityManager securityManager) {
	ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
	shiroFilterFactoryBean.setSecurityManager(securityManager);
	shiroFilterFactoryBean.setLoginUrl("/toLogin");
	shiroFilterFactoryBean.setSuccessUrl("/index");
	shiroFilterFactoryBean.setUnauthorizedUrl("/toLogin");

	Map<String, String> filterChainDefinitionMap = new HashMap<>();
	filterChainDefinitionMap.put("/toLogin", "anon");
	filterChainDefinitionMap.put("/toLogin.do", "anon");
	filterChainDefinitionMap.put("/login", "anon");
	filterChainDefinitionMap.put("login.do", "anon");
	filterChainDefinitionMap.put("/css/**", "anon");
	filterChainDefinitionMap.put("/js/**", "anon");
	filterChainDefinitionMap.put("/imgs/**", "anon");
	filterChainDefinitionMap.put("/**", "authc");
	shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
	return shiroFilterFactoryBean;
    }
}
