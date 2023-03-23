package com.ucan.shiro.listener;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

/**
 * @Description: shiro session监听器
 * @author liming.cen
 * @date 2023年2月28日 上午11:06:06
 */
public class ShiroSessionListener implements SessionListener {

    @Override
    public void onStart(Session session) {
	System.out.println(session.getId() + ":Session已经启动。。。");

    }

    @Override
    public void onStop(Session session) {
	System.out.println(session.getId() + ":Session已经停止。。。");

    }

    @Override
    public void onExpiration(Session session) {
	System.out.println(session.getId() + ":Session已经过期。。。");

    }

}
