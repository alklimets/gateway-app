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
import java.util.regex.Matcher;
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
        var start = getCurrentPeriodStartMillsWithShift(0);
        var end = getCurrentPeriodEndMillsWithShift(0);
        log.info("Start period mills {}", start);
        log.info("End period mills {}", end);

        List<String> userRequestsKeys = findKeysByPrefix(USER_REQUESTS);
        for (String key : userRequestsKeys) {
            var count = redisTemplate.opsForZSet().count(key, start, end);
            if (count > 0) {
                var userId = extractIdFromKey(key, USER_REQUESTS);
                var periodStart = LocalDateTime.ofInstant(Instant.ofEpochMilli(start), UTC);
                var periodEnd = LocalDateTime.ofInstant(Instant.ofEpochMilli(end), UTC);

                UserRequestsCountsKafkaEvent event = new UserRequestsCountsKafkaEvent(
                        periodStart.toString(),
                        periodEnd.toString(),
                        userId,
                        count
                );
                kafkaAdapter.send(event);
            }
        }

    }

    public List<String> findKeysByPrefix(RedisKeyPrefix prefix) {
        List<String> keys = new ArrayList<>();
        ScanOptions options = ScanOptions.scanOptions().match(prefix.getValue()).count(100).build();
        try (Cursor<String> cursor = redisTemplate.scan(options)) {
            while (cursor.hasNext()) {
                keys.add(cursor.next());
            }
        }
        return keys;
    }

    // todo: change to current hour
    private long getCurrentPeriodEndMillsWithShift(int shift) {
        return timeSource.getCurrentLocalDateTime()
                .withSecond(0)
                .withNano(0)
                .minusHours(shift)
                .toInstant(UTC).toEpochMilli();
    }

    // todo: change to current hour
    private long getCurrentPeriodStartMillsWithShift(int shift) {
        return timeSource.getCurrentLocalDateTime()
                .withSecond(0)
                .minusMinutes(1)
                .withNano(0)
                .minusHours(shift)
                .toInstant(UTC).toEpochMilli() + MILLISECOND;
    }

    private String extractIdFromKey(String key, RedisKeyPrefix redisPrefix) {
        String regex = redisPrefix.getValue().replace("*", "(.*)");
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(key);
        if (matcher.matches()) {
            return matcher.group(1);
        }
        log.warn("Key does not match the provided pattern ()" + redisPrefix.getValue());
        return "";
    }
}
