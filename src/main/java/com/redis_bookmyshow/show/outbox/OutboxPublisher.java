package com.redis_bookmyshow.show.outbox;

public interface OutboxPublisher {

    void publish(OutboxEvent event) throws Exception;
}
