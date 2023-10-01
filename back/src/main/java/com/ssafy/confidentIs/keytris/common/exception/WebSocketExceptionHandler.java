package com.ssafy.confidentIs.keytris.common.exception;

import com.ssafy.confidentIs.keytris.common.dto.ErrorResponseDto;
import com.ssafy.confidentIs.keytris.common.exception.customException.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Slf4j
@ControllerAdvice
public class WebSocketExceptionHandler {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebSocketExceptionHandler(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }


    // 소켓 요청자에게 에러 메시지 전달
    private void sendMessageToClient(String roomId, String playerId, ErrorResponseDto response) {
        log.info("roomId: {}, playerId: {}", roomId, playerId);
        log.info("ErrorResponse: {}", response);
        String destination = "/topic/multi/error/" + roomId + "/" + playerId;
        log.info("destination: {}", destination);
        messagingTemplate.convertAndSend(destination, response);
    }


    // 모든 예외 처리
    @MessageExceptionHandler(SocketException.class)
    public void handleSocketException(SocketException ex) {
        log.error("handleSocketException", ex);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(ErrorCode.INTERNAL_SERVER_ERROR);
        sendMessageToClient(ex.getRoomId(), ex.getPlayerId(), errorResponseDto);
    }
//
//
//    @ExceptionHandler(BadRequestException.class)
//    public final ResponseEntity<ErrorResponseDto> handleBadRequestException(BadRequestException ex) {
//        log.error("handleBadRequestException", ex);
//        ErrorResponseDto errorResponseDto = new ErrorResponseDto(ErrorCode.BAD_REQUEST);
//        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
//    }
//
//
//    @ExceptionHandler(InvalidInputValueException.class)
//    public final ResponseEntity<ErrorResponseDto> handleInvalidInputValueException(InvalidInputValueException ex) {
//        log.error("handleInvalidInputValueException", ex);
//        ErrorResponseDto errorResponseDto = new ErrorResponseDto(ex.getErrorCode());
//        return new ResponseEntity<>(errorResponseDto, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
//    }
//
//
    @MessageExceptionHandler(SocketInvalidWordException.class)
    public void handleInvalidWordException(SocketInvalidWordException ex) {
        log.error("handleSocketInvalidWordException", ex);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(ex.getErrorCode());
        sendMessageToClient(ex.getRoomId(), ex.getPlayerId(), errorResponseDto);
    }


    @MessageExceptionHandler(SocketPlayerNotFoundException.class)
    public void handlePlayerNotFoundException(SocketPlayerNotFoundException ex){
        log.error("handleSocketPlayerNotFoundException", ex);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(ex.getErrorCode());
        sendMessageToClient(ex.getRoomId(), ex.getPlayerId(), errorResponseDto);
    }


    @MessageExceptionHandler(SocketRoomNotFoundException.class)
    public void handleRoomNotFoundException(SocketRoomNotFoundException ex){
        log.error("handleSocketRoomNotFoundException", ex);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(ex.getErrorCode());
        sendMessageToClient(ex.getRoomId(), ex.getPlayerId(), errorResponseDto);
    }


    @MessageExceptionHandler(SocketInvalidStartException.class)
    public void handleInvalidStartException(SocketInvalidStartException ex){
        log.error("handleSocketInvalidStartException", ex);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(ex.getErrorCode());
        sendMessageToClient(ex.getRoomId(), ex.getPlayerId(), errorResponseDto);
    }


    @MessageExceptionHandler(SocketNotAuthorizedException.class)
    public void handleNotAuthorizedException(SocketNotAuthorizedException ex){
        log.error("handleSocketNotAuthorizedException", ex);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(ex.getErrorCode());
        sendMessageToClient(ex.getRoomId(), ex.getPlayerId(), errorResponseDto);
    }


}
