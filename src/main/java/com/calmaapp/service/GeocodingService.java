package com.calmaapp.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GeocodingService {
    private static final String NOMINATIM_BASE_URL = "https://nominatim.openstreetmap.org/search";
    private static final String FORMAT = "json";

    public static Coordinates getCoordinates(String address) throws IOException {
        String encodedAddress = java.net.URLEncoder.encode(address, "UTF-8");
        String apiUrl = String.format("%s?q=%s&format=%s", NOMINATIM_BASE_URL, encodedAddress, FORMAT);

        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            return parseCoordinates(response.toString());
        } finally {
            connection.disconnect();
        }
    }

    private static Coordinates parseCoordinates(String jsonResponse) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonResponse);

        if (rootNode.isArray() && rootNode.size() > 0) {
            JsonNode firstResult = rootNode.get(0);
            double latitude = firstResult.get("lat").asDouble();
            double longitude = firstResult.get("lon").asDouble();

            return new Coordinates(latitude, longitude);
        } else {
            throw new IOException("Invalid response from Nominatim API");
        }
    }

    private static class Coordinates {
        private final double latitude;
        private final double longitude;

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



