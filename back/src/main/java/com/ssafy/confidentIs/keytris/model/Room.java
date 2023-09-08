package com.ssafy.confidentIs.keytris.model;

import java.sql.Timestamp;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Room {

  private String roomId = "testingRoom"; //TODO: UUID나 ID 생성 추가 필요
  private RoomStatus roomStatus;
  private String master;
  private int limit;
  private Player[] playerList = new Player[limit];
  private Timestamp startTime;
  private List<String> targetWordList;
  private List<String> subWordList;
  private List<String> levelWordList;
  private String type;
  private int category;

  @Builder
  public Room(RoomStatus status, String master, int limit, Player[] playerList,
      Timestamp startTime,
      List<String> levelWordList) {
    this.roomStatus = status;
    this.master = master;
    this.limit = limit;
    this.playerList = playerList;
    this.startTime = startTime;
    this.levelWordList = levelWordList;
  }

  /*
      초기 생성시 필요한 정보 모두 포함,
   */
  @Builder
  public Room(String roomId, RoomStatus roomStatus, String master, int limit, Player[] playerList,
      List<String> targetWordList, List<String> subWordList, List<String> levelWordList, String type,
      int category) {
    this.roomId = roomId;
    this.roomStatus = roomStatus;
    this.master = master;
    this.limit = limit;
    this.playerList = playerList;
    this.targetWordList = targetWordList;
    this.subWordList = subWordList;
    this.levelWordList = levelWordList;
    this.type = type;
    this.category = category;
  }
}
