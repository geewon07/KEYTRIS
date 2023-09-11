package com.ssafy.confidentIs.keytris.service;

import com.ssafy.confidentIs.keytris.model.Player;
import com.ssafy.confidentIs.keytris.model.PlayerStatus;
import java.util.List;


public interface PlayerService {

  Player initialPlayer();

  Player updatePlayerWords(Player player, List<String> subWordList, List<String> targetWordList,
      List<String> currentWordList);

  Player updatePlayerIndex(Player player, int subWordIndex, int targetWordIndex);

  Player updatePlayerStatus(Player player, String pStatus);


}
