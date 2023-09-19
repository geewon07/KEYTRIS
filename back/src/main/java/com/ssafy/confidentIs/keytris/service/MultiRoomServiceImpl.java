package com.ssafy.confidentIs.keytris.service;


import com.ssafy.confidentIs.keytris.dto.dataDto.DataGuessWordRequest;
import com.ssafy.confidentIs.keytris.dto.dataDto.DataGuessWordResponse;
import com.ssafy.confidentIs.keytris.dto.dataDto.DataWordListRequest;
import com.ssafy.confidentIs.keytris.dto.dataDto.DataWordListResponse;
import com.ssafy.confidentIs.keytris.dto.multiDto.*;
import com.ssafy.confidentIs.keytris.model.*;
import com.ssafy.confidentIs.keytris.model.multiModel.MultiPlayer;
import com.ssafy.confidentIs.keytris.model.multiModel.MultiRoom;
import com.ssafy.confidentIs.keytris.repository.MultiRoomManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MultiRoomServiceImpl {

    private final DataServiceImpl dataServiceImpl;

    private final MultiRoomManager multiRoomManager;

    private static final int TARGET_AMOUNT = 5; // TARGET 단어 받아오는 단위
    private static final int AMOUNT = 10; // SUB, LEVEL 단어 받어오는 단위
    private static final int ADD_AMOUNT = 30; // 단어를 추가로 받아오는 기준


    // 멀티 게임 방 만들기
    public MultiGameConnectResponse createMultiGame(MultiGameCreateRequest request) {

        // 플레이어 생성
        MultiPlayer currentPlayer = initialMultiPlayer(request.getNickname(), PlayerStatus.READY, true);
        log.info("플레이어 생성 완료");

        Queue<String> levelWordList = new LinkedList<>();

        // 방 생성
        int category = request.getCategory();
        MultiRoom room = MultiRoom.builder()
                .roomId(UUID.randomUUID().toString())
                .category(request.getCategory())
                .roomStatus(RoomStatus.PREPARING)
                .targetWordList(getDataWordList(WordType.TARGET, category, TARGET_AMOUNT))
                .subWordList(getDataWordList(WordType.SUB, category, AMOUNT))
                .levelWordList(addLevelWords(levelWordList, WordType.LEVEL, category, AMOUNT))
                .limit(4)
                .playerList(new ArrayList<>())
                .master(currentPlayer)
                .overPlayerCnt(0)
                .build();

        // 플레이어 방에 등록, 방 manager에 등록
        room.getPlayerList().add(currentPlayer);
        multiRoomManager.addRoom(room);
        log.info("room: {}", room);
        
        return new MultiGameConnectResponse(room, currentPlayer.getPlayerId());
    }


    // 멀티 게임 방 입장하기
    public MultiGameConnectResponse connectMultiGame(String roomId, MultiGameConnectRequest request) {

        // 입장 가능한 방인 지 확인
        MultiRoom room = findByRoomId(roomId);

        if(room.getRoomStatus().equals(RoomStatus.ONGOING) || room.getRoomStatus().equals(RoomStatus.FINISHED)) {
            // TODO 예외처리. 입장할 수 없는 방 상태
            log.info("입장할 수 없는 방입니다");
        }

        if(room.getPlayerList().size() >= room.getLimit()) {
            // TODO 예외처리. 입장할 수 없는 방 상태
            log.info("입장할 수 없는 방입니다");
        }

        // 플레이어 생성, 방에 등록
        MultiPlayer currentPlayer = initialMultiPlayer(request.getNickname(), PlayerStatus.UNREADY, false);
        log.info("player 생성 완료: {}", currentPlayer.toString());

        room.getPlayerList().add(currentPlayer);

        return new MultiGameConnectResponse(room, currentPlayer.getPlayerId());
    }


    // 멀티 게임 시작하기
    public MultiGameInfoResponse startMultiGame(String roomId, MultiGamePlayerRequest request) {

        // 시작 조건을 만족하는 지 확인
        MultiRoom room = findByRoomId(roomId);

        if(!room.getRoomStatus().equals(RoomStatus.PREPARED) || room.getPlayerList().size() < 2) {
            // TODO 방 상태, 방 인원 예외처리
            log.info("시작할 수 없는 방 상태");
        }
        
        if(!room.getMaster().getPlayerId().equals(request.getPlayerId())) {
            // TODO player의 방장 여부 확인
            log.info("방장이 아님");
        }

        // 모든 플레이어의 상태를 gaming으로 업데이트
        for(MultiPlayer player : room.getPlayerList()) {
            player.updateStatus(PlayerStatus.GAMING);
        }

        // 방 상태를 ongoing으로 업데이트, 시작 시간 업데이트
        room.updateStatus(RoomStatus.ONGOING);
        room.updateStartTime(new Timestamp(new Date().getTime()));
        log.info("Room: {}", room);

        // 시작 단어 리스트가 포함된 response DTO 생성
        MultiGameInfoResponse response = MultiGameInfoResponse.builder()
                .roomId(room.getRoomId())
                .roomStatus(room.getRoomStatus())
                .category(room.getCategory())
                .playerList(room.getPlayerList())
                .startWordList(new MultiGameInfoResponse.StartWordList(room))
                .build();

        return response;
    }


    // 단어 입력하여 유사도 확인하기
    public MultiGuessResponse sortByProximity(String roomId, MultiGuessRequest request) {

        String playerId = request.getPlayerId();
        List<String> currentWordList = request.getCurrentWordList();
        String guessWord = request.getGuessWord();
        String targetWord = request.getTargetWord();

        MultiRoom room = findByRoomId(roomId);
        MultiPlayer currentPlayer = findByPlayerId(room, playerId);
        int category = room.getCategory();


        // 유사도 조회
        String[][] sortedWordList = getSortedWordList(guessWord, currentWordList);
        log.info("sortedWordList {}", Arrays.deepToString(sortedWordList));


        // TODO 점수 계산, 삭제 후 전달해야 할 데이터 정리
        // Long score = 0L;
        // 서버의 player idx 정보 등도 업데이트 한다.

        List<String> newSubWordList = new ArrayList<>();


        // 단어가 부족한 경우, 추가 단어 요청

        // 레벨어 추가
        if(room.getLevelWordList().size() <= ADD_AMOUNT) {
            addLevelWords(room.getLevelWordList(), WordType.LEVEL, category, AMOUNT);
        }
        
        // 타겟어 추가
        if (room.getTargetWordList().size() - currentPlayer.getTargetWordIndex() <= ADD_AMOUNT) {
            addWords(room.getTargetWordList(), WordType.TARGET, category, TARGET_AMOUNT);
            log.info("TARGET wordList 단어 추가");
        }

        // 서브어 추가
        if (room.getSubWordList().size() - currentPlayer.getSubWordIndex() <= 100) {
            addWords(room.getSubWordList(), WordType.SUB, category, AMOUNT);
            log.info("SUB wordList 단어 추가");
        }

        MultiGuessResponse multiGuessResponse = MultiGuessResponse.builder()
                .playerId(playerId)
                .sortedWordList(sortedWordList)
                .newScore(100L)
                .newTargetWord("새 타겟 단어")
                .newSubWordList(newSubWordList)
                .build();

        return multiGuessResponse;
    }


    // 멀티 게임 최종 결과 계산
    public MultiGameResultResponse getGameResult(String roomId) {
        MultiRoom room = multiRoomManager.getRoom(roomId);

        // TODO 종료 시간 별 점수 보너스 지급

        MultiGameResultResponse response = MultiGameResultResponse.builder()
                .playerResultList(new ArrayList<>())
                .build();

        for(MultiPlayer player : room.getPlayerList()) {
            MultiGameResultResponse.PlayerResult playerResult = MultiGameResultResponse.PlayerResult.builder()
                    .playerId(player.getPlayerId())
                    .nickname(player.getNickname())
                    .score(player.getScore())
                    .build();

            response.getPlayerResultList().add(playerResult);
        }

        return response;
    }


    
    // data api 에서 단어 유사도 확인하기
    private String[][] getSortedWordList(String guessWord, List<String> currentWordList) {
        DataGuessWordRequest dataGuessWordRequest = DataGuessWordRequest.builder()
                .guessWord(guessWord)
                .currentWordList(currentWordList)
                .build();
        DataGuessWordResponse dataGuessWordResponse = dataServiceImpl.sendGuessWordRequest(dataGuessWordRequest);
        return dataGuessWordResponse.getData().getCalWordList();
    }


    // data api 에서 단어 리스트 불러오기
    public List<String> getDataWordList(WordType wordType, int category, int amount) {
        DataWordListRequest dataWordListRequest = DataWordListRequest.builder()
                .type(wordType)
                .category(category)
                .amount(amount)
                .build();
        DataWordListResponse dataWordListResponse = dataServiceImpl.sendWordListRequest(dataWordListRequest);
        return dataWordListResponse.getData().getWordList();
    }



    // ============= 공통 코드 =============

    // TODO 커스텀 예외 처리 필요
    private MultiRoom findByRoomId(String roomId) {
        return Optional.ofNullable(multiRoomManager.getRoom(roomId))
                .orElseThrow(() -> new RuntimeException("Room with ID " + roomId + " does not exist."));
    }


    private MultiPlayer findByPlayerId(MultiRoom room, String playerId) {
        return room.getPlayerList().stream()
                .filter(player -> playerId.equals(player.getPlayerId()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Player with ID " + playerId + " does not exist in the room."));
    }


    // 플레이어를 생성하는 메서드
    public MultiPlayer initialMultiPlayer(String nickname, PlayerStatus playerStatus, Boolean isMaster) {
        return MultiPlayer.builder()
                .playerId(UUID.randomUUID().toString())
                .playerStatus(playerStatus)
                .score(0L)
                .targetWordIndex(0)
                .subWordIndex(0)
                .nickname(nickname)
                .isMaster(isMaster)
                .overTime(null)
                .build();
    }


    // 플레이어 상태를 Ready로 변경하는 메서드
    public UpdatedPlayerResponse updatePlayerToReady(String roomId, String playerId) {
        MultiRoom room = multiRoomManager.getRoom(roomId);
        MultiPlayer updatedPlayer = findByPlayerId(room, playerId);

        updatedPlayer.updateStatus(PlayerStatus.READY);

        return new UpdatedPlayerResponse(playerId, PlayerStatus.READY);
    }


    // 플레이어 상태를 Over로 변경하는 메서드
    public UpdatedPlayerResponse updatePlayerToOver(String roomId, String playerId) {
        MultiRoom room = multiRoomManager.getRoom(roomId);
        MultiPlayer updatedPlayer = findByPlayerId(room, playerId);

        updatedPlayer.updateStatus(PlayerStatus.OVER);
        updatedPlayer.updateOverTime();
        room.updateOverPlayerCnt();

        UpdatedPlayerResponse response = UpdatedPlayerResponse.builder()
                .playerId(playerId)
                .playerStatus(PlayerStatus.OVER)
                .build();

        // 한 명 제외하고 모두 OVER 된 경우 game status update
        if(room.getOverPlayerCnt() == room.getPlayerList().size()-1) {
            room.updateStatus(RoomStatus.FINISHED);
            response.updateRoomStatus(RoomStatus.FINISHED);
        }
        return response;
    }


    // 레벨어를 추가하는 메서드
    private Queue<String> addLevelWords(Queue<String> levelWordList, WordType wordType, int category, int amount) {
        List<String> tempWordList = getDataWordList(wordType, category, amount);
        for(String word : tempWordList) {
            levelWordList.add(word);
        }
        return levelWordList;
    }

    // 타겟어, 서브어를 추가로 가져오는 메서드
    private List<String> addWords(List<String> originalWordList, WordType wordType, int category, int amount) {
        List<String> tempWordList = getDataWordList(wordType, category, amount);
        for(String word : tempWordList) {
            originalWordList.add(word);
        }
        return originalWordList;
    }


}
