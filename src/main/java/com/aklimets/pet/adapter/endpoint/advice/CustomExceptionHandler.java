package com.aklimets.pet.adapter.endpoint.advice;

import com.aklimets.pet.model.envelope.ErrorResponseEnvelope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    private ResponseEntity<ErrorResponseEnvelope> handleException(Exception exception) {
        log.error("Exception occurred: {}", exception.getMessage());
        return ResponseEntity.status(500)
                .body(new ErrorResponseEnvelope("Internal server error", exception.getMessage()));
    }

}
