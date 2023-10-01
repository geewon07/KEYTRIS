package com.ssafy.confidentIs.keytris.dto.gameDto;

import com.ssafy.confidentIs.keytris.dto.WordListResponse;
import com.ssafy.confidentIs.keytris.model.Room;
import com.ssafy.confidentIs.keytris.model.SinglePlayer;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;

@ToString
@Slf4j
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StartResponse {

    private Timestamp startTime;
    StatusResponse statusResponse;
    WordListResponse wordListResponse;

    public StartResponse startRoom(Room room, WordListResponse wResponse) {
        SinglePlayer player = room.getPlayerList().get(0);
        StatusResponse sResponse = new StatusResponse();
        return StartResponse.builder()
                .startTime(room.getStartTime())
                .wordListResponse(wResponse)
                .statusResponse(sResponse.idStatus(player, room))
                .build();
    }
}
