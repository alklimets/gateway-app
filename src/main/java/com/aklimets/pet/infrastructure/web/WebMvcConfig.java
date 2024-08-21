package com.aklimets.pet.infrastructure.web;

import com.aklimets.pet.infrastructure.interceptor.IncomingRequestInterceptor;
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

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(incomingRequestInterceptor).addPathPatterns("/**");
    }

}
