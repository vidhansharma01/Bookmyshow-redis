package com.redis_bookmyshow.show.cqrs.query;

import java.time.Instant;

/** Read-model view used by queries. */
public record ItemView(String id, String name, long quantity, Instant lastUpdated) {
}
