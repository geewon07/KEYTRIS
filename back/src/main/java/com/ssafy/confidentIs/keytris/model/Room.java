package com.ssafy.confidentIs.keytris.model;

import java.sql.Timestamp;
import java.util.List;
import java.util.Queue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
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
  private Queue<String> levelWordList;
  private String type;
  private int category;

  @Builder
  public Room(RoomStatus status, String master, int limit, Player[] playerList,
      Timestamp startTime,
      Queue<String> levelWordList) {
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
      List<String> targetWordList, List<String> subWordList, Queue<String> levelWordList,
      String type,
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

  public void updateStatus(RoomStatus roomStatus) {
    this.roomStatus = roomStatus;
  }

  //TODO: 싱글플레이어 라 유저목록이 없음, 합친다면 멀티플레이어 방식으로, 그냥이라면 이대로
  public void updatePlayer(Player player) {
    this.playerList[0] = player;
  }

  public void updateStartTime(Timestamp startTime) {
    this.startTime = startTime;
  }


}
