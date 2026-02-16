package com.redis_bookmyshow.show.outbox;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface OutboxRepository {

    void add(OutboxEvent event);

    List<OutboxEvent> claimPending(int limit);

    boolean markSent(UUID id, Instant sentAt);

    boolean markRetry(UUID id, int retryCount);

    boolean markFailed(UUID id);
}
