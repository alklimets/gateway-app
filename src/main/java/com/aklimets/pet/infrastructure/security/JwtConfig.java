package com.aklimets.pet.infrastructure.security;

import com.aklimets.pet.jwt.util.JwtExtractor;
import com.aklimets.pet.jwt.util.JwtKeyReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Value("${jwt.access.public.key.path}")
    private String accessPublicKeyPath;

    @Value("${jwt.refresh.public.key.path}")
    private String refreshPublicKeyPath;

    @Bean
    public JwtKeyReader jwtKeyReader() {
        return new JwtKeyReader();
    }

    @Bean
    public JwtExtractor jwtExtractor() throws Exception {
        return new JwtExtractor(accessPublicKeyPath, refreshPublicKeyPath, jwtKeyReader());
    }
}
