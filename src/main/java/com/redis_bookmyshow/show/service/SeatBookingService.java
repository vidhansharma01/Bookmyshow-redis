package com.redis_bookmyshow.show.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SeatBookingService {

    private final RedisLockService lockService;

    public SeatBookingService(RedisLockService lockService) {
        this.lockService = lockService;
    }

    public String bookSeat(String showId, String seatId, String userId) {

        String lockKey = "seat:lock:" + showId + ":" + seatId;
        String lockValue = UUID.randomUUID().toString();
        long ttl = 300; // 5 minutes

        boolean locked = lockService.acquireLock(lockKey, lockValue, ttl);

        if (!locked) {
            return "Seat already locked by another user";
        }

        try {
            // 🔥 Critical section
            // 1. Check seat availability from DB
            // 2. Mark seat as BOOKED in DB
            // 3. Proceed with payment

            System.out.println("Seat booked successfully!");

            return "Seat booked successfully";

        } finally {
            // Safe unlock
            lockService.releaseLock(lockKey, lockValue);
        }
    }
}
