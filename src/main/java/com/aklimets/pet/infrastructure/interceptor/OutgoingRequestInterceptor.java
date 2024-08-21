package com.aklimets.pet.infrastructure.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class OutgoingRequestInterceptor implements ClientHttpRequestInterceptor {

    private static final String REQUEST_ID_HEADER = "X-Request-ID";
    private static final String AUTH_HEADER = "Authorization";

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        request.getHeaders().set(REQUEST_ID_HEADER, MDC.get("requestId"));
        request.getHeaders().set(AUTH_HEADER, MDC.get(AUTH_HEADER));
        log.info("Request ID has been populated");
        return execution.execute(request, body);
    }
}
