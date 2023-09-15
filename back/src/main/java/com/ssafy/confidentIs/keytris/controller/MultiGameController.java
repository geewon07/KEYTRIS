package com.ssafy.confidentIs.keytris.controller;

import com.ssafy.confidentIs.keytris.common.dto.response.ResponseDto;
import com.ssafy.confidentIs.keytris.dto.multiDto.*;
import com.ssafy.confidentIs.keytris.model.PlayerStatus;
import com.ssafy.confidentIs.keytris.service.MultiRoomServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;

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

        ResponseDto responseDto = new ResponseDto(success, "멀티모드 게임 접속",
                Collections.singletonMap("gameInfo", multiRoomServiceImpl.connectMultiGame(roomId, request)));
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


    // 플레이어 상태를 업데이트 하는 api
    @PutMapping("/{roomId}/player-status")
    public ResponseEntity<?> updatePlayerStatus(@PathVariable String roomId,
                                                @RequestBody UpdatePlayerRequest request) {

        String playerId = request.getPlayerId();
        PlayerStatus newStatus = request.getNewStatus();

        UpdatedPlayerResponse response = new UpdatedPlayerResponse();

        switch (newStatus) {
            case READY:
                response = multiRoomServiceImpl.updatePlayerToReady(roomId, playerId);
                break;
            case OVER:
                response = multiRoomServiceImpl.updatePlayerToOver(roomId, playerId);
                break;
        }

        messagingTemplate.convertAndSend("/topic/multigames/" + roomId, response);

        return null;
    }


    // 방장이 게임을 시작하는 api
    @PutMapping("/{roomId}/start")
    public ResponseEntity<?> startMultiGame(@PathVariable String roomId,
                                            @RequestBody @Validated MultiGamePlayerRequest request, Errors errors) {
        if(errors.hasErrors()) {
            // TODO 예외처리
        }
        log.info("roomId: {}, request: {}",roomId , request);

//        // TODO 초기 단어 리스트를 지금 보내줄 것인가? 입장하면 각자에게 보내줄 것인가? 결정 필요
//        MultiGameInfoRespone response = multiRoomServiceImpl.startMultiGame(roomId, request);
//        messagingTemplate.convertAndSend("/topic/multigames/" + roomId, response);

        return new ResponseEntity<>(null, HttpStatus.OK);
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
    @PostMapping("/{roomId}/send-message")
    public void sendMessage(@RequestBody ChatMessage message) {
        message.updateTime(LocalDateTime.now().toString());
        messagingTemplate.convertAndSend("/topic/multi/" + message.getRoomId(), message);
    }


}
