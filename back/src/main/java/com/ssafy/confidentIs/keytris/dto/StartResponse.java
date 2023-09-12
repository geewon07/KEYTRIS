package com.ssafy.confidentIs.keytris.dto;

import com.ssafy.confidentIs.keytris.model.Player;
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


  //TODO: roomManager-> 방의 정보 가져오기
  public StartResponse startRoom(Room room) {
    Player player = room.getPlayerList()[0];
    StatusResponse sResponse = new StatusResponse();
//    log.info("response params, room:{},player:{}",room,player);
    sResponse = sResponse.idStatus(player, room);

//    log.info("startResponse statusResponse:{}",sResponse.toString());

    return StartResponse.builder()
        .startTime(room.getStartTime())
        .targetWord(player.getTargetWord())
        .subWordList(player.getSubWordList())
        .statusResponse(sResponse)
        .build();
  }
}
