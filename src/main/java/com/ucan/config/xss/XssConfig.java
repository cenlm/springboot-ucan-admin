package com.ucan.config.xss;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ucan.filter.XssFilter;

/**
 * @Description: 注册Xss攻击过滤器
 * @author liming.cen
 * @date 2025-04-20 16:38:28
 * 
 */
//@Configuration
public class XssConfig {

    @Bean
    public FilterRegistrationBean<XssFilter> xssFilter() {
        FilterRegistrationBean<XssFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new XssFilter());
        registration.addUrlPatterns("/*");
        return registration;
    }
}
