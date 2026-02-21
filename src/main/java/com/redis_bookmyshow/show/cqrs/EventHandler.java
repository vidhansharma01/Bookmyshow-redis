package com.redis_bookmyshow.show.cqrs;

/** Read-side consumer that projects events into query models. */
public interface EventHandler {
    /** Handles an event on the read side (projection). */
    void handle(Event event);
}
