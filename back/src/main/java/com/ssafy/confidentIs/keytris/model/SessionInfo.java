package com.ssafy.confidentIs.keytris.model;

import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SessionInfo {

    private String roomType;
    private String roomId;
    private String playerId;

}
