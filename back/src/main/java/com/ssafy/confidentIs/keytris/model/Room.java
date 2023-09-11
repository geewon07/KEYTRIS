package com.ssafy.confidentIs.keytris.model;

import java.sql.Timestamp;
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
  private String[] targetWordList;
  private String[] subWordList;
  private String[] levelWordList;
  private String type;
  private String category;

  @Builder
  public Room(RoomStatus status, String master, int limit, Player[] playerList,
      Timestamp startTime,
      String[] levelWordList) {
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
      String[] targetWordList, String[] subWordList, String[] levelWordList, String type,
      String category) {
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
