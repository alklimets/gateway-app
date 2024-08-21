package com.aklimets.pet.adapter.controller;

import com.aklimets.pet.application.service.SecurityService;
import com.aklimets.pet.domain.dto.request.AuthenticationRequest;
import com.aklimets.pet.domain.dto.request.JwtRefreshTokenRequest;
import com.aklimets.pet.domain.dto.request.RegistrationRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/unauthenticated/security/v1")
@Slf4j
@AllArgsConstructor
public class SecurityEndpoint {

    private final SecurityService securityService;

    @PostMapping("/authenticate")
    public HttpEntity<?> authenticate(@Valid @RequestBody AuthenticationRequest request) {
        return securityService.authenticate(request);
    }

    @PostMapping("/refresh")
    public HttpEntity<?> refreshTokensPair(@Valid @RequestBody JwtRefreshTokenRequest tokens) {
        return securityService.refreshTokensPair(tokens);
    }

    @PostMapping("/register")
    public HttpEntity<?> register(@Valid @RequestBody RegistrationRequest request) {
        return securityService.register(request);
    }
}
