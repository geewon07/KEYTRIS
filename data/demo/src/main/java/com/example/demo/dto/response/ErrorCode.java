package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(500,"CMO2-ERR-500","INTERNAL SERVER ERROR"),
    INVALID_WORD( 200,"GAO1-ERR-400","INVALID WORD");

    private int status;
    private String errorCode;
    private String message;
}
