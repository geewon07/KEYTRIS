package com.ssafy.confidentIs.keytris.dto;

import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class GuessRequest {
  private String roomId;
  private String[][] currentWordList;
  private String guessWord;
  private String targetWord;
}
