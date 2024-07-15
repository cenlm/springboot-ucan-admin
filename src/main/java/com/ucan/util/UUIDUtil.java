package com.ucan.util;

import java.util.UUID;

/**
 * UUID生成工具
 * 
 * @author liming.cen
 * @date 2022年12月24日 上午9:30:00
 */
public class UUIDUtil {
    public static String getUuid() {
	return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
