package com.ssafy.confidentIs.keytris.dto.multiGameDto;

import com.ssafy.confidentIs.keytris.model.RoomStatus;
import com.ssafy.confidentIs.keytris.model.MultiPlayer;
import com.ssafy.confidentIs.keytris.model.MultiRoom;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MultiGameConnectResponse {

    private String roomId;
    private RoomStatus roomStatus;
    private int category;
    private String masterId;
    private List<MultiPlayer> playerList;
    private String currentPlayerId;


    public MultiGameConnectResponse(MultiRoom room, String currentPlayerId) {
        this.roomId = room.getRoomId();
        this.roomStatus = room.getRoomStatus();
        this.category = room.getCategory();
        this.masterId = room.getMasterId();
        this.playerList = room.getPlayerList();
        this.currentPlayerId = currentPlayerId;
    }

}
