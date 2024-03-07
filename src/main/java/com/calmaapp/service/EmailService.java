package com.calmaapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.calmaapp.entity.User;
import com.calmaapp.repository.UserRepository;

import java.util.Optional;
import java.util.Random;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private UserRepository userRepository;

    public void sendOTP(String recipientEmail,String otp) {
        // String otp = generateOTP(6); // Generate a 6-digit OTP

        // // Send the OTP via email
        sendEmail(recipientEmail, "OTP Verification", "Your OTP for registration is: " + otp);
    }

    public void sendPasswordResetEmail(String to, String resetLink) {
        String subject = "Password Reset Request";
        String text = "Dear User,\n\nPlease click on the following link to reset your password:\n\n" + resetLink;
        sendEmail(to, subject, text);
    }

    private void sendEmail(String recipientEmail, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

   

    private void saveOTPToDatabase(String email, String otp) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setOtp(otp); // Set OTP in the User entity
            userRepository.save(user); // Save the updated User entity with OTP to the database
        } else {
            // Handle the case when the user is not found
            // This could involve logging an error or throwing an exception
            System.err.println("User with email " + email + " not found.");
        }
    }
    
    
}
