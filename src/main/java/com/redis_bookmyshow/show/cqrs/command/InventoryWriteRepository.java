package com.redis_bookmyshow.show.cqrs.command;

/** Write-side repository abstraction used by the command handler. */
public interface InventoryWriteRepository {
    /** Returns the write-model item by id (or null if missing). */
    InventoryItem get(String id);

    /** Returns true if the item exists in the write model. */
    boolean exists(String id);

    /** Saves the write-model item. */
    void save(InventoryItem item);
}
