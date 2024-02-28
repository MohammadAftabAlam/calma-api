package com.calmaapp.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.calmaapp.UserType;
import com.calmaapp.authentication.JwtHelper;
import com.calmaapp.entity.Salon;
import com.calmaapp.entity.ServicesProvided;
import com.calmaapp.entity.User;
import com.calmaapp.exception.UnauthorizedAccessException;
//import com.calmaapp.mappingdistnace.DistanceMatrixService;
import com.calmaapp.payloads.SalonDTO;
import com.calmaapp.payloads.ServiceDTO;
import com.calmaapp.repository.SalonRepository;
import com.calmaapp.repository.UserRepository;
import com.calmaapp.service.GeocodingService.Coordinates;
import com.clmaapp.exception.SalonNotFoundException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import com.calmaapp.mappingdistnace.*;


@Service
public class SalonService {

    @Autowired
    private SalonRepository salonRepository;
   @Autowired 
   private UserRepository userRepository;
    
    @Autowired
    private UserService userService;

    @Autowired
private EntityManager entityManager;

    
    RestTemplate restTemplate = new RestTemplate();

    private final Logger logger = LoggerFactory.getLogger(SalonService.class);

    @Autowired
    private GeocodingService geocodingService;
 @Autowired
    private User user;
    
    @Autowired
private DirectionsService directionsService;
    
   
  //  private final DistanceMatrixService distanceMatrixService;
   
    // @Autowired
    // public SalonService(GeocodingService geocodingService, DistanceMatrixService distanceMatrixService, SalonRepository salonRepository) {
    //     this.geocodingService = geocodingService;
    //     this.distanceMatrixService = distanceMatrixService;
    //     this.salonRepository = salonRepository;
    // }


   public void registerSalon(User salonOwner, SalonDTO salonDTO) {
    // Check if the salon owner is valid and has the correct user type
    if (salonOwner == null || salonOwner.getUserType() != UserType.SALON_OWNER) {
        throw new UnauthorizedAccessException("Only salon owners can register salons");
    }

    // Check if the salon name already exists
    if (existsByName(salonDTO.getName())) {
        throw new RuntimeException("Salon with this name already exists");
    }
    
    // Convert the salon address to coordinates (latitude and longitude) using the geocoding service
    Coordinates coordinates = geocodingService.getCoordinatesFromAddress(salonDTO.getAddress());

    // Create a new `Salon` object
    Salon salon = new Salon();
    
    // Set the salon properties
    salon.setName(salonDTO.getName());
    salon.setAddress(salonDTO.getAddress());
    salon.setContactInfo(salonDTO.getContactInfo());
    salon.setClosingTime(salonDTO.getClosingTime());
    salon.setOpeningTime(salonDTO.getOpeningTime());
    salon.setLatitude(coordinates.getLatitude()); // Set the latitude
    salon.setLongitude(coordinates.getLongitude()); // Set the longitude

    // Associate the salon with the salon owner
    salon.setOwner(salonOwner);

    // Save the salon to the database
    salonRepository.save(salon);
}
 

    // @Transactional
    // public ResponseEntity<String> addServicesToSalon(Long salonId, List<ServiceDTO> serviceDTOs) {
    //     Salon salon = salonRepository.findById(salonId).orElse(null);

    //     if (salon != null) {
    //         List<String> existingServices = new ArrayList<>(); // Track existing services

    //         for (ServiceDTO serviceDTO : serviceDTOs) {
    //             boolean serviceExists = salon.getServices().stream()
    //                     .anyMatch(service -> service.getServiceName().equalsIgnoreCase(serviceDTO.getServiceName()));

    //             if (serviceExists) {
    //                 existingServices.add(serviceDTO.getServiceName()); // Track existing service names
    //             } else {
    //                 ServicesProvided service = new ServicesProvided();
    //                 service.setServiceName(serviceDTO.getServiceName().toUpperCase());
    //                 service.setCost(serviceDTO.getCost());
    //                 // Set other service properties if needed
    //                 service.setSalon(salon);

    //                 salon.getServices().add(service); // Add service to salon
    //             }
    //         }

