package com.ssafy.confidentIs.keytris.model;

import java.util.UUID;

import lombok.*;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class BasePlayer {

  public String playerId;
  public PlayerStatus playerStatus;
  public Long score;
  public int targetWordIndex;
  public int subWordIndex;


   public BasePlayer(String playerId, PlayerStatus playerStatus, Long score) {
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

  public void updateScore(Long score) {
    this.score = score;
  }


}
