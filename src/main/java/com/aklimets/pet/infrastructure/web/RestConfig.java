package com.aklimets.pet.infrastructure.web;

import com.aklimets.pet.infrastructure.interceptor.OutgoingRequestInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@AllArgsConstructor
public class RestConfig {

    private final OutgoingRequestInterceptor outgoingRequestInterceptor;

    @Bean
    public RestTemplate securityRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(outgoingRequestInterceptor);
        return restTemplate;
    }
}
