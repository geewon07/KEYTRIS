package com.ssafy.confidentIs.keytris.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.List;
import java.util.Queue;

@Getter
@ToString(callSuper = true)
public class Room extends BaseRoom {

    private List<SinglePlayer> playerList;
    private final String type = "Single";

    @Builder
    public Room(String roomId, int category, RoomStatus roomStatus, Timestamp startTime, List<String> targetWordList, List<String> subWordList, Queue<String> levelWordList, List<SinglePlayer> playerList) {
        super(roomId, category, roomStatus, startTime, targetWordList, subWordList, levelWordList);
        this.playerList = playerList;
    }

    public void updatePlayerList(SinglePlayer player) {
        this.playerList.add(player);
    }//제거?

    public void updatePlayer(SinglePlayer player) {
        this.playerList.set(0, player);
    }

    public void updateSubWordList(List<String> refill) {
        this.subWordList.addAll(refill);
    }

    public void updateTargetWordList(List<String> refill) {
        this.targetWordList.addAll(refill);
    }

    public void updateLevelWordList(List<String> refill) {
        this.levelWordList.addAll(refill);
    }

}
