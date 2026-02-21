package com.redis_bookmyshow.show.cqrs;

import java.time.Instant;

/** Shared event base that stamps creation time and exposes type. */
public abstract class BaseEvent implements Event {
    private final Instant occurredAt = Instant.now();

    /** Returns the event type based on the class name. */
    @Override
    public String type() {
        return getClass().getSimpleName();
    }

    /** Returns the time the event was created. */
    @Override
    public Instant occurredAt() {
        return occurredAt;
    }
}
