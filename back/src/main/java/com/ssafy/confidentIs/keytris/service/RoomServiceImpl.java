package com.ssafy.confidentIs.keytris.service;

import com.ssafy.confidentIs.keytris.common.dto.response.ResponseDto;
import com.ssafy.confidentIs.keytris.common.exception.ErrorCode;
import com.ssafy.confidentIs.keytris.common.exception.customException.InvalidWordException;
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
import com.ssafy.confidentIs.keytris.model.*;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import com.ssafy.confidentIs.keytris.repository.RoomManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

  private final RoomManager roomManager;
  private final ScoreService scoreService;
  private final PlayerService playerService;
  private final DataServiceImpl dataServiceImpl;
  private final SessionMethodService sessionMethodService;
  private static final int AMOUNT = 10;

  private static final int TARGET_AMOUNT = 5; // TARGET 단어 받아오는 단위
  private static final int SUB_AMOUNT = 15; // SUB 단어 받어오는 단위
  private static final int LEVEL_AMOUNT = 10; // LEVEL 단어 받아오는 단위
  private static final int TARGET_ADD_STANDARD = 3; // TARGET 단어를 추가로 받아오는 기준
  private static final int SUB_ADD_STANDARD = 5; // SUB 단어를 추가로 받아오는 기준


  //TODO: 주석처리한 부분 정리하기
  @Override
  public StatusResponse createRoom(int category) {
    SinglePlayer player = playerService.initialPlayer();
    List<SinglePlayer> playerList = new ArrayList<>();
    playerList.add(player);
    log.info("createRoom, player:{}", player);

    DataWordListRequest subWordRequest = new DataWordListRequest(WordType.SUB, category, 20);
    DataWordListRequest targetWordRequest = new DataWordListRequest(WordType.TARGET, category, 10);
    DataWordListRequest levelWordRequest = new DataWordListRequest(WordType.LEVEL, category, 30);

    DataWordListResponse subWords = dataServiceImpl.sendWordListRequest(subWordRequest);
    DataWordListResponse targetWords = dataServiceImpl.sendWordListRequest(targetWordRequest);
    Queue<String> levelWords = new LinkedList<>(
            dataServiceImpl.sendWordListRequest(levelWordRequest).getData()
                    .getWordList());

    Room created = Room.builder()
        .category(category)
        .roomId(UUID.randomUUID().toString())
        .roomStatus(RoomStatus.PREPARING)
        .playerList(playerList)
        .subWordList(subWords.getData().getWordList())
        .targetWordList(targetWords.getData().getWordList())
        .levelWordList(levelWords)
        .build();

    roomManager.addRoom(created);
    log.info("rooms: {}", roomManager.getAllRooms());

    enterRoom(created.getRoomId());
    StatusResponse statusResponse = new StatusResponse();
    return statusResponse.idStatus(player, created);
  }

  @Override
  public StatusResponse enterRoom(String roomId) {
    Room room = roomManager.getRoom(roomId);
    SinglePlayer player = room.getPlayerList().get(0);
    log.info("roomSTATUS:{}, pSTATUS:{}", room.getRoomStatus(), player.getPlayerStatus());
    if (!checkReady(room.getRoomStatus(), player.getPlayerStatus())) {
      updatePlayerStatus(room, player, PlayerStatus.READY);
      updateRoomStatus(roomId, RoomStatus.PREPARED);
    } else {
      log.info("error with status");
    }
    return new StatusResponse(player.getPlayerId(), player.getPlayerStatus(), roomId,
            room.getRoomStatus());
  }

  @Override
  public StartResponse startRoom(String roomId) {
    Room room = roomManager.getRoom(roomId);
    SinglePlayer currentPlayer = room.getPlayerList().get(0);
    List<String> subWordList = new ArrayList<>();
    String target = "";

    if (currentPlayer.getPlayerId() == null || room.getRoomId() == null) {
      //TODO: ID 없을때 예외처리
      //      responseDto = new ResponseDto("fail", "게임 시작 실패, 신원미상");
      //-> ID 배정
      log.info("null id, player:{},room:{}", currentPlayer.getPlayerId(), room.getRoomId());
    } else {
      if (checkReady(room.getRoomStatus(), currentPlayer.getPlayerStatus())) {
        //상태 변화
        updateRoomStatus(roomId, RoomStatus.ONGOING);
        updatePlayerStatus(room, currentPlayer, PlayerStatus.GAMING);
        //단어 가져오기
        target = room.getTargetWordList().get(0);
        subWordList = new ArrayList<>(room.getSubWordList().subList(0, 9));
        log.info("start, target:{}, subWordList:{}", target, subWordList);
        //인덱스 업데이트
        currentPlayer.updateIndex(8, 0);
        room.updatePlayer(currentPlayer);
        //시작 시간 설정
        Timestamp timestamp = new Timestamp(new Date().getTime());
        room.updateStartTime(timestamp);
        //저장
        roomManager.updateRoom(roomId, room);
        //2초마다 단어보내주기 시작
        sessionMethodService.startSessionMethod(roomId, RoomType.SINGLE);
      } else {
//        responseDto = new ResponseDto("fail", "게임 시작 실패, 준비안됨");
        log.info("failed to start: not ready");
      }
    }

    // 이차원 배열로 반환하기 위해 전환
    String[][] sortedWordList = new String[10][2];

    for(int i=0; i<sortedWordList.length; i++) {
      sortedWordList[i][1] = "";
    }

    for(int i=0; i<subWordList.size(); i++) {
      sortedWordList[i][0] = subWordList.get(i);
    }
    sortedWordList[9][0] = target;

    String[][] newTargetWord = new String[1][2];
    newTargetWord[0][0] = target;
    newTargetWord[0][1] = "";

//    int[] sortedIndex = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

    WordListResponse wordListResponse = WordListResponse.builder()
        .newScore(currentPlayer.getScore())
        .sortedWordList(sortedWordList)
        .newSubWordList(null)
        .newTargetWord(newTargetWord)
        .sortedIndex(null)
        .targetWordRank(9)
        .build();

    log.info("WordListResponse", wordListResponse);

    StartResponse startResponse = new StartResponse();;
    return startResponse.startRoom(room, wordListResponse);
  }


  @Override
  public ResponseDto enterWord(GuessRequest request) {
    Room room = roomManager.getRoom(request.getRoomId());
    log.info("room : {}", room);
    SinglePlayer currentPlayer = room.getPlayerList().get(0);
    String[][] newTargetWord = request.getTargetWord();
    String target = newTargetWord[0][0];

    String[][] currentWordList = request.getCurrentWordList();
    int currentListSize = currentWordList.length; // currentList 단어 개수

    // 단어 유사도 받아오기
    List<String> wordOnlyList = Arrays.stream(currentWordList)
            .map(row -> row[0])
            .collect(Collectors.toList());

    DataGuessWordResponse dataGuessWordResponse = dataServiceImpl.sendGuessWordRequest(
            DataGuessWordRequest.builder()
                    .guessWord(request.getGuessWord())
                    .currentWordList(wordOnlyList)
                    .build());
    log.info("dataGuessWordResponse : {}", dataGuessWordResponse);
    if(dataGuessWordResponse.getSuccess().equals("fail")) {
      throw new InvalidWordException("입력할 수 없는 단어", ErrorCode.INVALID_WORD);
    }

    String[][] sortedWordList = dataGuessWordResponse.getData().getCalWordList();

    // 단어가 정렬 후 어느 위치로 이동했는 지 계산
    int[] sortedIndex = new int[currentListSize];

    // sortedWordList의 원소 위치를 해시맵에 저장
    Map<String, Integer> positionMap = new HashMap<>();
    for (int i = 0; i < sortedWordList.length; i++) {
      positionMap.put(sortedWordList[i][0], i);
    }

    // currentWordList의 각 원소 위치를 찾아 sortedIndex에 저장
    for (int i = 0; i < currentWordList.length; i++) {
      sortedIndex[i] = positionMap.get(currentWordList[i][0]);
    }

    // == 유사도 순위에 따라 점수 계산, 새 단어 정리 ==

    // 타겟어의 유사도 순위 계산
    int targetWordRank = -1;
    for(int i=0; i<sortedWordList.length; i++) {
      if(sortedWordList[i][0].equals(target)) {
        targetWordRank = i;
        break;
      }
    }
    log.info("targetWordRank: {}", targetWordRank);

    List<String> subWordList = new ArrayList<>();

    // 타겟어 유사도가 순위권 내인 경우
    if (0 <= targetWordRank && targetWordRank < 4) {
      // 플레이어 점수, 단어 idx 업데이트 및 새롭게 클라이언트에 전달할 단어 추출
      subWordList = updatePlayerBasedOnRank(targetWordRank, currentPlayer, room, currentListSize);
      target = room.getTargetWordList().get(currentPlayer.getTargetWordIndex());

      // Room의 여분 타겟어, 서브어가 부족한 경우, 추가 단어 요청
      checkRefill(room, WordType.SUB);
      checkRefill(room, WordType.TARGET);
    }
    log.info("subWordList: {}", subWordList);

    // 이차원 배열로 반환하기 위해 전환
    String[][] newSubWordList = null;
    if(subWordList != null && !subWordList.isEmpty()) {
      newSubWordList = new String[subWordList.size()][2];
      for (int i = 0; i < subWordList.size(); i++) {
        newSubWordList[i][0] = subWordList.get(i);
        newSubWordList[i][1] = "";
      }
    }

    newTargetWord[0][0] = target;
    newTargetWord[0][1] = "";

    WordListResponse response = WordListResponse.builder()
      .newScore(currentPlayer.getScore())
      .sortedWordList(sortedWordList)
      .newSubWordList(newSubWordList)
      .newTargetWord(newTargetWord)
      .sortedIndex(sortedIndex)
      .targetWordRank(targetWordRank)
      .build();

    log.info("after word process, player: {}", currentPlayer);

    return new ResponseDto("success", dataGuessWordResponse.getMessage(),
            Collections.singletonMap("SortedWordListResponse", response));
  }

  // 단어 입력 유사도 확인 -> 타겟어 유사도 순위가 높은 경우 점수, 단어 인덱스 갱신 후 newSubWordList를 반환하는 메서드
  private List<String> updatePlayerBasedOnRank(int targetWordRank, SinglePlayer currentPlayer, Room room, int currentListSize) {
    int[] scoreUpdates = {55, 45, 35, 25};
    int[] deletedSubWordCnt = {3, 2, 1, 0};

    // 점수 업데이트
    currentPlayer.updateScore(currentPlayer.getScore() + scoreUpdates[targetWordRank]);

    int listSize = currentListSize - deletedSubWordCnt[targetWordRank];
    List<String> subWordList = new ArrayList<>();

    // 삭제 후 남은 서브어 개수가 10개 미만인 경우 서브어 돌려줌
    if(listSize < 10) {
      int subIdx = Math.min((10-listSize), deletedSubWordCnt[targetWordRank]); // 서브어 총 개수 9개에 맞춤
      subWordList = new ArrayList<>(room.getSubWordList().subList(currentPlayer.getSubWordIndex()+1, currentPlayer.getSubWordIndex()+1+subIdx));
      currentPlayer.updateIndex(currentPlayer.getSubWordIndex()+subIdx, currentPlayer.getTargetWordIndex()+1);
    } else { // 10개 이상인 경우 서브어 돌려주지 않음
      currentPlayer.updateIndex(currentPlayer.getSubWordIndex(), currentPlayer.getTargetWordIndex()+1);
    }

    log.info("단어 유사도 순위로 플레이어 정보 업데이트 완료: {}", currentPlayer);
    return subWordList;
  }



  @Override
  public OverResponse gameOver(OverRequest request) {
    OverResponse overResponse = new OverResponse();

    Room room = roomManager.getRoom(request.getRoomId());
    SinglePlayer player = room.getPlayerList().get(0);
    log.info("score discrepancy? server:{},player:{}", player.getScore(), request.getScore());
    updateRoomStatus(room.getRoomId(), RoomStatus.FINISHED);
    player.updateStatus(PlayerStatus.OVER);
    room.updatePlayer(player);
    sessionMethodService.stopSessionMethod(request.getRoomId());
    roomManager.updateRoom(request.getRoomId(), room);
    boolean isRecord;
    List<RankingResponse> rankingList = scoreService.getRanking(4);
//    List<RankingResponse> rankingList = new ArrayList<>();
    if (player.getScore() > rankingList.get(4).getScore()) {
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
  public void updatePlayerStatus(Room room, SinglePlayer player, PlayerStatus playerStatus) {
    player.updateStatus(playerStatus);
    room.updatePlayer(player);
    roomManager.updateRoom(room.getRoomId(), room);
  }

  public void checkRefill(Room room, WordType type) {
    DataWordListRequest dataWordListRequest;
    DataWordListResponse dataWordListResponse;
    dataWordListRequest = new DataWordListRequest(type, room.getCategory(), AMOUNT);
    dataWordListResponse = dataServiceImpl.sendWordListRequest(dataWordListRequest);
    switch (type) {
      case SUB:
        if (room.getPlayerList().get(0).getSubWordIndex() + 10 > room.getSubWordList().size()) {
          if (dataWordListResponse.getSuccess().equals("success")) {
            room.updateSubWordList(dataWordListResponse.getData().getWordList());
            log.info("SUB wordList 단어 추가: {}", room.getSubWordList());
          } else {
            log.info("refill sub fail");
          }
          //TODO: FAIL 일때 예외처리 어떻게 해줄 것인지, return 타입을 컨트롤러 단에서 부터 고쳐서 메세지가 통하게 만들 것인지
        }
        break;
      case TARGET:
        if (room.getPlayerList().get(0).getTargetWordIndex() + 10 > room.getTargetWordList()
                .size()) {
          if (dataWordListResponse.getSuccess().equals("success")) {
            room.updateTargetWordList(dataWordListResponse.getData().getWordList());
            log.info("TARGET wordList 단어 추가: {}", room.getSubWordList());
          } else {
            log.info("refill target fail");
          }
        }
        break;
      case LEVEL:
        if (10 > room.getLevelWordList().size()) {
          if (dataWordListResponse.getSuccess().equals("success")) {
            room.updateLevelWordList(dataWordListResponse.getData().getWordList());
          } else {
            log.info("refill level fail");
          }
        }
        break;
    }
    roomManager.updateRoom(room.getRoomId(), room);
  }


}
