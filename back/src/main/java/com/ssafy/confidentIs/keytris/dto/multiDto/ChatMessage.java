package com.ssafy.confidentIs.keytris.dto.multiDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class ChatMessage {

    private String roomId;
    private String playerId;
    private String content;
    private String timestamp;

    public void updateTime(String timestamp) {
        this.timestamp = timestamp;
    }

}
