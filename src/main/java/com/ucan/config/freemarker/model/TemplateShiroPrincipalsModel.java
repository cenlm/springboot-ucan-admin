package com.ucan.config.freemarker.model;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

/**
 * @Description: shiro principals 模板模型，返回列表
 * @author liming.cen
 * @date 2024-07-14 10:09:59
 * 
 */
public class TemplateShiroPrincipalsModel implements TemplateMethodModelEx {

    @Override
    public Object exec(List arguments) throws TemplateModelException {
        Subject currentUser = SecurityUtils.getSubject();
        return currentUser.getPrincipals();
    }

}
