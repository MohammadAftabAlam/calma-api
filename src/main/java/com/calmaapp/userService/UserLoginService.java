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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserLoginService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtHelper jwtHelper;

    public ResponseEntity<Map<String, String>> loginUser(UserDTO userDTO) {
        String phoneNumber = userDTO.getPhoneNumber();
        User user = userRepository.findByPhoneNumber(phoneNumber);

        if (user == null) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "User not found");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        if (passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            // Successful login logic
            String jwtToken = jwtHelper.generateToken(user);
            user.setJwtToken(jwtToken);
            userRepository.save(user);

            Map<String, String> response = new HashMap<>();
            response.put("jwtToken", jwtToken);
            return ResponseEntity.ok().body(response);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Invalid password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
