package com.ssafy.confidentIs.keytris.dto;

import java.util.List;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class GuessRequest {
  private String roomId;
  private List<String> currentWordList;
  private String guessWord;
}
