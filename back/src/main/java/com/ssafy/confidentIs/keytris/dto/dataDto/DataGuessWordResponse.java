package com.ssafy.confidentIs.keytris.dto.dataDto;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class DataGuessWordResponse {

    private String[][] sortedWordList; // 유사도 내림차순 정렬된 단어

}
