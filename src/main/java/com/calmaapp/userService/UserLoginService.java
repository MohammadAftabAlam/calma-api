package com.calmaapp.userService;

import com.calmaapp.entity.User;
import com.calmaapp.repository.UserRepository;
import com.calmaapp.authentication.JwtHelper;
import com.calmaapp.payloads.UserDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserLoginService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtHelper jwtHelper;

    public ResponseEntity<String> loginUser(UserDTO userDTO) {
        String phoneNumber = userDTO.getPhoneNumber();
        User user = userRepository.findByPhoneNumber(phoneNumber);

        if (user != null) {
            if (passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
                // Authentication successful, generate JWT token
                String jwtToken = jwtHelper.generateToken(user);
                user.setJwtToken(jwtToken);
                userRepository.save(user);
                return ResponseEntity.ok("Login successful! JWT Token: " + jwtToken);
            } else {
                // Incorrect password
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }
        } else {
            // User not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }        
}
