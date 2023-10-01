package com.ssafy.confidentIs.keytris.dto.multiGameDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
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
