package com.ssafy.confidentIs.keytris.model;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;

import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@SuperBuilder
public class BaseRoom {

  protected String roomId;
  protected int category;
  protected RoomStatus roomStatus;
  protected Timestamp startTime;
  protected List<String> targetWordList;
  protected List<String> subWordList;
  protected Queue<String> levelWordList;

  public BaseRoom(String roomId, int category, RoomStatus roomStatus, List<String> targetWordList,
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

  public void updateStartTime(Timestamp startTime) {
    this.startTime = startTime;
  }

  public String getLevelWord() {
    return this.levelWordList.poll();
  }
}
