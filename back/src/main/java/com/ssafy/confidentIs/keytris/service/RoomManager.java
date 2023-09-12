package com.ssafy.confidentIs.keytris.service;

import com.ssafy.confidentIs.keytris.model.Room;
import java.util.UUID;

public interface RoomManager {

  void addRoom(Room room);

  Room getRoom(UUID roomId);

  void updateRoom(UUID roomId, Room updated);

  void removeRoom(UUID roomId);

}
