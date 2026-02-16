package com.redis_bookmyshow.show.outbox;

import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryOutboxRepository implements OutboxRepository {

    private final Map<UUID, OutboxEvent> store = new ConcurrentHashMap<>();
    private final Queue<UUID> pendingQueue = new ArrayDeque<>();
    private final Object lock = new Object();

    @Override
    public void add(OutboxEvent event) {
        store.put(event.getId(), event);
        synchronized (lock) {
            pendingQueue.add(event.getId());
        }
    }

    @Override
    public List<OutboxEvent> claimPending(int limit) {
        List<OutboxEvent> batch = new ArrayList<>();
        synchronized (lock) {
            // Move events to IN_PROGRESS before returning, so only one dispatcher publishes them.
            while (!pendingQueue.isEmpty() && batch.size() < limit) {
                UUID id = pendingQueue.poll();
                OutboxEvent event = store.get(id);
                if (event == null || event.getStatus() != OutboxStatus.PENDING) {
                    continue;
                }
                OutboxEvent claimed = event.withStatus(OutboxStatus.IN_PROGRESS, null, event.getRetryCount());
                store.put(id, claimed);
                batch.add(claimed);
            }
        }
        return batch;
    }

    @Override
    public boolean markSent(UUID id, Instant sentAt) {
        OutboxEvent current = store.get(id);
        if (current == null) {
            return false;
        }
        store.put(id, current.withStatus(OutboxStatus.SENT, sentAt, current.getRetryCount()));
        return true;
    }

    @Override
    public boolean markRetry(UUID id, int retryCount) {
        OutboxEvent current = store.get(id);
        if (current == null) {
            return false;
        }
        store.put(id, current.withStatus(OutboxStatus.PENDING, null, retryCount));
        synchronized (lock) {
            // Re-queue for another attempt on the next dispatch cycle.
            pendingQueue.add(id);
        }
        return true;
    }

    @Override
    public boolean markFailed(UUID id) {
        OutboxEvent current = store.get(id);
        if (current == null) {
            return false;
        }
        store.put(id, current.withStatus(OutboxStatus.FAILED, null, current.getRetryCount()));
        return true;
    }
}
