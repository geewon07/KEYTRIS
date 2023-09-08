package com.ssafy.confidentIs.keytris.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class GuessRequest {
  private String[] currenWordList;
  private String guessWord;
}
