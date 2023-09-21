package com.ssafy.confidentIs.keytris.controller;

import com.ssafy.confidentIs.keytris.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

  @MessageMapping("/games/room/{roomId}")//{roomId}
  public void enterRoom(@DestinationVariable String roomId) {//@DestinationVariable String roomId
    log.info("when does it call {}", roomId);
    messagingTemplate.convertAndSend("/topic/games/room/" + roomId, roomService.enterRoom(roomId));
  }
}
