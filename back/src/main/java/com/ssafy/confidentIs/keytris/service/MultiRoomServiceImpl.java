package com.ssafy.confidentIs.keytris.service;


import com.ssafy.confidentIs.keytris.common.exception.ErrorCode;
import com.ssafy.confidentIs.keytris.common.exception.customException.*;
import com.ssafy.confidentIs.keytris.dto.WordListResponse;
import com.ssafy.confidentIs.keytris.dto.dataDto.DataGuessWordResponse;
import com.ssafy.confidentIs.keytris.dto.multiDto.*;
import com.ssafy.confidentIs.keytris.model.*;
import com.ssafy.confidentIs.keytris.repository.MultiRoomManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class MultiRoomServiceImpl {

    private final DataServiceImpl dataServiceImpl;

    private final MultiRoomManager multiRoomManager;

    private final CommonRoomServiceImpl commonRoomServiceImpl;

    private final SessionMethodService sessionMethodService;

    private static final int TARGET_AMOUNT = 5; // TARGET 단어 받아오는 단위
    private static final int SUB_AMOUNT = 15; // SUB 단어 받어오는 단위
    private static final int LEVEL_AMOUNT = 10; // LEVEL 단어 받아오는 단위
    private static final int TARGET_ADD_STANDARD = 3; // TARGET 단어를 추가로 받아오는 기준
    private static final int SUB_ADD_STANDARD = 5; // SUB 단어를 추가로 받아오는 기준
    private static final String SUCCESS = "success";


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
            throw new InaccessibleGameException("입장할 수 없는 방입니다.", ErrorCode.INACCESSIBLE_GAME);
        }

        if(room.getPlayerList().size() >= room.getLimit()) {
            throw new InaccessibleGameException("입장할 수 없는 방입니다.", ErrorCode.INACCESSIBLE_GAME);
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

        // 방 상태, 방 인원이 시작할 수 없는 경우
        if(!room.getRoomStatus().equals(RoomStatus.PREPARED) || room.getPlayerList().size() < 2) {
            throw new SocketInvalidStartException("시작할 수 없는 방 상태", ErrorCode.SOCKET_INVALID_START, request.getPlayerId(), roomId);
        }

        // 방장이 아닌 경우
        if(!room.getMaster().getPlayerId().equals(request.getPlayerId())) {
            throw new SocketNotAuthorizedException("방장이 아니어서 시작할 수 없음", ErrorCode.SOCKET_NOT_AUTHORIZED, request.getPlayerId(), roomId);
        }

        // 모든 플레이어의 상태를 gaming으로 업데이트
        for(MultiPlayer player : room.getPlayerList()) {
            player.updateStatus(PlayerStatus.GAMING);
        }

        // 방 상태를 ongoing으로 업데이트, 시작 시간 업데이트
        room.updateStatus(RoomStatus.ONGOING);
        room.updateStartTime(new Timestamp(new Date().getTime()));
        log.info("Room: {}", room);

        // 이차원 배열로 반환하기 위해 전환
        String[][] sortedWordList = IntStream.range(0, 10)
                .mapToObj(i -> new String[] {room.getSubWordList().get(i), ""})
                .toArray(String[][]::new);
        sortedWordList[9][0] = room.getTargetWordList().get(0);

        String[][] newTargetWord = new String[1][2];
        newTargetWord[0][0] = room.getTargetWordList().get(0);
        newTargetWord[0][1] = "";

        WordListResponse wordListResponse = WordListResponse.builder()
                .success("success")
                .playerId("master")
                .newScore(0L)
                .sortedWordList(sortedWordList)
                .newSubWordList(null)
                .newTargetWord(newTargetWord)
                .targetWordRank(9)
                .sortedIndex(null)
                .build();

        log.info("wordListResponse: {}", wordListResponse);

        // 시작 단어 리스트가 포함된 response DTO 생성
        MultiGameInfoResponse response = MultiGameInfoResponse.builder()
                .roomId(room.getRoomId())
                .roomStatus(room.getRoomStatus())
                .category(room.getCategory())
                .playerList(room.getPlayerList())
                .wordListResponse(wordListResponse)
                .build();

        // 2초마다 레벨어 보내주기 시작
        sessionMethodService.startSessionMethod(roomId, RoomType.MULTI);

        return response;
    }


    // 단어 입력하여 유사도 확인하기
    public WordListResponse sortByProximity(String roomId, MultiGuessRequest request) {
        String playerId = request.getPlayerId();
        MultiRoom room = findByRoomId(roomId);
        MultiPlayer currentPlayer = findByPlayerId(room, playerId);
        int category = room.getCategory();

        String guessWord = request.getGuessWord();
        String[][] newTargetWord = request.getTargetWord();
        String target = newTargetWord[0][0];
        String[][] currentWordList = request.getCurrentWordList();

        // 단어 유사도 받아오기
        List<String> wordOnlyList = Arrays.stream(currentWordList)
                .map(row -> row[0])
                .collect(Collectors.toList());

        DataGuessWordResponse dataGuessWordResponse = dataServiceImpl.getWordGuessResult(guessWord, wordOnlyList);
        log.info("dataGuessWordResponse: {}", dataGuessWordResponse);

        if(dataGuessWordResponse.getSuccess().equals("fail")) {
            // TODO 입력 할 수 없는 단어 커스텀 예외처리.
            log.info("입력할 수 없는 단어입니다.");
            return WordListResponse.builder()
                    .success("fail")
                    .build();
        }

        String[][] sortedWordList = dataGuessWordResponse.getData().getCalWordList();
        log.info("sortedWordList {}", Arrays.deepToString(sortedWordList));

        // 단어가 정렬 후 어느 위치로 이동했는 지 계산
        int[] sortedIndex = commonRoomServiceImpl.calculateSortedIndex(sortedWordList, currentWordList);


        // == 유사도 순위에 따라 점수 계산, 새 단어 정리 ==

        // 타겟어의 유사도 순위 계산
        int targetWordRank = commonRoomServiceImpl.calculateTargetWordRank(sortedWordList, target);

        List<String> subWordList = new ArrayList<>();

        // 타겟어 유사도가 순위권 내인 경우
        if (0 <= targetWordRank && targetWordRank < 4) {
            // 플레이어 점수, 단어 idx 업데이트 및 새롭게 클라이언트에 전달할 단어 추출
            subWordList = updatePlayerBasedOnRank(targetWordRank, currentPlayer, room, currentWordList.length);
            target = room.getTargetWordList().get(currentPlayer.getTargetWordIndex());

            // Room의 여분 타겟어, 서브어가 부족한 경우, 추가 단어 요청
            checkAndAddWords(currentPlayer, room, category);
        }
        log.info("subWordList: {}", subWordList);

        // 이차원 배열로 반환하기 위해 전환
        String[][] newSubWordList = commonRoomServiceImpl.convertTo2DArray(subWordList);

        newTargetWord[0][0] = target;
        newTargetWord[0][1] = "";

        WordListResponse wordListResponse = WordListResponse.builder()
                .success("success")
                .playerId(playerId)
                .newScore(currentPlayer.getScore())
                .sortedWordList(sortedWordList)
                .newSubWordList(newSubWordList)
                .newTargetWord(newTargetWord)
                .targetWordRank(targetWordRank)
                .sortedIndex(sortedIndex)
                .build();

        log.info("wordListResponse: {}", wordListResponse);

        return wordListResponse;
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
            newSubWordList = new ArrayList<>(room.getSubWordList().subList(currentPlayer.getSubWordIndex()+1, currentPlayer.getSubWordIndex()+1+subIdx));
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

            // 레벨어 전송 중단
            sessionMethodService.stopSessionMethod(roomId);
        }

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
                .orElseThrow(() -> new RoomNotFoundException("Room with ID " + roomId + " does not exist.", ErrorCode.ROOM_NOT_FOUND));
    }

    // playerId로 Player를 반환하는 메서드. TODO 커스텀 예외 처리 필요
    private MultiPlayer findByPlayerId(MultiRoom room, String playerId) {
        return room.getPlayerList().stream()
                .filter(player -> playerId.equals(player.getPlayerId()))
                .findFirst()
                .orElseThrow(() -> new PlayerNotFoundException("Player with ID " + playerId + " does not exist in the room.",  ErrorCode.PLAYER_NOT_FOUND));
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
