package com.calmaapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.calmaapp.entity.Booking;
import com.calmaapp.entity.BookingStatus;
import com.calmaapp.entity.Salon;
import com.calmaapp.entity.ServicesProvided;
import com.calmaapp.entity.User;
import com.calmaapp.repository.BookingRepository;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    // public Booking createBooking(Booking booking) {
    //     // Generate a unique code (e.g., OTP)
    //     String confirmationCode = generateConfirmationCode();
    
    //     // Set the confirmation code for the booking
    //     booking.setConfirmationCode(confirmationCode);

    //     Salon salon = booking.getSalon();
    // User user = booking.getUser();
    // ServicesProvided service = booking.getService();
    // LocalDateTime startTime = booking.getStartTime();
    // LocalDateTime endTime = booking.getEndTime();
    
    //     // Set the other properties on the booking object
    //     booking.setSalon(salon);
    //     booking.setUser(user);
    //     booking.setService(service);
    //     booking.setStatus(BookingStatus.PENDING);
    //     booking.setStartTime(startTime);
    //     booking.setEndTime(endTime);
    //     booking.setUserNotified(false);
    //     booking.setSalonNotified(false);
    
        // Save the booking to the database
    //     Booking savedBooking = bookingRepository.save(booking);
    
    //     // Send email confirmation to the user if the user is not null
    //     User user = savedBooking.getUser();
    //     if (user != null) {
    //         sendConfirmationEmail(user.getEmail(), confirmationCode);
    //     } else {
    //         // Log an error if the user is null
    //         System.err.println("User associated with the booking is null. Booking ID: " + savedBooking.getId());
    
    //         // You may also throw an exception here if desired
    //         // throw new IllegalStateException("User associated with the booking is null.");
    //     }
    
    //     return savedBooking;
    // }
    

    public void cancelBooking(Long bookingId) {
        // Retrieve the booking from the database
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isPresent()) {
            Booking booking = optionalBooking.get();

            // Remove the booking from the database
            bookingRepository.delete(booking);

            // Send a cancellation confirmation email to the user
            sendCancellationConfirmationEmail(booking.getUser().getEmail());
        } else {
            throw new NoSuchElementException("Booking with ID " + bookingId + " not found");
        }
    }

    private void sendCancellationConfirmationEmail(String recipientEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject("Booking Cancellation Confirmation");
        message.setText("Your booking has been cancelled. We hope to see you again soon!");
        javaMailSender.send(message);
    }

    private String generateConfirmationCode() {
        // Implement code generation logic (e.g., using UUID)
        return UUID.randomUUID().toString().substring(0, 6); // Example: Generate a 6-digit code
    }

   public void sendConfirmationEmail(String recipientEmail, String confirmationCode) {
    try {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject("Booking Confirmation");
        message.setText(
            "Your booking has been confirmed. Please use the following confirmation code: " + confirmationCode);

        javaMailSender.send(message);
    } catch (MailException e) {
        // Log any exceptions that occur during email sending
        System.err.println("Failed to send confirmation email to " + recipientEmail + ": " + e.getMessage());
        // You may also throw an exception or handle the error differently based on your application's requirements
    }
}


    public void updateBookingStatus(Long bookingId, BookingStatus newStatus) {
        // Retrieve the booking from the database
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isPresent()) {
            Booking booking = optionalBooking.get();

            // Update the booking status
            booking.setStatus(newStatus);
            bookingRepository.save(booking);

            // Optionally, send a confirmation email to the user if the booking is confirmed
            if (newStatus == BookingStatus.CONFIRMED) {
                sendCancellationConfirmationEmail(booking.getUser().getEmail());
            }
        } else {
            throw new NoSuchElementException("Booking with ID " + bookingId + " not found");
        }
    }
}
