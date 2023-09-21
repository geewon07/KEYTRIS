package com.ssafy.confidentIs.keytris.service;

import com.ssafy.confidentIs.keytris.model.Room;
import com.ssafy.confidentIs.keytris.repository.RoomManager;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SessionMethodService {

  private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
  private final SimpMessagingTemplate messagingTemplate;
  private final RoomManager roomManager;
//  private final RoomService roomService;
  //TODO: levelword 고갈 체크 방식 점검해야함
  private final Map<String, ScheduledFuture<?>> sessionTasks = new ConcurrentHashMap<>();

  public void startSessionMethod(String roomId) {
    if (!sessionTasks.containsKey(roomId) || sessionTasks.get(roomId).isDone()) {
      ScheduledFuture<?> scheduledFuture = executorService.scheduleAtFixedRate(
          () -> executeSessionMethod(roomId), 0, 2, TimeUnit.SECONDS);
      sessionTasks.put(roomId, scheduledFuture);
    }
  }

  public void stopSessionMethod(String roomId) {
    ScheduledFuture<?> scheduledFuture = sessionTasks.get(roomId);
    if (scheduledFuture != null) {
      scheduledFuture.cancel(true);
      sessionTasks.remove(roomId);
    }
  }

  private void executeSessionMethod(String roomId) {

    Room room = roomManager.getRoom(roomId);
    String nextWord = room.getLevelWord();
    messagingTemplate.convertAndSend("/topic/room/level-word/" + roomId, nextWord);
    System.out.println(nextWord + " :Session " + roomId + " method is running every 2 seconds...");
  }
}
