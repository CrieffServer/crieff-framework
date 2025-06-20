package com.crieff.framework.socket;

import com.corundumstudio.socketio.SocketIOClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description:
 * @author: aKuang
 * @time: 2025/1/6 11:40
 */
@Slf4j
@Component
public class SocketClient {

    public static ConcurrentHashMap<String, SocketIOClient> connectMap = new ConcurrentHashMap<>();

    /**
     * 单发消息（以 userId 为标识符，给用户发送消息）
     **/
    public static void sendToOne(String event, String userId, Object message) {
        //拿出某个客户端信息
        SocketIOClient socketClient = getSocketClient(userId);
        if (Objects.nonNull(socketClient)) {
            //单独给他发消息
            socketClient.sendEvent(event, message);
        } else {
            log.info(userId + "已下线，暂不发送消息。");
        }
    }

    /**
     * 群发消息
     **/
    public static void sendToAll(String event, Object message) {
        if (connectMap.isEmpty()) {
            return;
        }
        //给在这个频道的每个客户端发消息
        for (Map.Entry<String, SocketIOClient> entry : connectMap.entrySet()) {
            entry.getValue().sendEvent(event, message);
        }
    }

    /**
     * 根据 userId 识别出 socket 客户端
     */
    public static SocketIOClient getSocketClient(String userId) {
        SocketIOClient client = null;
        if (StringUtils.isNotBlank(userId) && !connectMap.isEmpty()) {
            for (String key : connectMap.keySet()) {
                if (userId.equals(key)) {
                    client = connectMap.get(key);
                }
            }
        }
        return client;
    }

    /**
     * 1）使用事件注解，服务端监听获取客户端消息；
     * 2）拿到客户端发过来的消息之后，可以再根据业务逻辑发送给想要得到这个消息的人；
     * 3）channel_system 之所以会向全体客户端发消息，是因为我跟前端约定好了，你们也可以自定定义；
     **/
//    @OnEvent(value = SocketEventContants.CHANNEL_SYSTEM)
//    public void channelSystemListener(String message) {
//        if (StringUtils.isBlank(message))){
//            return;
//        }
//        this.sendToAll(message);
//    }
}
