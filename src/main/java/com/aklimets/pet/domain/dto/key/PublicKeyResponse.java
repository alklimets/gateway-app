package com.aklimets.pet.domain.dto.key;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublicKeyResponse {

    private String id;
    private String publicKey;
    private String keyName;
    private String algorithm;
    private String state;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private DisposableKeyResponse disposableKey;

}
