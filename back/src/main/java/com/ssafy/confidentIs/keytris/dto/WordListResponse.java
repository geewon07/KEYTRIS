package com.ssafy.confidentIs.keytris.dto;

import com.ssafy.confidentIs.keytris.model.Player;
import com.ssafy.confidentIs.keytris.model.Room;
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

  private int score;
  private List<String> sortedWordList;
  private List<String> subWordList;
  private String targetWord;

  //TODO: 이렇게 주면 새로운 값을 받는게 되는데 이거 버퍼 FE에서 생각해야함
  public WordListResponse result(List<String> sortedList, Room room, int score) {
    Player player = room.getPlayerList()[0];
    return WordListResponse.builder()
        .sortedWordList(sortedList)
        .subWordList(player.getSubWordList())
        .targetWord(player.getTargetWord())
        .score(score)
        .build();
  }

}
