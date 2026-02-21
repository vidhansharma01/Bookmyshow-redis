package com.redis_bookmyshow.show.cqrs.query;

import java.util.Comparator;
import java.util.List;

/** Provides query access to the read model. */
public final class InventoryQueryService {
    private final InventoryReadModel readModel;

    public InventoryQueryService(InventoryReadModel readModel) {
        this.readModel = readModel;
    }

    /** Returns the read-model view or throws if missing. */
    public ItemView getById(String itemId) {
        ItemView view = readModel.get(itemId);
        if (view == null) {
            throw new IllegalArgumentException("Item not found in read model: " + itemId);
        }
        return view;
    }

    /** Returns items with quantity at or below the given threshold. */
    public List<ItemView> findLowStock(long thresholdInclusive) {
        return readModel.all().stream()
                .filter(view -> view.quantity() <= thresholdInclusive)
                .sorted(Comparator.comparingLong(ItemView::quantity))
                .toList();
    }
}
