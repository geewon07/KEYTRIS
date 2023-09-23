package com.ssafy.confidentIs.keytris.dto;

import com.ssafy.confidentIs.keytris.model.Room;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WordListResponse {

  private Long newScore;
  private String[][] sortedWordList;
  private List<String> newSubWordList;
  private String targetWord;
  private int targetWordRank; // 정렬된 리스트의 타겟어 인덱스 위치

  public WordListResponse result(String[][] sortedList, List<String> subWordList,
      String targetWord, Long score, int targetWordRank) {
    return WordListResponse.builder()
        .newScore(score)
        .sortedWordList(sortedList)
        .newSubWordList(subWordList)
        .targetWord(targetWord)
        .targetWordRank(targetWordRank)
        .build();
  }
  public WordListResponse start(List<String> subWordList, String targetWord, Long score){
    return WordListResponse.builder()
        .newScore(score)
        .newSubWordList(subWordList)
        .targetWord(targetWord)
        .build();
  }


}
