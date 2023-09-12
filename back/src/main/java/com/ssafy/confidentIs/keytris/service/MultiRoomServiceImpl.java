package com.ssafy.confidentIs.keytris.service;


import com.ssafy.confidentIs.keytris.dto.dataDto.DataGuessWordRequest;
import com.ssafy.confidentIs.keytris.dto.dataDto.DataGuessWordResponse;
import com.ssafy.confidentIs.keytris.dto.dataDto.DataWordListRequest;
import com.ssafy.confidentIs.keytris.dto.dataDto.DataWordListResponse;
import com.ssafy.confidentIs.keytris.dto.multiDto.MultiGuessRequest;
import com.ssafy.confidentIs.keytris.dto.multiDto.MultiGuessResponse;
import com.ssafy.confidentIs.keytris.model.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MultiRoomServiceImpl {

    private final DataServiceImpl dataServiceImpl;

    public MultiGuessResponse sortByProximity(MultiGuessRequest request) {

        String roomId = request.getRoomId();
        String playerId = request.getPlayerId();
        List<String> currentWordList = request.getCurrentWordList();
        String guessWord = request.getGuessWord();

        //TODO 예외처리 존재하지 않는 roomId
        //TODO 예외처리 존재하지 않는 playerId, roomId에 속하지 않는 playerId

        //유사도 요청
        DataGuessWordRequest dataGuessWordRequest = DataGuessWordRequest.builder()
                .guessWord(request.getGuessWord())
                .currentWordList(request.getCurrentWordList())
                .build();
        DataGuessWordResponse dataGuessWordResponse = dataServiceImpl.sendGuessWordRequest(dataGuessWordRequest);
        String[][] sortedWordList = dataGuessWordResponse.getSortedWordList();
        for(String[] arr : sortedWordList) {
            System.out.println(arr[0] + arr[1]);
        }
        //TODO 점수 계산, 삭제 후 전달해야 할 데이터 정리

        //단어가 부족한 경우, 추가 단어 요청
        DataWordListRequest dataWordListRequest = DataWordListRequest.builder()
                .type("target")
                .category(Category.POLITICS)
                .amount(30)
                .build();
        DataWordListResponse dataWordListResponse = dataServiceImpl.sendWordListRequest(dataWordListRequest);
        List<String> wordList = dataWordListResponse.getWordList();
        System.out.println("target " + wordList.toString());
        System.out.println("------------------");

        DataWordListRequest dataWordListRequest2 = DataWordListRequest.builder()
                .type("sub")
                .category(Category.ECONOMY)
                .amount(20)
                .build();
        DataWordListResponse dataWordListResponse2 = dataServiceImpl.sendWordListRequest(dataWordListRequest2);
        List<String> wordList2 = dataWordListResponse.getWordList();
        System.out.println("sub " + wordList2.toString());
        System.out.println("------------------");

        DataWordListRequest dataWordListRequest3 = DataWordListRequest.builder()
                .type("level")
                .category(Category.SOCIETY)
                .amount(15)
                .build();
        DataWordListResponse dataWordListResponse3 = dataServiceImpl.sendWordListRequest(dataWordListRequest3);
        List<String> wordList3 = dataWordListResponse.getWordList();
        System.out.println("level " + wordList3.toString());
        System.out.println("------------------");


        List<String> newSubWordList = new ArrayList<>();

        MultiGuessResponse multiGuessResponse = MultiGuessResponse.builder()
                .sortedWordList(sortedWordList)
                .newScore(100)
                .newTargetWord("새 타겟 단어")
                .newSubWordList(newSubWordList)
                .build();

        return multiGuessResponse;
    }
}
