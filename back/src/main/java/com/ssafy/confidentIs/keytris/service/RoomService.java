package com.ssafy.confidentIs.keytris.service;

import com.ssafy.confidentIs.keytris.model.Player;
import com.ssafy.confidentIs.keytris.model.PlayerStatus;
import com.ssafy.confidentIs.keytris.model.Room;
import com.ssafy.confidentIs.keytris.model.RoomStatus;
import java.sql.Timestamp;

public interface RoomService {


  Room createRoom(String type, String category, Player player);
  Boolean checkReady(RoomStatus rStatus, PlayerStatus pStatus);

//  Boolean checkExistance(String playerId, String roomId);
}
