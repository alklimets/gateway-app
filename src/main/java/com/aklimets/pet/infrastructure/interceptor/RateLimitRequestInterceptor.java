package com.aklimets.pet.infrastructure.interceptor;

import com.aklimets.pet.domain.dto.authentication.UserAuthentication;
import com.aklimets.pet.util.datetime.TimeSource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import static java.lang.String.format;
import static java.time.ZoneOffset.UTC;
import static org.springframework.http.HttpStatus.TOO_MANY_REQUESTS;

@Component
@Slf4j
@AllArgsConstructor
public class RateLimitRequestInterceptor implements HandlerInterceptor {

    private static final String KEY_PREFIX_TEMPLATE = "requests:count:userId:%s";
    private static final long LIMIT = 10L;
    public static final long MINUTE_NI_MILLS = 60_000L;

    private final RedisTemplate<String, Object> redisTemplate;

    private final TimeSource timeSource;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        var authentication = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
        long now = timeSource.getCurrentMills();
        long minuteAgo = now - MINUTE_NI_MILLS + 1; // make left border exclusive
        var count = redisTemplate.opsForZSet().count(format(KEY_PREFIX_TEMPLATE, authentication.getId()), minuteAgo, now);

        if (count != null && count + 1 > LIMIT) {
            response.setStatus(TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.getOutputStream().print("{\"errorCode\":\"429\",\"message\":\"Too many requests\"}");
            return false;
        }
        return true;
    }
}
