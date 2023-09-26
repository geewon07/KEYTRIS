package com.ssafy.confidentIs.keytris.common.exception.customException;

import com.ssafy.confidentIs.keytris.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class InaccessibleGameException extends RuntimeException {

    private ErrorCode errorCode;

    public InaccessibleGameException(String message, ErrorCode errorCode){
        super(message);
        this.errorCode = errorCode;
    }

}
