package com.ssafy.confidentIs.keytris.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WordListResponse {

  private List<String> subWordList;
  private List<String> targetWordList;

  public WordListResponse refill(List<String> subList, List<String> targetList){

    return WordListResponse.builder()
        .subWordList(subList)
        .targetWordList(targetList)
        .build();
  }

}
