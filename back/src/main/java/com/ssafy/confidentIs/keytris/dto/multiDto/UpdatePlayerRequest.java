package com.ssafy.confidentIs.keytris.dto.multiDto;

import com.ssafy.confidentIs.keytris.model.PlayerStatus;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UpdatePlayerRequest {

    private String playerId;
    private PlayerStatus newStatus;

}
