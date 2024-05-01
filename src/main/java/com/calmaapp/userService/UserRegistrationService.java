package com.calmaapp.userService;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.calmaapp.repository.UserRepository;
import com.calmaapp.service.EmailService;
import com.calmaapp.entity.User;
import com.calmaapp.payloads.UserDTO;

@Service
public class UserRegistrationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public ResponseEntity<String> registerUser(UserDTO userDTO) {
        // Check if the phone number or email already exists in the database
        if (userRepository.existsByPhoneNumber(userDTO.getPhoneNumber())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Phone number already taken. Please choose a different phone number.");
        }

        if (userRepository.existsByEmail(userDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Email already taken. Please choose a different email.");
        }

        try {
            // Proceed with user registration
            String hashedPassword = passwordEncoder.encode(userDTO.getPassword());
            User user = convertToUser(userDTO, hashedPassword);

            // Generate OTP and save it in the database
            String otp = generateOTP(6);
            user.setOtp(otp); // Assuming there's an 'otp' field in the User entity
            userRepository.save(user);

            // Send OTP email
            emailService.sendOTP(user.getEmail(), otp);

            return ResponseEntity.ok()
                    .body("User registered successfully. Please check your email for OTP verification.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to register user. Please try again.");
        }
    }

    private String generateOTP(int length) {
        // Generate a random OTP of the specified length
        Random random = new Random();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < length; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }

    private User convertToUser(UserDTO userDTO, String hashedPassword) {
        User user = new User();
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setPassword(hashedPassword);
        user.setEmail(userDTO.getEmail());
        user.setEmail(userDTO.getEmail());
        user.setAge(userDTO.getAge());
        user.setGender(userDTO.getGender());
        user.setName(userDTO.getName());
        user.setLocation(userDTO.getLocation());
        user.setUserType(userDTO.getUserType());

        // Set other user details as needed
        return user;
    }
}
