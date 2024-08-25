package com.aklimets.pet.infrastructure.web;

import com.aklimets.pet.infrastructure.interceptor.IncomingRequestCountInterceptor;
import com.aklimets.pet.infrastructure.interceptor.IncomingRequestInterceptor;
import com.aklimets.pet.infrastructure.interceptor.RateLimitRequestInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@AllArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private IncomingRequestInterceptor incomingRequestInterceptor;

    @Autowired
    private IncomingRequestCountInterceptor incomingRequestCountInterceptor;

    @Autowired
    private RateLimitRequestInterceptor rateLimitRequestInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(incomingRequestInterceptor).addPathPatterns("/**");
        registry.addInterceptor(rateLimitRequestInterceptor).addPathPatterns("/app/**");
        registry.addInterceptor(incomingRequestCountInterceptor).addPathPatterns("/app/**");
    }

}
