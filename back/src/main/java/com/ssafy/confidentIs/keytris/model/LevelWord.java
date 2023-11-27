package com.ssafy.confidentIs.keytris.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class LevelWord {
    private String roomId;
    private String levelWord;
}
