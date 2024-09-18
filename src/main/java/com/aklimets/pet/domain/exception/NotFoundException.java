package com.aklimets.pet.domain.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends DefaultDomainRuntimeException {

    public NotFoundException(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }
}
