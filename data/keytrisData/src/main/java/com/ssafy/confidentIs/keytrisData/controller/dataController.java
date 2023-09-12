package com.ssafy.confidentIs.keytrisData.controller;

import com.ssafy.confidentIs.keytrisData.common.dto.response.ResponseDto;
import com.ssafy.confidentIs.keytrisData.dto.GuessRequest;
import com.ssafy.confidentIs.keytrisData.dto.RefillRequest;
import com.ssafy.confidentIs.keytrisData.service.DataService;
import java.util.Collections;
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
@RequestMapping("/api/data")
public class dataController {

  private final DataService dataService;

  @PostMapping
  public ResponseEntity<?> guess(@RequestBody GuessRequest guessRequest) {

    // 단어 유사도 검사 요청
    //Request Body
    //
    //guessWord : String (사용자가 입력한 단어)
    //currentWordList : List<String> (유사도 검사해야 하는 단어)
    //유사도 확인할 수 없는 경우 뭔가 다르게 RETURN 부탁
    System.out.println(guessRequest.getCurrentWordList().toString());
    ResponseDto responseDto;
    // 1. 더미 데이터로 api 만들기
    // 2. 단어 입력받으면
    // 3. list에 있는 값들과 유사도 비교하여서 정렬해서 주기 ( 유사도 값과 같이 주기 : hashMap 형태 )
    if(dataService.calculateSimilarities(guessRequest.getCurrentWordList(), guessRequest.getGuessWord()) == null) {
      responseDto = new ResponseDto("fail","일치하는 단어를 찾을 수 없습니다.");
    } else {
      responseDto = new ResponseDto("success", "단어 유사도 정렬 성공",
          Collections.singletonMap("calWordList", dataService.calculateSimilarities(guessRequest.getCurrentWordList(), guessRequest.getGuessWord())));
    }
    return new ResponseEntity<>(responseDto, HttpStatus.OK);
  }

  @PostMapping("/start")
  public ResponseEntity<?> refill(@RequestBody RefillRequest refillRequest) {
    //모두 하나의 API로 처리하자
    //
    //타겟어 가져오기 : 기사에서 추출한 중요 키워드
    //서브어 가져오기 : 사전에서 가져와도 됨
    //레벨어(난이도 조절용 시간에 따라서 보내는 언어) 가져오기 : 사전에서 가져와도 됨
    //Request Body
    //
    //type : String (Enum으로 만들어도 됨 : TARGET, SUB, LEVEL)
    //category : int
    //amount : int
    // TARGET(타켓어), SUB(서브어), LEVEL(레벨에 따라 주기)
    ResponseDto responseDto;

    if( refillRequest.getType().equals("TARGET")) {
      responseDto = new ResponseDto("success", "타켓어 단어 리필 성공",
          Collections.singletonMap("wordList", dataService.giveTargetWords(refillRequest.getCategory(), refillRequest.getAmount())));
    } else {
      responseDto = new ResponseDto("success", "단어 리필 성공",
          Collections.singletonMap("wordList", dataService.giveSubWords(refillRequest.getAmount())));
    }
    return new ResponseEntity<>(responseDto,HttpStatus.OK);
  }
}


