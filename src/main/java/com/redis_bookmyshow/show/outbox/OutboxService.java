package com.redis_bookmyshow.show.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redis_bookmyshow.show.dto.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class OutboxService {

    private static final String AGGREGATE_USER = "User";

    private final OutboxRepository repository;
    private final ObjectMapper objectMapper;

    public OutboxService(OutboxRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    public OutboxEvent userUpdated(User user) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("id", user.getId());
        payload.put("name", user.getName());
        payload.put("email", user.getEmail());
        return new OutboxEvent(
                UUID.randomUUID(),
                AGGREGATE_USER,
                String.valueOf(user.getId()),
                "USER_UPDATED",
                toJson(payload),
                OutboxStatus.PENDING,
                Instant.now(),
                null,
                0
        );
    }

    public OutboxEvent userDeleted(Long userId) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("id", userId);
        return new OutboxEvent(
                UUID.randomUUID(),
                AGGREGATE_USER,
                String.valueOf(userId),
                "USER_DELETED",
                toJson(payload),
                OutboxStatus.PENDING,
                Instant.now(),
                null,
                0
        );
    }

    public void add(OutboxEvent event) {
        repository.add(event);
    }

    private String toJson(Object payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException ex) {
            // If serialization fails, still emit a minimal payload.
            return "{}";
        }
    }
}
