package com.ssafy.confidentIs.keytris.model.multiModel;

import com.ssafy.confidentIs.keytris.model.BasePlayer;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;
import java.util.List;

@SuperBuilder
@Getter
@ToString(callSuper=true)
public class MultiPlayer extends BasePlayer {

    private String nickname;
    private Boolean isMaster;
    private Timestamp overTime;
    private List<String> displayWordList;

}
