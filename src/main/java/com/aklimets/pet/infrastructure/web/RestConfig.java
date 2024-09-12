package com.aklimets.pet.infrastructure.web;

import com.aklimets.pet.infrastructure.web.handler.RestTemplateResponseErrorHandler;
import com.aklimets.pet.infrastructure.web.interceptor.OutgoingRequestInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@AllArgsConstructor
public class RestConfig {

    private final OutgoingRequestInterceptor outgoingRequestInterceptor;

    @Bean
    public RestTemplate securityRestTemplate() {
        var restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(outgoingRequestInterceptor);
        restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        return restTemplate;
    }
}
