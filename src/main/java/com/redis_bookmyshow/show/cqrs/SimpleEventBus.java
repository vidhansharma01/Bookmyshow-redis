package com.redis_bookmyshow.show.cqrs;

import java.util.concurrent.CopyOnWriteArrayList;

/** In-memory event bus that synchronously forwards events to handlers. */
public final class SimpleEventBus implements EventBus {
    private final CopyOnWriteArrayList<EventHandler> handlers = new CopyOnWriteArrayList<>();

    /** Delivers the event to all registered handlers. */
    @Override
    public void publish(Event event) {
        for (EventHandler handler : handlers) {
            handler.handle(event);
        }
    }

    /** Adds a handler if it is non-null. */
    @Override
    public void register(EventHandler handler) {
        if (handler != null) {
            handlers.add(handler);
        }
    }
}
