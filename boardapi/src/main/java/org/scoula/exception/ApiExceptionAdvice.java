package org.scoula.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class ApiExceptionAdvice {
    /**
     * NoSuchElementException 발생 시 호출되는 예외 처리 메서드.
     *
     * @param e 발생한 NoSuchElementException
     * @return HTTP 404 상태 코드와 함께 "해당 ID의 요소가 없습니다."라는 메시지를 포함한 ResponseEntity를 반환
     */
    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<String> handleNoSuchElementException(NoSuchElementException e) {
        // HTTP 404 상태 코드 (NOT FOUND)와 함께, 응답 헤더의 Content-Type을 "text/plain;charset=UTF-8"로 설정하고
        // "해당 ID의 요소가 없습니다."라는 메시지를 본문으로 하는 응답을 생성하여 반환
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .header("Content-Type", "text/plain;charset=UTF-8")
                .body("해당 ID의 요소가 없습니다.");
    }
    /**
     * 모든 Exception을 처리하는 기본 예외 처리 메서드.
     *
     * @param e 발생한 Exception
     * @return HTTP 500 상태 코드와 함께 발생한 예외의 메시지를 포함한 ResponseEntity를 반환
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<String> handleException(Exception e) {
        // HTTP 500 상태 코드 (INTERNAL SERVER ERROR)와 함께, 응답 헤더의 Content-Type을 "text/plain;charset=UTF-8"로 설정하고
        // 발생한 예외의 메시지를 본문으로 하는 응답을 생성하여 반환
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("Content-Type", "text/plain;charset=UTF-8")
                .body(e.getMessage());
    }
}