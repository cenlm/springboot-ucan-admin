package com.ucan.websocket;

import java.util.Iterator;
import java.util.Objects;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Description: SocketServer 获取工具
 * @author liming.cen
 * @date 2024-07-11 19:33:33
 * 
 */
@Component
public class SocketServerManager implements InitializingBean, DisposableBean, ApplicationContextAware {
    @Autowired
    private SocketServer socketServer;
    ApplicationContext applicationContext;

    /**
     * 从bean工厂中获取socketServer
     * 
     * @return
     */
    public SocketServer getSocketServer() {
        return socketServer;
    }

    @Override
    public void destroy() throws Exception {
        if (!Objects.isNull(socketServer)) {
            // 停止socket服务器
            socketServer.stop();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Object bean = applicationContext.getBean("userController");
        // 启动web socket服务
        socketServer.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        Object bean = applicationContext.getBean("userController");
//        for (int i = 0; i < beanDefinitionNames.length; i++) {
//            System.out.println(beanDefinitionNames[i]);
//        }

    }

}
