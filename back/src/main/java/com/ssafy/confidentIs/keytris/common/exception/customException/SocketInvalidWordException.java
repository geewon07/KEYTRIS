package com.ssafy.confidentIs.keytris.common.exception.customException;

import com.ssafy.confidentIs.keytris.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class SocketInvalidWordException extends SocketException {

    public SocketInvalidWordException(String message, ErrorCode errorCode, String playerId, String roomId) {
        super(message, errorCode, playerId, roomId);
    }
}
