package com.aklimets.pet.domain.dto.request;

import com.aklimets.pet.model.attribute.Password;
import com.aklimets.pet.model.attribute.Username;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public record AuthenticationRequest(
        @Valid @NotNull(message = "Username cannot be null") Username username,
        @Valid @NotNull(message = "Password cannot be null") Password password) {
}
