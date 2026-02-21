package com.redis_bookmyshow.show.cqrs.command;

import java.util.Objects;

/** Command to increase stock for an item. */
public final class AddStockCommand implements Command {
    private final String itemId;
    private final long amount;

    public AddStockCommand(String itemId, long amount) {
        this.itemId = Objects.requireNonNull(itemId, "itemId");
        this.amount = amount;
    }

    /** Returns the item id whose stock increases. */
    public String itemId() {
        return itemId;
    }

    /** Returns the amount to add. */
    public long amount() {
        return amount;
    }
}
