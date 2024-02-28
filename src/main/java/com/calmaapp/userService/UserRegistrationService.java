package com.calmaapp.userService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.calmaapp.repository.UserRepository;
import com.calmaapp.entity.User;
import com.calmaapp.payloads.UserDTO;

@Service
public class UserRegistrationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
            userRepository.save(user);

            return ResponseEntity.ok().body("User registered successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to register user. Please try again.");
        }
    }

    private User convertToUser(UserDTO userDTO, String hashedPassword) {
        User user = new User();
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setPassword(hashedPassword);
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

