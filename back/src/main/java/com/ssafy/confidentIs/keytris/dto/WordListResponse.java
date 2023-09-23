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
  private String[][] newSubWordList;
  private String[][] newTargetWord;
  private int[] sortedIndex; // 정렬 후 기존 단어가 이동되는 지점을 담는 배열
  private int targetWordRank; // sortedWordlist 내 targetWord의 위치

}
