package com.aklimets.pet.infrastructure.web.interceptor;

import com.aklimets.pet.domain.dto.authentication.UserAuthentication;
import com.aklimets.pet.util.datetime.TimeSource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import static java.lang.String.format;

@Component
@Slf4j
@AllArgsConstructor
public class IncomingRequestCountInterceptor implements HandlerInterceptor {

    private static final String KEY_PREFIX_TEMPLATE = "requests:count:userId:%s";

    private final RedisTemplate<String, Object> redisTemplate;

    private final TimeSource timeSource;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        var authentication = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
        redisTemplate.opsForZSet().add(
                format(KEY_PREFIX_TEMPLATE, authentication.getId()),
                MDC.get("requestId"),
                timeSource.getCurrentMills());
        return true;
    }
}
