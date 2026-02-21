package com.redis_bookmyshow.show.cqrs.command;

import java.util.Objects;

/** Command to create a new inventory item. */
public final class CreateItemCommand implements Command {
    private final String itemId;
    private final String name;

    public CreateItemCommand(String itemId, String name) {
        this.itemId = Objects.requireNonNull(itemId, "itemId");
        this.name = Objects.requireNonNull(name, "name");
    }

    /** Returns the item id to create. */
    public String itemId() {
        return itemId;
    }

    /** Returns the item name. */
    public String name() {
        return name;
    }
}
