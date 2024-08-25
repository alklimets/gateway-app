package com.aklimets.pet.domain.event;

import com.aklimets.pet.event.DomainEvent;

public record UserRequestsCountsKafkaEvent(String periodStart,
                                           String periodEnd,
                                           String userId,
                                           Long count) implements DomainEvent {

    @Override
    public String toString() {
        return "UserRequestsCountsKafkaEvent{" +
                "periodStart=" + periodStart +
                ", periodEnd=" + periodEnd +
                ", userId=" + userId +
                ", count=" + count +
                '}';
    }
}
