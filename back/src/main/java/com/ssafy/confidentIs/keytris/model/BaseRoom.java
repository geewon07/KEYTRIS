package com.ssafy.confidentIs.keytris.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.util.List;
import java.util.Queue;

@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
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
        if (!this.levelWordList.isEmpty()) {
            return this.levelWordList.poll();
        } else {
            return "단어";
        }
    }
}
