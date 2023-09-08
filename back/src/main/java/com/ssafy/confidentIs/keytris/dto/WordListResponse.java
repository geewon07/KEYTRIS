package com.ssafy.confidentIs.keytris.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WordListResponse {

  private String[] subWordList;
  private String[] targetWordList;

  public WordListResponse refill(String[] subList, String[] targetList){

    return WordListResponse.builder()
        .subWordList(subList)
        .targetWordList(targetList)
        .build();
  }

}
