package com.ssafy.confidentIs.keytris.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

// @SuperBuilder 상속관계에서 부모 자식 모두 붙여줘야 함
@Getter
@ToString(callSuper=true)
public class SinglePlayer extends BasePlayer {

    @Builder
    public SinglePlayer (String playerId,PlayerStatus playerStatus,Long score,int targetWordIndex,int subWordIndex){
        super(playerId, playerStatus,score,targetWordIndex,subWordIndex);
    }
}
