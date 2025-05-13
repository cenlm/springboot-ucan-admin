package com.ucan.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ucan.util.xss.XssFilterUtil;

/**
 * @Description: Xss数据拦截（通过环绕通知）与处理逻辑切面
 * @author liming.cen
 * @date 2025-05-13 14:19:21
 * 
 */
@Aspect
@Component
public class XssCleanAspect {
    Logger log = LoggerFactory.getLogger(XssCleanAspect.class);

    /**
     * 拦截标记了@XssClean 的方法
     * 
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("@annotation(com.ucan.annotation.XssClean) || @within(com.ucan.annotation.XssClean)")
    public Object cleanXss(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info(joinPoint.getSignature().getDeclaringTypeName() + "#" + joinPoint.getSignature().getName()
                + " 方法进行了XSS数据清洗");
        // 获取目标方法的所有参数
        Object[] args = joinPoint.getArgs();
        if (args.length != 0) {
            for (int i = 0; i < args.length; i++) {
                args[i] = XssFilterUtil.processObject(args[i]);
            }
        }
        // 执行目标方法
        return joinPoint.proceed(args);
    }
}
