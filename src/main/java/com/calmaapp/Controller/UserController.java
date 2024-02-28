package com.calmaapp.Controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.calmaapp.authentication.LogoutRequest;
import com.calmaapp.authentication.TokenBlacklistService;
import com.calmaapp.entity.Review;
import com.calmaapp.entity.Salon;
import com.calmaapp.entity.User;
import com.calmaapp.payloads.*;
import com.calmaapp.repository.SalonRepository;
import com.calmaapp.repository.UserRepository;
import com.calmaapp.service.SalonService;
import com.calmaapp.service.UserService;
import com.calmaapp.userService.UserLoginService;
import com.calmaapp.userService.UserLogoutService;
import com.calmaapp.userService.UserRegistrationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

import org.springframework.security.core.Authentication;


@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRegistrationService userRegistrationService;

    @Autowired
    private UserLoginService userLoginService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private SalonService salonService;


    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @Autowired
    private UserLogoutService userLogoutService;
    
    @Autowired
    private SalonRepository salonRepository;

    private Logger log;
  
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDTO userDTO) {
        return userRegistrationService.registerUser(userDTO);
    }


    
    private String validatePassword(String password) {
        String passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()-_=+{}\\[\\]:;\"'<>,.?/\\\\])[A-Za-z\\d!@#$%^&*()-_=+{}\\[\\]:;\"'<>,.?/\\\\]{8,20}$";
        if (!password.matches(passwordPattern)) {
            throw new IllegalArgumentException("Password must be between 8 and 20 characters and contain at least one uppercase letter, one lowercase letter, one digit, and one special character.");
        }
        return "Password is valid";
    }
    



	@PostMapping("/login")
