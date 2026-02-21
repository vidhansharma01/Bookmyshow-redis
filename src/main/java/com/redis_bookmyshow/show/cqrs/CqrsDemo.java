package com.redis_bookmyshow.show.cqrs;

import com.redis_bookmyshow.show.cqrs.command.AddStockCommand;
import com.redis_bookmyshow.show.cqrs.command.CreateItemCommand;
import com.redis_bookmyshow.show.cqrs.command.InMemoryInventoryWriteRepository;
import com.redis_bookmyshow.show.cqrs.command.InventoryCommandHandler;
import com.redis_bookmyshow.show.cqrs.command.InventoryWriteRepository;
import com.redis_bookmyshow.show.cqrs.command.RemoveStockCommand;
import com.redis_bookmyshow.show.cqrs.query.InventoryProjection;
import com.redis_bookmyshow.show.cqrs.query.InventoryQueryService;
import com.redis_bookmyshow.show.cqrs.query.InventoryReadModel;
import com.redis_bookmyshow.show.cqrs.query.ItemView;

public final class CqrsDemo {
    /** Wires write-side, read-side, and the event bus, then runs a short scenario. */
    public static void main(String[] args) {
        EventBus bus = new SimpleEventBus();
        InventoryReadModel readModel = new InventoryReadModel();
        bus.register(new InventoryProjection(readModel));

        InventoryWriteRepository writeRepository = new InMemoryInventoryWriteRepository();
        InventoryCommandHandler commandHandler = new InventoryCommandHandler(writeRepository, bus);
        InventoryQueryService queryService = new InventoryQueryService(readModel);

        String itemId = "item-1001";
        commandHandler.handle(new CreateItemCommand(itemId, "Red Chair"));
        commandHandler.handle(new AddStockCommand(itemId, 50));
        commandHandler.handle(new RemoveStockCommand(itemId, 12));

        ItemView view = queryService.getById(itemId);
        System.out.println("Read model view: " + view);
        System.out.println("Low stock (<= 20): " + queryService.findLowStock(20));
        System.out.println("Write model quantity: " + writeRepository.get(itemId).quantity());
    }
}
