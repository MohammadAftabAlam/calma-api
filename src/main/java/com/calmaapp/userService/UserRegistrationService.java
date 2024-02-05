package com.calmaapp.userService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.calmaapp.authentication.JwtHelper;
import com.calmaapp.entity.User;
import com.calmaapp.payloads.UserDTO;
import com.calmaapp.repository.UserRepository;

@Service
public class UserRegistrationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtHelper jwtHelper;

    public String registerUser(UserDTO userDTO) {
    	
    	final Logger log = LoggerFactory.getLogger(UserRegistrationService.class);

		try {
            String phoneNumber = userDTO.getPhoneNumber();

            // Validate if the provided password is secure enough (add your validation logic)
            validatePassword(userDTO.getPassword());

            // Hash the password using PasswordEncoder
            String hashedPassword = passwordEncoder.encode(userDTO.getPassword());

            // Log successful registration
            log.info("User registered successfully: {}", userDTO);

            // Convert UserDTO to User and save in the repository
            User user = convertToUser(userDTO, hashedPassword);
            userRepository.save(user);

            // No need to generate JWT token here

            // Return a success message or any other necessary response
            return "User registered successfully";
        } catch (Exception e) {
            // Log and rethrow the exception
            log.error("Error during user registration", e);
            throw new RuntimeException("Error during user registration", e);
        }
    }

    private User convertToUser(UserDTO userDTO, String hashedPassword) {
        User user = new User();
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setPassword(hashedPassword);
        user.setAge(userDTO.getAge());
        user.setGender(userDTO.getGender());
        user.setName(userDTO.getName());
        user.setLocation(userDTO.getLocation());
        user.setUserType(userDTO.getUserType());
        // Set other user details as needed
        return user;
    }

 // Modify the method to return a String message
    private String validatePassword(String password) {
        try {
            // Password validation logic
            if (password.length() < 8 || password.length() > 20) {
                throw new IllegalArgumentException("Password must be between 8 and 20 characters.");
            }

            if (!password.matches(".*[A-Z].*")) {
                throw new IllegalArgumentException("Password must contain at least one uppercase letter.");
            }

            if (!password.matches(".*[a-z].*")) {
                throw new IllegalArgumentException("Password must contain at least one lowercase letter.");
            }

            if (!password.matches(".*\\d.*")) {
                throw new IllegalArgumentException("Password must contain at least one digit.");
            }

            if (!password.matches(".*[!@#$%^&*()-_=+{}\\[\\]:;\"'<>,.?/\\\\].*")) {
                throw new IllegalArgumentException("Password must contain at least one special character.");
            }

            // If validation succeeds, return a success message
            return "Password is valid";
        } catch (IllegalArgumentException e) {
            // If validation fails, return the exception message
            return e.getMessage();
        }
    }


}

