package com.ssafy.confidentIs.keytris.dto.multiGameDto;

import com.ssafy.confidentIs.keytris.model.MultiPlayer;
import com.ssafy.confidentIs.keytris.model.RoomStatus;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UpdatedPlayerResponse {

    private RoomStatus roomStatus;
    private MultiPlayer player;

    public void updateRoomStatus(RoomStatus roomStatus) {
        this.roomStatus = roomStatus;
    }

}
