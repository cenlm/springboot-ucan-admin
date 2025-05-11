package com.ucan.util.xss;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.owasp.encoder.Encode;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @Description:XSS处理工具类
 * @author liming.cen
 * @date 2025-05-09 14:58:23
 * 
 */
public class XssFilterUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * 处理 xss 内容
     * 
     * @param content
     * @return
     * @throws IOException
     */
    public static String processXss(String content) throws IOException {
        if (content == null || content.isEmpty()) {
            return content;
        }
        String decodeContent = "";
        try {
            // 先对内容进行解码，避免cleanHtml中的正则匹配失败（自动编码会将 < 变成 %3C）
            decodeContent = decodeRecursively(content);
            // 尝试将请求数据解析为JSON格式，并进行XSS风险过滤
            ObjectNode root = (ObjectNode) mapper.readTree(decodeContent);
            processJson(root);
            return mapper.writeValueAsString(root);
        } catch (Exception e) {
            // 非JSON内容直接处理
            return cleanHtml(decodeContent);
        }
    }

    /**
     * 对JSON数据进行XSS风险过滤
     * 
     * @param node
     */
    private static void processJson(ObjectNode node) {
        node.fields().forEachRemaining(entry -> {
            if (entry.getValue().isTextual()) {
                String value = entry.getValue().asText();
                node.put(entry.getKey(), cleanHtml(value));
            } else if (entry.getValue().isObject()) {
                processJson((ObjectNode) entry.getValue());
            }
        });
    }

    /**
     * 
     * HTML内容的替换
     * 
     * @param input
     * @return
     * @throws IOException
     */
    private static String cleanHtml(String input) {

        if (input == null || input.isEmpty()) {
            return input;
        }
        // 规范化输入
        String normalized = input.replace("\r\n", "\n").replace("\r", "\n");

        // 高危模式过滤（不区分大小写），防绕行
        // (?i)<script[^>]*+> // 使用占有量词防止回溯
        String filtered = normalized.replaceAll("(?i)<script[^>]*+>", "")
                .replaceAll("(?i)</script[^>]*+>", "")
                .replaceAll("(?i)<form[^>]*+>", "")//防伪造登录表单钓鱼
                .replaceAll("(?i)</form[^>]*+>", "")
                .replaceAll("(?i)<iframe[^>]*+>", "")
                .replaceAll("(?i)<img[^>]*+>", "")
                .replaceAll("(?i)<style[^>]*+>", "")
                .replaceAll("(?i)style\\s*\\=", "")
                .replaceAll("(?i)javascript\\s*:", "")
                .replaceAll("(?i)alert\\s*\\(", "")
                .replaceAll("(?i)on\\w+\\s*=", "")//onXXX事件
                .replaceAll("(?i)data\\s*:", "")
                .replaceAll("(?i)expression\\s*\\(", "");
//        System.out.println("清洗后的数据：" + filtered);
        // HTML实体编码
        String encoded = Encode.forHtml(filtered);

        // 安全格式化（使用白名单机制）
        String formatted = encoded.replace("\n", "<br>").replace(" ", "&nbsp;");

        Safelist safelist = Safelist.relaxed().removeAttributes(":all", "on*");// 移除所有 on 事件属性
        return Jsoup.clean(formatted, safelist);
    }

    /**
     * 递归解码直到无%xx编码
     * 
     * @param input
     * @return
     * @throws Exception
     */
    private static String decodeRecursively(String input) throws Exception {
        String current = input;
        String previous;
        do {
            previous = current;
            current = URLDecoder.decode(current, StandardCharsets.UTF_8.name());
        } while (!current.equals(previous));
        return current;
    }
}
