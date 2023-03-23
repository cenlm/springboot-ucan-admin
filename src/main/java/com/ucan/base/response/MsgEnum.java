package com.ucan.base.response;

/**
 * @Description: 响应消息枚举类
 * @author liming.cen
 * @date 2023年1月3日 下午2:44:36
 */
public enum MsgEnum {
    FAIL(-1, "Fail!"), SUCCESS(0, "Ok!"), SERVER_ERROR(1, "服务器内部错误！");

    /**
     * 响应码
     */
    private int code;
    /**
     * 响应消息
     */
    private String msg;

    private MsgEnum(int code, String msg) {
	this.code = code;
	this.msg = msg;
    }

    public int getCode() {
	return code;
    }

    public void setCode(int code) {
	this.code = code;
    }

    public String getMsg() {
	return msg;
    }

    public void setMsg(String msg) {
	this.msg = msg;
    }

}
