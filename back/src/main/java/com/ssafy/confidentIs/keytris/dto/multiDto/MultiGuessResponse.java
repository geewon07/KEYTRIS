package com.ssafy.confidentIs.keytris.dto.multiDto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
public class MultiGuessResponse {

    private String playerId;
    private String[][] sortedWordList;
    private Long newScore;
    private String newTargetWord;
    private List<String> newSubWordList;

}
