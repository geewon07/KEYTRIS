package com.ssafy.confidentIs.keytris.controller;

import com.ssafy.confidentIs.keytris.common.dto.ResponseDto;
import com.ssafy.confidentIs.keytris.dto.gameDto.CreateRequest;
import com.ssafy.confidentIs.keytris.dto.gameDto.GuessRequest;
import com.ssafy.confidentIs.keytris.dto.gameDto.NewsRequest;
import com.ssafy.confidentIs.keytris.dto.gameDto.OverRequest;
import com.ssafy.confidentIs.keytris.dto.gameDto.RankingRequest;
import com.ssafy.confidentIs.keytris.dto.gameDto.StartRequest;
import com.ssafy.confidentIs.keytris.service.PlayerService;
import com.ssafy.confidentIs.keytris.service.RoomService;
import com.ssafy.confidentIs.keytris.service.ScoreService;

import java.util.Collections;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@ToString
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/games")
public class GameController {

    private final PlayerService playerService;
    private final RoomService roomService;
    private final ScoreService scoreService;

    private final SimpMessagingTemplate messagingTemplate;
    private final RestTemplate restTemplate;

    @Value("${naver.client.id}")
    private String naverClientId;

    @Value("${naver.client.secret}")
    private String naverClientSecret;


    @MessageMapping("")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateRequest request) {
        log.info("create singlePlayer & room, category: {}", request.getCategory());
        ResponseDto responseDto = new ResponseDto("success", "방만들기 성공",
                Collections.singletonMap("StatusResponse",
                        roomService.createRoom(request.getCategory())));//101 ~105
        log.info("created :{}", responseDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/start")
    public ResponseEntity<?> start(@RequestBody StartRequest request) {
        ResponseDto responseDto = new ResponseDto("success", "게임 시작",
                Collections.singletonMap("StartResponse", roomService.startRoom(request.getRoomId())));
        log.info("started :{}", responseDto);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/guess-word")
    public ResponseEntity<?> enter(@RequestBody GuessRequest request) {
        log.info("GuessRequest: {}", request);
        return new ResponseEntity<>(roomService.enterWord(request), HttpStatus.OK);
    }

    @PostMapping("/over")
    public ResponseEntity<?> gameOver(@RequestBody OverRequest request) {
        ResponseDto responseDto;
        responseDto = new ResponseDto("success", "게임 종료",
                Collections.singletonMap("OverResponse",
                        roomService.gameOver(request)));
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/ranking")
    public ResponseEntity<?> newRecord(@RequestBody RankingRequest request) {
        log.info("new record, name:{}", request.getNickname());//랭킹 redis 저장 대체
        ResponseDto responseDto = new ResponseDto("success", "신기록 등록", Collections.singletonMap("RankingResponse",
                scoreService.addHighscore(request.getNickname(), request.getRoomId())));
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/news")
    public ResponseEntity<?> getNaverNews(@RequestBody NewsRequest request) {
        log.info("뉴스 가져오기, lastWord:{}", request.getLastWord());

        HttpHeaders headers = new HttpHeaders();

        headers.add("X-Naver-Client-Id", naverClientId);
        headers.add("X-Naver-Client-Secret", naverClientSecret);

        HttpEntity<MultiValueMap<String, String>> newsRequest = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("https://openapi.naver.com/v1/search/news.json?query=" + request.getLastWord() + "&display=3", HttpMethod.GET,
            newsRequest, String.class);

        ResponseDto responseDto = new ResponseDto("success", "뉴스 가져오기 성공", Collections.singletonMap("NewsResponse",
            response.getBody()));
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


}


