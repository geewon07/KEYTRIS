package com.ssafy.confidentIs.keytris.service;

import com.ssafy.confidentIs.keytris.model.Room;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class RoomManagerImpl implements RoomManager {

  private static final ConcurrentHashMap<String, Room> roomRegistry = new ConcurrentHashMap<>();

  @Override
  public void addRoom(Room room) {
    //TODO : 예외처리 어떻게 해줄 것인지, 같은 ID 발생할 가능성이 있나?
    roomRegistry.put(room.getRoomId(), room);
  }

  @Override
  public Room getRoom(String roomId) {
    return roomRegistry.get(roomId);
  }

  @Override
  public void updateRoom(String roomId, Room updated) {
    roomRegistry.put(roomId, updated);
  }

  @Override
  public void removeRoom(String roomId) {
    roomRegistry.remove(roomId);
  }
}
