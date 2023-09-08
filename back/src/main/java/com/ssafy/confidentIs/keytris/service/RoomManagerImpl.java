package com.ssafy.confidentIs.keytris.service;

import com.ssafy.confidentIs.keytris.model.Room;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class RoomManagerImpl implements RoomManager{
private ConcurrentHashMap<String, Room> roomRegistry = new ConcurrentHashMap<>();
  @Override
  public void addRoom(Room room) {
    //TODO : 예외처리 어떻게 해줄 것인지, 같은 ID 발생할 가능성이 있나?
      roomRegistry.put(room.getRoomId(), room);
  }

  @Override
  public Room getRoom(String roomId) {

    return  roomRegistry.get(roomId);
  }

  @Override
  public void updateRoom(String roomId) {

  }

  @Override
  public void removeRoom(String roomId) {

  }
}
