package com.ucan.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;

import com.ucan.shiro.ResourceSuffix;

/**
 * 集成认证访问权限和rememberMe的过滤功能
 * 
 * @Description:
 * @author liming.cen
 * @date 2024-07-05 14:59:41
 * 
 */
public class UcanAuthenticatingFilter extends FormAuthenticationFilter {


     /**
      * * 合并（authc）FormAuthenticationFilter、（user，rememberMe）UserFilter的访问权限控制功能<br>
     * 说明：shiro访问拦截时会先执行匹配到的shiro过滤器链，然后再执行原始过滤器链。<br>
     * 传统web项目（.war）的过滤器声明与拦截规则在web.xml中配置，且过滤器会加入到Servlet容器（如tomcat）<br>
     * 全局过滤器链（ApplicationFilterChain）中，而Springboot会自动扫描spring容器中的Filter并将其加入到全局过滤器链中，<br>
     * 所以通配符路径过滤器 /** = someFilter 需要通过new
     * 关键字进行实例化，以摆脱spring容器的扫描，具体方式请查阅{@ShiroConfig}{@link #shiroFilterFactory(DefaultWebSecurityManager securityManager)}<br>
     * 当前过滤器负责处理的路径（appliedPaths）查找： {@PathMatchingFilter}{@link #preHandle(ServletRequest, ServletResponse)}<br>
     * 原始过滤器链内的过滤器查看：{@ProxiedFilterChain} {@link #doFilter(ServletRequest, ServletResponse)} 里面的 this.orig
      */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        Subject subject = getSubject(request, response);
        boolean isAccessAllowed = false;
        // 当前用户是否已认证？
        boolean authenticated = subject.isAuthenticated();
        PrincipalCollection principals = subject.getPrincipals();

        // 用户已认证且principal不为空
        if (authenticated && principals != null && !principals.isEmpty()) {
            isAccessAllowed = true;
        } else if (!isLoginRequest(request, response) && isPermissive(mappedValue)) {
            isAccessAllowed = true;
        } else if (subject.isRemembered()) {// 用户已启用rememberMe功能，新用户，未认证
//            if (isLoginRequest(request, response)) {// 登录请求直接放行
//                isAccessAllowed = true;
//            }
            isAccessAllowed = true;
        }
        return isAccessAllowed;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        return super.onAccessDenied(request, response);
    }

}
