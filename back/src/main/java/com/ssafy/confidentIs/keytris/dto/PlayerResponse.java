package com.ssafy.confidentIs.keytris.dto;

import com.ssafy.confidentIs.keytris.model.Player;
import com.ssafy.confidentIs.keytris.model.PlayerStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayerResponse {

  private String playerId;
  private PlayerStatus playerStatus;
  private String[] targetWordList;
  private String[] subWordList;


  public PlayerResponse makeSinglePlayer(Player player) {
    return PlayerResponse.builder()
        .playerId(player.getPlayerId())
        .playerStatus(player.getPlayerStatus())
        .targetWordList(player.getTargetWordList())
        .subWordList(player.getSubWordList())
        .build();
  }
}
