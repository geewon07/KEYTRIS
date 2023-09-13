package com.ssafy.confidentIs.keytris.service;

import com.ssafy.confidentIs.keytris.dto.StartResponse;
import com.ssafy.confidentIs.keytris.dto.StatusResponse;
import com.ssafy.confidentIs.keytris.dto.WordListResponse;
import com.ssafy.confidentIs.keytris.model.BasePlayer;
import com.ssafy.confidentIs.keytris.model.SinglePlayer;
import com.ssafy.confidentIs.keytris.model.PlayerStatus;
import com.ssafy.confidentIs.keytris.model.Room;
import com.ssafy.confidentIs.keytris.model.RoomStatus;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

  private final RoomManager roomManager;
  private final WordService wordService;
  private final PlayerService playerService;

  //TODO: 주석처리한 부분 정리하기
  @Override
  public StatusResponse createRoom(int category) {
    SinglePlayer player = playerService.initialPlayer();
    log.info("createRoom, player:{}",player);
    //type - game type
    //TODO : 멀티 가능하게  string type 숫자로 적는 법? 일단 1인 모드만 고려
    List<String> subWords = wordService.getWords("sub", category, 20);//subwords: 1
    List<String> targetWords = wordService.getWords("target", category,
        10);//later number of words to be brought, target:2
    Queue<String> levelWords = new LinkedList<>(wordService.getWords("level", category, 10));
    List<SinglePlayer> playerList =new ArrayList<>();
    playerList.add(player);
    Room created = Room.builder()
        .category(category)
        .roomId(UUID.randomUUID())
        .roomStatus(RoomStatus.PREPARED)
        .playerList(playerList)
        .subWordList(subWords)
        .targetWordList(targetWords)
        .levelWordList(levelWords)
        .build();
    roomManager.addRoom(created);
    StatusResponse statusResponse = new StatusResponse();
    return statusResponse.idStatus(player, created);
  }

  @Override
  public StartResponse startRoom(UUID roomId) {
    Room room = roomManager.getRoom(roomId);
    SinglePlayer player = room.getPlayerList().get(0);
    List<String> subWordList = new ArrayList<>();
    String targetWord = "";
    if (player.getPlayerId() == null || room.getRoomId() == null) {
      //TODO: ID 없을때 예외처리
      //      responseDto = new ResponseDto("fail", "게임 시작 실패, 신원미상");
      //-> ID 배정
      log.info("null id, player:{},room:{}", player.getPlayerId(), room.getRoomId());
    } else {
      if (checkReady(room.getRoomStatus(), player.getPlayerStatus())) {
        updateRoomStatus(roomId, RoomStatus.ONGOING);
        updatePlayerStatus(room, player, "start");
        targetWord = room.getTargetWordList().get(0);
        subWordList = new ArrayList<>(room.getSubWordList().subList(0, 9));
        player.updateIndex(9, 1);
        Date currentDate = new Date();// Convert the Date object to a Timestamp
        Timestamp timestamp = new Timestamp(currentDate.getTime());
        room.updateStartTime(timestamp);

      } else {
//        responseDto = new ResponseDto("fail", "게임 시작 실패, 준비안됨");
        log.info("failed to start: not ready");
      }
    }
    StartResponse startResponse = new StartResponse();
    return startResponse.startRoom(room, targetWord, subWordList);
  }

  //TODO : 예외처리 -> 단어가 DB에 없을때 ?

  @Override
  public WordListResponse enterWord(UUID roomId, List<String> currentWordList, String guess) {
//    log.info("null troubleshoot currWL:{}",currentWordList);
    // TODO:  단어를 입력받으면 =>1.유효성 체크 2.
    Room room = roomManager.getRoom(roomId);
    SinglePlayer player = room.getPlayerList().get(0);
    String target = room.getTargetWordList().get(player.getTargetWordIndex());
    List<String> subWordList = new ArrayList<>();
    if (wordService.checkDataBase(guess)) {
      //TODO : 지원이가 만든 API 로 다시 할 예정
      List<String> sortedWordList = wordService.sortByProximity(currentWordList, guess);

      //점수
      int index = wordService.getIndex(target, sortedWordList);
      long score = index <= 3 ? (4 - index) * 10L + 15 : 0;
      player.updateScore(player.getScore() + score);
      int toDelete = index <= 3 ? (3 - index) : 0;
      updatePlayerWordIndex(room, player, "next", toDelete);
      room.updatePlayer(player);
      //삭제 성공시 추가될 단어
      target = room.getTargetWordList().get(player.getTargetWordIndex());
      subWordList = new ArrayList<>(
          room.getSubWordList()
              .subList(player.getSubWordIndex(), player.getSubWordIndex() + toDelete));
      player.updateIndex(player.getSubWordIndex() + toDelete, player.getTargetWordIndex() + 1);

      log.info("after word process, player:{}", player.toString());

      return new WordListResponse().result(sortedWordList, subWordList, target, room, score);
    } else {
      log.info("not in db");
      return new WordListResponse();
    }

  }

  @Override
  public void gameOver(UUID roomId, String lastWord, long score) {
    Room room = roomManager.getRoom(roomId);
    SinglePlayer player = room.getPlayerList().get(0);
    updateRoomStatus(roomId, RoomStatus.FINISHED);
    updatePlayerStatus(room, player, "over");
//    player.updateLastWord(lastWord);

    room.updatePlayer(player);
    //TODO : news API 해야되
    roomManager.updateRoom(roomId, room);
    //TODO: redis-> 5위점수 < score
    if (score > 2000) {
      log.info("score higher than 5th place -> add to redis");
//      responseDto = new ResponseDto("success", "신기록 갱신, 입력창 보여주기",
//          Collections.singletonMap("isRecord", true));
    } else {
//      responseDto = new ResponseDto("success", "게임 종료, 기사 목록",
//          Collections.singletonMap("articleList",
//              new String[]{"https://developers.naver.com/docs/serviceapi/search/news/news.md"}));
      //regardless give some articles -> naver api search news keyword ( lastWord : latest )
    }
  }

  @Override
  public Boolean checkReady(RoomStatus rStatus, PlayerStatus pStatus) {
    return rStatus.equals(RoomStatus.PREPARED) && pStatus.equals(PlayerStatus.READY);
  }

  @Override
  public void updateRoomStatus(UUID roomId, RoomStatus rStatus) {
    Room updatedRoom = roomManager.getRoom(roomId);
    updatedRoom.updateStatus(rStatus);
    roomManager.updateRoom(roomId, updatedRoom);
  }

  @Override
  public void updatePlayerStatus(Room room, SinglePlayer player, String step) {
    switch (step) {
      case "start":
        player.updateStatus(PlayerStatus.GAMING);
        break;
      case "over":
        player.updateStatus(PlayerStatus.OVER);
        break;
    }
    room.updatePlayer(player);
    roomManager.updateRoom(room.getRoomId(), room);
  }

  @Override
  public void updatePlayerWordIndex(Room room, SinglePlayer player, String step, int toDelete) {
    List<String> initial;
    List<String> current;

    String target;
    switch (step) {
      case "start":
//        target = room.getTargetWordList().get(0);
//        initial = new ArrayList<>(room.getSubWordList().subList(0, 9));
//        current = new ArrayList<>(initial);
//        current.add(target);
        player.updateIndex(9, 1);
        break;
      case "next":
//        target = room.getTargetWordList().get(player.getTargetWordIndex());
//        initial = new ArrayList<>(
//            room.getSubWordList()
//                .subList(player.getSubWordIndex(), player.getSubWordIndex() + toDelete));
//        current = new ArrayList<>(initial);
//        current.add(target);
        player.updateIndex(player.getSubWordIndex() + toDelete, player.getTargetWordIndex() + 1);
        break;
    }
    room.updatePlayer(player);
    roomManager.updateRoom(room.getRoomId(), room);
  }


}
