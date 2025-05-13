package com.ucan.req;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.ucan.util.xss.XssFilterUtil;

/**
 * @Description: 已废弃，仅作为理解HttpServletRequestWrapper包装类和Spring MVC传参处理逻辑使用<br>
 * 
 *               自定义RequestWrapper（使用OWASP Java Encoder），供XssFilter使用<br>
 *               <br>
 *               SpringMVC在参数绑定过程中，主要依赖 ServletRequest的以下方法：<br>
 * 
 *               getParameter()：单值参数绑定
 * 
 *               getParameterValues()：多值参数绑定
 * 
 *               getInputStream()：请求体数据解析（如JSON）
 * 
 *               getParameterMap()：用于POJO对象反射填充
 * @author liming.cen
 * @date 2025-04-20 16:31:51
 * 
 */
@Deprecated
public class XssRequestWrapper extends HttpServletRequestWrapper {

    private final byte[] cachedBody;
    private final Map<String, ArrayList<String>> parameterMap;

    /**
     * 请求包装类构造方法
     * 
     * @param request
     * @throws IOException
     */
    public XssRequestWrapper(HttpServletRequest request) throws Exception {
        super(request);
        this.cachedBody = readAndProcessBody(request);
        // 解析表单数据到参数集合
        this.parameterMap = parseFormData();
    }

    /**
     * 读取原始数据并进行XSS过滤处理（一般为form表单提交过来的JSON数据）
     * 
     * @param request
     * @return
     * @throws IOException
     */
    private byte[] readAndProcessBody(HttpServletRequest request) throws Exception {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        String processed = "";
        @SuppressWarnings("unused")
        String requestURI = request.getRequestURI();
        try {
            // 读取原始请求数据字节流
            InputStream inputStream = request.getInputStream();
            byte[] data = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, bytesRead);
            }
            buffer.flush();
            processed = XssFilterUtil.processXss(buffer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return processed.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * 请求体数据解析（如JSON）
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new CachedBodyServletInputStream(cachedBody);
    }

    /**
     * 重写请求体缓存读取器
     */
    @Override
    public BufferedReader getReader() {
        // 动态解析字符编码，异常时回退至UTF-8
        String charsetName = determineCharset();
        Charset charset;
        try {
            charset = Charset.forName(charsetName);
        } catch (IllegalArgumentException e) {
            charset = StandardCharsets.UTF_8; // 编码无效时强制使用UTF-8
        }
        return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(cachedBody), charset));
    }

    // 自定义ServletInputStream（增强读取性能）
    private static class CachedBodyServletInputStream extends ServletInputStream {

        private final ByteArrayInputStream buffer;

        public CachedBodyServletInputStream(byte[] cachedBody) {
            this.buffer = new ByteArrayInputStream(cachedBody);
        }

        @Override
        public int read() {
            return buffer.read();
        }

        // 重写批量读取方法提升效率
        @Override
        public int read(byte[] b, int off, int len) {
            return buffer.read(b, off, len);
        }

        @Override
        public boolean isFinished() {
            return buffer.available() == 0;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener listener) {
            throw new UnsupportedOperationException("异步处理未实现");
        }
    }

    /**
     * 动态解析字符编码
     * 
     * @return
     */
    private String determineCharset() {
        // 1. 优先从请求直接获取编码
        String charset = super.getCharacterEncoding();
        if (charset != null)
            return charset;

        // 2. 尝试从Content-Type头解析
        String contentType = getContentType();
        if (contentType != null) {
            String[] parts = contentType.split(";");
            for (String part : parts) {
                part = part.trim();
                if (part.startsWith("charset=")) {
                    return part.substring("charset=".length()).trim();
                }
            }
        }

        // 3. 使用默认编码（推荐UTF-8，根据规范可改用ISO-8859-1）
        return StandardCharsets.UTF_8.name();
    }

    @Override
    public String getParameter(String name) {
        ArrayList<String> paramValues = parameterMap.get(name);
        return paramValues != null && paramValues.size() > 0 ? paramValues.get(0) : null;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        // parameterMap=Map<String, ArrayList<String>>();
        Map<String, String[]> paramMap = new LinkedHashMap<String, String[]>();
        parameterMap.forEach((k, v) -> {
            String[] value;
            if (Objects.isNull(v)) {
                value = null;
            } else {
                value = (String[]) v.toArray();
            }
            paramMap.put(k, value);
        });
        return paramMap;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(parameterMap.keySet());
    }

    @Override
    public String[] getParameterValues(String name) {
        ArrayList<String> paramValues = parameterMap.get(name);
        if (Objects.isNull(paramValues) || paramValues.size() == 0) {
            return null;
        } else {

            String[] strArray = new String[paramValues.size()];
            for (int i = 0; i < strArray.length; i++) {
                // 将列表元素放入到strArray
                if (paramValues.get(i) != null) {
                    strArray[i] = paramValues.get(i);
                } else {
                    strArray[i] = ""; // 处理 null 值
                }
            }
            return strArray;
        }
    }

    /**
     * 解析经过XSS过滤处理的数据，参考org.apache.tomcat.util.http.Parameters#addParameter(k,v)
     */
    private Map<String, ArrayList<String>> parseFormData() {
        Map<String, ArrayList<String>> paramHashValues = new LinkedHashMap<>();
        try {
            String body = new String(cachedBody, getCharacterEncoding());
            // 手动解析键值对，例如：name=John&age=20
            String[] pairs = body.split("&amp;");
            for (String pair : pairs) {
                String[] kv = pair.split("=");
                if (kv.length == 2) {
                    String key = URLDecoder.decode(kv[0], getCharacterEncoding());
                    String value = URLDecoder.decode(kv[1], getCharacterEncoding());
                    paramHashValues.computeIfAbsent(key, k -> new ArrayList<>(1)).add(value);
                }
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Failed to parse form data", e);
        }
        return paramHashValues;
    }

}
