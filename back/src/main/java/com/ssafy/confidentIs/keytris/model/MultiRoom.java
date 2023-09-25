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
    private MultiPlayer master;
    private int overPlayerCnt;

    @Builder
    public MultiRoom(String roomId, int category, RoomStatus roomStatus, Timestamp startTime, List<String> targetWordList, List<String> subWordList, Queue<String> levelWordList, int limit, List<MultiPlayer> playerList, MultiPlayer master, int overPlayerCnt) {
        super(roomId, category, roomStatus, startTime, targetWordList, subWordList, levelWordList);
        this.limit = limit;
        this.playerList = playerList;
        this.master = master;
        this.overPlayerCnt = overPlayerCnt;
    }

    public void updateMaster(MultiPlayer currentPlayer) {
        this.master = currentPlayer;
    }

    public void updateOverPlayerCnt() {
        this.overPlayerCnt += 1;
    }

}
