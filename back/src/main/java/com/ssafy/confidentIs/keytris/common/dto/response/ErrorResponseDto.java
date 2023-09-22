package com.ssafy.confidentIs.keytris.common.dto.response;

import com.ssafy.confidentIs.keytris.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
//TODO: 에러 처리, 상황에 맞는 에러 지정
@Getter
public class ErrorResponseDto {

    private String success;
    private int status;
    private String errorCode;
    private String message;

    public ErrorResponseDto(ErrorCode errorCode){
        this.success = "FAIL";
        this.status = errorCode.getStatus();
        this.errorCode = errorCode.getErrorCode();
        this.message = errorCode.getMessage();
    }

}
