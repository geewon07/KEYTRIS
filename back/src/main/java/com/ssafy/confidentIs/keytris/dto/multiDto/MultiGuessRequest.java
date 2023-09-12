package com.ssafy.confidentIs.keytris.dto.multiDto;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class MultiGuessRequest {

    private String roomId;
    private String playerId;
    private List<String> currentWordList;
    private String guessWord;
    private String targetWord;

}
