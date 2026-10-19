package com.app.ies.security.auth.infrastructure.exceptions;

import com.app.ies.security.auth.domain.exceptions.DomainException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class HandlerControllerException {

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<Map<String, Object>> getResponseEntity(DomainException exception) {
        return ResponseEntity.status(exception.getStatusCode())
                .body(
                        Map.of(
                                "message",  exception.getMessage(),
                                "code", exception.getStatusCode()
                        )
                );
    }

}
