package com.calmaapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.calmaapp.entity.Distance;
import com.calmaapp.entity.Salon;
import com.calmaapp.entity.User;
import com.calmaapp.repository.DistanceRepository;
import com.calmaapp.repository.SalonRepository;

@Service
public class DistanceCalculationService {

    @Autowired
    private DistanceRepository distanceRepository;

    @Autowired
    private SalonRepository salonRepository;

    public void calculateAndStoreDistances(User userId, Double userLatitude, Double userLongitude) {
        // Retrieve all salons
        List<Salon> salons = salonRepository.findAll();

        for (Salon salon : salons) {
            double salonLat = salon.getLatitude();
            double salonLon = salon.getLongitude();
            double distance = calculateDistance(userLatitude, userLongitude, salonLat, salonLon);

            // Store/update distance in the distance table
            Distance distanceEntity = new Distance();
            distanceEntity.setUserId(userId);
            Salon salonEntity = salonRepository.findById(salon.getId()).orElse(null);
            distanceEntity.setSalon(salonEntity);
            distanceEntity.setDistance(distance);
            distanceRepository.save(distanceEntity);
        }
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the Earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c; // Convert to kilometers

        return distance;
    }

}

