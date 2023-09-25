package com.ssafy.confidentIs.keytris.dto;

import com.ssafy.confidentIs.keytris.model.PlayerStatus;
import com.ssafy.confidentIs.keytris.model.Room;
import com.ssafy.confidentIs.keytris.model.RoomStatus;
import com.ssafy.confidentIs.keytris.model.SinglePlayer;
import lombok.*;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatusResponse {

    private String playerId;
    private PlayerStatus playerStatus;
    private String roomId;
    private RoomStatus roomStatus;

    public StatusResponse idStatus(SinglePlayer player, Room room) {
        return StatusResponse.builder()
                .playerId(player.getPlayerId())
                .playerStatus(player.getPlayerStatus())
                .roomId(room.getRoomId())
                .roomStatus(room.getRoomStatus())
                .build();
    }
}
