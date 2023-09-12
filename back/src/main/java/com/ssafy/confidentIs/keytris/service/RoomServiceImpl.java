package com.ssafy.confidentIs.keytris.service;

import com.ssafy.confidentIs.keytris.model.Player;
import com.ssafy.confidentIs.keytris.model.PlayerStatus;
import com.ssafy.confidentIs.keytris.model.Room;
import com.ssafy.confidentIs.keytris.model.RoomStatus;
import java.util.ArrayList;
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
  private final PlayerService playerService;
  @Override
  public Room createRoom(String type, int category, Player player) {
    //type - game type
    //TODO : 멀티 가능하게  string type 숫자로 적는 법? 일단 1인 모드만 고려


    List<String> subWords = wordService.getWords("sub",category,20);//subwords: 1
    List<String> targetWords = wordService.getWords("target",category,10);//later number of words to be brought, target:2
    List<String> levelWords = wordService.getWords("level",category,10);
    Room created = Room.builder()
        .type(type)
        .category(category)
        .roomId("singletest")
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

  @Override
  public Room updateRoomStatus(String roomId, RoomStatus rStatus) {
    Room updatedRoom = roomManager.getRoom(roomId);
    updatedRoom.updateStatus(rStatus);
    roomManager.updateRoom(roomId,updatedRoom);
    return updatedRoom;
  }

  @Override
  public void updatePlayer(String roomId, String step) {
    Room room = roomManager.getRoom(roomId);
    Player player = room.getPlayerList()[0];
    List<String> initial;
    List<String> current;

    String target;
    switch (step){
      case "start":
        player.updateStatus(PlayerStatus.GAMING);
        target = room.getTargetWordList().get(0);
        initial = new ArrayList<>(room.getSubWordList().subList(0, 8));
        current = new ArrayList<>(initial);
        current.add(target);
        player.updateWords(initial,target,current);
        player.updateIndex(8,0);
        break;
      case "next":
        target = room.getTargetWordList().get(0);
        initial = new ArrayList<>(room.getSubWordList().subList(0, 8));
        current = new ArrayList<>(initial);
        current.add(target);
        player.updateWords(initial,target,current);
        player.updateIndex(8,0);
      case "over":
        player.updateStatus(PlayerStatus.OVER);
        break;
    }
    room.updatePlayer(player);

    roomManager.updateRoom(roomId,room);
  }


}
