package com.ssafy.confidentIs.keytris.model;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BasePlayer {

  protected UUID playerId;
  protected PlayerStatus playerStatus;
  protected long score;
  protected int targetWordIndex;
  protected int subWordIndex;

  public BasePlayer(UUID playerId, PlayerStatus playerStatus, long score) {
    this.playerId = playerId;
    this.playerStatus = playerStatus;
    this.score = score;
  }

  public void updateIndex(int subWordIndex, int targetWordIndex) {
    this.subWordIndex = subWordIndex;
    this.targetWordIndex = targetWordIndex;
  }

  public void updateStatus(PlayerStatus playerStatus) {
    this.playerStatus = playerStatus;
  }

  public void updateScore(long score) {
    this.score = score;
  }


}
