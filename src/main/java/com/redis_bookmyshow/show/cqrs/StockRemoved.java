package com.redis_bookmyshow.show.cqrs;

/** Event emitted when stock is removed. */
public final class StockRemoved extends BaseEvent {
    private final String itemId;
    private final long amount;

    public StockRemoved(String itemId, long amount) {
        this.itemId = itemId;
        this.amount = amount;
    }

    /** Returns the item id. */
    public String itemId() {
        return itemId;
    }

    /** Returns the amount removed. */
    public long amount() {
        return amount;
    }
}
