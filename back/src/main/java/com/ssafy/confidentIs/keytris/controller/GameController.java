package com.ssafy.confidentIs.keytris.controller;

import com.ssafy.confidentIs.keytris.common.dto.response.ResponseDto;
import com.ssafy.confidentIs.keytris.dto.CreateRequest;
import com.ssafy.confidentIs.keytris.dto.GuessRequest;
import com.ssafy.confidentIs.keytris.dto.StatusResponse;
import com.ssafy.confidentIs.keytris.dto.OverRequest;
import com.ssafy.confidentIs.keytris.dto.StartRequest;
import com.ssafy.confidentIs.keytris.dto.WordListResponse;
import com.ssafy.confidentIs.keytris.model.Player;
import com.ssafy.confidentIs.keytris.model.Room;
import com.ssafy.confidentIs.keytris.service.PlayerService;
import com.ssafy.confidentIs.keytris.service.RoomService;
import com.ssafy.confidentIs.keytris.service.WordService;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
//@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/games")
public class GameController {

  private final PlayerService playerService;
  private final RoomService roomService;
  private final WordService wordService;

  private final int REFILL_SUB_AMOUNT = 300;
  private final int REFILL_TARGET_AMOUNT = 30;


  @PostMapping
  public ResponseEntity<?> create(@RequestBody CreateRequest request) {
    log.info("not used in testing type:{} , category: {}", request.getType(),
        request.getCategory());
    log.info("called create");
//    Date currentDate = new Date();// Convert the Date object to a Timestamp
//    Timestamp timestamp = new Timestamp(currentDate.getTime());
    Player single = playerService.initialPlayer();

    //단어 가져오기 요청
    Room room = roomService.createRoom("type", 0, single);

    StatusResponse makeRoomResponse = new StatusResponse();
    ResponseDto responseDto = new ResponseDto("success", "방만들기 성공",
        Collections.singletonMap("newRoomResponseDto", makeRoomResponse.idStatus(single, room)));

    return new ResponseEntity<>(responseDto, HttpStatus.OK);
  }

  @PostMapping("/start")
  public ResponseEntity<?> start(@RequestBody StartRequest request) {
    Date currentDate = new Date();// Convert the Date object to a Timestamp
    Timestamp timestamp = new Timestamp(currentDate.getTime());
    ResponseDto responseDto;
    if (request.getPlayerId() == null || request.getRoomId() == null) {
      responseDto = new ResponseDto("fail", "게임 시작 실패, 신원미상");
      //-> ID 배정
    } else {
      if (roomService.checkReady(request.getRoomStatus(), request.getPlayerStatus())) {

        responseDto = new ResponseDto("success", "게임 시작",
            Collections.singletonMap("startTime", timestamp));
      } else {
        responseDto = new ResponseDto("fail", "게임 시작 실패, 준비안됨");
      }
    }
    return new ResponseEntity<>(responseDto, HttpStatus.OK);
  }

  @PostMapping("/guess-word")
  public ResponseEntity<?> enter(@RequestBody GuessRequest request) {
    ResponseDto responseDto;
    if (wordService.checkDataBase(request.getGuessWord())) {
      List<String> sortedWordList = wordService.sortByProximity(request.getCurrenWordList());
      responseDto = new ResponseDto("success", "유사도 정렬",
          Collections.singletonMap("sortedWordList", sortedWordList));
    } else {
      responseDto = new ResponseDto("error", "입력 단어 부재");
    }
    return new ResponseEntity<>(responseDto, HttpStatus.OK);
  }

  @PostMapping("/more-word")
  public ResponseEntity<?> refill() {
    //@RequestBody String type, String category
    ResponseDto responseDto;
    WordListResponse wordListResponse = new WordListResponse();
//    wordListResponse.refill();
    responseDto = new ResponseDto("success", "단어 목록 보강",
        Collections.singletonMap("wordListResponse",
            wordListResponse.refill(wordService.getWords("sub", 0, REFILL_SUB_AMOUNT),
                wordService.getWords("target", 0, REFILL_TARGET_AMOUNT))));
    return new ResponseEntity<>(responseDto, HttpStatus.OK);
  }

  @PostMapping("/over")
  public ResponseEntity<?> gameOver(@RequestBody OverRequest request) {
    //change stats to finished, over
    ResponseDto responseDto;
    if (request.getScore() > 2000) {
      //highscore, 랭킹 조회하기
      responseDto = new ResponseDto("success", "신기록 갱신, 입력창 보여주기",
          Collections.singletonMap("isRecord", true));
    } else {
      responseDto = new ResponseDto("success", "게임 종료, 기사 목록",
          Collections.singletonMap("articleList",
              new String[]{"https://developers.naver.com/docs/serviceapi/search/news/news.md"}));
      //regardless give some articles -> naver api search news keyword ( lastWord : latest )
    }
    return new ResponseEntity<>(responseDto, HttpStatus.OK);
  }

  @PostMapping("/ranking")
  public ResponseEntity<?> newRecord(@RequestBody String nickname, @RequestBody int score) {
    log.info("new record, score:{}, name:{}", nickname, score);//랭킹 redis 저장 대체
    ResponseDto responseDto = new ResponseDto("success", "신기록 등록");
    return new ResponseEntity<>(responseDto, HttpStatus.OK);
  }


}


