package com.redis_bookmyshow.show.controller;

import com.redis_bookmyshow.show.service.SeatBookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/booking")
public class BookingController {

    private final SeatBookingService bookingService;

    public BookingController(SeatBookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/seat")
    public ResponseEntity<String> bookSeat(
            @RequestParam String showId,
            @RequestParam String seatId,
            @RequestParam String userId) {

        return ResponseEntity.ok(
                bookingService.bookSeat(showId, seatId, userId)
        );
    }
}
