package com.aklimets.pet.adapter.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class ControllerUtil {

    public String extractApiPath(HttpServletRequest request, String appName) {
        return request.getRequestURI().replaceFirst("/app/" + appName + "/api", "/api");
    }
}
