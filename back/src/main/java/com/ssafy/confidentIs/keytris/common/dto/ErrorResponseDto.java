package com.ssafy.confidentIs.keytris.common.dto;

import com.ssafy.confidentIs.keytris.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

//TODO: 에러 처리, 상황에 맞는 에러 지정
@Getter
@ToString
public class ErrorResponseDto {

    private String success;
    private int status;
    private String errorCode;
    private String message;

    public ErrorResponseDto(ErrorCode errorCode) {
        this.success = "fail";
        this.status = errorCode.getStatus();
        this.errorCode = errorCode.getErrorCode();
        this.message = errorCode.getMessage();
    }

}
