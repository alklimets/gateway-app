package com.aklimets.pet.adapter.endpoint;

import com.aklimets.pet.application.service.CommonAppService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/{appName}/api/**")
@Slf4j
@AllArgsConstructor
public class CommonAppEndpoint {

    private final CommonAppService commonAppService;

    @GetMapping
    public ResponseEntity<?> handleGet(HttpServletRequest request, @PathVariable("appName") String appName) {
        return commonAppService.exchangeGet(request, appName);
    }

    @PostMapping
    public ResponseEntity<?> handlePost(HttpServletRequest request, @RequestBody Object body, @PathVariable("appName") String appName) {
        return commonAppService.exchangePost(request, appName, body);
    }

    @PutMapping
    public ResponseEntity<?> handlePut(HttpServletRequest request, @RequestBody Object body, @PathVariable("appName") String appName) {
        return commonAppService.exchangePut(request, appName, body);
    }

    @DeleteMapping
    public ResponseEntity<?> handleDelete(HttpServletRequest request, @PathVariable("appName") String appName) {
        return commonAppService.exchangeDelete(request, appName);
    }
}
