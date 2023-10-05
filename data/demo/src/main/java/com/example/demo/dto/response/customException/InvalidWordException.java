package com.example.demo.dto.response.customException;

import com.example.demo.dto.response.ErrorCode;
import lombok.Getter;

@Getter
public class InvalidWordException extends RuntimeException {

    private ErrorCode errorCode;

    public InvalidWordException(String message, ErrorCode errorCode){
        super(message);
        this.errorCode = errorCode;
    }

}
