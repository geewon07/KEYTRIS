package com.ssafy.confidentIs.keytris.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;
import java.util.List;
import java.util.Queue;


@Getter
@ToString(callSuper = true)
public class MultiRoom extends BaseRoom {

    private int limit;
    private List<MultiPlayer> playerList;
    private String masterId;
    private int overPlayerCnt;

    @Builder
    public MultiRoom(String roomId, int category, RoomStatus roomStatus, Timestamp startTime, List<String> targetWordList, List<String> subWordList, Queue<String> levelWordList, int limit, List<MultiPlayer> playerList, String masterId, int overPlayerCnt) {
        super(roomId, category, roomStatus, startTime, targetWordList, subWordList, levelWordList);
        this.limit = limit;
        this.playerList = playerList;
        this.masterId = masterId;
        this.overPlayerCnt = overPlayerCnt;
    }

    public void updateMaster(MultiPlayer currentPlayer) {
        this.masterId = currentPlayer.getPlayerId();
    }

    public void updateOverPlayerCnt() {
        this.overPlayerCnt += 1;
    }

    public void updatePlayerList(List<MultiPlayer> playerList) {
        this.playerList = playerList;
    }
}
