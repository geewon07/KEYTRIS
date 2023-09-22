package com.ssafy.confidentIs.keytris.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    NOT_FOUND(404,"CM01-ERR-404","PAGE NOT FOUND"),
    INTERNAL_SERVER_ERROR(500,"CMO2-ERR-500","INTERNAL SERVER ERROR"),
    BAD_REQUEST(400, "CM03-ERR-400", "BAD REQUEST"),
    INVALID_INPUT_VALUE(403,"CMO4-ERR-403","INVALID INPUT VALUE"),
    INVALID_WORD(403,"GAO1-ERR-403","INVALID WORD"),
    PLAYER_NOT_FOUND(403, "GA02-ERR-403", "PLAYER NOT FOUND"),
    ROOM_NOT_FOUND(403, "GA03-ERR-403", "ROOM NOT FOUND"),
    ;
    

    private int status;
    private String errorCode;
    private String message;

}
