package com.ssafy.confidentIs.keytris.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class GuessRequest {
  private List<String> currenWordList;
  private String guessWord;
}
