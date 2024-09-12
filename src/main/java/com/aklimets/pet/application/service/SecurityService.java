package com.aklimets.pet.application.service;

import com.aklimets.pet.domain.dto.request.AuthenticationRequest;
import com.aklimets.pet.domain.dto.request.JwtRefreshTokenRequest;
import com.aklimets.pet.domain.dto.request.RegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    private static final String AUTHENTICATION_ENDPOINT_URL_V1 = "/api/v1/security/authenticate";
    private static final String REFRESH_ENDPOINT_URL_V1 = "/api/v1/security/refresh";
    private static final String REGISTRATION_ENDPOINT_URL_V1 = "/api/v1/security/register";
    private static final String CONFIRM_PROFILE_ENDPOINT_URL_V1 = "/api/v1/security/profile/confirm/";

    @Value("${security.app.url}")
    public String securityEndpoint;

    @Autowired
    private RestTemplateExecutorService restService;

    public ResponseEntity<?> authenticate(AuthenticationRequest request) {
        return restService.exchangePostWrapped(securityEndpoint + AUTHENTICATION_ENDPOINT_URL_V1, request);
    }

    public ResponseEntity<?> refreshTokensPair(JwtRefreshTokenRequest tokens) {
        return restService.exchangePostWrapped(securityEndpoint + REFRESH_ENDPOINT_URL_V1, tokens);
    }

    public ResponseEntity<?> register(RegistrationRequest request) {
        return restService.exchangePostWrapped(securityEndpoint + REGISTRATION_ENDPOINT_URL_V1, request);
    }

    public ResponseEntity<?> confirmProfile(String code) {
        return restService.exchangeGetWrapped(securityEndpoint + CONFIRM_PROFILE_ENDPOINT_URL_V1 + code);
    }
}
