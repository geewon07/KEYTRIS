package com.ssafy.confidentIs.keytris.dto;

import java.util.UUID;
import lombok.Getter;

@Getter
public class OverRequest {
  private UUID roomId;
  private String lastWord;
  private long score;
}
