package com.aklimets.pet.adapter.schedule;

import com.aklimets.pet.application.redis.RedisService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@AllArgsConstructor
@Transactional
@Slf4j
public class RequestsProcessingScheduler {

    private final RedisService redisService;

    @Scheduled(cron = "${user.requests.counts.scheduling.pattern}")
    public void processUserRequests() {
        redisService.processUserRequests();
    }


    @Scheduled(cron = "${user.requests.counts.scheduling.pattern}")
    public void cleanUpProcessedRequests() {
        redisService.cleanUpProcessedRequests();
    }

}
