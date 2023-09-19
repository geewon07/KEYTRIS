package com.ssafy.confidentIs.keytris.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.redis.core.ZSetOperations;
@ToString
@Getter
@Builder
public class RankingResponse {
  private String nickname;
  private Long score;

  public static RankingResponse convert(ZSetOperations.TypedTuple<String> tuple){
    return RankingResponse.builder()
        .nickname(tuple.getValue().split(":")[0])
        .score(tuple.getScore().longValue())
        .build();
  }
}
