package com.aklimets.pet.domain.dto.request;

import com.aklimets.pet.model.attribute.EmailAddress;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record ForgetPasswordRequest(
        @Valid @NotNull(message = "Email could not be null") EmailAddress email) {
}
