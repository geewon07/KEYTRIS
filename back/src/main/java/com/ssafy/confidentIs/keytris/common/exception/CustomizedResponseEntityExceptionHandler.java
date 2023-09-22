package com.ssafy.confidentIs.keytris.common.exception;

import com.ssafy.confidentIs.keytris.common.dto.response.ErrorResponseDto;
import com.ssafy.confidentIs.keytris.common.exception.customException.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    // 모든 예외 처리
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorResponseDto> handleException(Exception ex) {
        log.error("handleException", ex);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(ErrorCode.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }


//    @ExceptionHandler(NotFoundException.class)
//    public final ResponseEntity<ErrorResponseDto> handleNotFoundException(NotFoundException ex) {
//        log.error("handleNotFoundException", ex);
//        ErrorResponseDto errorResponseDto = new ErrorResponseDto(ErrorCode.NOT_FOUND);
//        return new ResponseEntity<>(errorResponseDto, HttpStatus.NOT_FOUND);
//    }


    @ExceptionHandler(BadRequestException.class)
    public final ResponseEntity<ErrorResponseDto> handleBadRequestException(BadRequestException ex) {
        log.error("handleBadRequestException", ex);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(ErrorCode.BAD_REQUEST);
        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(InvalidInputValueException.class)
    public final ResponseEntity<ErrorResponseDto> handleInvalidInputValueException(InvalidInputValueException ex) {
        log.error("handleInvalidInputValueException", ex);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(ex.getErrorCode());
        return new ResponseEntity<>(errorResponseDto, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }


    @ExceptionHandler(InvalidWordException.class)
    public final ResponseEntity<ErrorResponseDto> handleInvalidWordException(InvalidWordException ex) {
        log.error("handleInvalidWordException", ex);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(ex.getErrorCode());
        return new ResponseEntity<>(errorResponseDto, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }


    @ExceptionHandler(PlayerNotFoundException.class)
    public ResponseEntity<?> handlePlayerNotFoundException(PlayerNotFoundException ex){
        log.error("handlePlayerNotFoundException", ex);
        ErrorResponseDto response = new ErrorResponseDto(ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }


    @ExceptionHandler(RoomNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleRoomNotFoundException(RoomNotFoundException ex){
        log.error("handleRoomNotFoundException", ex);
        ErrorResponseDto response = new ErrorResponseDto(ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

}
