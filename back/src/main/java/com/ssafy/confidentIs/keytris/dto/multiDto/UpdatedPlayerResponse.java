package com.ssafy.confidentIs.keytris.dto.multiDto;

import com.ssafy.confidentIs.keytris.model.PlayerStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UpdatedPlayerResponse {

    private String playerId;
    private PlayerStatus playerStatus;

}
