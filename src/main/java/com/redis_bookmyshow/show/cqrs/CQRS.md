# CQRS (Command Query Responsibility Segregation)

## Intent
Separate **writes (commands)** from **reads (queries)** so each side can evolve independently. The write side enforces business rules and emits events; the read side builds optimized views for queries.

## This Repository
The CQRS example lives in `src/main/java/com/redis_bookmyshow/show/cqrs`.

Packages are split to mirror two services:
- **Command service package**: `src/main/java/com/redis_bookmyshow/show/cqrs/command`
- **Query service package**: `src/main/java/com/redis_bookmyshow/show/cqrs/query`
- **Shared contracts** (events + bus + demo): `src/main/java/com/redis_bookmyshow/show/cqrs`

### Components
- **Command side (write)**: `CreateItemCommand`, `AddStockCommand`, `RemoveStockCommand`, `InventoryItem`, `InventoryWriteRepository`, `InventoryCommandHandler`
- **Shared events**: `ItemCreated`, `StockAdded`, `StockRemoved`
- **Shared bus**: `EventBus` + `SimpleEventBus`
- **Query side (read)**: `InventoryReadModel`, `ItemView`, `InventoryProjection`, `InventoryQueryService`
- **Main**: `CqrsDemo` wires command and query services together and runs a short scenario

## Flow
1. A command is sent to the **command handler**.
2. The handler validates and updates the **write model**.
3. The handler publishes an **event**.
4. A **projection** consumes events and updates the **read model**.
5. Queries read from the **read model** only.

## How To Run
```powershell
mvn -q -DskipTests package
java -cp target/classes com.redis_bookmyshow.show.cqrs.CqrsDemo
```

## Notes
- This is an in-memory example to keep it minimal and runnable.
- The write model and read model are deliberately separate to illustrate CQRS.
