package com.aklimets.pet.domain.exception;

import lombok.Getter;

@Getter
public abstract class DefaultDomainRuntimeException extends RuntimeException{

    private final String errorCode;
    private final String errorMessage;

    public DefaultDomainRuntimeException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }
}
