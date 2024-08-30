package com.aklimets.pet.application.redis;

import com.aklimets.pet.domain.event.UserRequestsCountsKafkaEvent;
import com.aklimets.pet.event.DomainEventAdapter;
import com.aklimets.pet.util.datetime.TimeSource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.aklimets.pet.application.redis.RedisKeyPrefix.USER_REQUESTS;
import static java.time.ZoneOffset.UTC;

@Service
@Slf4j
@AllArgsConstructor
public class RedisService {
    public static final int MILLISECOND = 1;

    private final RedisTemplate<String, Object> redisTemplate;

    private final TimeSource timeSource;

    private final DomainEventAdapter kafkaAdapter;

    @Async("threadPoolTaskExecutor")
    public void processUserRequests() {
        var start = getCurrentPeriodStartMillsWithShift();
        var end = getCurrentPeriodEndMillsWithShift();
        log.info("Start period mills for process {}", start);
        log.info("End period mills for process {}", end);
        findKeysByPrefix(USER_REQUESTS).forEach(key -> processUserRequestsKey(start, end, key));
    }

    @Async("threadPoolTaskExecutor")
    public void cleanUpProcessedRequests() {
        var start = getCurrentPeriodStartMillsWithShift(1);
        var end = getCurrentPeriodEndMillsWithShift(1);
        log.info("Start period mills for clean up {}", start);
        log.info("End period mills for clean up {}", end);
        findKeysByPrefix(USER_REQUESTS).forEach(key -> cleanUpUserRequestsByKey(start, end, key));
    }

    private void processUserRequestsKey(long start, long end, String key) {
        var count = redisTemplate.opsForZSet().count(key, start, end);
        if (count > 0) {
            var event = new UserRequestsCountsKafkaEvent(
                    millsToDateTimeString(start),
                    millsToDateTimeString(end),
                    extractIdFromKey(key, USER_REQUESTS),
                    count);
            kafkaAdapter.send(event, "UserRequestsCount");
        }
    }

    private void cleanUpUserRequestsByKey(long start, long end, String key) {
        var count = redisTemplate.opsForZSet().removeRangeByScore(key, start, end);
        var userId = extractIdFromKey(key, USER_REQUESTS);
        log.info("{} requests where removed for user id {}", count, userId);
    }

    public List<String> findKeysByPrefix(RedisKeyPrefix prefix) {
        List<String> keys = new ArrayList<>();
        var options = ScanOptions.scanOptions().match(prefix.getValue()).count(100).build();
        try (Cursor<String> cursor = redisTemplate.scan(options)) {
            while (cursor.hasNext()) {
                keys.add(cursor.next());
            }
        }
        return keys;
    }

    private long getCurrentPeriodEndMillsWithShift() {
        return getCurrentPeriodEndMillsWithShift(0);
    }

    private long getCurrentPeriodEndMillsWithShift(int shift) {
        return timeSource.getCurrentLocalDateTime()
                .withSecond(0)
                .withNano(0)
                .withMinute(0)
                .minusHours(shift)
                .toInstant(UTC).toEpochMilli();
    }

    private long getCurrentPeriodStartMillsWithShift() {
        return getCurrentPeriodStartMillsWithShift(0);
    }

    private long getCurrentPeriodStartMillsWithShift(int shift) {
        return timeSource.getCurrentLocalDateTime()
                .withSecond(0)
                .withNano(0)
                .withMinute(0)
                .minusHours(shift + 1)
                .toInstant(UTC).toEpochMilli() + MILLISECOND; // makes mills exclusive
    }

    private String extractIdFromKey(String key, RedisKeyPrefix redisPrefix) {
        var regex = redisPrefix.getValue().replace("*", "(.*)");
        var pattern = Pattern.compile(regex);
        var matcher = pattern.matcher(key);
        if (matcher.matches()) {
            return matcher.group(1);
        }
        log.warn("Key does not match the provided pattern {}", redisPrefix.getValue());
        return "";
    }

    private String millsToDateTimeString(long mills) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(mills), UTC).toString();
    }
}
