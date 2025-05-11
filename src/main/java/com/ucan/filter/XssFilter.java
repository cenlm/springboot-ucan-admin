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
 * @Description: XSS攻击过滤器，非HEAD、OPTIONS、文件上传 请求都要进行数据清洗
 * @author liming.cen
 * @date 2025-04-20 16:28:51
 * 
 */
public class XssFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        String contentType = httpServletRequest.getContentType();
        // HEAD、OPTIONS、文件上传 请求，不用进行XSS过滤
        if (!Objects.isNull(contentType) && ("HEAD".equalsIgnoreCase(httpServletRequest.getMethod())
                || "OPTIONS".equalsIgnoreCase(httpServletRequest.getMethod())
                || contentType.startsWith("multipart/form-data"))) {
            chain.doFilter(request, response);
            return;
        }else {//XSS过滤
            HttpServletRequest wrappedRequest = new XssRequestWrapper((HttpServletRequest) request);
            chain.doFilter(wrappedRequest, response);
        }
    }

}
