package com.ssafy.confidentIs.keytris.dto.multiDto;

import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@ToString
public class MultiGamePlayerRequest {

    @NotNull(message = "플레이어 id는 필수 값입니다.")
    private String playerId;

}
