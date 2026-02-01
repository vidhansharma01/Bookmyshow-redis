package com.redis_bookmyshow.show.repository;

import com.redis_bookmyshow.show.dto.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserRepository {

    private static final Map<Long, User> DB = new HashMap<>();

    static {
        DB.put(1L, new User(1L, "Vidhan", "vidhan@gmail.com"));
        DB.put(2L, new User(2L, "Amit", "amit@gmail.com"));
    }

    public User findById(Long id) {
        return DB.get(id);
    }

    public User save(User user) {
        DB.put(user.getId(), user);
        return user;
    }

    public void delete(Long id) {
        DB.remove(id);
    }
}

