package com.ssafy.confidentIs.keytris.dto.multiDto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.TreeMap;

@Getter
@Builder
@ToString
public class MultiGuessResponse {

    private String[][] sortedWordList;
    private double newScore;
    private String newTargetWord;
    private List<String> newSubWordList;

}
