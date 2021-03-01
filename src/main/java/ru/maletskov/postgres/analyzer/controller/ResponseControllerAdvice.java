package ru.maletskov.postgres.analyzer.controller;

import java.util.Arrays;
import javax.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.maletskov.postgres.analyzer.dto.ErrorDto;

@Slf4j
@RestControllerAdvice
public class ResponseControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception e) {
        log.error("Got error ", e);
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorDto.builder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                        .errorMessage(e.getMessage())
                        .errorPath(requestAttributes == null ? null : requestAttributes.getRequest().getServletPath())
                        .stackTrace(Arrays.toString(e.getStackTrace()))
                        .build());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDto> handleEntityNotFoundException(EntityNotFoundException e) {
        log.error("Got not found error ", e);
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorDto.builder()
                        .code(HttpStatus.NOT_FOUND.toString())
                        .errorMessage(e.getMessage())
                        .errorPath(requestAttributes == null ? null : requestAttributes.getRequest().getServletPath())
                        .stackTrace(Arrays.toString(e.getStackTrace()))
                        .build());
    }
}
