package com.calmaapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.calmaapp.entity.Review;
import com.calmaapp.entity.Salon;
import com.calmaapp.entity.User;

import com.calmaapp.payloads.ReviewDTO;
import com.calmaapp.payloads.UserDTO;
import com.calmaapp.repository.SalonRepository;
import com.calmaapp.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void updateUserLocation(Long userId, double latitude, double longitude) {
        Optional<User> optionalUser = userRepository.findById(userId);
        optionalUser.ifPresent(user -> {
            user.setLatitude(latitude);
            user.setLongitude(longitude);
            userRepository.save(user);
        });
    }

    public boolean updateUserDetails(Long userId, UserDTO updatedUserDTO) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();

            existingUser.setName(updatedUserDTO.getName());
            existingUser.setAge(updatedUserDTO.getAge());
            existingUser.setLocation(updatedUserDTO.getLocation());
            existingUser.setGender(updatedUserDTO.getGender());
            // Update other fields as needed

            userRepository.save(existingUser);
            return true;
        }
        return false;
    }

    public User getUserById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        return userOptional.orElse(null);
    }

    public User getUserByPhoneNumber(String name) {
        return userRepository.findByPhoneNumber(name);
    }

    @Autowired
    private SalonRepository salonRepository;

    @Autowired
    private UserService userService;

    public ResponseEntity<String> addReviewToSalon(Long salonId, ReviewDTO reviewDTO, Principal principal) {
        try {
            User user = userService.getUserByPhoneNumber(principal.getName());
            Salon salon = salonRepository.findById(salonId).orElse(null);

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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add review");
        }
    }

    public ResponseEntity<List<ReviewDTO>> getReviewsForSalon(Long salonId) {
        try {
            Salon salon = salonRepository.findById(salonId).orElse(null);
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

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public User findByResetToken(String resetToken) {
        return userRepository.findByResetToken(resetToken);
    }
}
