package com.ucan.shiro;

import java.io.Serializable;
import java.util.UUID;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;

/**
* @Description: 
* @author liming.cen
* @date 2023-03-09 20:46:29 
* 
*/
public class UcanSessionIdGenerator implements SessionIdGenerator {

    @Override
    public Serializable generateId(Session session) {
	return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
