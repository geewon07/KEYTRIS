package com.example.demo.dto.response;

import com.example.demo.dto.response.customException.InvalidWordException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorResponseDto> handleException(Exception ex) {
        log.error("handleException", ex);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(ErrorCode.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidWordException.class)
    public final ResponseEntity<ErrorResponseDto> handleInvalidWordException(InvalidWordException ex) {
        log.error("handleInvalidWordException", ex);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(ex.getErrorCode());
        return new ResponseEntity<>(errorResponseDto, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

}
