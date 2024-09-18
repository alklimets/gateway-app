package com.aklimets.pet.infrastructure.web;

import com.aklimets.pet.infrastructure.web.handler.RestTemplateResponseErrorHandler;
import com.aklimets.pet.infrastructure.web.interceptor.OutgoingRequestInterceptor;
import com.aklimets.pet.infrastructure.web.interceptor.OutgoingSecretsRequestInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@AllArgsConstructor
public class RestConfig {

    private final OutgoingSecretsRequestInterceptor outgoingSecretsRequestInterceptor;

    private final OutgoingRequestInterceptor outgoingRequestInterceptor;

    @Bean
    @Qualifier("restTemplate")
    public RestTemplate restTemplate() {
        var restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(outgoingRequestInterceptor);
        restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        return restTemplate;
    }


    @Bean
    @Qualifier("secretsRestTemplate")
    public RestTemplate secretsRestTemplate() {
        var restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(outgoingSecretsRequestInterceptor);
        restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        return restTemplate;
    }
}
