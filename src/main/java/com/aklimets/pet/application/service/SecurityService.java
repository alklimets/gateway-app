package com.aklimets.pet.application.service;

import com.aklimets.pet.domain.dto.request.AuthenticationRequest;
import com.aklimets.pet.domain.dto.request.JwtRefreshTokenRequest;
import com.aklimets.pet.domain.dto.request.RegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    private static final String AUTHENTICATION_ENDPOINT_URL_V1 = "/api/v1/security/authenticate";
    private static final String REFRESH_ENDPOINT_URL_V1 = "/api/v1/security/refresh";
    private static final String REGISTRATION_ENDPOINT_URL_V1 = "/api/v1/security/registration";

    @Value("${security.app.url}")
    public String securityEndpoint;

    @Autowired
    private RestTemplateExecutorService restService;

    public HttpEntity<?> authenticate(AuthenticationRequest request) {
        return restService.exchangePostWrapped(securityEndpoint + AUTHENTICATION_ENDPOINT_URL_V1, request);
    }

    public HttpEntity<?> refreshTokensPair(JwtRefreshTokenRequest tokens) {
        return restService.exchangePostWrapped(securityEndpoint + REFRESH_ENDPOINT_URL_V1, tokens);
    }

    public HttpEntity<?> register(RegistrationRequest request) {
        return restService.exchangePostWrapped(securityEndpoint + REGISTRATION_ENDPOINT_URL_V1, request);
    }
}
