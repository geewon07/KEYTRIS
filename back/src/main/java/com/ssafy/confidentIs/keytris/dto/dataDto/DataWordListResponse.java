package com.ssafy.confidentIs.keytris.dto.dataDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class DataWordListResponse {

    private String success;
    private int status;
    private String errorCode;
    private String message;
    private Data data;

    @Getter
    @Setter
    @ToString
    public static class Data {
        private List<String> wordList;
    }

}
