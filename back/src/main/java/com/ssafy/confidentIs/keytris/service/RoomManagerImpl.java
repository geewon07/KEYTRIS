package com.ssafy.confidentIs.keytris.service;

import com.ssafy.confidentIs.keytris.model.Room;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RoomManagerImpl implements RoomManager {

//  private final RoomService roomService;
  private static final ConcurrentHashMap<String, Room> roomRegistry = new ConcurrentHashMap<>();

  @Override
  public void addRoom(Room room) {
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

  //TODO : 소켓 세션과 방시작, 경과시간을
  @Override
  public void removeRoom(String roomId) {
    roomRegistry.remove(roomId);
  }
}
