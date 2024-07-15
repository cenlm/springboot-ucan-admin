package com.ucan.shiro.util;

import org.apache.shiro.session.ExpiredSessionException;

/**
 * @Description: Session过期时抛出的异常，主要用于强制指定用户的session过期，并将其踢出系统
 * @author liming.cen
 * @date 2024-07-11 10:14:35
 * 
 */
public class UcanExpiredSessionException extends ExpiredSessionException {

    private static final long serialVersionUID = 2750696201598174359L;

    public UcanExpiredSessionException() {
        super();
    }

    public UcanExpiredSessionException(String message) {
        super(message);
    }

    public UcanExpiredSessionException(Throwable cause) {
        super(cause);
    }

    public UcanExpiredSessionException(String message, Throwable cause) {
        super(message, cause);
    }

}
