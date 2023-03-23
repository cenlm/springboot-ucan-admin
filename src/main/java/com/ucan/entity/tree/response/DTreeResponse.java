package com.ucan.entity.tree.response;

/**
 * @Description: DTree树节点数据响应类
 * @author liming.cen
 * @date 2023年2月19日 下午3:45:27
 */
public class DTreeResponse {
//    /** 状态码 */
//    private int code;
//    /** 信息标识 */
//    private String msg;
    /** 状态类 */
    private Status status;
    /**
     * 从数据库查询，经过拼装后，符合节点树格式的数据
     */
    private Object data;

    public Status getStatus() {
	return status;
    }

    public void setStatus(Status status) {
	this.status = status;
    }

    public Object getData() {
	return data;
    }

    public void setData(Object data) {
	this.data = data;
    }

}
