package com.ucan.config.freemarker.model;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

/**
 * @Description: shiro session 模板模型
 * @author liming.cen
 * @date 2024-07-14 10:09:59
 * 
 */
public class TemplateShiroSessionIdModel implements TemplateMethodModelEx {

    @Override
    public Object exec(List arguments) throws TemplateModelException {
        Subject currentUser = SecurityUtils.getSubject();
        // 从服务器获取已存在的session，不存在时不会自动创建
        Session session = currentUser.getSession(false);
        return session;
    }

}
