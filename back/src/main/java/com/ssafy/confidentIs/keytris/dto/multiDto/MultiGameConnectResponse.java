package com.ssafy.confidentIs.keytris.dto.multiDto;

import com.ssafy.confidentIs.keytris.model.RoomStatus;
import com.ssafy.confidentIs.keytris.model.multiModel.MultiPlayer;
import com.ssafy.confidentIs.keytris.model.multiModel.MultiRoom;
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
    private List<MultiPlayer> playerList;
    private String currentPlayerId;

    public MultiGameConnectResponse(MultiRoom room, String currentPlayerId) {
        this.roomId = room.getRoomId();
        this.roomStatus = room.getRoomStatus();
        this.playerList = room.getPlayerList();
        this.currentPlayerId = currentPlayerId;
    }
}
