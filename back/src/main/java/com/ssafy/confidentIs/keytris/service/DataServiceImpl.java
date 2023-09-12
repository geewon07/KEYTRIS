package com.ssafy.confidentIs.keytris.service;

import com.ssafy.confidentIs.keytris.dto.dataDto.DataGuessWordRequest;
import com.ssafy.confidentIs.keytris.dto.dataDto.DataGuessWordResponse;
import com.ssafy.confidentIs.keytris.dto.dataDto.DataWordListRequest;
import com.ssafy.confidentIs.keytris.dto.dataDto.DataWordListResponse;
import com.ssafy.confidentIs.keytris.dto.multiDto.MultiGuessRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

    // 추가 단어 요청


}
