package com.redis_bookmyshow.show.outbox;

public enum OutboxStatus {
    PENDING,
    IN_PROGRESS,
    SENT,
    FAILED
}
