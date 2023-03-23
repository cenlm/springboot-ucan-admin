package com.ucan.controller.error;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Description: 错误页跳转
 * @author liming.cen
 * @date 2023-03-23 17:14:55
 * 
 */
@Component
public class UcanErrorViewResolver implements ErrorViewResolver {

    @Override
    public ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status, Map<String, Object> model) {
	ModelAndView mavAndView = null;
	if (status == HttpStatus.INTERNAL_SERVER_ERROR) {// 500错误
	    mavAndView = new ModelAndView("error/500");
	} else if (status == HttpStatus.NOT_FOUND) {// 404
	    mavAndView = new ModelAndView("error/404");
	}
	// 其他错误页。。。
	return mavAndView;
    }

}
