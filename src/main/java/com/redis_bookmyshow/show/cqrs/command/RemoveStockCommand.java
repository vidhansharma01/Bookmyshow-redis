package com.redis_bookmyshow.show.cqrs.command;

import java.util.Objects;

/** Command to decrease stock for an item. */
public final class RemoveStockCommand implements Command {
    private final String itemId;
    private final long amount;

    public RemoveStockCommand(String itemId, long amount) {
        this.itemId = Objects.requireNonNull(itemId, "itemId");
        this.amount = amount;
    }

    /** Returns the item id whose stock decreases. */
    public String itemId() {
        return itemId;
    }

    /** Returns the amount to remove. */
    public long amount() {
        return amount;
    }
}
