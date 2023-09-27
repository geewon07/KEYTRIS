package com.ssafy.confidentIs.keytris.service;

import com.ssafy.confidentIs.keytris.model.SessionInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class SocketSessionMappingManager {

    private final MultiRoomServiceImpl multiRoomService;


    // 웹소켓세션ID를 기반으로 방, 플레이어 정보 저장
    private final ConcurrentHashMap<String, SessionInfo> sessionInfoMap = new ConcurrentHashMap<>();

    // 방 ID를 기반으로 해당 방에 연결된 세션ID 저장
    private final ConcurrentHashMap<String, Set<String>> roomSessionsMap = new ConcurrentHashMap<>();


    public void registerSession(String sessionId, String roomType, String roomId, String playerId) {

        // TODO 하나의 클라이언트에서 동시에 여러 게임방에 접속하여 sessionId가 겹치는 위험이 있을 수 있음.
        // 클라이언트에서 여러 게임방에 동시 접속이 불가능하게 구현 필요
        if(sessionInfoMap.contains(sessionId)) {
//            throw new Exception("SessionId already registred: " + sessionId);
            log.info("동일한 세션이 이미 존재합니다. 다른 게임을 종료 후 다시 시도해주세요. sessionId: {}", sessionId);
        }

        SessionInfo info = SessionInfo.builder()
                .roomType(roomType).roomId(roomId).playerId(playerId).build();
        sessionInfoMap.put(sessionId, info);

        roomSessionsMap.computeIfAbsent(roomId, k -> new HashSet<>()).add(sessionId);
    }


    public void deregisterSession(String sessionId) {
        SessionInfo info = sessionInfoMap.remove(sessionId);
        if (info != null) {
            String roomType = info.getRoomType();
            String roomId = info.getRoomId();
            String playerId = info.getPlayerId();
            log.info("roomType: {}, roomId: {}, playerId: {}", roomType, roomId, playerId);

            Set<String> sessionsInRoom = roomSessionsMap.get(roomId);
            if (sessionsInRoom != null) {
                sessionsInRoom.remove(sessionId);
                // TODO 멀티인 경우에만 player 삭제? status 업데이트?
                // 시작 전에 나가면 삭제하고 아니면 finished로 업데이트 해야함
                if (sessionsInRoom.isEmpty()) {
                    roomSessionsMap.remove(roomId);
                    // TODO room status를 finished로 업데이트 하는 로직 추가
                    // TODO 레벨어 전송 종료
                }
            }
        }
    }

}
