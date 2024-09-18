package com.aklimets.pet.infrastructure.security;

import com.aklimets.pet.crypto.provider.VersionedKeyPairProvider;
import com.aklimets.pet.jwt.util.JwtExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Autowired
    private VersionedKeyPairProvider jwtAccessKeyPairProvider;

    @Autowired
    private VersionedKeyPairProvider jwtRefreshKeyPairProvider;

    @Bean
    public JwtExtractor jwtExtractor() {
        return new JwtExtractor(jwtAccessKeyPairProvider, jwtRefreshKeyPairProvider);
    }
}
