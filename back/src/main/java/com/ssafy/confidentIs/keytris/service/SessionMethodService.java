package com.ssafy.confidentIs.keytris.service;

import com.ssafy.confidentIs.keytris.model.BaseRoom;
import com.ssafy.confidentIs.keytris.model.RoomType;
import com.ssafy.confidentIs.keytris.model.WordType;
import com.ssafy.confidentIs.keytris.repository.MultiRoomManager;
import com.ssafy.confidentIs.keytris.repository.RoomManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class SessionMethodService {

  private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
  private final SimpMessagingTemplate messagingTemplate;
  private final RoomManager roomManager;
  private final MultiRoomManager multiRoomManager;
  private final DataServiceImpl dataServiceImpl;

  //TODO: levelword 고갈 체크 방식 점검해야함

  private static final int LEVEL_ADD_STANDARD = 5;
  private static final int LEVEL_AMOUNT = 10; // LEVEL 단어 받아오는 단위

  private final Map<String, ScheduledFuture<?>> sessionTasks = new ConcurrentHashMap<>();

  public void startSessionMethod(String roomId, RoomType roomType) {
    if (!sessionTasks.containsKey(roomId) || sessionTasks.get(roomId).isDone()) {
      ScheduledFuture<?> scheduledFuture = executorService.scheduleAtFixedRate(
          () -> executeSessionMethod(roomId, roomType), 10, 2, TimeUnit.SECONDS);
      sessionTasks.put(roomId, scheduledFuture);
    }
  }

  public void stopSessionMethod(String roomId) {
    ScheduledFuture<?> scheduledFuture = sessionTasks.get(roomId);
    if (scheduledFuture != null) {
      scheduledFuture.cancel(true);
      sessionTasks.remove(roomId);
      log.info("레벨어 전송 중단");
    }
  }

  private void executeSessionMethod(String roomId, RoomType roomType) {
    BaseRoom room;
    String nextWord;

    if(roomType.equals(RoomType.SINGLE)) {
      room = roomManager.getRoom(roomId);
      nextWord = room.getLevelWord();
      messagingTemplate.convertAndSend("/topic/room/level-word/" + roomId, nextWord);
      // TODO : (임시) 시작한지 특정 시간이 지나면 멈추기로 함. if(room.getStartTime() )
    } else {
      room = multiRoomManager.getRoom(roomId);
      nextWord = room.getLevelWord();
      messagingTemplate.convertAndSend("/topic/multi/level-word/" + roomId, nextWord);
    }

    if(room.getLevelWordList().size() < LEVEL_ADD_STANDARD) {
//      log.info("레벨어 부족으로 추가. 추가 전: {}", room.getLevelWordList());
      dataServiceImpl.addLevelWords(room.getLevelWordList(), WordType.LEVEL, room.getCategory(), LEVEL_AMOUNT);
//      log.info("레벨어 부족으로 추가. 추가 후: {}", room.getLevelWordList());
    }

//    log.info(nextWord + " :Session " + roomId + " method is running every 2 seconds...");
  }



}
