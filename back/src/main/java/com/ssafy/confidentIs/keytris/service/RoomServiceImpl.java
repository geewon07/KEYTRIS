package com.ssafy.confidentIs.keytris.service;

import com.ssafy.confidentIs.keytris.dto.GuessRequest;
import com.ssafy.confidentIs.keytris.dto.OverRequest;
import com.ssafy.confidentIs.keytris.dto.OverResponse;
import com.ssafy.confidentIs.keytris.dto.RankingResponse;
import com.ssafy.confidentIs.keytris.dto.StartResponse;
import com.ssafy.confidentIs.keytris.dto.StatusResponse;
import com.ssafy.confidentIs.keytris.dto.WordListResponse;
import com.ssafy.confidentIs.keytris.dto.dataDto.DataGuessWordRequest;
import com.ssafy.confidentIs.keytris.dto.dataDto.DataGuessWordResponse;
import com.ssafy.confidentIs.keytris.dto.dataDto.DataWordListRequest;
import com.ssafy.confidentIs.keytris.dto.dataDto.DataWordListResponse;
import com.ssafy.confidentIs.keytris.model.PlayerStatus;
import com.ssafy.confidentIs.keytris.model.Room;
import com.ssafy.confidentIs.keytris.model.RoomStatus;
import com.ssafy.confidentIs.keytris.model.SinglePlayer;
import com.ssafy.confidentIs.keytris.model.WordType;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

  private final RoomManager roomManager;
  private final WordService wordService;
  private final PlayerService playerService;
  private final DataServiceImpl dataServiceImpl;
  private final RedisTemplate redisTemplate;

  private static final int AMOUNT = 10;
  //TODO: 주석처리한 부분 정리하기
  @Override
  public StatusResponse createRoom(int category) {
    SinglePlayer player = playerService.initialPlayer();
    List<SinglePlayer> playerList = new ArrayList<>();
    playerList.add(player);
    log.info("createRoom, player:{}", player);

    DataWordListRequest subWordRequest = new DataWordListRequest(WordType.SUB, category, 20);
    DataWordListRequest targetWordRequest = new DataWordListRequest(WordType.TARGET, category, 10);
    DataWordListRequest levelWordRequest = new DataWordListRequest(WordType.LEVEL, category, 10);

    DataWordListResponse subWords = dataServiceImpl.sendWordListRequest(subWordRequest);
    DataWordListResponse targetWords = dataServiceImpl.sendWordListRequest(targetWordRequest);
    Queue<String> levelWords = new LinkedList<>(
        dataServiceImpl.sendWordListRequest(levelWordRequest).getData()
            .getWordList());

    Room created = Room.builder()
        .category(category)
        .roomId(UUID.randomUUID().toString())
        .roomStatus(RoomStatus.PREPARED)
        .playerList(playerList)
        .subWordList(subWords.getData().getWordList())
        .targetWordList(targetWords.getData().getWordList())
        .levelWordList(levelWords)
        .build();

    roomManager.addRoom(created);
    StatusResponse statusResponse = new StatusResponse();
    return statusResponse.idStatus(player, created);
  }

  @Override
  public StartResponse startRoom(String roomId) {
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
        //상태 변화
        updateRoomStatus(roomId, RoomStatus.ONGOING);
        updatePlayerStatus(room, player, "start");
        //단어 가져오기
        targetWord = room.getTargetWordList().get(0);
        subWordList = new ArrayList<>(room.getSubWordList().subList(0, 9));
        log.info("start, target:{}, subWordList:{}", targetWord, subWordList);
        //인덱스 업데이트
        player.updateIndex(9, 1);
        room.updatePlayer(player);
        //시작 시간 설정
        Date currentDate = new Date();// Convert the Date object to a Timestamp
        Timestamp timestamp = new Timestamp(currentDate.getTime());
        room.updateStartTime(timestamp);
        //저장
        roomManager.updateRoom(roomId, room);
      } else {
//        responseDto = new ResponseDto("fail", "게임 시작 실패, 준비안됨");
        log.info("failed to start: not ready");
      }
    }
    StartResponse startResponse = new StartResponse();
    return startResponse.startRoom(room, targetWord, subWordList);
  }

  //TODO : 전부다 산산조각내서 별도의 메소드로 만들고 싶은 어떤 마음,,,
  @Override
  public WordListResponse enterWord(GuessRequest request) {
//    log.info("null troubleshoot currWL:{}",currentWordList);

    Room room = roomManager.getRoom(request.getRoomId());
    SinglePlayer player = room.getPlayerList().get(0);
    String target = request.getTargetWord();
    List<String> subWordList;

    DataGuessWordRequest dataGuessWordRequest = DataGuessWordRequest.builder()
        .guessWord(request.getGuessWord())
        .currentWordList(request.getCurrentWordList())
        .build();
    DataGuessWordResponse dataGuessWordResponse = dataServiceImpl.sendGuessWordRequest(
        dataGuessWordRequest);
    if (dataGuessWordResponse.getSuccess().equals("success")) {

      String[][] sortedWordList = dataGuessWordResponse.getData().getCalWordList();

      //점수
      //TODO : index -1 일때 없을 때 제외 시켜야..
//      int index = wordService.getIndex(target, sortedWordList);
      int index = -1;
      for (int i = 0; i < sortedWordList.length; i++) {
        String[] wordData = sortedWordList[i];
        if (wordData[0].equals(target)) {
          index = i;
          break;// Return the index if the word is found.
        }
      }
      Long score = 0L;
      int toDelete = 0;
      if (index >= 0 && index <= 3) {
        score = (4 - index) * 10L + 15;
        toDelete = 3 - index;
      }
      if(score==0)
        log.info("score 0 failed to guess");
      //TODO 0 점 반환될때 부정적인 모션 흔들림 같은.
      checkRefill(room,WordType.SUB);
      checkRefill(room,WordType.TARGET);
      checkRefill(room,WordType.LEVEL);

      player.updateIndex(player.getSubWordIndex() + toDelete, player.getTargetWordIndex() + toDelete>0?1:0);
      room.updatePlayer(player);
      //삭제 성공시 추가될 단어
      target = room.getTargetWordList().get(player.getTargetWordIndex());
      subWordList = new ArrayList<>(
          room.getSubWordList()
              .subList(player.getSubWordIndex(), player.getSubWordIndex() + toDelete));
      player.updateIndex(player.getSubWordIndex() + toDelete, player.getTargetWordIndex() + 1);

      log.info("after word process, player:{}", player);

      return new WordListResponse().result(sortedWordList, subWordList, target, room, score);
    } else if(dataGuessWordResponse.getMessage().equals("fail")) {
      log.info("fail message:{}",dataGuessWordResponse.getMessage());
      return new WordListResponse();
    }
    return new WordListResponse();
  }

  @Override
  public OverResponse gameOver(OverRequest request) {
    OverResponse overResponse = new OverResponse();

    Room room = roomManager.getRoom(request.getRoomId());
    SinglePlayer player = room.getPlayerList().get(0);
    updateRoomStatus(room.getRoomId(), RoomStatus.FINISHED);
    player.updateStatus(PlayerStatus.OVER);
    room.updatePlayer(player);

    //TODO : news API
    // URL :https://openapi.naver.com/v1/search/news.json
    // method : GET

    roomManager.updateRoom(request.getRoomId(), room);
    //TODO: redis-> 5위점수 < score
    boolean isRecord;
    List<RankingResponse> rankingList = getRanking();
    if (request.getScore() > rankingList.get(4).getScore()) {
      log.info("score higher than 5th place -> add to redis");
      isRecord = true;
    } else {
      isRecord = false;
    }
    return overResponse.gameOver(isRecord, room, rankingList);
  }

  @Override
  public Boolean checkReady(RoomStatus rStatus, PlayerStatus pStatus) {
    return rStatus.equals(RoomStatus.PREPARED) && pStatus.equals(PlayerStatus.READY);
  }

  @Override
  public void updateRoomStatus(String roomId, RoomStatus rStatus) {
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

  public List<RankingResponse> getRanking() {
    String key = "ranking";
    ZSetOperations<String, String> stringStringZSetOperations = redisTemplate.opsForZSet();
    Set<TypedTuple<String>> typedTuples = stringStringZSetOperations.reverseRangeWithScores(key, 0,
        4);
    //TODO null의 경우 생각하기 -> 초기 레벨로 랭킹값 만들기?
    List<RankingResponse> toList = typedTuples.stream().map(RankingResponse::convert).collect(
        Collectors.toList());
    return toList;
  }

  ;

  public List<RankingResponse> addHighscore(String nickname, String roomId) {
    Long score = roomManager.getRoom(roomId).getPlayerList().get(0).getScore();
    redisTemplate.opsForZSet().add("ranking", nickname, score);
    return getRanking();
  }

  public void checkRefill (Room room, WordType type){
    DataWordListRequest dataWordListRequest;
    DataWordListResponse dataWordListResponse;
    dataWordListRequest = new DataWordListRequest(type, room.getCategory(),AMOUNT);
    dataWordListResponse = dataServiceImpl.sendWordListRequest(dataWordListRequest);
    switch (type){
      case SUB:
        if(room.getPlayerList().get(0).getSubWordIndex() + 10 > room.getSubWordList().size()){
          if(dataWordListResponse.getSuccess().equals("success")){
            room.updateSubWordList(dataWordListResponse.getData().getWordList());
          }else {
            log.info("refill sub fail");
          }
          //TODO: FAIL 일때 예외처리 어떻게 해줄 것인지, return 타입을 컨트롤러 단에서 부터 고쳐서 메세지가 통하게 만들 것인지
        }
        break;
      case TARGET:
        if(room.getPlayerList().get(0).getTargetWordIndex() + 10 > room.getTargetWordList().size()){
          if(dataWordListResponse.getSuccess().equals("success")){
            room.updateTargetWordList(dataWordListResponse.getData().getWordList());
          }else {
            log.info("refill target fail");
          }}
        break;
      case LEVEL:
        if(10 > room.getLevelWordList().size()){
          if(dataWordListResponse.getSuccess().equals("success")){
            room.updateLevelWordList(dataWordListResponse.getData().getWordList());
          }else {
            log.info("refill level fail");
          }}
        break;
    }
    roomManager.updateRoom(room.getRoomId(), room);
  }


}
