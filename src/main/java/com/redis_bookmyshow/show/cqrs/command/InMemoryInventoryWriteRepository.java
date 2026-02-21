package com.redis_bookmyshow.show.cqrs.command;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** In-memory write-side repository for inventory items. */
public final class InMemoryInventoryWriteRepository implements InventoryWriteRepository {
    private final Map<String, InventoryItem> store = new ConcurrentHashMap<>();

    /** Loads an item from the in-memory write store. */
    @Override
    public InventoryItem get(String id) {
        return store.get(id);
    }

    /** Checks existence in the in-memory write store. */
    @Override
    public boolean exists(String id) {
        return store.containsKey(id);
    }

    /** Saves to the in-memory write store. */
    @Override
    public void save(InventoryItem item) {
        store.put(item.id(), item);
    }
}
