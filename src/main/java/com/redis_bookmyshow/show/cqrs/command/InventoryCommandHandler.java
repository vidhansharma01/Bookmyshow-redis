package com.redis_bookmyshow.show.cqrs.command;

import com.redis_bookmyshow.show.cqrs.EventBus;
import com.redis_bookmyshow.show.cqrs.ItemCreated;
import com.redis_bookmyshow.show.cqrs.StockAdded;
import com.redis_bookmyshow.show.cqrs.StockRemoved;

/** Handles commands by updating the write model and emitting events. */
public final class InventoryCommandHandler {
    private final InventoryWriteRepository repository;
    private final EventBus bus;

    public InventoryCommandHandler(InventoryWriteRepository repository, EventBus bus) {
        this.repository = repository;
        this.bus = bus;
    }

    /** Creates a new item in the write model and publishes ItemCreated. */
    public void handle(CreateItemCommand command) {
        if (repository.exists(command.itemId())) {
            throw new IllegalStateException("Item already exists: " + command.itemId());
        }
        if (command.name().isBlank()) {
            throw new IllegalArgumentException("Item name is required");
        }
        InventoryItem item = new InventoryItem(command.itemId(), command.name());
        repository.save(item);
        bus.publish(new ItemCreated(item.id(), item.name()));
    }

    /** Adds stock and publishes StockAdded. */
    public void handle(AddStockCommand command) {
        InventoryItem item = requireItem(command.itemId());
        if (command.amount() <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        item.addStock(command.amount());
        repository.save(item);
        bus.publish(new StockAdded(item.id(), command.amount()));
    }

    /** Removes stock and publishes StockRemoved. */
    public void handle(RemoveStockCommand command) {
        InventoryItem item = requireItem(command.itemId());
        if (command.amount() <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (item.quantity() < command.amount()) {
            throw new IllegalStateException("Insufficient stock");
        }
        item.removeStock(command.amount());
        repository.save(item);
        bus.publish(new StockRemoved(item.id(), command.amount()));
    }

    /** Loads an item or throws if the item is missing. */
    private InventoryItem requireItem(String itemId) {
        InventoryItem item = repository.get(itemId);
        if (item == null) {
            throw new IllegalArgumentException("Item not found: " + itemId);
        }
        return item;
    }
}
