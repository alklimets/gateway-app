package com.aklimets.pet.adapter.endpoint;

import com.aklimets.pet.application.service.SecurityService;
import com.aklimets.pet.domain.dto.request.AuthenticationRequest;
import com.aklimets.pet.domain.dto.request.JwtRefreshTokenRequest;
import com.aklimets.pet.domain.dto.request.RegistrationRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/unauthenticated/security/v1")
@Slf4j
@AllArgsConstructor
public class SecurityEndpoint {

    private final SecurityService securityService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@Valid @RequestBody AuthenticationRequest request) {
        return securityService.authenticate(request);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshTokensPair(@Valid @RequestBody JwtRefreshTokenRequest tokens) {
        return securityService.refreshTokensPair(tokens);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationRequest request) {
        return securityService.register(request);
    }

    @GetMapping("/profile/confirm/{code}")
    public ResponseEntity<?> confirmProfile(@PathVariable String code) {
        return securityService.confirmProfile(code);
    }
}
