package com.aklimets.pet.domain.dto.key;

import java.time.LocalDateTime;

public record DisposableKeyResponse(String keyId, String publicKey, LocalDateTime validUntil) {
}
