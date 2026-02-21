package com.redis_bookmyshow.show.cqrs.command;

/** Write-model entity used by commands. */
public final class InventoryItem {
    private final String id;
    private final String name;
    private long quantity;

    public InventoryItem(String id, String name) {
        this.id = id;
        this.name = name;
        this.quantity = 0;
    }

    /** Returns the item id. */
    public String id() {
        return id;
    }

    /** Returns the item name. */
    public String name() {
        return name;
    }

    /** Returns the current quantity in the write model. */
    public long quantity() {
        return quantity;
    }

    /** Increases quantity in the write model. */
    public void addStock(long amount) {
        quantity += amount;
    }

    /** Decreases quantity in the write model. */
    public void removeStock(long amount) {
        quantity -= amount;
    }
}
