package com.crieff.framework.socket;

import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: aKuang
 * @time: 2025/1/6 10:21
 */
@Configuration
public class SocketIOConfig {

    @Value("${socketio.host:127.0.0.1}")
    private String host;
    @Value("${socketio.port:10001}")
    private Integer port;
    @Value("${socketio.bossCount:1}")
    private Integer bossCount;
    @Value("${socketio.workCount:10}")
    private Integer workCount;
    @Value("${socketio.allowCustomRequests:true}")
    private Boolean allowCustomRequests;
    @Value("${socketio.upgradeTimeout:3000}")
    private Integer upgradeTimeout;
    @Value("${socketio.pingTimeout:3000}")
    private Integer pingTimeout;
    @Value("${socketio.pingInterval:3000}")
    private Integer pingInterval;

    @Bean
    public SocketIOServer socketIOServer() {
        SocketConfig socketIOConfig = new SocketConfig();
        socketIOConfig.setTcpNoDelay(true);
        socketIOConfig.setSoLinger(0);
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setSocketConfig(socketIOConfig);
        config.setHostname(host);
        config.setPort(port);
        config.setBossThreads(bossCount);
        config.setWorkerThreads(workCount);
        config.setAllowCustomRequests(allowCustomRequests);
        config.setUpgradeTimeout(upgradeTimeout);
        config.setPingTimeout(pingTimeout);
        config.setPingInterval(pingInterval);
        return new SocketIOServer(config);
    }

    @Bean
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketIOServer) {
        return new SpringAnnotationScanner(socketIOServer);
    }
}
