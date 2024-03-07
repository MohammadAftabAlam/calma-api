package com.calmaapp.service;

import org.springframework.stereotype.Component;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

@Component
public class GeocodingService {

    private static final String API_KEY = "AIzaSyAZTiI4i3NSYcuZ3DUBeNYnANMYGToosxs";

    public Coordinates getCoordinatesFromAddress(String address) {
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(API_KEY)
                .build();

        try {
            GeocodingResult[] results = GeocodingApi.geocode(context, address).await();

            if (results.length > 0) {
                double latitude = results[0].geometry.location.lat;
                double longitude = results[0].geometry.location.lng;
                return new Coordinates(latitude, longitude);
            } else {
                throw new RuntimeException("Unable to fetch coordinates for the address: " + address);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while fetching coordinates: " + e.getMessage());
        }
    }

    public static class Coordinates {

        private double latitude;
        private double longitude;

        public Coordinates(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }
    }
}