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
  public Room createRoom(String type, int category, Player player) {
    //TODO : 멀티 가능하게  string type 숫자로 적는 법? 일단 1인 모드만 고려

    List<String> subWords = wordService.getWords("sub",category,17);//subwords: 1
    List<String> targetWords = wordService.getWords("target",category,1);//later number of words to be brought, target:2
    List<String> levelWords = wordService.getWords("level",category,10);
    Room created = Room.builder()
        .roomStatus(RoomStatus.PREPARED)
        .limit(type.equals("single")?1:Integer.parseInt(type))
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
