package com.ssafy.confidentIs.keytris.dto;

import com.ssafy.confidentIs.keytris.model.PlayerStatus;
import com.ssafy.confidentIs.keytris.model.Room;
import com.ssafy.confidentIs.keytris.model.RoomStatus;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StartResponse {
  private Room room;
  private RoomStatus roomStatus;
  private List<String> currentWordList;
  private PlayerStatus playerStatus;
  private String targetWord;



  //TODO: roomManager-> 방의 정보 가져오기
  public StartResponse getRoom(Room room) {
    List<String> initial = new ArrayList<>(room.getSubWordList().subList(0, 8));
    String target = room.getTargetWordList().get(0);
    initial.add(target);
    return StartResponse.builder()
        .roomStatus(getRoomStatus())
        .currentWordList(initial)
        .targetWord(target)
        .build();
  }
}
