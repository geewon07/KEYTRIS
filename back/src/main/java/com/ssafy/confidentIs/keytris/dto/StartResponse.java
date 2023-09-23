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

  public StartResponse startRoom(Room room, String targetWord, List<String> subWordList) {
    SinglePlayer player = room.getPlayerList().get(0);
    StatusResponse sResponse = new StatusResponse();
    WordListResponse wResponse = new WordListResponse();
//    log.info("response params, room:{},player:{}",room,player);
    log.info("startResponse wordList:{}", wResponse.toString());
    return StartResponse.builder()
        .startTime(room.getStartTime())
        .wordListResponse(wResponse.start(subWordList, targetWord, 0L))
        .statusResponse(sResponse.idStatus(player, room))
        .build();
  }
}
