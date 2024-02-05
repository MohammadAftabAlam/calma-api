package com.calmaapp.userService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.calmaapp.authentication.TokenBlacklistService;

@Service
public class UserLogoutService {

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @Value("${jwt.secret}")
    private String jwtSecret;  // Add this field for JWT secret

    public void logoutUser(String phoneNumber, String jwtToken) {
        // Blacklist the token
        tokenBlacklistService.blacklistToken(jwtToken);

        // Additional logout logic (if needed) goes here
    }
}
