package com.ssafy.confidentIs.keytris.model;

import java.sql.Timestamp;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BaseRoom {
  protected UUID roomId; //TODO: UUID나 ID 생성 추가 필요
  protected int category;
  protected RoomStatus roomStatus;
  protected Timestamp startTime;
  protected List<String> targetWordList;
  protected List<String> subWordList;
  protected Queue<String> levelWordList;

  public BaseRoom(UUID roomId, int category, RoomStatus roomStatus, List<String> targetWordList,
      List<String> subWordList, Queue<String> levelWordList) {
    this.roomId = roomId;
    this.category = category;
    this.roomStatus = roomStatus;
    this.targetWordList = targetWordList;
    this.subWordList = subWordList;
    this.levelWordList = levelWordList;
  }


  public void updateStatus(RoomStatus roomStatus) {
    this.roomStatus = roomStatus;
  }

  //TODO: 싱글플레이어 라 유저목록이 없음, 합친다면 멀티플레이어 방식으로, 그냥이라면 이대로

  public void updateStartTime(Timestamp startTime) {
    this.startTime = startTime;
  }

}
