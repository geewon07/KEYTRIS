package com.ssafy.confidentIs.keytris.dto.multiDto;

import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@ToString
public class MultiGameConnectRequest {

    @NotNull(message = "닉네임은 필수 값입니다.")
    @Size(max = 15, message = "닉네임은 최대 15byte 까지 입력 가능합니다.")
    private String nickname;

    @NotNull(message = "게임 코드는 필수 값입니다.")
    private String roomId;

}
