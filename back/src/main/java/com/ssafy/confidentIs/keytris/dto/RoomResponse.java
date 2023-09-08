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
public class RoomResponse {

  private String roomId;
  private RoomStatus roomStatus;

  public RoomResponse makeRoom(Room room) {
    return RoomResponse.builder()
        .roomId(room.getRoomId())
        .roomStatus(room.getRoomStatus())
        .build();
  }
}
