package com.redis_bookmyshow.show.cqrs;

import java.time.Instant;

/** Base event contract published by the write side and consumed by projections. */
public interface Event {
    /** Returns the event type name used by handlers and logs. */
    String type();

    /** Returns when the event occurred. */
    Instant occurredAt();
}
