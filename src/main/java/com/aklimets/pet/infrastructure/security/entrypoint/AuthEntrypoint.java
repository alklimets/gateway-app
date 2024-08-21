package com.aklimets.pet.infrastructure.security.entrypoint;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Returns generic response if authentication failed
 */
@Component
@Slf4j
public class AuthEntrypoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        log.warn("Authentication failed: {}", authException.getMessage());
        response.setContentType("application/json");
        response.setStatus(401);
        response.getOutputStream().print("{\"errorCode\":\"401\",\"message\":\"UNAUTHORIZED\"}");
    }

}
