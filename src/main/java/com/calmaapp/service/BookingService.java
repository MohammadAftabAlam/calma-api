package com.calmaapp.service;

import com.calmaapp.entity.Booking;
import com.calmaapp.entity.BookingStatus;
import com.calmaapp.entity.User;
import com.calmaapp.repository.BookingRepository;
import com.calmaapp.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;
@Autowired
    private UserRepository userRepository;

    @Autowired
    private WebSocketService webSocketService; // Assuming you have a WebSocketService to handle WebSocket notifications

    public Booking createBooking(Booking booking) {
        // Extract user ID from the authentication token
        Long userId = extractUserIdFromToken();
        if (userId == null) {
            // Handle case where user ID cannot be extracted
            // This could involve sending an error message or logging the issue
            return null;
        }

        // Set the user ID in the booking object
        booking.setCustomerId(userId);

        // Send notification to salon owner about new booking request
        webSocketService.sendNotificationToSalonOwner(booking);

        // Save the booking details in the database
        return bookingRepository.save(booking);
    }

    public Long extractUserIdFromToken() {
        // Extract user ID from the authentication token stored in the security context
        // Assuming the user ID is stored as the principal in the authentication object
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            // Assuming UserDetails is your custom UserDetails implementation
            UserDetails userDetails = (UserDetails) principal;
    
            // Retrieve the user ID from the UserDetails object
            Long userId = Long.parseLong(userDetails.getUsername());
    
            return userId;
        } else {
            // Handle case where user ID cannot be extracted
            // This could involve returning null or logging the issue
            return null;
        }
    }
    
    public User findUserByIdFromToken() {
        // Extract user ID from the authentication token
        Long userId = extractUserIdFromToken();
    
        if (userId != null) {
            // Retrieve the user from the database using the user ID
            Optional<User> userOptional = userRepository.findById(userId);
    
            // Check if the user exists in the database
            if (userOptional.isPresent()) {
                return userOptional.get();
            } else {
                // Handle case where user does not exist in the database
                // This could involve throwing an exception, returning null, or logging the issue
                return null;
            }
        } else {
            // Handle case where user ID is not available
            // This could involve throwing an exception, returning null, or logging the issue
            return null;
        }
    }
    

    public void confirmBooking(Long bookingId) {
        // Retrieve the booking from the database
        // Implementation of confirmBooking method remains the same
    }

    public boolean isSlotAvailableManually(LocalDateTime requestedTime) {
        // Fetch bookings from the database for the requested time
        List<Booking> bookings = bookingRepository.findByBookingDateTime(requestedTime);
    
        // Check if any bookings exist for the requested time slot
        return bookings.isEmpty();
    }
    
}
