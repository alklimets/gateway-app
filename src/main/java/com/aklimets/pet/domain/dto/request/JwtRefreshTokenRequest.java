package com.aklimets.pet.domain.dto.request;


import com.aklimets.pet.jwt.model.attribute.RefreshToken;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public record JwtRefreshTokenRequest(
        @Valid @NotNull(message = "Refresh token cannot be null") RefreshToken refreshToken) {
}
