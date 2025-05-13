package com.ucan.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
/**
 * @Description: 对方法参数进行Xss数据清洗
 * @author liming.cen
 * @date 2025-05-13 14:14:29
 * 
 */
public @interface XssClean {

}
