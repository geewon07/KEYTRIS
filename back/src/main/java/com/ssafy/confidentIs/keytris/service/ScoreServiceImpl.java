package com.ssafy.confidentIs.keytris.service;

import com.ssafy.confidentIs.keytris.dto.gameDto.RankingResponse;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.ssafy.confidentIs.keytris.repository.RoomManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScoreServiceImpl implements ScoreService {

  private final RedisTemplate redisTemplate;
  private final RoomManager roomManager;
  private final String KEY = "ranking";

  public void setRanking() {
//    redisTemplate.opsForZSet().add("ranking", "EXPERT", 5000);
//    redisTemplate.opsForZSet().add("ranking", "진짜", 4000);
//    redisTemplate.opsForZSet().add("ranking", "고수", 3000);
//    redisTemplate.opsForZSet().add("ranking", "중수", 2000);
//    redisTemplate.opsForZSet().add("ranking", "초보", 1000);
    for(int i=0;i<5;i++){
      boolean added = redisTemplate.opsForZSet().add(KEY, ":"+i, 0);
      log.info("added initial ranks:{}",added);
    }
  }

  public List<RankingResponse> getRanking(int limit) {
    ZSetOperations<String, String> stringStringZSetOperations = redisTemplate.opsForZSet();
    Set<TypedTuple<String>> typedTuples = stringStringZSetOperations.reverseRangeWithScores(KEY, 0,
        limit);
    if(typedTuples.isEmpty()|| typedTuples ==null){
      log.warn("Ranking data is unavailable. Resetting the ranking.");
      setRanking();
      typedTuples = stringStringZSetOperations.reverseRangeWithScores(KEY, 0,
          limit);
    }
    log.info("get ranking result :{}", typedTuples.toString());
    List<RankingResponse> toList = typedTuples.stream().map(RankingResponse::convert).collect(
        Collectors.toList());
    log.info("convert rank to list :{}", toList);
    return toList;
  }
  @Scheduled(cron = "0 0 9 * * ?")
  public void resetRanking(){
    boolean isDelete = redisTemplate.delete(KEY);
    log.info("deleted ranks:{}",isDelete);
    setRanking();
  }

  public List<RankingResponse> addHighscore(String nickname, String roomId) {
    Long score = roomManager.getRoom(roomId).getPlayerList().get(0).getScore();
    nickname = nickname+":"+roomManager.getRoom(roomId).getPlayerList().get(0).getPlayerId();
    redisTemplate.opsForZSet().add(KEY, nickname, score);
    return getRanking(4);
  }

}
