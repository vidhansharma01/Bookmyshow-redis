package com.redis_bookmyshow.show.cqrs;

/** Event emitted when stock is added. */
public final class StockAdded extends BaseEvent {
    private final String itemId;
    private final long amount;

    public StockAdded(String itemId, long amount) {
        this.itemId = itemId;
        this.amount = amount;
    }

    /** Returns the item id. */
    public String itemId() {
        return itemId;
    }

    /** Returns the amount added. */
    public long amount() {
        return amount;
    }
}
