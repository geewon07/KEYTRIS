package com.ssafy.confidentIs.keytris.repository;

import com.ssafy.confidentIs.keytris.model.Room;

import java.util.Collection;

public interface RoomManager {

    void addRoom(Room room);

    Room getRoom(String roomId);

    void updateRoom(String roomId, Room updated);

    void removeRoom(String roomId);

    Collection<Room> getAllRooms();

}
