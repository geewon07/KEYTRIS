package com.ssafy.confidentIs.keytris.repository;

import com.ssafy.confidentIs.keytris.model.multiModel.MultiRoom;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class MultiRoomManager {

    // ConcurrentHashMap : 멀티스레드에 안전한 자료구조
    private static final ConcurrentHashMap<String, MultiRoom> multiRoomRegistry = new ConcurrentHashMap<>();

    public void addRoom(MultiRoom room) {
        multiRoomRegistry.put(room.getRoomId(), room);
    }

    public MultiRoom getRoom(String roomId) {
        return multiRoomRegistry.get(roomId);
    }

    public void updateRoom(String roomId, MultiRoom updatedRoom) {
        multiRoomRegistry.put(roomId, updatedRoom);
    }

    public void removeRoom(String roomId) {
        multiRoomRegistry.remove(roomId);
    }

    public Collection<MultiRoom> getAllRooms() {
        return multiRoomRegistry.values();
    }
}
