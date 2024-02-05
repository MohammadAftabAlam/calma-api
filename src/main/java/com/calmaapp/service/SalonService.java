package com.calmaapp.service;


import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.calmaapp.UserType;
import com.calmaapp.distanceService.NominatimResponse;
import com.calmaapp.entity.Salon;
import com.calmaapp.entity.ServicesProvided;
import com.calmaapp.entity.User;
import com.calmaapp.exception.UnauthorizedAccessException;
import com.calmaapp.payloads.SalonDTO;
import com.calmaapp.payloads.ServiceDTO;
import com.calmaapp.repository.SalonRepository;
import com.clmaapp.exception.SalonNotFoundException;
import com.clmaapp.exception.ServiceNotFoundException;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
public class SalonService {

    @Autowired
    private SalonRepository salonRepository;
    
    @Autowired
    private UserService userService;
    
    RestTemplate restTemplate = new RestTemplate();

    private final Logger logger = LoggerFactory.getLogger(SalonService.class);
    

    public void registerSalon(User salonOwner, SalonDTO salonDTO) {
        if (salonOwner == null || salonOwner.getUserType() != UserType.SALON_OWNER) {
            throw new UnauthorizedAccessException("Only salon owners can register salons");
        }

        // Check if the salon name already exists
        if (existsByName(salonDTO.getName())) {
            throw new RuntimeException("Salon with this name already exists");
        }

        Salon salon = new Salon();
        salon.setName(salonDTO.getName());
        salon.setAddress(salonDTO.getAddress());
        salon.setContactInfo(salonDTO.getContactInfo());

        // Associate the salon with the salon owner
        salon.setOwner(salonOwner);

        salonRepository.save(salon);
    }
    
//    @Transactional
    public ResponseEntity<String> addServicesToSalon(Long salonId, List<ServiceDTO> serviceDTOs) {
        Salon salon = salonRepository.findById(salonId).orElse(null);

        if (salon != null) {
            List<String> existingServices = new ArrayList<>(); // Track existing services

            for (ServiceDTO serviceDTO : serviceDTOs) {
                boolean serviceExists = salon.getServicesProvideds().stream()
                        .anyMatch(service -> service.getServiceName().equalsIgnoreCase(serviceDTO.getServiceName()));

                if (serviceExists) {
                    existingServices.add(serviceDTO.getServiceName()); // Track existing service names
                } else {
                    ServicesProvided service = new ServicesProvided();
                    service.setServiceName(serviceDTO.getServiceName().toUpperCase());
                    service.setCost(serviceDTO.getCost());
                    // Set other service properties if needed
                    service.setSalon(salon);

                    salon.getServicesProvideds().add(service); // Add service to salon
                }
            }

            if (!existingServices.isEmpty()) {
                // Duplicate services found
                logger.warn("Duplicate services found: {}", existingServices);
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Services already exist: " + existingServices);
            } else {
                // Save non-duplicate services to the salon
                salonRepository.save(salon);
                logger.info("Services added successfully to salon with ID: {}", salonId);
                return ResponseEntity.ok("Services added successfully to salon");
            }
        } else {
            // Salon with given salonId not found
            logger.warn("Salon not found with ID: {}", salonId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Salon not found");
        }
    }




  

    
    @Transactional
    public void deleteServiceByNameFromSalon(Long salonId, String serviceName) {
        Salon salon = salonRepository.findById(salonId)
                .orElseThrow(() -> new SalonNotFoundException("Salon with ID " + salonId + " not found"));

        salonRepository.deleteServicesProvidedBySalonIdAndServiceName(salon.getId(), serviceName);
    }
    

    public ResponseEntity<String> updateSalon(Long salonId, SalonDTO updatedSalonDTO) {
        try {
            Salon salon = salonRepository.findById(salonId).orElse(null);

            if (salon != null) {
                // Update salon properties based on the values from updatedSalonDTO
                salon.setName(updatedSalonDTO.getName());
                salon.setAddress(updatedSalonDTO.getAddress());
                salon.setContactInfo(updatedSalonDTO.getContactInfo());
                salon.setOpeningTime(updatedSalonDTO.getOpeningTime());
                salon.setClosingTime(updatedSalonDTO.getClosingTime());
                // Update other properties as needed...

                salonRepository.save(salon);

                return ResponseEntity.ok("Salon updated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Salon not found with ID: " + salonId);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception details
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update salon");
        }
    }



	public boolean existsByName(String name) {
		return salonRepository.existsByName(name);
	}

	public Salon getSalonById(Long salonId) {
        
        return salonRepository.findById(salonId).orElse(null);
    }


	


	// Assuming you have a logger instance in your class
	//private static final Logger logger = LoggerFactory.getLogger(YourClassName.class);

	// Your existing method
	public void getDistanceForSalon(Salon salon, User user) {
	    try {
	        String salonAddress = salon.getAddress();
	        String apiKey = "AIzaSyAZTiI4i3NSYcuZ3DUBeNYnANMYGToosxs";
	        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + URLEncoder.encode(salonAddress, StandardCharsets.UTF_8) + "&key=" + apiKey;

	        ResponseEntity<Map> responseEntity = restTemplate.getForEntity(url, Map.class);
	        Map<String, Object> response = responseEntity.getBody();

	        if (response != null && response.containsKey("results")) {
	            List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");
	            if (!results.isEmpty()) {
	                Map<String, Object> location = (Map<String, Object>) results.get(0).get("geometry");
	                Map<String, Double> coordinates = (Map<String, Double>) location.get("location");

	                double salonLatitude = coordinates.get("lat");
	                double salonLongitude = coordinates.get("lng");

	                double distance = calculateDistance(user.getLatitude(), user.getLongitude(), salonLatitude, salonLongitude);
	                double roundedDistance = Math.round(distance * 10.0) / 10.0;
	                salon.setDistanceFromCustomer(roundedDistance);
	                salonRepository.save(salon);

	                logger.info("Geocoding salon address: {}", salon.getAddress());
	                logger.info("Calculated Distance: {} km", roundedDistance);
	            } else {
	                logger.warn("No results received from the geocoding service for salon: {}", salon.getName());
	            }
	        } else {
	            logger.warn("Invalid response from the geocoding service for salon: {}", salon.getName());
	        }
	    } catch (HttpClientErrorException e) {
	        logger.error("HTTP client error: {}", e.getMessage(), e);
	    } catch (Exception e) {
	        logger.error("An error occurred: {}", e.getMessage(), e);
	    }
	}




	 public static double calculateDistance(double userLatitude, double userLongitude, double salonDistance) {
		    // Assuming salonDistance is in kilometers
		    return Math.abs(salonDistance - calculateDistance(0, 0, userLatitude, userLongitude));
		}

		private static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
		    final int R = 6371; // Radius of the Earth in kilometers

		    double dLat = Math.toRadians(lat2 - lat1);
		    double dLon = Math.toRadians(lon2 - lon1);

		    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
		            Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
		            Math.sin(dLon / 2) * Math.sin(dLon / 2);

		    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		   
		    return R * c;
		}
}



	


		




