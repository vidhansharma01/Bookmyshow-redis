package com.redis_bookmyshow.show.cqrs.query;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** In-memory read model updated by projections. */
public final class InventoryReadModel {
    private final Map<String, ItemView> items = new ConcurrentHashMap<>();

    /** Adds a read-model view when an item is created. */
    public void create(String itemId, String name, Instant updatedAt) {
        items.put(itemId, new ItemView(itemId, name, 0, updatedAt));
    }

    /** Adjusts quantity in the read model for stock changes. */
    public void adjustQuantity(String itemId, long delta, Instant updatedAt) {
        items.compute(itemId, (id, current) -> {
            if (current == null) {
                throw new IllegalStateException("Read model missing item: " + itemId);
            }
            long newQty = current.quantity() + delta;
            return new ItemView(itemId, current.name(), newQty, updatedAt);
        });
    }

    /** Returns a read-model view by item id. */
    public ItemView get(String itemId) {
        return items.get(itemId);
    }

    /** Returns all read-model views. */
    public List<ItemView> all() {
        return List.copyOf(items.values());
    }
}
