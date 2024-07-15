package com.ucan.config.freemarker;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagregory.shiro.freemarker.ShiroTags;
import com.ucan.config.freemarker.model.TemplateShiroCurrentUserIdModel;
import com.ucan.config.freemarker.model.TemplateShiroPrincipalModel;
import com.ucan.config.freemarker.model.TemplateShiroPrincipalsModel;
import com.ucan.config.freemarker.model.TemplateShiroSessionIdModel;
import com.ucan.config.freemarker.model.TemplateShiroSessionModel;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;

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
        // 设置freemarker异常处理器
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setSharedVariable("shiro", new ShiroTags());
        configuration.setSharedVariable("session", new TemplateShiroSessionModel());
        configuration.setSharedVariable("sessionId", new TemplateShiroSessionIdModel());
        configuration.setSharedVariable("principal", new TemplateShiroPrincipalModel());
        configuration.setSharedVariable("principals", new TemplateShiroPrincipalsModel());
        configuration.setSharedVariable("userId", new TemplateShiroCurrentUserIdModel());
    }

}