    //         if (!existingServices.isEmpty()) {
    //             // Duplicate services found
    //             logger.warn("Duplicate services found: {}", existingServices);
    //             return ResponseEntity.status(HttpStatus.CONFLICT)
    //                     .body("Services already exist: " + existingServices);
    //         } else {
    //             // Save non-duplicate services to the salon
    //             salonRepository.save(salon);
    //             logger.info("Services added successfully to salon with ID: {}", salonId);
    //             return ResponseEntity.ok("Services added successfully to salon");
    //         }
    //     } else {
    //         // Salon with given salonId not found
    //         logger.warn("Salon not found with ID: {}", salonId);
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Salon not found");
    //     }
    // }
    @Transactional
public ResponseEntity<String> addServicesToSalon(Long salonId, List<ServiceDTO> serviceDTOs) {
    Salon salon = salonRepository.findById(salonId).orElse(null);

    if (salon != null) {
        List<String> existingServices = new ArrayList<>(); // Track existing services

        for (ServiceDTO serviceDTO : serviceDTOs) {
            boolean serviceExists = salon.getServices().stream()
                    .anyMatch(service -> service.getServiceName().equalsIgnoreCase(serviceDTO.getServiceName()));

            if (serviceExists) {
                existingServices.add(serviceDTO.getServiceName()); // Track existing service names
            } else {
                ServicesProvided service = new ServicesProvided();
                service.setServiceName(serviceDTO.getServiceName().toUpperCase());
                service.setCost(serviceDTO.getCost());
                // Set other service properties if needed
                service.setSalon(salon);
                salon.getServices().add(service); // Add service to salon
            }
        }

        if (!existingServices.isEmpty()) {
            // Duplicate services found
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Services already exist: " + existingServices);
        } else {
            // Save non-duplicate services to the salon
            entityManager.merge(salon); // Merge the salon entity to update the changes
            return ResponseEntity.ok("Services added successfully to salon");
        }
    } else {
        // Salon with given salonId not found
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

        public String saveImage(MultipartFile image) throws IOException {
            String uploadDir = "C:\\Users\\MohdAzam\\Pictures\\imageforapi"; // Local directory path
    
            // Logic to save the image to the local directory
            String filename = image.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, filename);
            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
    
            // Return the relative path of the saved image
            return filePath.toString();
        }

        public void deleteSalon(Long salonId, String salonOwnerUsername) {
            // Retrieve the salon from the database
            Salon salon = salonRepository.findById(salonId)
                    .orElseThrow(() -> new RuntimeException("Salon not found with ID: " + salonId));
    
            // Check if the authenticated user is the owner of the salon
            if (!salon.getOwner().getUsername().equals(salonOwnerUsername)) {
                throw new RuntimeException("Unauthorized: Only the salon owner can delete the salon");
            }
    
            // Delete the salon from the repository
            salonRepository.delete(salon);
        }



@Transactional
public ResponseEntity<String> updateSalonDistance(Long salonId, Double userLatitude, Double userLongitude) {
    try {
        // Retrieve salon information using the salonId
        Optional<Salon> optionalSalon = salonRepository.findById(salonId);
        if (!optionalSalon.isPresent()) {
            return ResponseEntity.notFound().build(); // Salon not found
        }
        Salon salon = optionalSalon.get();
        
        if (userLatitude != null && userLongitude != null) {
            // Calculate distance using Haversine formula
            double salonLat = salon.getLatitude();
            double salonLon = salon.getLongitude();
            double distance = calculateDistance(salonLat, salonLon, userLatitude, userLongitude);

            // Update salon distance
            salon.setDistanceFromCustomer(distance);
            salonRepository.save(salon);

            // Return success response with distance in the body
            return ResponseEntity.ok("Distance updated successfully: " + distance + " km");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User's location coordinates are missing or not set");
        }
    } catch (Exception e) {
        // Handle exception
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating salon distance.");
    }
}

// Calculate distance using Haversine formula
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
 public List<Salon> getTop10NearestSalons(double userLatitude, double userLongitude) {
        // Retrieve all salons from the database
        List<Salon> allSalons = salonRepository.findAll();

        // Calculate distance for each salon and sort the list based on distance
        List<Salon> sortedSalons = allSalons.stream()
                .map(salon -> {
                    double distance = calculateDistance(userLatitude, userLongitude, salon.getLatitude(), salon.getLongitude());
                    salon.setDistanceFromCustomer(distance);
                    return salon;
                })
                .sorted(Comparator.comparingDouble(Salon::getDistanceFromCustomer))
                .collect(Collectors.toList());

        // Return the top 10 salons (or less if there are fewer than 10 salons)
        return sortedSalons.stream().limit(10).collect(Collectors.toList());
    }


}