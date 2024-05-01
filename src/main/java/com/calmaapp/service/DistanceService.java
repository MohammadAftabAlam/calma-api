package com.calmaapp.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.calmaapp.entity.Distance;
import com.calmaapp.entity.Salon;
import com.calmaapp.entity.User;
import com.calmaapp.exception.DistanceNotFoundException;
import com.calmaapp.exception.UserNotFoundException;
import com.calmaapp.repository.DistanceRepository;
import com.calmaapp.repository.SalonRepository;
import com.calmaapp.repository.UserRepository;
import com.clmaapp.exception.SalonNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Service
public class DistanceService {

    private final DistanceRepository distanceRepository;
    private final UserRepository userRepository;
    private final SalonRepository salonRepository;

    @Autowired
    public DistanceService(DistanceRepository distanceRepository, UserRepository userRepository, SalonRepository salonRepository) {
        this.distanceRepository = distanceRepository;
        this.userRepository = userRepository;
        this.salonRepository = salonRepository;
    }

    public Double getDistanceBetweenUserAndSalon(String userPhoneNumber, Long salonId) {
        User user = userRepository.findByPhoneNumber(userPhoneNumber);
        Salon salon = salonRepository.findById(salonId).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("User not found with phone number: " + userPhoneNumber);
        }
        if (salon == null) {
            throw new SalonNotFoundException("Salon not found with ID: " + salonId);
        }
        Distance distance = distanceRepository.findByUserIdAndSalon(user, salon);
        if (distance != null) {
            return distance.getDistance();
        } else {
            throw new DistanceNotFoundException("Distance not found for user: " + userPhoneNumber + " and salon ID: " + salonId);
        }
    }



}