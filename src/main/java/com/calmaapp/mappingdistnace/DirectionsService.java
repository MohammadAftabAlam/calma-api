package com.calmaapp.mappingdistnace;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;
// import org.springframework.web.client.RestTemplate;

// @Service
// public class DistanceMatrixService {

//     @Value("${google.distance.matrix.api.key}")
//     private String apiKey;

//     public double calculateDistance(double originLat, double originLon, double destLat, double destLon) {
//         String apiUrl = "https://maps.googleapis.com/maps/api/distancematrix/json"
//                 + "?origins=" + originLat + "," + originLon
//                 + "&destinations=" + destLat + "," + destLon
//                 + "&key=" + apiKey;

//         RestTemplate restTemplate = new RestTemplate();
//         DistanceMatrixResponse response = restTemplate.getForObject(apiUrl, DistanceMatrixResponse.class);

//         if (response != null && "OK".equals(response.getStatus())) {
//             return response.getRows()[0].getElements()[0].getDistance().getValue();
//         } else {
//             // Handle API error or invalid response
//             throw new RuntimeException("Error while calling the Distance Matrix API");
//         }
//     }
// }


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DirectionsService {

    @Value("${google.directions.api.key}")
    private String apiKey;

    public double calculateDistance(double originLat, double originLon, double destLat, double destLon) {
        String apiUrl = "https://maps.googleapis.com/maps/api/directions/json"
                + "?origin=" + originLat + "," + originLon
                + "&destination=" + destLat + "," + destLon
                + "&key=" + apiKey;

        RestTemplate restTemplate = new RestTemplate();
        DirectionsResponse response = restTemplate.getForObject(apiUrl, DirectionsResponse.class);

        if (response != null && "OK".equals(response.getStatus())) {
            return response.getRoutes()[0].getLegs()[0].getDistance().getValue();
        } else {
            // Handle API error or invalid response
            throw new RuntimeException("Error while calling the Directions API");
        }
    }
}
