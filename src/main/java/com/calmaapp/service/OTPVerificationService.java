package com.calmaapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.calmaapp.entity.User;
import com.calmaapp.repository.UserRepository;

@Service
public class OTPVerificationService {
    
    @Autowired
    private UserRepository userRepository;

    public boolean verifyOTP(String email, String enteredOTP) {
       
        String storedOTP = retrieveOTPFromDatabase(email);
        return enteredOTP.equals(storedOTP);
    }

    private String retrieveOTPFromDatabase(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getOtp() != null) {
            return user.getOtp();
        } else {
            
            return null;
        }
    }
    
}

