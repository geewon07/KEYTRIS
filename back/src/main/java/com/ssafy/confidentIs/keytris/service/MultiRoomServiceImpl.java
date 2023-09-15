package com.ssafy.confidentIs.keytris.service;


import com.ssafy.confidentIs.keytris.dto.dataDto.DataGuessWordRequest;
import com.ssafy.confidentIs.keytris.dto.dataDto.DataGuessWordResponse;
import com.ssafy.confidentIs.keytris.dto.dataDto.DataWordListRequest;
import com.ssafy.confidentIs.keytris.dto.dataDto.DataWordListResponse;
import com.ssafy.confidentIs.keytris.dto.multiDto.MultiGuessRequest;
import com.ssafy.confidentIs.keytris.dto.multiDto.MultiGuessResponse;
import com.ssafy.confidentIs.keytris.model.Category;
import com.ssafy.confidentIs.keytris.model.WordType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MultiRoomServiceImpl {

    private final DataServiceImpl dataServiceImpl;

    private static final int AMOUNT = 10;

    public MultiGuessResponse sortByProximity(MultiGuessRequest request) {

        String roomId = request.getRoomId();
        String playerId = request.getPlayerId();
        List<String> currentWordList = request.getCurrentWordList();
        String guessWord = request.getGuessWord();
        String targetWord = request.getTargetWord();

        //TODO 예외처리 존재하지 않는 roomId
        //TODO 예외처리 존재하지 않는 playerId, roomId에 속하지 않는 playerId


        //유사도 요청
        String[][] sortedWordList = getSortedWordList(guessWord, currentWordList);
        log.info("sortedWordList {}", Arrays.deepToString(sortedWordList));


        //TODO 점수 계산, 삭제 후 전달해야 할 데이터 정리

        

        //단어가 부족한 경우, 추가 단어 요청
        WordType wordType = WordType.TARGET;
        Category category = Category.POLITICS;
        
        if(true) {  // TODO 어떤 단어가 부족한지 확인하는 조건으로 변경하기
            List<String> wordList = getDataWordList(wordType, category, AMOUNT);
            log.info("type {} {} ", wordType, wordList.toString());
        }


        List<String> newSubWordList = new ArrayList<>();

        MultiGuessResponse multiGuessResponse = MultiGuessResponse.builder()
                .sortedWordList(sortedWordList)
                .newScore(100)
                .newTargetWord("새 타겟 단어")
                .newSubWordList(newSubWordList)
                .build();

        return multiGuessResponse;
    }

    private String[][] getSortedWordList(String guessWord, List<String> currentWordList) {
        DataGuessWordRequest dataGuessWordRequest = DataGuessWordRequest.builder()
                .guessWord(guessWord)
                .currentWordList(currentWordList)
                .build();
        DataGuessWordResponse dataGuessWordResponse = dataServiceImpl.sendGuessWordRequest(dataGuessWordRequest);
        return dataGuessWordResponse.getData().getCalWordList();
    }


    // data api 에서 단어 리스트 불러오기
    private List<String> getDataWordList(WordType wordType, Category category, int amount) {
        DataWordListRequest dataWordListRequest = DataWordListRequest.builder()
                .type(wordType)
                .category(category.getCode())
                .amount(amount)
                .build();
        DataWordListResponse dataWordListResponse = dataServiceImpl.sendWordListRequest(dataWordListRequest);
        return dataWordListResponse.getData().getWordList();
    }


}
