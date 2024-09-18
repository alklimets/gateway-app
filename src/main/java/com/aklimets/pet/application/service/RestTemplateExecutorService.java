package com.aklimets.pet.application.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class RestTemplateExecutorService {

    @Qualifier("restTemplate")
    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<?> exchangeGetWrapped(String url) {
        return exchangeWrapped(url, HttpMethod.GET, HttpEntity.EMPTY);
    }

    public ResponseEntity<?> exchangePostWrapped(String url, Object request) {
        return exchangeWrapped(url, HttpMethod.POST, new HttpEntity<>(request));
    }

    public ResponseEntity<?> exchangePutWrapped(String url, Object request) {
        return exchangeWrapped(url, HttpMethod.PUT, new HttpEntity<>(request));
    }

    public ResponseEntity<?> exchangeDeleteWrapped(String url) {
        return exchangeWrapped(url, HttpMethod.DELETE, HttpEntity.EMPTY);
    }

    private ResponseEntity<?> exchangeWrapped(String url, HttpMethod method, HttpEntity<?> entity) {
        return wrapHeaders(restTemplate.exchange(url,
                method,
                entity,
                String.class));
    }

    private ResponseEntity<?> wrapHeaders(ResponseEntity<String> response) {
        var customHeaders = new HttpHeaders();
        response.getHeaders().toSingleValueMap()
                .entrySet().stream().filter(entry -> !entry.getKey().equals("Transfer-Encoding"))
                .forEach(entry -> customHeaders.put(entry.getKey(), List.of(entry.getValue())));
        return new ResponseEntity<>(response.getBody(), customHeaders, response.getStatusCode());
    }
}
