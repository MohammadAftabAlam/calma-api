package com.calmaapp.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.calmaapp.service.OTPVerificationService;

@RestController
public class OTPVerificationController {

    @Autowired
    private OTPVerificationService otpVerificationService;

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOTP(@RequestParam String email, @RequestParam String enteredOTP) {
        boolean isOTPVerified = otpVerificationService.verifyOTP(email, enteredOTP);
        if (isOTPVerified) {
            // Proceed with the action (e.g., user registration)
            return ResponseEntity.ok("OTP verification successful");
        } else {
            return ResponseEntity.badRequest().body("Invalid OTP. Please try again.");
        }
    }
}

