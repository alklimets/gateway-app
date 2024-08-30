package com.aklimets.pet.application.service;

import com.aklimets.pet.adapter.util.ControllerUtil;
import com.aklimets.pet.infrastructure.application.ApplicationsUrlSupplier;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class CommonAppService {

    private final ApplicationsUrlSupplier urlSupplier;

    private final RestTemplateExecutorService restService;

    private final ControllerUtil controllerUtil;

    public ResponseEntity<?> exchangeGet(HttpServletRequest request, String appName) {
        return restService.exchangeGetWrapped(createRequestUrl(request, appName));
    }

    public ResponseEntity<?> exchangePost(HttpServletRequest request, String appName, Object body) {
        return restService.exchangePostWrapped(createRequestUrl(request, appName), body);
    }

    public ResponseEntity<?> exchangePut(HttpServletRequest request, String appName, Object body) {
        return restService.exchangePutWrapped(createRequestUrl(request, appName), body);
    }

    public ResponseEntity<?> exchangeDelete(HttpServletRequest request, String appName) {
        return restService.exchangeDeleteWrapped(createRequestUrl(request, appName));
    }

    private String createRequestUrl(HttpServletRequest request, String appName) {
        var apiPath = controllerUtil.extractApiPath(request, appName);
        var url = urlSupplier.supplyUrl(appName);
        return url + apiPath;
    }
}
