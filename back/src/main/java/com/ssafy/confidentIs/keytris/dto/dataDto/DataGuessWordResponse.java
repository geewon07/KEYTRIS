package com.ssafy.confidentIs.keytris.dto.dataDto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class DataGuessWordResponse {

    private String success;
    private int status;
    private String errorCode;
    private String message;
    private Data data;

    @Getter
    @Setter
    @ToString
    public static class Data {
        private String[][] calWordList;// 유사도 내림차순 정렬된 단어
    }

}
