package com.ssafy.confidentIs.keytris.controller;

import com.ssafy.confidentIs.keytris.dto.StatusResponse;
import com.ssafy.confidentIs.keytris.model.Room;
import com.ssafy.confidentIs.keytris.model.SinglePlayer;
import com.ssafy.confidentIs.keytris.service.RoomManager;
import com.ssafy.confidentIs.keytris.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j // 로그 찍기
@RequiredArgsConstructor // private final 타입 의존성 주입을 자동으로 해줌
public class SocketController {
    private final SimpMessagingTemplate messagingTemplate;
    private final RoomService roomService;

    @MessageMapping("/keytris/room/enter/{roomId}")
    public void enterRoom(@DestinationVariable String roomId){//@PathVariable String enterWord

        messagingTemplate.convertAndSend("/from/room/enter/"+roomId,roomService.enterRoom(roomId));
    }



//    @PutMapping("/{enterWord}")
//    public ResponseEntity<?> exchange(@PathVariable String enterWord, @RequestBody String[] words){
//        System.out.println("get called "+enterWord);
//        String[] s = {"하잉","언니"};
//        Arrays.sort(words);
//        return ResponseEntity.ok(words);
//    }
}
