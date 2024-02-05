package com.calmaapp.userService;

import com.calmaapp.entity.User;
import com.calmaapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.calmaapp.authentication.JwtHelper;
import com.calmaapp.payloads.UserDTO;

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
                List<String> roles = user.getRoles(); 
                
                jwtHelper.setUser(user);

                String jwtToken = jwtHelper.generateToken(user.getPhoneNumber(), roles);

          
                user.setJwtToken(jwtToken);
                userRepository.save(user);

                return ResponseEntity.ok("Login successful! Token: " + jwtToken);
            } else {
                return ResponseEntity.status(401).body("Incorrect password. Please try again.");
            }
        } else {
            return ResponseEntity.status(404).body("User not found. Please register.");
        }
    }
}
