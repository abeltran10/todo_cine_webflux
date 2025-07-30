package com.todocine_webflux.exceptions.controller;

import com.todocine_webflux.exceptions.BadGatewayException;
import com.todocine_webflux.exceptions.BadRequestException;
import com.todocine_webflux.exceptions.ForbiddenException;
import com.todocine_webflux.exceptions.NotFoudException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleGenericException(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        response.put("message", "Ha ocurrido un error inesperado");
        return Mono.just(new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(BadGatewayException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleBadGatewayException(BadGatewayException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.BAD_GATEWAY.value());
        response.put("error", HttpStatus.BAD_GATEWAY.getReasonPhrase());
        response.put("message", ex.getMessage());
        return Mono.just(new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY));
    }

    @ExceptionHandler(BadRequestException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleBadRequestException(BadRequestException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        response.put("message", ex.getMessage());
        return Mono.just(new ResponseEntity<>(response, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(NotFoudException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleNotFoundException(NotFoudException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.NOT_FOUND.value());
        response.put("error", HttpStatus.NOT_FOUND.getReasonPhrase());
        response.put("message", ex.getMessage());
        return Mono.just(new ResponseEntity<>(response, HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(ForbiddenException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleNotFoundException(ForbiddenException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.FORBIDDEN.value());
        response.put("error", HttpStatus.FORBIDDEN.getReasonPhrase());
        response.put("message", ex.getMessage());
        return Mono.just(new ResponseEntity<>(response, HttpStatus.FORBIDDEN));
    }
}
