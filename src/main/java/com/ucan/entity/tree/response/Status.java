package com.ucan.entity.tree.response;

/**
 * @Description: 树节点数据返回状态类
 * @author liming.cen
 * @date 2023年2月19日 下午3:38:32
 */
public class Status {
    /** 状态码 */
    private int code = 200;
    /** 信息标识 */
    private String message = "success";

    public int getCode() {
	return code;
    }

    public void setCode(int code) {
	this.code = code;
    }

    public String getMessage() {
	return message;
    }

    public void setMessage(String message) {
	this.message = message;
    }

}
