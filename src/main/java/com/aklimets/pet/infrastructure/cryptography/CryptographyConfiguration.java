package com.aklimets.pet.infrastructure.cryptography;

import com.aklimets.pet.crypto.util.AsymmetricKeyUtil;
import com.aklimets.pet.crypto.util.SymmetricKeyUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
@Slf4j
public class CryptographyConfiguration {

    @Bean
    public AsymmetricKeyUtil asymmetricKeyUtil() {
        return new AsymmetricKeyUtil();
    }

}
