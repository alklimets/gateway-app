package com.aklimets.pet.domain.dto.request;

import com.aklimets.pet.domain.model.passwordreset.attribute.ResetCode;
import com.aklimets.pet.model.attribute.Password;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record ResetPasswordRequest(
        @Valid @NotNull(message = "Reset code could not be null") ResetCode resetCode,
        @Valid @NotNull(message = "Password could not be null") Password password) {
}
