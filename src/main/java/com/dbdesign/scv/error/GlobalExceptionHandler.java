package com.dbdesign.scv.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.MethodNotAllowedException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /*
    현재 서버에서 던지는 예외 정리
    1. 규칙 : 에러 코드 기준 내림차순으로 정리
    2. 마지막 에러는 Exception 으로, 다루지 못한 에러에 대해서도 일관성 있는 data 를 반환하기 위해 작성
    3. 단, 인터셉터에서 던지는 에러는 형식이 다름
     */

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {

        log.error(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return ResponseEntity.badRequest().body(new ErrorResponse("400", e.getBindingResult().getAllErrors().get(0).getDefaultMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> accessDeniedExceptionHandler(AccessDeniedException e) {

        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse("403", e.getMessage()));
    }

    @ExceptionHandler(MethodNotAllowedException.class)
    public ResponseEntity<ErrorResponse> methodNotAllowedExceptionHandler(MethodNotAllowedException e) {

        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new ErrorResponse("405", e.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> illegalStateExceptionHandler(IllegalStateException e) {

        log.error(e.getMessage());
        return ResponseEntity.internalServerError().body(new ErrorResponse("500", e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> illegalArgumentExceptionHandler(IllegalArgumentException e) {

        log.error(e.getMessage());
        return ResponseEntity.internalServerError().body(new ErrorResponse("500", e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ErrorResponse exceptionHandler(Exception e) {

        log.error(e.getMessage());
        return new ErrorResponse("Unspecified", e.getMessage());
    }
}
