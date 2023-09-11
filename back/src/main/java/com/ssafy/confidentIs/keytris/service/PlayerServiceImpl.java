package com.ssafy.confidentIs.keytris.service;

import com.ssafy.confidentIs.keytris.model.Player;
import com.ssafy.confidentIs.keytris.model.PlayerStatus;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PlayerServiceImpl implements PlayerService {

  private final WordService wordService;

  @Override
  public Player initialPlayer() {

    return Player.builder()
        .playerId("testerPlayer")
        .playerStatus(PlayerStatus.READY)
        .score(0)
        .streak(0)
        .build();
  }

  @Override
  public Player updatePlayerWords(Player player, List<String> subWordList,
      List<String> targetWordList, List<String> currentWordList) {
    player.updateWords(player.getSubWordList(), player.getTargetWord(),
        player.getCurrentWordList());
    return player;
  }

  @Override
  public Player updatePlayerIndex(Player player, int subWordIndex, int targetWordIndex) {
    player.updateIndex(player.getSubWordIndex(), player.getTargetWordIndex());
    return player;
  }

  @Override
  public Player updatePlayerStatus(Player player, String pStatus) {
    player.updateStatus(PlayerStatus.valueOf(pStatus));
    return player;
  }


}
