package com.ssafy.confidentIs.keytris.common.exception.customException;

import com.ssafy.confidentIs.keytris.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public abstract class SocketException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String playerId;
    private final String roomId;

    public SocketException(String message, ErrorCode errorCode, String playerId, String roomId) {
        super(message);
        this.errorCode = errorCode;
        this.playerId = playerId;
        this.roomId = roomId;
    }

}
