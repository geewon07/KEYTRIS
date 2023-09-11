package com.ssafy.confidentIs.keytris.dto;

import com.ssafy.confidentIs.keytris.model.PlayerStatus;
import com.ssafy.confidentIs.keytris.model.RoomStatus;
import lombok.Getter;

@Getter
public class StartRequest {
  private String playerId;
  private PlayerStatus playerStatus;
  private String roomId;
  private RoomStatus roomStatus;

}
