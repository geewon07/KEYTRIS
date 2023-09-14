package com.ssafy.confidentIs.keytris.controller;

import com.ssafy.confidentIs.keytris.common.dto.response.ResponseDto;
import com.ssafy.confidentIs.keytris.dto.multiDto.*;
import com.ssafy.confidentIs.keytris.service.MultiRoomServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/multigames")
public class MultiGameController {

    private static final String success = "SUCCESS";

    private final MultiRoomServiceImpl multiRoomServiceImpl;

    private SimpMessagingTemplate messagingTemplate;


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


    @PostMapping("/connect")
    public ResponseEntity<?> connectMultiGame(@RequestBody MultiGameConnectRequest request, Errors errors) {

        if(errors.hasErrors()) {
            // TODO 예외처리
        }
        log.info("request: {}", request);

        ResponseDto responseDto = new ResponseDto(success, "멀티모드 게임 생성",
                Collections.singletonMap("gameInfo", multiRoomServiceImpl.connectMultiGame(request)));
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }



    @PostMapping("/guess-word")
    public ResponseEntity<?> guessWord(@RequestBody MultiGuessRequest request) {
        log.info("roomId: {}, playerId: {}, guessWord: {}", request.getRoomId(), request.getPlayerId(), request.getGuessWord());
        MultiGuessResponse response = multiRoomServiceImpl.sortByProximity(request);

        ResponseDto responseDto = new ResponseDto(success, "guess-word", Collections.singletonMap("response", response));
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


}
