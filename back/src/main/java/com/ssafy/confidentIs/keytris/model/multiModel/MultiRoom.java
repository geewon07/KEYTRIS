package com.ssafy.confidentIs.keytris.model.multiModel;

import com.ssafy.confidentIs.keytris.model.BaseRoom;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;


@SuperBuilder
@Getter
@ToString(callSuper=true)
public class MultiRoom extends BaseRoom {

    private int limit;
    private List<MultiPlayer> playerList;
    private MultiPlayer master;
    private int overPlayerCnt;

    public void updateMaster(MultiPlayer currentPlayer) {
        this.master = currentPlayer;
    }

    public void updateOverPlayerCnt() {
        this.overPlayerCnt += 1;
    }

}
