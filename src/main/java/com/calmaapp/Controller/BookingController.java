package com.calmaapp.Controller;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.calmaapp.entity.Booking;
import com.calmaapp.service.BookingService;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    // Endpoint to handle the creation of new booking requests
    @PostMapping("/create")
    public ResponseEntity<?> createBooking(@RequestBody Booking booking) {
        try {
            Booking createdBooking = bookingService.createBooking(booking);
            return ResponseEntity.ok(createdBooking);
        } catch (Exception e) {
            // Handle any exceptions and return an appropriate response
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Error creating booking: " + e.getMessage());
        }
    }

    // Endpoint to handle the confirmation of booking requests by the salon owner
    @PostMapping("/confirm/{bookingId}")
    public ResponseEntity<?> confirmBooking(@PathVariable Long bookingId) {
        try {
            bookingService.confirmBooking(bookingId);
            return ResponseEntity.ok("Booking confirmed successfully");
        } catch (Exception e) {
            // Handle any exceptions and return an appropriate response
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Error confirming booking: " + e.getMessage());
        }
    }
}
