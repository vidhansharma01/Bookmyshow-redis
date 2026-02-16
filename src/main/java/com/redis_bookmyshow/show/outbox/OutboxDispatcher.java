package com.redis_bookmyshow.show.outbox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class OutboxDispatcher {

    private static final Logger log = LoggerFactory.getLogger(OutboxDispatcher.class);
    private static final int BATCH_SIZE = 10;
    private static final int MAX_RETRIES = 3;

    private final OutboxRepository repository;
    private final OutboxPublisher publisher;

    public OutboxDispatcher(OutboxRepository repository, OutboxPublisher publisher) {
        this.repository = repository;
        this.publisher = publisher;
    }

    @Scheduled(fixedDelayString = "${outbox.dispatcher.delay-ms:1000}")
    public void dispatch() {
        // Claim a batch first so concurrent dispatchers do not double-publish.
        List<OutboxEvent> batch = repository.claimPending(BATCH_SIZE);
        if (batch.isEmpty()) {
            return;
        }

        for (OutboxEvent event : batch) {
            try {
                publisher.publish(event);
                repository.markSent(event.getId(), Instant.now());
            } catch (Exception ex) {
                int nextRetry = event.getRetryCount() + 1;
                if (nextRetry <= MAX_RETRIES) {
                    repository.markRetry(event.getId(), nextRetry);
                    log.warn("Outbox publish failed, retrying id={} attempt={}", event.getId(), nextRetry, ex);
                } else {
                    repository.markFailed(event.getId());
                    log.error("Outbox publish failed, giving up id={}", event.getId(), ex);
                }
            }
        }
    }
}
