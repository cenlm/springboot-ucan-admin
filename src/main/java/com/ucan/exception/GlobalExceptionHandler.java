package com.ucan.exception;

import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson2.JSON;
import com.ucan.base.response.Response;

/**
 * @Description: 全局异常处理器
 * @author liming.cen
 * @date 2023-03-29 20:14:21
 * 
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理自定义异常
     * 
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(CustomException.class)
    public String customExceptionHandler(Exception ex) {
	log.error(ex.getMessage());
	return JSON.toJSONString(Response.fail(ex.getMessage()));
    }

    /**
     * 账号限制登录异常
     * 
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(DisabledAccountException.class)
    public String disabledAccountExceptionHandler(Exception ex) {
	log.error(ex.getMessage());
	return JSON.toJSONString(Response.fail(ex.getMessage()));
    }

    /**
     * 账号密码不匹配
     * 
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(IncorrectCredentialsException.class)
    public String incorrectCredentialsExceptionHandler(Exception ex) {
	return JSON.toJSONString(Response.fail("用户名或密码错误！"));
    }

    /**
     * 空指针异常处理
     * 
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(NullPointerException.class)
    public String nullPointerExceptionHandler(Exception ex) {
	log.error(ex.getMessage());
	ex.printStackTrace();
	return JSON.toJSONString(Response.fail("系统出现空指针异常！"));
    }

    /**
     * 其他异常处理
     * 
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler
    public String exceptionHandler(Exception ex) {
	log.error(ex.getMessage());
	ex.printStackTrace();
	return JSON.toJSONString(Response.fail("系统出现异常！"));
    }

}
