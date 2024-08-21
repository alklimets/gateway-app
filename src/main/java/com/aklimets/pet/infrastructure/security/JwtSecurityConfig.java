package com.aklimets.pet.infrastructure.security;

import com.aklimets.pet.infrastructure.security.filter.JwtAuthenticationTokenFilter;
import com.aklimets.pet.infrastructure.security.handler.JwtSuccessHandler;
import com.aklimets.pet.infrastructure.security.provider.JwtAuthenticationProvider;
import com.aklimets.pet.jwt.util.JwtExtractor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collections;

/**
 * Configuration enables jwt security for the module
 * In general this app does not need authentication for
 * incoming requests, but for reference as example it is
 * places here, could be activated by choosing jwt-auth
 * spring profile
 */
@Configuration
@EnableWebSecurity
@Slf4j
@AllArgsConstructor
public class JwtSecurityConfig {

    private final AuthenticationEntryPoint entryPoint;

    private final JwtAuthenticationProvider authenticationProvider;

    private final JwtExtractor jwtExtractor;

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(Collections.singletonList(authenticationProvider));
    }

    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() {
        return new JwtAuthenticationTokenFilter(
                authenticationManager(),
                new JwtSuccessHandler(),
                jwtExtractor);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.headers(Customizer.withDefaults());
        http.cors(AbstractHttpConfigurer::disable);
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                        .requestMatchers("/unauthenticated/**").permitAll()
                        .anyRequest().authenticated()
        );
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.exceptionHandling(handler -> handler.authenticationEntryPoint(entryPoint));
        http.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
