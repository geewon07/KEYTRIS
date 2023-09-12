package com.ssafy.confidentIs.keytris.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Builder
@Getter
public class Room extends BaseRoom {
  private UUID roomId; //TODO: UUID나 ID 생성 추가 필요
  private int category;
  private RoomStatus roomStatus;
  private Timestamp startTime;
  private List<String> targetWordList;
  private List<String> subWordList;
  private Queue<String> levelWordList;
  private List<SinglePlayer> playerList = new ArrayList<>();

  private final String type = "Single";

  public void updatePlayerList(SinglePlayer player) {
    this.playerList.add(player);
  }
  public void updatePlayer(SinglePlayer player){
    this.playerList.set(0,player);
  }

}

  /*
      초기 생성시 필요한 정보 모두 포함,
   */

