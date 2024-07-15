package com.ucan.config.shiro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;
import org.apache.shiro.mgt.SecurityManager;

import org.apache.shiro.authc.AuthenticationListener;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.ExecutorServiceSessionValidationScheduler;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.SessionValidationScheduler;
import org.apache.shiro.session.mgt.ValidatingSessionManager;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.config.ShiroAnnotationProcessorConfiguration;
import org.apache.shiro.spring.config.ShiroBeanConfiguration;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;

import com.ucan.shiro.filter.UcanAuthenticatingFilter;
import com.ucan.shiro.filter.UcanLoginedFilter;
import com.ucan.shiro.realm.UCanRealm;

/**
 * @Description: shiro配置类
 * @author liming.cen
 * @date 2023-03-21 20:15:51
 * 
 */
@Configuration
@Import({ ShiroBeanConfiguration.class })
public class ShiroConfig {

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
    public Realm uCanRealm(@Qualifier("limitLoginMatcher") CredentialsMatcher limitLoginMatcher) {
        UCanRealm realm = new UCanRealm();
        realm.setCachingEnabled(true);
        realm.setAuthorizationCachingEnabled(true);
        realm.setAuthorizationCacheName("shiroAuthzCache");
        realm.setCredentialsMatcher(limitLoginMatcher);
        return (Realm) realm;
    }

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
            @Qualifier("sessionIdCookie") SimpleCookie sessionIdCookie) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(sessionDao);
        sessionManager.setSessionValidationScheduler(sessionValidationScheduler);
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionIdCookieEnabled(true);
        sessionManager.setSessionListeners(Collections.singletonList(shiroSessionListener));
        sessionManager.setGlobalSessionTimeout(2700000);
        sessionManager.setSessionIdCookie(sessionIdCookie);
        return sessionManager;

    }

    /**
     * 配置cookie模板
     * 
     * @return
     */
    @Bean("sessionIdCookie")
    public SimpleCookie uCanCookie() {
        SimpleCookie cookie = new SimpleCookie("sessionIdCookie");
        // cookie有效期：关闭浏览器就清除
        cookie.setMaxAge(-1);
        cookie.setHttpOnly(true);
        return cookie;
    }

    @Bean("sessionDAO")
    public SessionDAO shiroSessionDao(@Qualifier("uCanSessionIdGenerator") SessionIdGenerator sessionIdGenerator) {
        CachingSessionDAO sessionDao = new EnterpriseCacheSessionDAO();
        sessionDao.setActiveSessionsCacheName("shiroActiveSessions");
        sessionDao.setSessionIdGenerator(sessionIdGenerator);
        return sessionDao;
    }

    /**
     * 配置rememberMe cookie模板
     * 
     * @return
     */
    @Bean("rememberMeCookie")
    public SimpleCookie rememberMeCookie() {
        SimpleCookie cookie = new SimpleCookie();
        cookie.setName("rememberMeCookie");
        // cookie有效期：3天（259200），如果是-1，则关闭浏览器立马清除cookie
        cookie.setMaxAge(259200);
        cookie.setHttpOnly(true);
        return cookie;
    }

    /**
     * rememberMeCookie 管理器
     * 
     * @param cookie
     * @return
     */
    @Bean("rmmManager")
    public RememberMeManager rememberMeCookieManager(@Qualifier("rememberMeCookie") Cookie cookie) {
        CookieRememberMeManager rememberMeCookieManager = new CookieRememberMeManager();
        rememberMeCookieManager.setCookie(cookie);
        return rememberMeCookieManager;
    }

    @Bean("securityManager")
    public DefaultWebSecurityManager securityManager(@Qualifier("uCanRealm") Realm uCanRealm,
            @Qualifier("sessionManager") DefaultWebSessionManager sessionManager,
            @Qualifier("rmmManager") RememberMeManager rmmManager,
            @Qualifier("ehCacheManager") EhCacheManager cacheManager,
            @Qualifier("authenticationListener") AuthenticationListener authListener) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(uCanRealm);
        securityManager.setSessionManager(sessionManager);
        securityManager.setRememberMeManager(rmmManager);
        securityManager.setCacheManager(cacheManager);
        ModularRealmAuthenticator authenticator = (ModularRealmAuthenticator) securityManager.getAuthenticator();
        Collection<AuthenticationListener> listeners = new ArrayList<>();
        listeners.add(authListener);
        authenticator.setAuthenticationListeners(listeners);
        return securityManager;

    }

    @Bean("shiroFilterFactoryBean")
    public ShiroFilterFactoryBean shiroFilterFactory(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl("/toLogin");
        shiroFilterFactoryBean.setSuccessUrl("/index");
        shiroFilterFactoryBean.setUnauthorizedUrl("/toLogin");
        Map<String, Filter> filters = new LinkedHashMap<String, Filter>();
        // 已认证和rememberMe用户对指定路径（spring-shiro.xmlfilterChainDefinitions中配置）进行访问时，重定向到主页homePage
        UcanLoginedFilter loginedFilter = new UcanLoginedFilter();
        loginedFilter.setHomePage("/index");
        filters.put("ucanLogined", loginedFilter);
        // 集成认证访问权限和rememberMe的过滤功能
        // 重要！：使用new
        // 关键字实例化过滤器，而不是使用spring容器自动扫描，因为自动扫描会将他们当成全局过滤器而加入到ApplicationFilterChain中,
        // 导致执行完指定的shiro过滤器链后，又会从ApplicationFilterChain中执行不必要的过滤器（如authc），造成静态资源等被拦截
        filters.put("ucanAuth", new UcanAuthenticatingFilter());
        shiroFilterFactoryBean.setFilters(filters);

        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/toLogin", "anon,ucanLogined");
        filterChainDefinitionMap.put("/toLogin.do", "anon,ucanLogined");// ucanLogined
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/login.do", "anon");
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/imgs/**", "anon");
        filterChainDefinitionMap.put("/**", "ucanAuth");// ucanAuth authc
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /**
     * 开启shiro注解支持<br>
     * shiro注解只在@Controller 控制器上生效，在@Service服务类上不生效，<br>
     * 因为在搜索advisor去生成Service代理时，AuthorizationAttributeSourceAdvisor处于创建中，因此被忽略掉~
     */
    @Bean
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(
            @Qualifier("securityManager") SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

}
