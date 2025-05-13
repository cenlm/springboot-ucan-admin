package com.ucan.util.xss;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    /**
     * 基本数据类型的包装类
     */
    private static List<Class<?>> wrapperClasses = new ArrayList<Class<?>>(Arrays.asList(Byte.class, Short.class,
            Integer.class, Long.class, Float.class, Double.class, Character.class, Boolean.class));

    /**
     * 对参数对象进行数据清洗
     * 
     * @param obj
     * @return
     * @throws Exception
     */
    public static Object processObject(Object obj) throws Exception {
        if (obj == null)
            return null;

        // 如果是字符串直接转义或替换
        if (obj instanceof String) {
            return cleanHtml((String) obj);
        }

        // 如果是集合，递归处理元素
        if (obj instanceof Collection) {
            Collection<?> collection = (Collection<?>) obj;
            List<Object> cleanedList = new ArrayList<>();
            for (Object item : collection) {// 递归处理集合
                cleanedList.add(processObject(item));
            }
            return cleanedList;
        } else if (obj instanceof Map) {// 清洗map数据
            Map<?, ?> map = (Map<?, ?>) obj;
            Map<Object, Object> cleanedMap = new HashMap<>();
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                cleanedMap.put(processObject(entry.getKey()), processObject(entry.getValue()));
            }
            return cleanedMap;
        } else if (obj.getClass().isArray()) {// 清洗数组数据
            Object[] array = (Object[]) obj;
            for (int i = 0; i < array.length; i++) {// 递归处理数组
                array[i] = processObject(array[i]);
            }
            return array;
        }

        try {
            Class<?> clazz = obj.getClass();
            // 如果参数类型是 基本数据类型的包装类，直接返回原数据
            if (wrapperClasses.contains(clazz)) {
                return obj;
            }
            // 如果是普通对象，递归处理其字段
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                Object fieldValue = field.get(obj);
                // 如果对象的属性值为null或者类型是基本数据类型的包装类，
                // 跳过数据清洗，继续下一个属性的判断与处理
                if (Objects.isNull(fieldValue) || "".equals(fieldValue)
                        || wrapperClasses.contains(fieldValue.getClass())) {
                    continue;
                }
                Object cleanedValue = processObject(fieldValue);
                // 重置属性新值
                field.set(obj, cleanedValue);
            }
        } catch (IllegalAccessException e) {
            // 处理反射异常（按需处理）
            throw new RuntimeException("XSS清洗失败", e);
        }

        return obj;
    }

    /**
     * HTML内容清洗
     * 
     * @param input
     * @return
     * @throws Exception
     * @throws IOException
     */
    private static String cleanHtml(String input) throws Exception {

        if (input == null || input.isEmpty()) {
            return input;
        }
        // 规范化输入
        String normalized = input.replace("\r\n", "\n").replace("\r", "\n");
        // 对字符进行解码
        normalized = decodeRecursively(normalized);

        // 高危模式过滤（不区分大小写），防绕行
        // (?i)<script[^>]*+> // 使用占有量词防止回溯
        String filtered = normalized.replaceAll("(?i)<script[^>]*+>", "").replaceAll("(?i)</script[^>]*+>", "")
                .replaceAll("(?i)<form[^>]*+>", "")// 防伪造登录表单钓鱼
                .replaceAll("(?i)</form[^>]*+>", "").replaceAll("(?i)<iframe[^>]*+>", "")
                .replaceAll("(?i)<img[^>]*+>", "").replaceAll("(?i)<style[^>]*+>", "")
                .replaceAll("(?i)style\\s*\\=", "").replaceAll("(?i)javascript\\s*:", "")
                .replaceAll("(?i)alert\\s*\\(", "").replaceAll("(?i)(?:^|\\s)(on[a-z]{3,})\\s*", "")// onXXX事件
                .replaceAll("(?i)data\\s*:", "").replaceAll("(?i)expression\\s*\\(", "");
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

    /**
     * ===============以下内容已废弃========================
     */

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * 处理 xss 内容
     * 
     * @param content
     * @return
     * @throws Exception
     */
    public static String processXss(String content) throws Exception {
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
                try {
                    node.put(entry.getKey(), cleanHtml(value));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (entry.getValue().isObject()) {
                processJson((ObjectNode) entry.getValue());
            }
        });
    }
}
