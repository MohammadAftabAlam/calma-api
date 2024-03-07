package com.calmaapp.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.calmaapp.entity.Booking;
import com.calmaapp.entity.BookingStatus;
import com.calmaapp.exception.BookingNotFoundException;
import com.calmaapp.exception.SlotNotFoundException;
import com.calmaapp.repository.BookingRepository;

@Service
public class SalonConfirmationService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    // Method to check slot availability and update booking status
    public void confirmBooking(Long bookingId) {
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isPresent()) {
            Booking booking = optionalBooking.get();
            // Implement slot availability check logic
            if (isSlotAvailable(booking)) {
                booking.setStatus(BookingStatus.CONFIRMED);
                bookingRepository.save(booking);
                sendConfirmationEmail(booking.getUser().getEmail());
            } else {
                throw new SlotNotFoundException("The slot is not available for booking.");
            }
        } else {
            throw new BookingNotFoundException("Booking with ID " + bookingId + " not found.");
        }
    }
    
    // Method to send email confirmation to user
    private void sendConfirmationEmail(String userEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userEmail);
        message.setSubject("Booking Confirmed");
        message.setText("Your booking has been confirmed by the salon.");
        javaMailSender.send(message);
    }

    private boolean isSlotAvailable(Booking booking) {
        // Retrieve the salon's existing bookings for the same date and time
        List<Booking> existingBookings = bookingRepository.findBySalonAndBookingDateTime(
                booking.getSalon(), booking.getBookingDateTime());
    
        // Check if the salon's capacity has been exceeded
        int salonCapacity = booking.getSalon().getCapacity();
        if (existingBookings.size() >= salonCapacity) {
            return false; // Slot not available if salon capacity has been exceeded
        }
    
        // Optionally, you can check other factors such as the salon's opening hours
        // For demonstration purposes, assume the slot is available if capacity is not exceeded
        return true;
    }

   public void rejectBooking(Long bookingId) {
        // Retrieve the booking from the database
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isPresent()) {
            Booking booking = optionalBooking.get();
            
            // Update booking status to rejected
            booking.setStatus(BookingStatus.REJECTED);
            bookingRepository.save(booking);
            
            // Send email notification to user
            sendCancellationEmail(booking.getUser().getEmail());
        } else {
            throw new NoSuchElementException("Booking with ID " + bookingId + " not found");
        }
    }

    // Method to send email notification to user
    private void sendCancellationEmail(String userEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userEmail);
        message.setSubject("Booking Rejected");
        message.setText("Your booking has been rejected by the salon.");
        javaMailSender.send(message);
    }
}