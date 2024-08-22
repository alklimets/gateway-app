package com.aklimets.pet.infrastructure.security.filter;

import com.aklimets.pet.domain.dto.authentication.UserAuthentication;
import com.aklimets.pet.domain.dto.authentication.attribute.UserIdNumber;
import com.aklimets.pet.infrastructure.security.handler.JwtSuccessHandler;
import com.aklimets.pet.jwt.model.JwtUser;
import com.aklimets.pet.jwt.model.attribute.AccessToken;
import com.aklimets.pet.jwt.util.JwtExtractor;
import com.aklimets.pet.model.attribute.Username;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;
import java.util.List;


@Slf4j
public class JwtAuthenticationTokenFilter extends AbstractAuthenticationProcessingFilter {

    private static final String AUTH_HEADER = "Authorization";

    private static final String ACCESS_PREFIX = "Bearer";

    private final JwtExtractor jwtExtractor;


    public JwtAuthenticationTokenFilter(AuthenticationManager authenticationManager,
                                        JwtSuccessHandler jwtSuccessHandler,
                                        JwtExtractor jwtExtractor) {
        super("/app/**");
        setAuthenticationManager(authenticationManager);
        setAuthenticationSuccessHandler(jwtSuccessHandler);
        this.jwtExtractor = jwtExtractor;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
        var accessToken = extractTokenFromHeader(request);

        if (isAccessTokenInvalid(accessToken)) {
            sendUnauthorizedError(response);
            return null;
        }
        Authentication authentication = null;
        try {
            authentication = createUserAuthentication(jwtExtractor.extractAccessJwtUser(extractTokenValue(accessToken)));
        } catch (JwtException e) {
            log.warn("Error during jwt verification: {}", e.getMessage());
            sendUnauthorizedError(response);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        MDC.put(AUTH_HEADER, accessToken);
        return authentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }

    private String extractTokenFromHeader(HttpServletRequest request) {
        return request.getHeader(AUTH_HEADER);
    }

    private AccessToken extractTokenValue(String accessToken) {
        return new AccessToken(accessToken.substring(ACCESS_PREFIX.length() + 1));
    }

    private void sendUnauthorizedError(HttpServletResponse response) throws IOException {
        response.sendError(401, "UNAUTHORIZED");
    }

    private boolean isAccessTokenInvalid(String accessToken) {
        return accessToken == null || !accessToken.startsWith(ACCESS_PREFIX);
    }

    private UserAuthentication createUserAuthentication(JwtUser jwtUser) {
        return new UserAuthentication(new UserIdNumber(jwtUser.id().getValue()),
                new Username(jwtUser.username().getValue()),
                List.of(new SimpleGrantedAuthority(jwtUser.role().name())));
    }
}
