package com.redis_bookmyshow.show.cqrs;

/** Event emitted when an item is created. */
public final class ItemCreated extends BaseEvent {
    private final String itemId;
    private final String name;

    public ItemCreated(String itemId, String name) {
        this.itemId = itemId;
        this.name = name;
    }

    /** Returns the id of the created item. */
    public String itemId() {
        return itemId;
    }

    /** Returns the name of the created item. */
    public String name() {
        return name;
    }
}
