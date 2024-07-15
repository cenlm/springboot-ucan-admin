package com.ucan.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticationFilter;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.util.WebUtils;

/**
 * @Description: 已认证和rememberMe用户对指定路径（spring-shiro.xml
 *               filterChainDefinitions中配置）进行访问时，重定向到主页 homePage
 * @author liming.cen
 * @date 2024-07-10 19:44:37
 * 
 */
public class UcanLoginedFilter extends AuthenticationFilter {
    /**
     * 用户登录后的主页地址
     */
    private String homePage;

    /**
     * 未认证且不是已被rememberMe标识的用户直接放行
     * 
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        Subject subject = getSubject(request, response);
        return !subject.isAuthenticated() && !subject.isRemembered();
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
//        WebUtils.saveRequest(request);
        WebUtils.issueRedirect(request, response, getHomePage());
        return false;
    }

    public String getHomePage() {
        return homePage;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

}
