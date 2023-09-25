package com.ssafy.confidentIs.keytris.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@ToString(callSuper = true)
public class MultiPlayer extends BasePlayer {

    private String nickname;
    private Boolean isMaster;
    private Timestamp overTime;

    public void updateOverTime() {
        this.overTime = new Timestamp(System.currentTimeMillis());
    }

    @Builder
    public MultiPlayer(String playerId, PlayerStatus playerStatus, Long score, int targetWordIndex, int subWordIndex, String nickname, Boolean isMaster, Timestamp overTime) {
        super(playerId, playerStatus, score, targetWordIndex, subWordIndex);
        this.nickname = nickname;
        this.isMaster = isMaster;
        this.overTime = overTime;
    }

}
