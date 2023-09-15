package com.ssafy.confidentIs.keytris.service;

import com.ssafy.confidentIs.keytris.model.SinglePlayer;
import com.ssafy.confidentIs.keytris.model.PlayerStatus;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PlayerServiceImpl implements PlayerService {
  @Override
  public SinglePlayer initialPlayer() {
    return SinglePlayer.builder()
        .playerId(UUID.randomUUID().toString())
        .playerStatus(PlayerStatus.UNREADY)
        .score(0L)
        .build();
  }

}
