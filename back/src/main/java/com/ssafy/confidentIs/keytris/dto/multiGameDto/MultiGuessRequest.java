package com.ssafy.confidentIs.keytris.dto.multiGameDto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MultiGuessRequest {

    private String playerId;
    private String guessWord;
    private String[][] targetWord;
    private String[][] currentWordList;

}
