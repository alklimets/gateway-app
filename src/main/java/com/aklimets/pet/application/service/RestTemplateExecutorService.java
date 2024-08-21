package com.aklimets.pet.application.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class RestTemplateExecutorService {

    private final RestTemplate restTemplate;

    public HttpEntity<?>  exchangeGetWrapped(String url) {
        return exchangeWrapped(url, HttpMethod.GET, HttpEntity.EMPTY);
    }

    public HttpEntity<?>  exchangePostWrapped(String url, Object request) {
        return exchangeWrapped(url, HttpMethod.POST, new HttpEntity<>(request));
    }
    
    public HttpEntity<?>  exchangePutWrapped(String url, Object request) {
        return exchangeWrapped(url, HttpMethod.PUT, new HttpEntity<>(request));
    }

    public HttpEntity<?>  exchangeDeleteWrapped(String url) {
        return exchangeWrapped(url, HttpMethod.DELETE, HttpEntity.EMPTY);
    }

    private  HttpEntity<?> exchangeWrapped(String url, HttpMethod method, HttpEntity<?> entity) {
        return restTemplate.exchange(url,
                method,
                entity,
                String.class);
    }
}
