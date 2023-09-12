package com.ssafy.confidentIs.keytris.dto;

import com.ssafy.confidentIs.keytris.model.Player;
import com.ssafy.confidentIs.keytris.model.PlayerStatus;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayerResponse {
  // 단어 변동 시 보낼 것

  private String playerId;
  private PlayerStatus playerStatus;
  private String targetWord;
  private List<String> subWordList;


  public PlayerResponse makeSinglePlayer(Player player) {
    return PlayerResponse.builder()
        .playerId(player.getPlayerId())
        .playerStatus(player.getPlayerStatus())
        .targetWord(player.getTargetWord())
        .subWordList(player.getSubWordList())
        .build();
  }
}
