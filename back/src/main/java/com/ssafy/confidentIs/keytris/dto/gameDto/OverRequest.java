package com.ssafy.confidentIs.keytris.dto.gameDto;


import lombok.Getter;

@Getter
public class OverRequest {
    private String roomId;
    private String[][] lastWord;
    private Long score;
}
