package com.redis_bookmyshow.show.cqrs.query;

import com.redis_bookmyshow.show.cqrs.Event;
import com.redis_bookmyshow.show.cqrs.EventHandler;
import com.redis_bookmyshow.show.cqrs.ItemCreated;
import com.redis_bookmyshow.show.cqrs.StockAdded;
import com.redis_bookmyshow.show.cqrs.StockRemoved;

/** Projects events into the read model. */
public final class InventoryProjection implements EventHandler {
    private final InventoryReadModel readModel;

    public InventoryProjection(InventoryReadModel readModel) {
        this.readModel = readModel;
    }

    /** Updates the read model based on event type. */
    @Override
    public void handle(Event event) {
        if (event instanceof ItemCreated created) {
            readModel.create(created.itemId(), created.name(), created.occurredAt());
        } else if (event instanceof StockAdded added) {
            readModel.adjustQuantity(added.itemId(), added.amount(), added.occurredAt());
        } else if (event instanceof StockRemoved removed) {
            readModel.adjustQuantity(removed.itemId(), -removed.amount(), removed.occurredAt());
        }
    }
}
