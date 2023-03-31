package com.ucan.exception;

/**
 * @Description: 自定义异常
 * @author liming.cen
 * @date 2023年2月12日 上午11:32:41
 */
public class CustomException extends Exception {
    private static final long serialVersionUID = 9197207011122464627L;

    public CustomException() {
	super();
    }

    public CustomException(String msg) {
	super(msg);
    }

    public CustomException(Throwable cause) {
	super(cause);
    }

    public CustomException(String msg, Throwable cause) {
	super(msg, cause);
    }
}
