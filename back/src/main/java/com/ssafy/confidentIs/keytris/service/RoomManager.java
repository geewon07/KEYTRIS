package com.ssafy.confidentIs.keytris.service;

import com.ssafy.confidentIs.keytris.model.Room;

public interface RoomManager {
  void addRoom(Room room);
  Room getRoom(String roomId);
  void updateRoom(String roomId);
  void removeRoom(String roomId);

}
