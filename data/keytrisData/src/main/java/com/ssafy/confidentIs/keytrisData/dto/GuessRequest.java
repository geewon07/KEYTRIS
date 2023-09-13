package com.ssafy.confidentIs.keytrisData.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class GuessRequest {
  private List<String> currentWordList;
  private String guessWord;
}
