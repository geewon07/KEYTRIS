package com.ssafy.confidentIs.keytris.service;

import com.ssafy.confidentIs.keytris.model.Player;
import com.ssafy.confidentIs.keytris.model.PlayerStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PlayerServiceImpl implements PlayerService {

  private final WordService wordService;
  @Override
  public Player intialPlayer() {

    return Player.builder()
        .playerId("testerPlayer")
        .playerStatus(PlayerStatus.READY)
        .score(0)
        .streak(0)
        .build();
  }
}
