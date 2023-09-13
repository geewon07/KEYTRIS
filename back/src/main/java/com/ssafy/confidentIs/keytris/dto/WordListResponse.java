package com.ssafy.confidentIs.keytris.dto;

import com.ssafy.confidentIs.keytris.model.SinglePlayer;
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

  private Long score;
  private String[][] sortedWordList;
  private List<String> subWordList;
  private String targetWord;

  //TODO: 이렇게 주면 새로운 값을 받는게 되는데 이거 버퍼 FE에서 생각해야함
  public WordListResponse result(String[][] sortedList, List<String> subWordList,
      String targetWord, Room room, Long score) {
    return WordListResponse.builder()
        .sortedWordList(sortedList)
        .subWordList(subWordList)
        .targetWord(targetWord)
        .score(score)
        .build();
  }
  public WordListResponse start(List<String> subWordList, String targetWord, Long score){
    return WordListResponse.builder()
        .score(score)
        .subWordList(subWordList)
        .targetWord(targetWord)
        .build();
  }


}
