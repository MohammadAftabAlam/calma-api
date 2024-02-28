package com.calmaapp.Controller;



import com.calmaapp.UserType;
import com.calmaapp.entity.Salon;
import com.calmaapp.entity.User;
import com.calmaapp.payloads.*;
import com.calmaapp.payloads.SalonDTO;
import com.calmaapp.payloads.ServiceDTO;
import com.calmaapp.repository.SalonRepository;
import com.calmaapp.service.SalonService;
import com.calmaapp.service.UploadResponse;
import com.calmaapp.service.UserService;
import com.clmaapp.exception.SalonNotFoundException;
import com.mysql.cj.util.StringUtils;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.management.ServiceNotFoundException;

@RestController
@RequestMapping("/api/salons")
public class SalonController {

    @Autowired
    private SalonService salonService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private SalonRepository salonRepository;
     
    private Logger logger;
    
    @PostMapping("/{salonOwnerId}")
    public ResponseEntity<String> registerSalon(
            @PathVariable Long salonOwnerId,
            @RequestBody SalonDTO salonDTO) {
        try {
            // Fetch the salon owner by salonOwnerId (replace this with your logic)
            User salonOwner = userService.getUserById(salonOwnerId);

            // Check if the user is a salon owner
            if (salonOwner.getUserType() != UserType.SALON_OWNER) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only salon owners can register salons");
            }

            // Check if a salon with the same name already exists
            if (salonService.existsByName(salonDTO.getName())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Salon with this name already exists");
            }

            // Proceed with the salon registration
            salonService.registerSalon(salonOwner, salonDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body("Salon registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register salon");
        }
    }

    
    @PutMapping("/{salonId}")
    public ResponseEntity<String> updateSalon(
            @PathVariable Long salonId,
            @RequestBody SalonDTO updatedSalonDTO) {
        try {
            ResponseEntity<String> response = salonService.updateSalon(salonId, updatedSalonDTO);
            return response;
        } catch (SalonNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Salon not found with ID: " + salonId);
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception details
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update salon");
        }
    }




    
    //  @PostMapping("/{salonId}/services")
    // public ResponseEntity<String> addServicesToSalon(
    //         @PathVariable Long salonId,
    //         @RequestBody List<ServiceDTO> serviceDTOs) {
    //     try {
    //         ResponseEntity<String> response = salonService.addServicesToSalon(salonId, serviceDTOs);
    //         if (response.getStatusCode().is2xxSuccessful()) {
    //             return ResponseEntity.ok(response.getBody());
    //         } else {
    //             return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    //         }
    //     } catch (Exception e) {
    //         // Log the exception
    //         logger.error("Failed to add services to salon with ID: " + salonId, e);
    //         // Return a 500 Internal Server Error response
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add services to salon");
    //     }
    // }

    @PostMapping("/{salonId}/services")
    public ResponseEntity<String> addServicesToSalon(@PathVariable Long salonId, @RequestBody List<ServiceDTO> serviceDTOs) {
        return salonService.addServicesToSalon(salonId, serviceDTOs);
    }

          
    @DeleteMapping("/{salonId}/services/{serviceName}")
    public ResponseEntity<String> deleteServiceFromSalon(
            @PathVariable Long salonId,
            @PathVariable String serviceName) {
        try {
            salonService.deleteServiceByNameFromSalon(salonId, serviceName);
            return ResponseEntity.ok("Service '" + serviceName + "' deleted from salon ID: " + salonId);
        } catch (SalonNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Salon with ID " + salonId + " not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete service from salon");
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<UploadResponse> uploadImages(@RequestParam("images") List<MultipartFile> images) {
        try {
            // Check if the number of images is within the allowed range
            if (images.size() < 2 || images.size() > 5) {
                return ResponseEntity.badRequest().body(new UploadResponse("At least 2 images and at most 5 images are required.", null));
            }
    
            List<String> imagePaths = new ArrayList<>();
    
            for (MultipartFile image : images) {
                try {
                    String imagePath = salonService.saveImage(image);
                    imagePaths.add(imagePath);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to save image: " + e.getMessage());
                }
            }
    
            UploadResponse response = new UploadResponse("Images uploaded successfully", imagePaths);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new UploadResponse("Failed to upload images: " + e.getMessage(), null));
        }
    }

    



@DeleteMapping("/{salonId}")
public ResponseEntity<?> deleteSalon(@PathVariable Long salonId, Principal principal) {
    try {
        // Get the username of the currently authenticated salon owner
        String salonOwnerUsername = principal.getName();
        
        // Delete the salon with the given ID
        salonService.deleteSalon(salonId, salonOwnerUsername);
        
        return ResponseEntity.ok("Salon deleted successfully");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to delete salon: " + e.getMessage());
    }
}


// @PutMapping("/{salonId}/updateDistance")
//     public ResponseEntity<String> updateSalonDistance(@PathVariable Long salonId, HttpServletRequest request) {
//         Salon salon = salonService.getSalonById(salonId);
//         if (salon == null) {
//             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Salon not found");
//         }
//         ResponseEntity<String> response = salonService.updateSalonDistance(salon, request);
//         return response;
//     }
@GetMapping("/{salonId}")
public ResponseEntity<?> getSalonById(@PathVariable Long salonId) {
    try {
        // Retrieve the salon from the service
        Salon salon = salonService.getSalonById(salonId);
        if (salon != null) {
            // If services are eagerly fetched with the salon, they should be available here
            // If services are lazily fetched, you may need to initialize them explicitly
            salon.getServices().size(); // This initializes the services collection

            SalonDTO salonDTO = new SalonDTO(salon);
            return ResponseEntity.ok(salonDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}

        // }
        //             .orElseThrow(() -> new Exception("Salon not found with ID: " + salonId));
            
        //     // Return the salon in the response body
        //     return ResponseEntity.ok(salon);
        // } catch (Exception e) {
        //     // Handle exceptions and return an error response
        //     e.printStackTrace();
        //     return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Salon not found with ID: " + salonId);
        // }
    //}
    
    @GetMapping("/api/salons/top10")
    public ResponseEntity<List<Salon>> getTop10NearestSalons(@RequestParam("latitude") Double userLatitude,
                                                             @RequestParam("longitude") Double userLongitude) {
        if (userLatitude == null || userLongitude == null) {
            return ResponseEntity.badRequest().build();
        }
    
        try {
            List<Salon> nearestSalons = salonService.getTop10NearestSalons(userLatitude, userLongitude);
            return ResponseEntity.ok(nearestSalons);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
}


    


    

    
    
   




