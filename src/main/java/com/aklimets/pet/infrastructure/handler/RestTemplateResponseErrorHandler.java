package com.aklimets.pet.infrastructure.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

@Slf4j
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().value() != HttpStatus.OK.value();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        log.info("Response from underlying service returned error");
    }
}

