package com.calmaapp.Config;

import java.util.List;

import com.calmaapp.payloads.SalonDTO;

public class DistanceCalculator {

    private static final double EARTH_RADIUS = 6371.0; // Earth's radius in kilometers

    // Calculate distance between two sets of latitude and longitude using Haversine formula
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c; // Distance in kilometers
    }

    // Sort salons by distance in ascending order
    public static List<SalonDTO> sortSalonsByDistance(double userLat, double userLon, List<SalonDTO> salons) {
        salons.sort((s1, s2) -> {
            double distance1 = calculateDistance(userLat, userLon, s1.getLatitude(), s1.getLongitude());
            double distance2 = calculateDistance(userLat, userLon, s2.getLatitude(), s2.getLongitude());

            return Double.compare(distance1, distance2);
        });

        return salons;
    }
}

