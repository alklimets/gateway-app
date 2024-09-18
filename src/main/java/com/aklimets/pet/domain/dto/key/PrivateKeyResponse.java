package com.aklimets.pet.domain.dto.key;

public record PrivateKeyResponse(String id, String privateKey, String keyName, String algorithm, String state) {
}
