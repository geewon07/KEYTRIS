package com.ssafy.confidentIs.keytris.service;

import com.ssafy.confidentIs.keytris.dto.RankingResponse;
import java.util.List;

public interface ScoreService {
  void setRanking();
  void resetRanking();
  List<RankingResponse> getRanking(int limit);
  List<RankingResponse> addHighscore(String nickname, String roomId);

}
