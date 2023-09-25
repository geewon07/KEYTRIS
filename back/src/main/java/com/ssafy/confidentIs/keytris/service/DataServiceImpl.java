package com.ssafy.confidentIs.keytris.service;

import com.ssafy.confidentIs.keytris.dto.dataDto.DataGuessWordRequest;
import com.ssafy.confidentIs.keytris.dto.dataDto.DataGuessWordResponse;
import com.ssafy.confidentIs.keytris.dto.dataDto.DataWordListRequest;
import com.ssafy.confidentIs.keytris.dto.dataDto.DataWordListResponse;
import com.ssafy.confidentIs.keytris.model.WordType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Queue;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataServiceImpl {

    private final RestTemplate restTemplate;

    @Value("${data.url}")
    private String dataServerUrl;


    // 단어 유사도 확인 및 정렬된 데이터 요청
    public DataGuessWordResponse sendGuessWordRequest(DataGuessWordRequest dataGuessWordRequest) {
        String serverBUrl = dataServerUrl + "/guess-words";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<DataGuessWordRequest> entity = new HttpEntity<>(dataGuessWordRequest, headers);

        ResponseEntity<DataGuessWordResponse> response = restTemplate.exchange(serverBUrl, HttpMethod.POST, entity, DataGuessWordResponse.class);

        return response.getBody();
    }

    public DataWordListResponse sendWordListRequest(DataWordListRequest dataWordListRequest) {
        String serverBUrl = dataServerUrl + "/get-words";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<DataWordListRequest> entity = new HttpEntity<>(dataWordListRequest, headers);

        ResponseEntity<DataWordListResponse> response = restTemplate.exchange(serverBUrl, HttpMethod.POST, entity, DataWordListResponse.class);

        return response.getBody();
    }


    // data api 에서 단어 유사도 확인하기
    public DataGuessWordResponse getWordGuessResult(String guessWord, List<String> currentWordList) {
        DataGuessWordRequest dataGuessWordRequest = DataGuessWordRequest.builder()
                .guessWord(guessWord)
                .currentWordList(currentWordList)
                .build();
        return sendGuessWordRequest(dataGuessWordRequest);
    }

    // data api 에서 단어 리스트 불러오기
    public List<String> getDataWordList(WordType wordType, int category, int amount) {
        DataWordListRequest dataWordListRequest = DataWordListRequest.builder()
                .type(wordType)
                .category(category)
                .amount(amount)
                .build();
        DataWordListResponse dataWordListResponse = sendWordListRequest(dataWordListRequest);
        return dataWordListResponse.getData().getWordList();
    }


    // 레벨어를 추가하는 메서드
    public Queue<String> addLevelWords(Queue<String> levelWordList, WordType wordType, int category, int amount) {
        List<String> tempWordList = getDataWordList(wordType, category, amount);
        for(String word : tempWordList) {
            levelWordList.add(word);
        }
        return levelWordList;
    }


}
