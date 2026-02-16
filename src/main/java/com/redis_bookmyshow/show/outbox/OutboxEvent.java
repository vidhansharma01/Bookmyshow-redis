package com.redis_bookmyshow.show.outbox;

import java.time.Instant;
import java.util.UUID;

public final class OutboxEvent {

    private final UUID id;
    private final String aggregateType;
    private final String aggregateId;
    private final String eventType;
    private final String payload;
    private final OutboxStatus status;
    private final Instant createdAt;
    private final Instant sentAt;
    private final int retryCount;

    public OutboxEvent(UUID id,
                       String aggregateType,
                       String aggregateId,
                       String eventType,
                       String payload,
                       OutboxStatus status,
                       Instant createdAt,
                       Instant sentAt,
                       int retryCount) {
        this.id = id;
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.payload = payload;
        this.status = status;
        this.createdAt = createdAt;
        this.sentAt = sentAt;
        this.retryCount = retryCount;
    }

    public UUID getId() {
        return id;
    }

    public String getAggregateType() {
        return aggregateType;
    }

    public String getAggregateId() {
        return aggregateId;
    }

    public String getEventType() {
        return eventType;
    }

    public String getPayload() {
        return payload;
    }

    public OutboxStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getSentAt() {
        return sentAt;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public OutboxEvent withStatus(OutboxStatus newStatus, Instant newSentAt, int newRetryCount) {
        return new OutboxEvent(
                id,
                aggregateType,
                aggregateId,
                eventType,
                payload,
                newStatus,
                createdAt,
                newSentAt,
                newRetryCount
        );
    }
}
