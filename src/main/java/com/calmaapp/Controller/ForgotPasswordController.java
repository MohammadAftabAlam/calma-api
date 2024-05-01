package com.calmaapp.Controller;

import com.calmaapp.entity.Salon;
import com.calmaapp.entity.User;
import com.calmaapp.service.EmailService;
import com.calmaapp.service.SalonService;
import com.calmaapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/apiss")
public class ForgotPasswordController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SalonService salonService;

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        String email = request.getEmail();

        // Check if the email exists in the database
        User user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // Generate a password reset token
        String resetToken = UUID.randomUUID().toString();
        user.setResetToken(resetToken);
        userService.save(user);

        // Send password reset email
        String resetLink = "http://your-app.com/reset-password?token=" + resetToken;
        emailService.sendPasswordResetEmail(email, resetLink);

        return ResponseEntity.ok("Password reset email sent successfully");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        String resetToken = request.getToken();
        String newPassword = request.getPassword();

        // Find user by reset token
        User user = userService.findByResetToken(resetToken);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid reset token");
        }

        // Update user's password
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        userService.save(user);

        return ResponseEntity.ok("Password reset successfully");
    }
}


//package com.calmaapp.Controller;
//
//
//
//import com.calmaapp.entity.Salon;
//import com.calmaapp.entity.User;
//import com.calmaapp.service.EmailService;
//import com.calmaapp.service.SalonService;
//import com.calmaapp.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/apiss")
//public class ForgotPasswordController {
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private EmailService emailService;
// @Autowired
//    private SalonService salonService;
//
//    @PostMapping("/forgot-password")
//    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
//        String email = request.getEmail();
//
//        // Check if the email exists in the database
//        User user = userService.findByEmail(email);
//        if (user == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
//        }
//
//        // Generate a password reset token
//        String resetToken = UUID.randomUUID().toString();
//        user.setResetToken(resetToken);
//        userService.save(user);
//
//        // Send password reset email
//        String resetLink = "http://your-app.com/reset-password?token=" + resetToken;
//        emailService.sendPasswordResetEmail(email, resetLink);
//
//        return ResponseEntity.ok("Password reset email sent successfully");
//    }
//
//    @PostMapping("/reset-password")
//    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
//        String resetToken = request.getToken();
//        String newPassword = request.getPassword();
//
//        // Find user by reset token
//        User user = userService.findByResetToken(resetToken);
//        if (user == null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid reset token");
//        }
//
//        // Update user's password
//        user.setPassword(passwordEncoder.encode(newPassword));
//        user.setResetToken(null);
//        userService.save(user);
//
//        return ResponseEntity.ok("Password reset successfully");
//    }
//
//
//}
//
