package com.ssafy.confidentIs.keytris.dto;

import com.ssafy.confidentIs.keytris.model.Room;
import com.ssafy.confidentIs.keytris.model.RoomStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StartResponse {
  private int score;
  private Room room;
  private RoomStatus roomStatus;

//TODO: roomManager-> 방의 정보 가져오기
  public StartResponse getRoom(Room room) {

    return StartResponse.builder()

        .build();
  }
}
