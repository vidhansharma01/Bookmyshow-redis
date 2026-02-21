package com.redis_bookmyshow.show.cqrs;

/** Simple bus used to deliver events from the write side to projections. */
public interface EventBus {
    /** Publishes an event to all registered handlers. */
    void publish(Event event);

    /** Registers a handler that will receive published events. */
    void register(EventHandler handler);
}
