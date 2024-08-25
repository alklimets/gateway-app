package com.aklimets.pet.application.redis;

public enum RedisKeyPrefix {
    USER_REQUESTS("requests:count:userId:*");

    private final String value;

    RedisKeyPrefix(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
