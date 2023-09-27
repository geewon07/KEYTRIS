package com.ssafy.confidentIs.keytris.config;

import com.ssafy.confidentIs.keytris.service.SocketSessionMappingManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

@Component
@Slf4j
@RequiredArgsConstructor
public class StompEventListener {

    // TODO ConnectEvent에서는 roomId, playerId를 임시 저장소에 보관하고, Connected가 되면 그때 세션매니저에 저장하기
    // redis에 임시보관하거나 JVM에 보관하는 걸 검토해보자

    private final SocketSessionMappingManager socketSessionMappingManager;

    // STOMP 연결될 때 수행할 로직
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());

        String sessionId = sha.getSessionId();
        String roomId = sha.getFirstNativeHeader("roomId");
        String playerId = sha.getFirstNativeHeader("playerId");
        String roomType = sha.getFirstNativeHeader("roomType");
        log.info("socket 연결됨. session: {}, roomType: {}, room: {}, player: {}", sessionId, roomType, roomId, playerId);

        socketSessionMappingManager.registerSession(sessionId, roomType, roomId, playerId);
    }

    // STOMP 연결이 끊어질 때 수행할 로직
    @EventListener
    public void handleWebSocketDisconnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());

        String sessionId = sha.getSessionId();
//        String roomId = sha.getFirstNativeHeader("roomId");
//        String playerId = sha.getFirstNativeHeader("playerId");
//        log.info("socket 연결 끊김. session: {}, room: {}, player: {}", sessionId, roomId, playerId);

        log.info("socket 연결 끊김. session: {}", sessionId);

        socketSessionMappingManager.deregisterSession(sessionId);
        // TODO XX님이 퇴장했다고 이야기 해야하나? 그럼 닉네임도 받아와야 함.
    }

}