public ResponseEntity<String> loginUser(@RequestBody UserDTO userDTO) {
    try {
        // Attempt to login the user based on the provided phone number
        ResponseEntity<String> response = userLoginService.loginUser(userDTO);
        
        if (response.getStatusCode().is2xxSuccessful()) {
            // If login successful, return the JWT token in the response
            return ResponseEntity.ok(response.getBody());
        } else {
            // If login failed, return the response as it is
            return response;
        }
    } catch (Exception e) {
        // Handle any exceptions that occur during login process
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred during login process: " + e.getMessage());
    }
}

     
    @GetMapping("/user/{userId}")
    public ResponseEntity<UserDTO> getUserDetails(@PathVariable Long userId) {
        try {
            User user = userService.getUserById(userId);

            if (user != null) {
                // Map User entity to UserDTO if needed
                UserDTO userDTO = new UserDTO(user);
                return ResponseEntity.ok(userDTO);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    

    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(@RequestBody Map<String, String> requestParams) {
        String phoneNumber = requestParams.get("phoneNumber");
        String jwtToken = requestParams.get("jwtToken");

        // Check if the token is blacklisted
        if (tokenBlacklistService.isTokenBlacklisted(jwtToken)) {
            return ResponseEntity.status(401).body("Invalid token. User already logged out.");
        }

        // Logout user (simulate logout)
        userLogoutService.logoutUser(phoneNumber, jwtToken);

        return ResponseEntity.ok("User logged out successfully");
    }


    @PutMapping("/updateLocation/{userId}")
    public ResponseEntity<String> updateUserLocation(
            @PathVariable Long userId,
            @RequestBody Map<String, Double> location) {
        try {
            // Fetch the user by userId
            User user = userService.getUserById(userId);

            if (user != null) {
                // Extract latitude and longitude from the request body
                Double latitude = location.get("latitude");
                Double longitude = location.get("longitude");

                // Update user location
                userService.updateUserLocation(userId, latitude, longitude);

                return ResponseEntity.ok("User location updated successfully");
            } else {
                return ResponseEntity.status(404).body("User not found");
            }
        } catch (Exception e) {
            // Handle exception
            return ResponseEntity.status(500).body("Failed to update user location");
        }
    }
    
    @PutMapping("/updateUser/{userId}")
    public ResponseEntity<String> updateUserDetails(
            @PathVariable Long userId,
            @RequestBody UserDTO updatedUserDTO) {
        try {
            // Fetch the user by userId
            User user = userService.getUserById(userId);

            if (user != null) {
                // Update user details
                boolean isUpdated = userService.updateUserDetails(userId, updatedUserDTO);

                if (isUpdated) {
                    return ResponseEntity.ok("User details updated successfully");
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update user details");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
        } catch (Exception e) {
            
            log.error("Exception while updating user details", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update user details");
        }
    }
    @PostMapping("/salons/{salonId}/reviews")
    public ResponseEntity<String> addReviewToSalon(
            @PathVariable Long salonId,
            @RequestBody ReviewDTO reviewDTO,
            Principal principal) { // Include Principal for user authentication
        try {
            User user = userService.getUserByPhoneNumber(principal.getName());
            Salon salon = salonService.getSalonById(salonId);

            System.out.println("User: " + user);  // Add this line
            System.out.println("Salon: " + salon);  // Add this line

            if (user != null && salon != null) {
                Review review = new Review();
                review.setRating(reviewDTO.getRating());
                review.setComment(reviewDTO.getComment());
                review.setUser(user);
                review.setSalon(salon);

                salon.getReviews().add(review);
                salonRepository.save(salon);

                return ResponseEntity.ok("Review added successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User or salon not found");
            }
        } catch (Exception e) {
            e.printStackTrace();  // Add this line
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add review");
        }
    }

    @GetMapping("/salons/{salonId}/reviews")
    public ResponseEntity<List<ReviewDTO>> getReviewsForSalon(@PathVariable Long salonId) {
        try {
            Salon salon = salonService.getSalonById(salonId);
            if (salon != null) {
                List<ReviewDTO> reviews = salon.getReviews().stream()
                        .map(this::mapToReviewDTO)
                        .collect(Collectors.toList());
                return ResponseEntity.ok(reviews);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
  
    
    private ReviewDTO mapToReviewDTO(Review review) {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setRating(review.getRating());
        reviewDTO.setComment(review.getComment());
        return reviewDTO;
    }

//     @Transactional
//     @GetMapping("/salons/{salonId}/distance")
//     public ResponseEntity<Double> getDistanceForSalon(@PathVariable Long salonId, Authentication authentication) {
//     try {
//         // Check if user is authenticated and is an instance of User
//         if (authentication != null && authentication.getPrincipal() instanceof User) {
//             User user = (User) authentication.getPrincipal();
//             Salon salon = salonService.getSalonById(salonId);

//             // Check if the salon exists
//             if (salon != null) {
//                 // Update salon distance based on user's location
//                 salonService.updateSalonDistance(salon, user);

//                 // Get the updated distance from the salon object
//                 Double distance = salon.getDistanceFromCustomer();
                
//                 // Check if distance is available
//                 if (distance != null) {
//                     // Return the distance in the response
//                     return ResponseEntity.ok(distance);
//                 } else {
//                     // If distance is null, return not found
//                     return ResponseEntity.notFound().build();
//                 }
//             } else {
//                 // If salon is null, return not found
//                 return ResponseEntity.notFound().build();
//             }
//         } else {
//             // If user authentication fails, return unauthorized
//             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//         }
//     } catch (Exception e) {
//         // Log the exception details
//         e.printStackTrace();
//         // If an exception occurs, return internal server error
//         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//     }
// }

// @PutMapping("/{salonId}/updateDistance")
//     public ResponseEntity<String> updateSalonDistance(@PathVariable Long salonId, HttpServletRequest request) {
//         Salon salon = salonService.getSalonById(salonId);
//         if (salon == null) {
//             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Salon not found");
//         }
//         ResponseEntity<String> response = salonService.updateSalonDistance(salon, request);
//         return response;
//     }
@PostMapping("/salon/update-distance/{salonId}")
public ResponseEntity<String> updateSalonDistance(@PathVariable Long salonId,
                                                   @RequestParam Double userLatitude,
                                                   @RequestParam Double userLongitude) {
    try {
        return salonService.updateSalonDistance(salonId, userLatitude, userLongitude);
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred while processing the request.");
    }
}


    

    
    
}

