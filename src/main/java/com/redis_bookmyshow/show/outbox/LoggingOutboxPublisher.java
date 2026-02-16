package com.redis_bookmyshow.show.outbox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LoggingOutboxPublisher implements OutboxPublisher {

    private static final Logger log = LoggerFactory.getLogger(LoggingOutboxPublisher.class);

    @Override
    public void publish(OutboxEvent event) {
        // Replace this with a real broker publisher (Kafka, RabbitMQ, etc.).
        log.info("Outbox publish id={} type={} aggregate={} payload={}",
                event.getId(),
                event.getEventType(),
                event.getAggregateId(),
                event.getPayload());
    }
}
