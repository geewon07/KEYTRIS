package com.ssafy.confidentIs.keytris.controller;

import com.ssafy.confidentIs.keytris.model.PlayerStatus;
import com.ssafy.confidentIs.keytris.model.RoomStatus;
import com.ssafy.confidentIs.keytris.model.WordType;
import com.ssafy.confidentIs.keytris.model.multiModel.MultiPlayer;
import com.ssafy.confidentIs.keytris.model.multiModel.MultiRoom;
import com.ssafy.confidentIs.keytris.repository.MultiRoomManager;
import com.ssafy.confidentIs.keytris.service.MultiRoomServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/test")
@Slf4j // 로그 찍기
@RequiredArgsConstructor // private final 타입 의존성 주입을 자동으로 해줌
public class TestController {

    private final MultiRoomManager multiRoomManager;
    private final MultiRoomServiceImpl multiRoomServiceImpl;

    @GetMapping
    public ResponseEntity<?> test(){//@PathVariable String enterWord
        String[] s = {"하잉","언니"};
        return ResponseEntity.ok(s);
    }

//    @PutMapping("/{enterWord}")
//    public ResponseEntity<?> exchange(@PathVariable String enterWord, @RequestBody String[] words){
//        System.out.println("get called "+enterWord);
//        String[] s = {"하잉","언니"};
//        Arrays.sort(words);
//        return ResponseEntity.ok(words);
//    }

    
    // 테스트용 더미데이터 생성
    @PostMapping("/insertData")
    public ResponseEntity<?> dummyDataInsert() {

        int category = 100;
        int TARGET_AMOUNT = 5; // TARGET 단어 받아오는 단위
        int AMOUNT = 10; // SUB, LEVEL 단어 받어오는 단위

        Queue<String> levelWordList = new LinkedList<>();
        List<String> tempWordList = multiRoomServiceImpl.getDataWordList(WordType.LEVEL, category, AMOUNT);
        for(String word : tempWordList) {
            levelWordList.add(word);
        }

        MultiRoom room = MultiRoom.builder()
                .roomId("tempRoomId")
                .category(category)
                .roomStatus(RoomStatus.PREPARING)
                .targetWordList(multiRoomServiceImpl.getDataWordList(WordType.TARGET, category, TARGET_AMOUNT))
                .subWordList(multiRoomServiceImpl.getDataWordList(WordType.SUB, category, AMOUNT))
                .levelWordList(levelWordList)
                .limit(4)
                .playerList(new ArrayList<>())
                .overPlayerCnt(0)
                .build();

        for(int i=0; i<4; i++) {
            MultiPlayer currentPlayer = multiRoomServiceImpl.initialMultiPlayer("nickname" + i, PlayerStatus.READY, true);
            room.getPlayerList().add(currentPlayer);
        }

        multiRoomManager.addRoom(room);

        return new ResponseEntity<>(room, HttpStatus.OK);
    }


    // room 조회
    @GetMapping("/multigames/{roomId}")
    public ResponseEntity<?> findById(@PathVariable String roomId) {
        MultiRoom room = multiRoomManager.getRoom(roomId);
        log.info("room {}: ", room);
        return new ResponseEntity<>(room, HttpStatus.OK);
    }


}
