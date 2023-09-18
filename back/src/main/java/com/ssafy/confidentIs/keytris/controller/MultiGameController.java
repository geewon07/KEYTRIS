package com.ssafy.confidentIs.keytris.controller;

import com.ssafy.confidentIs.keytris.common.dto.response.ResponseDto;
import com.ssafy.confidentIs.keytris.dto.multiDto.*;
import com.ssafy.confidentIs.keytris.model.PlayerStatus;
import com.ssafy.confidentIs.keytris.service.MultiRoomServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/multigames")
public class MultiGameController {

    private static final String success = "SUCCESS";

    private final MultiRoomServiceImpl multiRoomServiceImpl;

    private final SimpMessagingTemplate messagingTemplate;


    @PostMapping
    public ResponseEntity<?> createMultiGame(@RequestBody MultiGameCreateRequest request, Errors errors) {
        if(errors.hasErrors()) {
            // TODO 예외처리
        }
        log.info("nickname: {}", request);

        ResponseDto responseDto = new ResponseDto(success, "멀티모드 게임 생성",
                Collections.singletonMap("gameInfo", multiRoomServiceImpl.createMultiGame(request)));
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


    @PostMapping("/{roomId}")
    public ResponseEntity<?> connectMultiGame(@PathVariable String roomId,
                                              @RequestBody @Validated MultiGameConnectRequest request, Errors errors) {
        if(errors.hasErrors()) {
            // TODO 예외처리
        }
        log.info("roomId: {}, request: {}", roomId , request);

        MultiGameConnectResponse response = multiRoomServiceImpl.connectMultiGame(roomId, request);

        messagingTemplate.convertAndSend("/topic/multi/" + roomId, response);

        ChatMessage chatMessage = ChatMessage.builder()
                .roomId(roomId)
                .playerId("notification")
                .content(request.getNickname()+"님이 입장했습니다.")
                .timestamp(new Date().toString())
                .build();
        messagingTemplate.convertAndSend("/topic/multi/" + roomId + "/chat", chatMessage);

        ResponseDto responseDto = new ResponseDto(success, "멀티모드 게임 접속",
                Collections.singletonMap("gameInfo", response));
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


    // 방장이 게임을 시작하는 api
    @PutMapping("/{roomId}/start")
    public ResponseEntity<?> startMultiGame(@PathVariable String roomId,
                                            @RequestBody @Validated MultiGamePlayerRequest request, Errors errors) {
        if(errors.hasErrors()) {
            // TODO 예외처리
        }
        log.info("roomId: {}, request: {}",roomId , request);

        MultiGameInfoResponse response = multiRoomServiceImpl.startMultiGame(roomId, request);
        messagingTemplate.convertAndSend("/topic/multi/" + roomId, response);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    // 플레이어 상태를 ready로 업데이트 하는 api
    @PutMapping("/{roomId}/players/{playerId}/ready")
    public ResponseEntity<?> updatePlayerToReady(@PathVariable String roomId, @PathVariable String playerId) {

        UpdatedPlayerResponse response = multiRoomServiceImpl.updatePlayerToReady(roomId, playerId);
        messagingTemplate.convertAndSend("/topic/multi/" + roomId + "/player-status", response);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    // 플레이어 상태를 over로 업데이트 하는 api
    @PutMapping("/{roomId}/players/{playerId}/over")
    public ResponseEntity<?> updatePlayerToOver(@PathVariable String roomId, @PathVariable String playerId) {

        UpdatedPlayerResponse response = multiRoomServiceImpl.updatePlayerToOver(roomId, playerId);
        messagingTemplate.convertAndSend("/topic/multi/" + roomId + "/player-status", response);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping("/guess-word")
    public ResponseEntity<?> guessWord(@RequestBody MultiGuessRequest request) {
        log.info("roomId: {}, playerId: {}, guessWord: {}", request.getRoomId(), request.getPlayerId(), request.getGuessWord());
        MultiGuessResponse response = multiRoomServiceImpl.sortByProximity(request);


        // socket으로 변경되는 점수 모두에게 전송
        messagingTemplate.convertAndSend("/topic/multi/" + request.getRoomId(), Collections.singletonMap("guess-word-response", response));

        ResponseDto responseDto = new ResponseDto(success, "guess-word", Collections.singletonMap("response", response));
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


    // 채팅 메시지 전달
    @MessageMapping("/multi/chat/{roomId}")
    public void sendMessage(@DestinationVariable String roomId, @RequestBody ChatMessage message) {
        log.info("message.content: {}", message.getContent());
        message.updateTime(LocalDateTime.now().toString());
        log.info("timeStamp {}", message.getTimestamp());
        messagingTemplate.convertAndSend("/topic/multi/chat/" + message.getRoomId(), message);
    }


}
