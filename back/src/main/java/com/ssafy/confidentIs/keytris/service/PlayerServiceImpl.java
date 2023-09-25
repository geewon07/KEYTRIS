package com.ssafy.confidentIs.keytris.service;

import com.ssafy.confidentIs.keytris.model.BasePlayer;
import com.ssafy.confidentIs.keytris.model.MultiPlayer;
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
        .targetWordIndex(0)
        .subWordIndex(0)
        .build();
  }

  public BasePlayer testBasePlayer() {
    return BasePlayer.builder()
            .playerId(UUID.randomUUID().toString())
            .playerStatus(PlayerStatus.UNREADY)
            .score(0L)
            .targetWordIndex(0)
            .subWordIndex(0)
            .build();
  }

  public MultiPlayer testMultiPlayer() {
    return MultiPlayer.builder()
            .playerId(UUID.randomUUID().toString())
            .playerStatus(PlayerStatus.UNREADY)
            .score(0L)
            .targetWordIndex(0)
            .subWordIndex(0)
            .nickname("멀티플레이어 닉네임")
            .isMaster(true)
            .build();
  }

}
