package com.ssafy.confidentIs.keytris.controller;

import com.ssafy.confidentIs.keytris.common.dto.response.ErrorResponseDto;
import com.ssafy.confidentIs.keytris.common.dto.response.ResponseDto;
import com.ssafy.confidentIs.keytris.common.exception.ErrorCode;
import com.ssafy.confidentIs.keytris.common.exception.customException.InvalidInputValueException;
import com.ssafy.confidentIs.keytris.dto.WordListResponse;
import com.ssafy.confidentIs.keytris.dto.multiDto.*;
import com.ssafy.confidentIs.keytris.model.RoomStatus;
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

    private static final String SUCCESS = "success";

    private final MultiRoomServiceImpl multiRoomServiceImpl;

    private final SimpMessagingTemplate messagingTemplate;


    @PostMapping
    public ResponseEntity<?> createMultiGame(@RequestBody MultiGameCreateRequest request, Errors errors) {
        if (errors.hasErrors()) {
            // TODO 예외처리
        }
        log.info("멀티 게임 방 생성 요청: {}", request);

        ResponseDto responseDto = new ResponseDto(SUCCESS, "멀티모드 게임 생성",
                Collections.singletonMap("gameInfo", multiRoomServiceImpl.createMultiGame(request)));
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


    @PostMapping("/{roomId}")
    public ResponseEntity<?> connectMultiGame(@PathVariable String roomId,
                                              @RequestBody @Validated MultiGameConnectRequest request, Errors errors) {
        if (errors.hasErrors()) {
            throw new InvalidInputValueException("입력 값이 올바르지 않습니다.", ErrorCode.INVALID_INPUT_VALUE);
        }
        log.info("roomId: {}, request: {}", roomId, request);

        MultiGameConnectResponse response = multiRoomServiceImpl.connectMultiGame(roomId, request);

        messagingTemplate.convertAndSend("/topic/multi/" + roomId, response);

        ChatMessage chatMessage = ChatMessage.builder()
                .roomId(roomId)
                .playerId("notification")
                .content(request.getNickname() + "님이 입장했습니다.")
                .timestamp(new Date().toString())
                .build();
        messagingTemplate.convertAndSend("/topic/multi/chat/" + roomId, chatMessage);

        ResponseDto responseDto = new ResponseDto(SUCCESS, "멀티모드 게임 접속",
                Collections.singletonMap("gameInfo", response));
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


    // 방장이 게임을 시작하는 api
    @MessageMapping("/multi/start/{roomId}")
    public void startMultiGame(@DestinationVariable String roomId, @RequestBody @Validated MultiGamePlayerRequest request) {
        log.info("roomId: {}, request: {}", roomId, request);

        MultiGameInfoResponse response = multiRoomServiceImpl.startMultiGame(roomId, request);
        messagingTemplate.convertAndSend("/topic/multi/start/" + roomId, response);
    }


    // 플레이어 상태를 ready로 업데이트 하는 api
    @MessageMapping("/multi/player-ready/{roomId}")
    public void updatePlayerToReady(@DestinationVariable String roomId, @RequestBody MultiGamePlayerRequest request) {
        log.info("roomId: {}, playerId: {}", roomId, request.getPlayerId());
        UpdatedPlayerResponse response = multiRoomServiceImpl.updatePlayerToReady(roomId, request.getPlayerId());
        messagingTemplate.convertAndSend("/topic/multi/player-ready/" + roomId, response);
    }


    // 플레이어 상태를 over로 업데이트 하는 api
    @MessageMapping("/multi/player-over/{roomId}")
    public void updatePlayerToOver(@DestinationVariable String roomId, @RequestBody MultiGamePlayerRequest request) {
        log.info("player-over 요청. roomId: {}, playerId: {}", roomId, request.getPlayerId());
        UpdatedPlayerResponse response = multiRoomServiceImpl.updatePlayerToOver(roomId, request.getPlayerId());
        messagingTemplate.convertAndSend("/topic/multi/player-over/" + roomId, response);

        if (response.getRoomStatus().equals(RoomStatus.FINISHED)) {
            MultiGameResultResponse resultResponse = multiRoomServiceImpl.getGameResult(roomId);
            messagingTemplate.convertAndSend("/topic/multi/end/" + roomId, resultResponse);
        }
    }


    // 단어 입력 api
    @MessageMapping("/multi/play/{roomId}")
    public void guessWord(@DestinationVariable String roomId, @RequestBody MultiGuessRequest request) {
        log.info("roomId: {}, playerId: {}, guessWord: {}, targetWord: {}, currentWordList: {}",
                roomId, request.getPlayerId(), request.getGuessWord(), request.getTargetWord(), request.getCurrentWordList());

        WordListResponse response = multiRoomServiceImpl.sortByProximity(roomId, request);

        messagingTemplate.convertAndSend("/topic/multi/play/" + roomId, response);
    }


    // 단어 입력 api http 버전
    @PostMapping("/{roomId}/guess-word")
    public ResponseEntity<?> guessWord2(@PathVariable String roomId, @RequestBody MultiGuessRequest request) {
        log.info("roomId: {}, playerId: {}, guessWord: {}, targetWord: {}, currentWordList: {}",
                roomId, request.getPlayerId(), request.getGuessWord(), request.getTargetWord(), request.getCurrentWordList());

        WordListResponse response = multiRoomServiceImpl.sortByProximity(roomId, request);

        ResponseDto responseDto = new ResponseDto(SUCCESS, "guess-word", Collections.singletonMap("response", response));
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


    // 채팅 메시지 전달
    @MessageMapping("/multi/chat/{roomId}")
    public void sendMessage(@DestinationVariable String roomId, @RequestBody ChatMessage message) {
        log.info("message.content: {}", message.getContent());
        message.updateTime(LocalDateTime.now().toString());
        messagingTemplate.convertAndSend("/topic/multi/chat/" + roomId, message);
    }


}
