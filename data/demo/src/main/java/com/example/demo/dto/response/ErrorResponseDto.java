package com.example.demo.dto.response;

import lombok.Getter;

@Getter
public class ErrorResponseDto {

    private String success;
    private int status;
    private String errorCode;
    private String message;

    public ErrorResponseDto(ErrorCode errorCode){
        this.success = "fail";
        this.status = errorCode.getStatus();
        this.errorCode = errorCode.getErrorCode();
        this.message = errorCode.getMessage();
    }

}
