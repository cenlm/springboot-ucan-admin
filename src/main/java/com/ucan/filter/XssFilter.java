package com.ucan.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.ucan.req.XssRequestWrapper;

/**
 * 
 * @Description:已废弃！改为 自定义注解 + AOP环绕通知进行更精细的Xss数据清洗<br>
 *                     XSS攻击过滤器，非HEAD、OPTIONS、文件上传 请求都要进行数据清洗<br>
 * 
 * @author liming.cen
 * @date 2025-04-20 16:28:51
 * 
 */
@Deprecated
public class XssFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        String contentType = httpServletRequest.getContentType();
        String method = httpServletRequest.getMethod();
        String requestURI = httpServletRequest.getRequestURI();
        System.out.println("method:" + method + " requestUrI:" + requestURI + " contentType:" + contentType);
      
        // GET、POST（且不为文件上传操作）、PUT、PATCH、DELETE请求，进行XSS过滤
        if ("GET".equalsIgnoreCase(httpServletRequest.getMethod())
                || ("POST".equalsIgnoreCase(httpServletRequest.getMethod())
                        && !contentType.startsWith("multipart/form-data"))
                || "PUT".equalsIgnoreCase(httpServletRequest.getMethod())
                || "PATCH".equalsIgnoreCase(httpServletRequest.getMethod())
                || "DELETE".equalsIgnoreCase(httpServletRequest.getMethod())) {
            HttpServletRequest wrappedRequest = null;
            try {
                wrappedRequest = new XssRequestWrapper((HttpServletRequest) request);
            } catch (Exception e) {
                e.printStackTrace();
            }
            chain.doFilter(wrappedRequest, response);
//            chain.doFilter(request, response);
        } else {// 不进行XSS过滤，直接进入下一个过滤器
            chain.doFilter(request, response);
        }
    }

}
