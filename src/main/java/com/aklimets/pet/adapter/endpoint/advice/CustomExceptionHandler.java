package com.aklimets.pet.adapter.endpoint.advice;

import com.aklimets.pet.domain.exception.DefaultDomainRuntimeException;
import com.aklimets.pet.domain.exception.NotFoundException;
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

    @ExceptionHandler(value = NotFoundException.class)
    private ResponseEntity<ErrorResponseEnvelope> handleCustomException(NotFoundException exception) {
        return withStatus(404, exception);
    }

    private ResponseEntity<ErrorResponseEnvelope> withStatus(int status, DefaultDomainRuntimeException exception) {
        logException(exception);
        return ResponseEntity.status(status)
                .body(new ErrorResponseEnvelope(exception.getErrorCode(), exception.getErrorMessage()));
    }

    private static void logException(Exception exception) {
        log.error("Exception occurred: {}", exception.getMessage());
    }

}
