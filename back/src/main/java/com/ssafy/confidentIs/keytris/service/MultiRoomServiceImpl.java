package com.ssafy.confidentIs.keytris.service;


import com.ssafy.confidentIs.keytris.common.exception.ErrorCode;
import com.ssafy.confidentIs.keytris.common.exception.customException.InvalidWordException;
import com.ssafy.confidentIs.keytris.dto.dataDto.DataGuessWordResponse;
import com.ssafy.confidentIs.keytris.dto.multiDto.*;
import com.ssafy.confidentIs.keytris.model.PlayerStatus;
import com.ssafy.confidentIs.keytris.model.RoomStatus;
import com.ssafy.confidentIs.keytris.model.RoomType;
import com.ssafy.confidentIs.keytris.model.WordType;
import com.ssafy.confidentIs.keytris.model.multiModel.MultiPlayer;
import com.ssafy.confidentIs.keytris.model.multiModel.MultiRoom;
import com.ssafy.confidentIs.keytris.repository.MultiRoomManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MultiRoomServiceImpl {

    private final DataServiceImpl dataServiceImpl;

    private final MultiRoomManager multiRoomManager;

    private final SimpMessagingTemplate messagingTemplate;

    private final SessionMethodService sessionMethodService;

    private static final int TARGET_AMOUNT = 5; // TARGET 단어 받아오는 단위
    private static final int SUB_AMOUNT = 15; // SUB 단어 받어오는 단위
    private static final int LEVEL_AMOUNT = 10; // LEVEL 단어 받아오는 단위
    private static final int TARGET_ADD_STANDARD = 3; // TARGET 단어를 추가로 받아오는 기준
    private static final int SUB_ADD_STANDARD = 5; // SUB 단어를 추가로 받아오는 기준


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
                .targetWordList(dataServiceImpl.getDataWordList(WordType.TARGET, category, TARGET_AMOUNT))
                .subWordList(dataServiceImpl.getDataWordList(WordType.SUB, category, SUB_AMOUNT))
                .levelWordList(dataServiceImpl.addLevelWords(levelWordList, WordType.LEVEL, category, LEVEL_AMOUNT))
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

        // 2초마다 레벨어 보내주기 시작
        sessionMethodService.startSessionMethod(roomId, RoomType.MULTI);

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
        DataGuessWordResponse dataGuessWordResponse = dataServiceImpl.getWordGuessResult(guessWord, currentWordList);
        log.info("dataGuessWordResponse: {}", dataGuessWordResponse);
        if(dataGuessWordResponse.getSuccess().equals("fail")) {
            // TODO 입력 할 수 없는 단어 커스텀 예외처리.
            log.info("입력할 수 없는 단어입니다.");
            return MultiGuessResponse.builder()
                    .success("fail")
                    .build();
        }
        String[][] sortedWordList = dataGuessWordResponse.getData().getCalWordList();
        log.info("sortedWordList {}", Arrays.deepToString(sortedWordList));


        // == 유사도 순위에 따라 점수 계산, 새 단어 정리 ==

        // currentList 의 개수
        int currentListSize = request.getCurrentWordList().size();


        // 타겟어의 유사도 순위 계산
        int targetWordRank = -1;
        for(int i=0; i<sortedWordList.length; i++) {
            if(sortedWordList[i][0].equals(targetWord)) {
                targetWordRank = i;
                break;
            }
        }
        log.info("targetWordRank: {}", targetWordRank);

        List<String> newSubWordList = new ArrayList<>();
        String newTargetWord = null;

        // 타겟어 유사도가 순위권 내인 경우
        if (0 <= targetWordRank && targetWordRank < 4) {
            // 플레이어 점수, 단어 idx 업데이트 및 새롭게 클라이언트에 전달할 단어 추출
            newSubWordList = updatePlayerBasedOnRank(targetWordRank, currentPlayer, room, currentListSize);
            newTargetWord = room.getTargetWordList().get(currentPlayer.getTargetWordIndex());

            // Room의 여분 타겟어, 서브어가 부족한 경우, 추가 단어 요청
            checkAndAddWords(currentPlayer, room, category);
        }

        MultiGuessResponse multiGuessResponse = MultiGuessResponse.builder()
                .success("success")
                .playerId(playerId)
                .sortedWordList(sortedWordList)
                .newScore(currentPlayer.getScore())
                .newTargetWord(newTargetWord)   // 유사도 순위 낮은 경우, null 반환
                .newSubWordList(newSubWordList) // 유사도 순위 낮은 경우, 빈 리스트 반환
                .build();

        log.info("multiGuessResponse: {}", multiGuessResponse);

        return multiGuessResponse;
    }


    // 단어 입력 유사도 확인 -> 타겟어 유사도 순위가 높은 경우 점수, 단어 인덱스 갱신 후 newSubWordList를 반환하는 메서드
    private List<String> updatePlayerBasedOnRank(int targetWordRank, MultiPlayer currentPlayer, MultiRoom room, int currentListSize) {
        int[] scoreUpdates = {55, 45, 35, 25};
        int[] deletedSubWordCnt = {3, 2, 1, 0};

        // 점수 업데이트
        currentPlayer.updateScore(currentPlayer.getScore() + scoreUpdates[targetWordRank]);

        int listSize = currentListSize - deletedSubWordCnt[targetWordRank];
        List<String> newSubWordList = new ArrayList<>();

        // 삭제 후 남은 서브어 개수가 10개 미만인 경우 서브어 돌려줌
        if(listSize < 10) {
            int subIdx = Math.min((10-listSize), deletedSubWordCnt[targetWordRank]); // 서브어 총 개수 9개에 맞춤
            newSubWordList = room.getSubWordList().subList(currentPlayer.getSubWordIndex()+1, currentPlayer.getSubWordIndex()+1+subIdx);
            currentPlayer.updateIndex(currentPlayer.getSubWordIndex()+subIdx, currentPlayer.getTargetWordIndex()+1);
        } else { // 10개 이상인 경우 서브어 돌려주지 않음
            currentPlayer.updateIndex(currentPlayer.getSubWordIndex(), currentPlayer.getTargetWordIndex()+1);
        }

        log.info("단어 유사도 순위로 플레이어 정보 업데이트 완료: {}", currentPlayer);
        return newSubWordList;
    }
    
    // 단어 입력 유사도 확인 -> 타겟어 유사도 순위가 높은 경우, 여분 타겟어, 서브어가 부족한 지 확인 후 추가하는 메서드
    private void checkAndAddWords(MultiPlayer currentPlayer, MultiRoom room, int category) {
        // 타겟어 추가
        if (room.getTargetWordList().size() - currentPlayer.getTargetWordIndex() <= TARGET_ADD_STANDARD) {
            refillWords(room.getTargetWordList(), WordType.TARGET, category, TARGET_AMOUNT);
            log.info("TARGET wordList 단어 추가");
        }

        // 서브어 추가
        if (room.getSubWordList().size() - currentPlayer.getSubWordIndex() <= SUB_ADD_STANDARD) {
            refillWords(room.getSubWordList(), WordType.SUB, category, SUB_AMOUNT);
            log.info("SUB wordList 단어 추가");
        }
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



    // 플레이어 상태를 Ready로 변경하는 메서드
    public UpdatedPlayerResponse updatePlayerToReady(String roomId, String playerId) {
        MultiRoom room = multiRoomManager.getRoom(roomId);
        MultiPlayer updatedPlayer = findByPlayerId(room, playerId);

        updatedPlayer.updateStatus(PlayerStatus.READY);

        // 모든 플레이어가 Ready 상태이면 방 상태 Prepared로 변경
        Boolean isAllReady = true;
        for(MultiPlayer player : room.getPlayerList()) {
            if(!player.getPlayerStatus().equals(PlayerStatus.READY)) {
                isAllReady = false;
                break;
            }
        }
        if(isAllReady) room.updateStatus(RoomStatus.PREPARED);

        return new UpdatedPlayerResponse(playerId, PlayerStatus.READY, room.getRoomStatus());
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
                .roomStatus(room.getRoomStatus())
                .build();

        // 한 명 제외하고 모두 OVER 된 경우 game status update
        if(room.getOverPlayerCnt() == room.getPlayerList().size()-1) {
            room.updateStatus(RoomStatus.FINISHED);
            response.updateRoomStatus(RoomStatus.FINISHED);
        }

        // 레벨어 전송 중단
        sessionMethodService.stopSessionMethod(roomId);

        return response;
    }

    



    // ========================== 공통 코드 ==========================

    // 타겟어, 서브어를 추가로 가져오는 메서드
    private List<String> refillWords(List<String> originalWordList, WordType wordType, int category, int amount) {
        List<String> tempWordList = dataServiceImpl.getDataWordList(wordType, category, amount);
        for(String word : tempWordList) {
            originalWordList.add(word);
        }
        return originalWordList;
    }


    // roomId로 Room을 반환하는 메서드. TODO 커스텀 예외 처리 필요
    private MultiRoom findByRoomId(String roomId) {
        return Optional.ofNullable(multiRoomManager.getRoom(roomId))
                .orElseThrow(() -> new RuntimeException("Room with ID " + roomId + " does not exist."));
    }

    // playerId로 Player를 반환하는 메서드. TODO 커스텀 예외 처리 필요
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
                .subWordIndex(9)
                .nickname(nickname)
                .isMaster(isMaster)
                .overTime(null)
                .build();
    }


}
