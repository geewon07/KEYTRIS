package com.ssafy.confidentIs.keytris.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class SinglePlayer extends BasePlayer {

    @Builder
    public SinglePlayer(String playerId, PlayerStatus playerStatus, Long score, int targetWordIndex, int subWordIndex) {
        super(playerId, playerStatus, score, targetWordIndex, subWordIndex);
    }
}
