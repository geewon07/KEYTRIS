package com.ssafy.confidentIs.keytris.dto;

import com.ssafy.confidentIs.keytris.model.Player;
import com.ssafy.confidentIs.keytris.model.PlayerStatus;
import com.ssafy.confidentIs.keytris.model.Room;
import com.ssafy.confidentIs.keytris.model.RoomStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatusResponse {

//private PlayerResponse playerInfo;
//private RoomResponse roomInfo;

  private String playerId;
  private PlayerStatus playerStatus;
  private String roomId;
  private RoomStatus roomStatus;

  public StatusResponse idStatus(Player player, Room room) {
    return StatusResponse.builder()
        .playerId(player.getPlayerId())
        .playerStatus(player.getPlayerStatus())
        .roomId(room.getRoomId())
        .roomStatus(room.getRoomStatus())
        .build();
  }
}
