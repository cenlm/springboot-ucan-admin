package com.ucan.websocket;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @Description: web socket服务器
 * @author liming.cen
 * @date 2024-07-11 13:13:22
 * 
 */
@Component("socketServer")
@PropertySource(value = {"classpath:socket.properties"})
public class SocketServer extends WebSocketServer {
    private static Logger log = LoggerFactory.getLogger(SocketServer.class);
    private Map<String, WebSocket> clients = new HashMap<String, WebSocket>();
    private Map<WebSocket, String> temp = new HashMap<WebSocket, String>();

    public SocketServer() {
    }

    @Autowired
    public SocketServer(@Value("${socket-port}") int port) throws UnknownHostException {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onStart() {
        log.info("Web Socket服务器已启动");
    }

    @Override
    public void onOpen(WebSocket client, ClientHandshake handshake) {
        log.info(client.getRemoteSocketAddress().getAddress().getHostAddress() + "：远程客户端已连接!");
    }

    @Override
    public void onMessage(WebSocket client, String principal) {
        // 保存已连接进来的客户端连接
        clients.put(principal, client);
        // 用于在客户端与服务端断开连接时检索无效连接
        temp.put(client, principal);
        log.info("【" + principal.split("_")[0] + "】已连接web socket服务器");
    }

    @Override
    public void onClose(WebSocket client, int code, String reason, boolean remote) {
        // 断开连接（浏览器关闭）时，需要删除无效连接缓存
        String principal = temp.get(client);
        clients.remove(principal);
        temp.remove(client);
        log.info(client + "已断开连接");
    }

    @Override
    public void onError(WebSocket client, Exception ex) {
        log.error(client.getRemoteSocketAddress().getAddress().getHostAddress() + "连接错误：" + ex.getMessage());
    }

    /**
     * 获取Web Socket服务端的所有客户端连接
     * 
     * @return
     */
    public Map<String, WebSocket> getClients() {
        return clients;
    }

    /**
     * 获取指定用户的socket连接
     * 
     * @param principal
     * @return
     */
    public WebSocket getSocket(String principal) {
        return clients.get(principal);
    }

}
