package com.ssafy.confidentIs.keytris.controller;

import com.ssafy.confidentIs.keytris.common.dto.response.ResponseDto;
import com.ssafy.confidentIs.keytris.dto.CreateRequest;
import com.ssafy.confidentIs.keytris.dto.GuessRequest;
import com.ssafy.confidentIs.keytris.dto.OverRequest;
import com.ssafy.confidentIs.keytris.dto.OverResponse;
import com.ssafy.confidentIs.keytris.dto.StartRequest;
import com.ssafy.confidentIs.keytris.service.PlayerService;
import com.ssafy.confidentIs.keytris.service.RoomService;
import com.ssafy.confidentIs.keytris.service.WordService;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@ToString
@Slf4j
@RestController
@RequiredArgsConstructor
//@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/games")
public class GameController {

  private final PlayerService playerService;
  private final RoomService roomService;
  private final WordService wordService;

  @PostMapping
  public ResponseEntity<?> create(@RequestBody CreateRequest request) {
    log.info("create singlePlayer & room, category: {}", request.getCategory());
    ResponseDto responseDto = new ResponseDto("success", "방만들기 성공",
        Collections.singletonMap("statusResponse",
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
//    log.info("null TS, request:{}",request);
    ResponseDto responseDto = new ResponseDto("success", "유사도 정렬",
        Collections.singletonMap("sortedWordListResponse",
            roomService.enterWord(request)));
    return new ResponseEntity<>(responseDto, HttpStatus.OK);
  }

  //TODO: 소켓으로 알아서 모자라면 보내주는 것으로 바꿔 만들어야 함, 어떨때 체크해야 할까?

  //@PostMapping("/more-word")
  //  public ResponseEntity<?> refill() {
  //    //@RequestBody String type, String category
  //    ResponseDto responseDto;
  //    StatusResponse statusResponse;
  //    WordListResponse wordListResponse = new WordListResponse();
  ////    wordListResponse.refill();
  //    responseDto = new ResponseDto("success", "단어 목록 보강",
  //        Collections.singletonMap("wordListResponse",
  //            wordListResponse.refill(wordService.getWords("sub", 0, REFILL_SUB_AMOUNT),
  //                wordService.getWords("target", 0, REFILL_TARGET_AMOUNT))));
  //    return new ResponseEntity<>(responseDto, HttpStatus.OK);
  //  }

  @PostMapping("/over")
  public ResponseEntity<?> gameOver(@RequestBody OverRequest request) {
    //change stats to finished, over
    ResponseDto responseDto;
    responseDto = new ResponseDto("success", "게임 종료, 기사 목록",
          Collections.singletonMap("OverResponse",
        roomService.gameOver(request)));
//          Collections.singletonMap("articleList",
//              new String[]{"https://developers.naver.com/docs/serviceapi/search/news/news.md"})
    //regardless give some articles -> naver api search news keyword ( lastWord : latest )
    return new ResponseEntity<>(responseDto, HttpStatus.OK);
  }

  @PostMapping("/ranking")
  public ResponseEntity<?> newRecord(@RequestBody String nickname, @RequestBody String roomId) {
    log.info("new record, score:{}, name:{}", nickname);//랭킹 redis 저장 대체
    ResponseDto responseDto = new ResponseDto("success", "신기록 등록", Collections.singletonMap("NewRanking",
        roomService.addHighscore(nickname,roomId)));
    return new ResponseEntity<>(responseDto, HttpStatus.OK);
  }


}


