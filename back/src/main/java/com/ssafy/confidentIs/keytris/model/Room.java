package com.ssafy.confidentIs.keytris.model;

import java.util.List;
import lombok.Getter;
import lombok.experimental.SuperBuilder;


@SuperBuilder
@Getter
public class Room extends BaseRoom {

  private List<SinglePlayer> playerList;
  private final String type = "Single";

  public void updatePlayerList(SinglePlayer player) {
    this.playerList.add(player);
  }//제거?

  public void updatePlayer(SinglePlayer player) {
    this.playerList.set(0, player);
  }

  public void updateSubWordList(List<String> refill){
    this.subWordList.addAll(refill);
  }
  public void updateTargetWordList(List<String> refill){
    this.targetWordList.addAll(refill);
  }
  public void updateLevelWordList(List<String> refill){
    this.levelWordList.addAll(refill);
  }

}
