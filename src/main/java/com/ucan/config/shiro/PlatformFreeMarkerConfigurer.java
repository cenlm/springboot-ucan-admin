package com.ucan.config.shiro;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagregory.shiro.freemarker.ShiroTags;

import freemarker.template.Configuration;

/**
 * @Description:配置在ftl页面中使用shiro标签
 * @author liming.cen
 * @date 2023-03-23 15:13:19
 * 
 */
@Component
public class PlatformFreeMarkerConfigurer implements InitializingBean {
    @Autowired
    private Configuration configuration;

    @Override
    public void afterPropertiesSet() throws Exception {
	configuration.setSharedVariable("shiro", new ShiroTags());
    }

}
