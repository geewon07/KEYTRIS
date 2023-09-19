package com.ssafy.confidentIs.keytris.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
public class LevelWord {
  private String roomId;
  //private String playerId;
  private String levelWord;
}
