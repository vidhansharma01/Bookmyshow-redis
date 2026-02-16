package com.redis_bookmyshow.show.service;

import com.redis_bookmyshow.show.dto.User;
import com.redis_bookmyshow.show.outbox.OutboxEvent;
import com.redis_bookmyshow.show.outbox.OutboxService;
import com.redis_bookmyshow.show.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository repository;
    private final OutboxService outboxService;
    private final Object outboxLock = new Object();

    public UserService(UserRepository repository, OutboxService outboxService) {
        this.repository = repository;
        this.outboxService = outboxService;
    }

    // READ → Cache result
    @Cacheable(value = "users", key = "#id")
    public User getUser(Long id) {
        log.info("Fetching user from DB...");
        return repository.findById(id);
    }

    // UPDATE → Update cache
    @CachePut(value = "users", key = "#user.id")
    public User updateUser(User user) {
        OutboxEvent event = outboxService.userUpdated(user);
        synchronized (outboxLock) {
            // Update the user and enqueue the outbox event together.
            log.info("Updating user in DB & cache...");
            User saved = repository.save(user);
            outboxService.add(event);
            return saved;
        }
    }

    // DELETE → Remove cache
    @CacheEvict(value = "users", key = "#id")
    public void deleteUser(Long id) {
        OutboxEvent event = outboxService.userDeleted(id);
        synchronized (outboxLock) {
            // Delete the user and enqueue the outbox event together.
            log.info("Deleting user & evicting cache...");
            repository.delete(id);
            outboxService.add(event);
        }
    }

    // CLEAR ALL CACHE
    @CacheEvict(value = "users", allEntries = true)
    public void clearCache() {
        log.info("Clearing all users cache...");
    }
}
