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
  private String targetWord;
  private List<String> subWordList;

  StatusResponse statusResponse;
  WordListResponse wordListResponse;

  //TODO: roomManager-> 방의 정보 가져오기
  public StartResponse startRoom(Room room, String targetWord, List<String> subWordList) {
    SinglePlayer player = room.getPlayerList().get(0);
    StatusResponse sResponse = new StatusResponse();
    WordListResponse wResponse = new WordListResponse();
//    log.info("response params, room:{},player:{}",room,player);
    sResponse = sResponse.idStatus(player, room);
    wResponse.start(subWordList, targetWord, 0);
//    log.info("startResponse statusResponse:{}",sResponse.toString());
    return StartResponse.builder()
        .startTime(room.getStartTime())
        .wordListResponse(wResponse)
        .statusResponse(sResponse)
        .build();
  }
}
