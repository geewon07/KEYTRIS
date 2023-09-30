package com.ssafy.confidentIs.keytris.config;

import com.ssafy.confidentIs.keytris.service.SocketSessionMappingManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@Slf4j
@RequiredArgsConstructor
public class StompEventListener {

    // TODO ConnectEvent에서는 roomId, playerId를 임시 저장소에 보관하고, Connected가 되면 그때 세션매니저에 저장하기
    // redis에 임시보관하거나 JVM에 보관하는 걸 검토해보자

    private final SocketSessionMappingManager socketSessionMappingManager;
    private final SimpMessagingTemplate messagingTemplate;


    // STOMP 연결될 때 수행할 로직
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());

        String sessionId = sha.getSessionId();
        String roomId = sha.getFirstNativeHeader("roomId");
        String playerId = sha.getFirstNativeHeader("playerId");
        String roomType = sha.getFirstNativeHeader("roomType");
        log.info("socket 연결 요청 도착. session: {}, roomType: {}, room: {}, player: {}", sessionId, roomType, roomId, playerId);

        socketSessionMappingManager.registerSession(sessionId, roomType, roomId, playerId);
//        messagingTemplate.convertAndSend("/topic/multi/chat/"+roomId, "소켓 연결 성공");
    }

    // STOMP 연결이 끊어질 때 수행할 로직
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());

        String sessionId = sha.getSessionId();
        socketSessionMappingManager.deregisterSession(sessionId);
    }

}
