package com.crieff.framework.socket;

import com.corundumstudio.socketio.SocketIOServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @description:
 * @author: aKuang
 * @time: 2025/1/6 10:17
 */
@Slf4j
@Component
public class SocketHandler {

    @Autowired
    private SocketIOServer socketIoServer;

    /**
     * 服务端启动
     * Spring IoC容器创建之后，在加载SocketIOService Bean之后启动
     */
    @PostConstruct
    private void autoStartup() {
        socketIoServer.start();
        init();
    }

    /**
     * 服务端关闭
     * Spring IoC容器在销毁SocketIOService Bean之前关闭,避免重启项目服务端口占用问题
     */
    @PreDestroy
    private void autoStop() {
        socketIoServer.stop();
    }

    public void init() {
        log.debug("SocketEventListener initialized");
        //添加监听，客户端自动连接到 socket 服务端
        socketIoServer.addConnectListener(client -> {
            String userId = client.getHandshakeData().getSingleUrlParam("userId");
            SocketClient.connectMap.put(userId, client);
            log.debug("客户端userId: " + userId + "已连接，客户端ID为：" + client.getSessionId());
        });

        //添加监听，客户端跟 socket 服务端自动断开
        socketIoServer.addDisconnectListener(client -> {
            String userId = client.getHandshakeData().getSingleUrlParam("userId");
            SocketClient.connectMap.remove(userId, client);
            log.debug("客户端userId:" + userId + "断开连接，客户端ID为：" + client.getSessionId());
        });
    }
}
