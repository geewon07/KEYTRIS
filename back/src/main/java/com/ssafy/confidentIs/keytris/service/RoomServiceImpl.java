package com.ssafy.confidentIs.keytris.service;

import com.ssafy.confidentIs.keytris.model.Player;
import com.ssafy.confidentIs.keytris.model.PlayerStatus;
import com.ssafy.confidentIs.keytris.model.Room;
import com.ssafy.confidentIs.keytris.model.RoomStatus;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
  private final RoomManager roomManager;
  private final WordService wordService;
  @Override
  public Room createRoom(String type, String category, Player player) {

    String[] subWords = wordService.getWords("categoryOrSub",1);//subwords: 1
    String[] targetWords = wordService.getWords("categoryOrTarget",2);//later number of words to be brought, target:2
    String[] levelWords = wordService.getWords("categoryOrSub",1);
    Room created = Room.builder()
        .roomStatus(RoomStatus.PREPARED)
        .limit(1)
        .playerList(new Player[]{player})
        .master(player.getPlayerId())
        .subWordList(subWords)
        .targetWordList(targetWords)
        .levelWordList(levelWords)
        .build();
    roomManager.addRoom(created);
    return created;
  }

  @Override
  public Boolean checkReady(RoomStatus rStatus, PlayerStatus pStatus) {
    return rStatus.equals(RoomStatus.PREPARED) && pStatus.equals(PlayerStatus.READY);
  }

//  @Override
//  public Boolean checkExistance(String playerId, String roomId) {
//    return  !playerId.equals(null);
//  }

}
