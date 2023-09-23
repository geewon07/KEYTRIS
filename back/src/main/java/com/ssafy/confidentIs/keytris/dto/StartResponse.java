package com.ssafy.confidentIs.keytris.dto;

import com.ssafy.confidentIs.keytris.model.SinglePlayer;
import com.ssafy.confidentIs.keytris.model.Room;
import java.sql.Timestamp;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

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
