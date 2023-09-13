package com.ssafy.confidentIs.keytris.service;

import com.ssafy.confidentIs.keytris.model.Room;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class RoomManagerImpl implements RoomManager {

  private static final ConcurrentHashMap<UUID, Room> roomRegistry = new ConcurrentHashMap<>();

  @Override
  public void addRoom(Room room) {
    //TODO : 예외처리 어떻게 해줄 것인지, 같은 ID 발생할 가능성이 있나?
    roomRegistry.put(room.getRoomId(), room);
  }

  @Override
  public Room getRoom(UUID roomId) {
    return roomRegistry.get(roomId);
  }

  @Override
  public void updateRoom(UUID roomId, Room updated) {
    roomRegistry.put(roomId, updated);
  }

  @Override
  public void removeRoom(UUID roomId) {
    roomRegistry.remove(roomId);
  }
}
