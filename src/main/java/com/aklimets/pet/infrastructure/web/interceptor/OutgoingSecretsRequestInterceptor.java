package com.aklimets.pet.infrastructure.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class OutgoingSecretsRequestInterceptor implements ClientHttpRequestInterceptor {

    @Value("${simple.secrets.manager.api-key}")
    private String apiKey;

    private static final String REQUEST_ID_HEADER = "X-Request-ID";
    private static final String API_KEY_HEADER = "APP-API-KEY";

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        var requestId = MDC.get("requestId");
        request.getHeaders().set(REQUEST_ID_HEADER, requestId);
        request.getHeaders().set(API_KEY_HEADER, apiKey);
        log.info("Request ID {} has been populated", requestId);
        return execution.execute(request, body);
    }
}
