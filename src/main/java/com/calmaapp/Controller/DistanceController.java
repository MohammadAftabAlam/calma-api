package com.calmaapp.Controller;

import java.util.List;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.calmaapp.entity.Distance;
import com.calmaapp.entity.User;
import com.calmaapp.exception.DistanceNotFoundException;
import com.calmaapp.exception.UserNotFoundException;
import com.calmaapp.repository.DistanceRepository;
import com.calmaapp.repository.UserRepository;
import com.calmaapp.service.DistanceCalculationService;
import com.calmaapp.service.DistanceService;
import com.clmaapp.exception.SalonNotFoundException;

@RestController
@RequestMapping("/distances")
public class DistanceController {

    @Autowired
    private DistanceCalculationService distanceCalculationService;

    @Autowired
    private DistanceRepository distanceRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private final DistanceService distanceService;
    // Endpoint to calculate and store distances for a user
    @PostMapping("/calculate")
    public ResponseEntity<String> calculateDistances(@RequestParam User userId,
                                                     @RequestParam Double userLatitude,
                                                     @RequestParam Double userLongitude) {
        distanceCalculationService.calculateAndStoreDistances(userId, userLatitude, userLongitude);
        return ResponseEntity.ok("Distances calculated and stored successfully.");
    }

    @Autowired
    public DistanceController(DistanceService distanceService) {
        this.distanceService = distanceService;
    }
    @GetMapping("/user/{userPhoneNumber}/salon/{salonId}")
    public ResponseEntity<?> getDistanceBetweenUserAndSalon(@PathVariable String userPhoneNumber, @PathVariable Long salonId) {
        try {
            Double distance = distanceService.getDistanceBetweenUserAndSalon(userPhoneNumber, salonId);
            return ResponseEntity.ok(distance);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body("User not found with phone number: " + userPhoneNumber);
        } catch (SalonNotFoundException e) {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body("Salon not found with ID: " + salonId);
        } catch (DistanceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body("Distance not found for user: " + userPhoneNumber + " and salon ID: " + salonId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }




}
