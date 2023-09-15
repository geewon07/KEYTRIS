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


    // 멀티 게임 방 만들기
    public MultiGameConnectResponse createMultiGame(MultiGameCreateRequest request) {

        int category = request.getCategory();

        // 메서드 분리 할 것
        Queue<String> levelWordList = new LinkedList<>();
        List<String> tempWordList = getDataWordList(WordType.LEVEL, category, AMOUNT);
        for(String word : tempWordList) {
            levelWordList.add(word);
        }

        MultiPlayer currentPlayer = initialMultiPlayer(request.getNickname(), PlayerStatus.READY, true);
        log.info("플레이어 생성 완료");
        
        MultiRoom room = MultiRoom.builder()
                .roomId(UUID.randomUUID().toString())
                .category(request.getCategory())
                .roomStatus(RoomStatus.PREPARING)
                .targetWordList(getDataWordList(WordType.TARGET, category, TARGET_AMOUNT))
                .subWordList(getDataWordList(WordType.SUB, category, AMOUNT))
                .levelWordList(levelWordList)
                .limit(4)
                .playerList(new ArrayList<>())
                .build();

        room.getPlayerList().add(currentPlayer);
        room.updateMaster(currentPlayer);

        log.info("roomId {}", room.getRoomId());
        log.info("TARGET {}", room.getTargetWordList().toString());
        log.info("SUB {}", room.getSubWordList().toString());
        log.info("LEVEL {}", room.getLevelWordList());
        log.info("limit {}", room.getLimit());
        log.info("playerList {}", room.getPlayerList().toString());

        multiRoomManager.addRoom(room);

        Collection<MultiRoom> allRooms = multiRoomManager.getAllRooms();
        for(MultiRoom r : allRooms) {
            System.out.println(r);
        }

        return new MultiGameConnectResponse(room, currentPlayer.getPlayerId());
    }


    /* 멀티 게임 방 입장하기
    - 접근 가능한 방인지 확인 (방 존재 여부, 방 상태, 제한 인원)
    - 플레이어 등록
     */
    public MultiGameConnectResponse connectMultiGame(String roomId, MultiGameConnectRequest request) {

        if (multiRoomManager.getRoom(roomId) == null) {
            // TODO 예외처리. 존재하지 않는 roomId
        }

        MultiRoom room = multiRoomManager.getRoom(roomId);

        if(room.getRoomStatus().equals(RoomStatus.ONGOING) || room.getRoomStatus().equals(RoomStatus.FINISHED)) {
            // TODO 예외처리. 입장할 수 없는 방 상태
        }

        if(room.getPlayerList().size() >= room.getLimit()) {
            // TODO 예외처리. 입장할 수 없는 방 상태
        }

        MultiPlayer currentPlayer = initialMultiPlayer(request.getNickname(), PlayerStatus.UNREADY, false);
        log.info("player: {}", currentPlayer.toString());

        room.getPlayerList().add(currentPlayer);
        log.info("room: {}", room);

        return new MultiGameConnectResponse(room, currentPlayer.getPlayerId());
    }



    public void startMultiGame(String roomId, MultiGamePlayerRequest request) {
        if (multiRoomManager.getRoom(roomId) == null) {
            // TODO 예외처리. 존재하지 않는 roomId
        }

        MultiRoom room = multiRoomManager.getRoom(roomId);

        if(!room.getRoomStatus().equals(RoomStatus.PREPARED) || room.getPlayerList().size() < 2) {
            System.out.println("시작할 수 없는 방 상태");
            // TODO 방 상태, 방 인원 예외처리
        }

        if(!room.getMaster().getPlayerId().equals(request.getPlayerId())) {
            // TODO player의 방장 여부 확인
            System.out.println("방장이 아님");
        }

        // 모든 플레이어의 상태를 gaming으로 업데이트
        for(MultiPlayer player : room.getPlayerList()) {
            player.updateStatus(PlayerStatus.GAMING);
        }

        // 방 상태를 ongoing으로 업데이트, 시작 시간 업데이트
        room.updateStatus(RoomStatus.ONGOING);
        room.updateStartTime(new Timestamp(new Date().getTime()));

        log.info("Room: {}", room);
    }




    public MultiGuessResponse sortByProximity(MultiGuessRequest request) {

        String roomId = request.getRoomId();
        String playerId = request.getPlayerId();
        List<String> currentWordList = request.getCurrentWordList();
        String guessWord = request.getGuessWord();
        String targetWord = request.getTargetWord();

        if (multiRoomManager.getRoom(roomId) == null) {
            // TODO 예외처리. 존재하지 않는 roomId
        }
        MultiRoom room = multiRoomManager.getRoom(roomId);

        MultiPlayer currentPlayer = ensurePlayerExists(room, playerId);


        //유사도 요청
        String[][] sortedWordList = getSortedWordList(guessWord, currentWordList);
        log.info("sortedWordList {}", Arrays.deepToString(sortedWordList));


        // TODO 점수 계산, 삭제 후 전달해야 할 데이터 정리
        // Long score = 0L;
        // 서버의 player idx 정보 등도 업데이트 한다.
        

        //단어가 부족한 경우, 추가 단어 요청
        WordType wordType = WordType.TARGET;
        Category category = Category.POLITICS;
        
        if(true) {  // TODO 어떤 단어가 부족한지 확인하는 조건으로 변경하기
            List<String> wordList = getDataWordList(wordType, category.getCode(), AMOUNT);
            log.info("type {} {} ", wordType, wordList.toString());
        }

        List<String> newSubWordList = new ArrayList<>();




        MultiGuessResponse multiGuessResponse = MultiGuessResponse.builder()
                .playerId(playerId)
                .sortedWordList(sortedWordList)
                .newScore(100L)
                .newTargetWord("새 타겟 단어")
                .newSubWordList(newSubWordList)
                .build();


        return multiGuessResponse;
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
    private List<String> getDataWordList(WordType wordType, int category, int amount) {
        DataWordListRequest dataWordListRequest = DataWordListRequest.builder()
                .type(wordType)
                .category(category)
                .amount(amount)
                .build();
        DataWordListResponse dataWordListResponse = dataServiceImpl.sendWordListRequest(dataWordListRequest);
        return dataWordListResponse.getData().getWordList();
    }


    // === 공통 코드 ===
    private MultiPlayer initialMultiPlayer(String nickname, PlayerStatus playerStatus, Boolean isMaster) {
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



    private MultiPlayer ensurePlayerExists(MultiRoom room, String playerId) {
        return room.getPlayerList().stream()
                .filter(player -> playerId.equals(player.getPlayerId()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Player with ID " + playerId + " does not exist in the room."));
        //TODO 예외 처리 필요
    }


    // 플레이어 상태를 Ready로 변경하는 메서드
    public UpdatedPlayerResponse updatePlayerToReady(String roomId, String playerId) {
        MultiRoom room = multiRoomManager.getRoom(roomId);
        MultiPlayer updatedPlayer = ensurePlayerExists(room, playerId);

        updatedPlayer.updateStatus(PlayerStatus.READY);

        return new UpdatedPlayerResponse(playerId, PlayerStatus.READY);
    }


    // 플레이어 상태를 Over로 변경하는 메서드
    public UpdatedPlayerResponse updatePlayerToOver(String roomId, String playerId) {
        MultiRoom room = multiRoomManager.getRoom(roomId);
        MultiPlayer updatedPlayer = ensurePlayerExists(room, playerId);

        updatedPlayer.updateStatus(PlayerStatus.OVER);

        // TODO 한 명 빼고 모두 OVER 된 경우 game status update


        return new UpdatedPlayerResponse(playerId, PlayerStatus.OVER);
    }
}
