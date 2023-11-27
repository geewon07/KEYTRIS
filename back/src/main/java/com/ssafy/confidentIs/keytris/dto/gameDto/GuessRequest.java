package com.ssafy.confidentIs.keytris.dto.gameDto;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class GuessRequest {
    private String roomId;
    private String guessWord;
    private String[][] targetWord;
    private String[][] currentWordList;
}
