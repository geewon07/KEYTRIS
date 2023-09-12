package com.ssafy.confidentIs.keytris.dto;

import com.ssafy.confidentIs.keytris.model.PlayerStatus;
import com.ssafy.confidentIs.keytris.model.RoomStatus;
import java.util.UUID;
import lombok.Getter;

@Getter
public class StartRequest {

  private UUID playerId;
  private PlayerStatus playerStatus;
  private UUID roomId;
  private RoomStatus roomStatus;
}
