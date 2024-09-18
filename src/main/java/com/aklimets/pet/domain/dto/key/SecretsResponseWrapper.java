package com.aklimets.pet.domain.dto.key;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SecretsResponseWrapper <T> {
    private T data;
}
