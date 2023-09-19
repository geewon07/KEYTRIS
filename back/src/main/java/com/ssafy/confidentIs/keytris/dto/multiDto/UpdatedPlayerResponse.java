package com.ssafy.confidentIs.keytris.dto.multiDto;

import com.ssafy.confidentIs.keytris.model.PlayerStatus;
import com.ssafy.confidentIs.keytris.model.RoomStatus;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UpdatedPlayerResponse {

    private String playerId;
    private PlayerStatus playerStatus;
    private RoomStatus roomStatus;

    public UpdatedPlayerResponse(String playerId, PlayerStatus playerStatus) {
        this.playerId = playerId;
        this.playerStatus = playerStatus;
    }

    public void updateRoomStatus(RoomStatus roomStatus) {
        this.roomStatus = roomStatus;
    }

}
