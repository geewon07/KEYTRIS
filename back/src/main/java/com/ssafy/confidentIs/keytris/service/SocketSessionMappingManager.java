package com.ssafy.confidentIs.keytris.service;

import com.ssafy.confidentIs.keytris.dto.multiDto.ChatMessage;
import com.ssafy.confidentIs.keytris.dto.multiDto.MultiGameResultResponse;
import com.ssafy.confidentIs.keytris.dto.multiDto.UpdatedPlayerResponse;
import com.ssafy.confidentIs.keytris.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class SocketSessionMappingManager {

    private final RoomService roomService;
    private final MultiRoomServiceImpl multiRoomService;
    private final SessionMethodService sessionMethodService;
    private final SimpMessagingTemplate messagingTemplate;


    // 웹소켓세션ID를 기반으로 방, 플레이어 정보 저장
    private final ConcurrentHashMap<String, SessionInfo> sessionInfoMap = new ConcurrentHashMap<>();

    // 방 ID를 기반으로 해당 방에 연결된 세션ID 저장
    private final ConcurrentHashMap<String, Set<String>> roomSessionsMap = new ConcurrentHashMap<>();


    public void registerSession(String sessionId, String roomType, String roomId, String playerId) {
        SessionInfo info = SessionInfo.builder()
                .roomType(roomType).roomId(roomId).playerId(playerId).build();
        sessionInfoMap.put(sessionId, info);

        roomSessionsMap.computeIfAbsent(roomId, k -> new HashSet<>()).add(sessionId);
        log.info("sessionInfoMap 개수: {}, roomSessionMap 개수: {}", sessionInfoMap.size(), roomSessionsMap.size());
    }


    public void deregisterSession(String sessionId) {
        SessionInfo info = sessionInfoMap.remove(sessionId);
        if (info != null) {
            String roomType = info.getRoomType();
            String roomId = info.getRoomId();
            String playerId = info.getPlayerId();

            Set<String> sessionsInRoom = roomSessionsMap.get(roomId);
            log.info("sessionsInRoom: {}", sessionsInRoom);
            if (sessionsInRoom != null) {
                sessionsInRoom.remove(sessionId);

                // 멀티인 경우, player 상태 변경 또는 제거 로직 수행
                if(roomType.equals(RoomType.MULTI.toString())) {
                    MultiRoom room = multiRoomService.findByRoomId(roomId, playerId);
                    log.info("기존 room status 정보: {}", room.getRoomStatus());

                    if(room.getRoomStatus().equals(RoomStatus.ONGOING)) { 
                        // 게임 중에 나가는 경우 플레이어 상태 OVER로 업데이트 & 다른 플레이어들에게 정보 전달
                        UpdatedPlayerResponse response = multiRoomService.updatePlayerToOver(roomId, playerId);
                        messagingTemplate.convertAndSend("/topic/multi/player-over/" + roomId, response);

                        if (response.getRoomStatus().equals(RoomStatus.FINISHED)) {
                            MultiGameResultResponse resultResponse = multiRoomService.getGameResult(roomId);
                            messagingTemplate.convertAndSend("/topic/multi/end/" + roomId, resultResponse);
                        }
                    } else {
                        // TODO 플레이어 제거 로직 필요. 방장이 연결 끊기면 방장이 변경되거나 게임 나가라는 메시지 보내야 함.
//                        multiRoomService.removePlayer(room, playerId);
//                        String message = "플레이어 퇴장: " + playerId;
//                        messagingTemplate.convertAndSend("/topic/multi/chat" + roomId, message);
                    }
                }

                if (sessionsInRoom.isEmpty()) {
                    roomSessionsMap.remove(roomId);
                    // 레벨어 전송 중단
                    sessionMethodService.stopSessionMethod(roomId);

                    /* room status와 관계없이 주기적으로 room 삭제하자
                    // room status를 finished로 업데이트
                    if(roomType.equals(RoomType.MULTI.toString())) {
                        MultiRoom room = multiRoomService.findByRoomId(roomId, playerId);
                        room.updateStatus(RoomStatus.FINISHED);
                    } else if(roomType.equals(RoomType.SINGLE.toString())) {
                        Room room = roomService.findByRoomId(roomId);
                        room.updateStatus(RoomStatus.FINISHED);
                    }
                     */
                }
            }

            log.info("socket 연결 끊기. sessionInfo: {}", info);
            log.info("socket 연결 끊기. sessionInfoMap 개수: {}, roomSessionMap 개수: {}", sessionInfoMap.size(), roomSessionsMap.size());
        }
    }

}
