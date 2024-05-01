package com.calmaapp.Controller;

import com.calmaapp.UserType;
import com.calmaapp.entity.Salon;
import com.calmaapp.entity.User;
import com.calmaapp.exception.UnauthorizedAccessException;
import com.calmaapp.payloads.*;
import com.calmaapp.repository.SalonRepository;
import com.calmaapp.service.ExpertEmployeeService;
import com.calmaapp.service.ImageUploadService;
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
import org.springframework.transaction.annotation.Transactional;
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
    private ImageUploadService imageUploadService;

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
            // Fetch the salon owner by salonOwnerId
            User salonOwner = userService.getUserById(salonOwnerId);

            // Proceed with the salon registration
            salonService.registerSalon(salonOwner, salonDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body("Details added successfully. Please proceed with authentication.");
        } catch (UnauthorizedAccessException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only salon owners can register salons");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to register salon: " + e.getMessage());
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

    @Transactional
    @PostMapping("/{salonId}/services")
    public ResponseEntity<String> addServicesToSalon(@PathVariable Long salonId,
                                                     @RequestBody List<ServiceDTO> serviceDTOs) {
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

    @GetMapping("/{salonId}")
    public ResponseEntity<?> getSalonByIdWithServicesAndReviews(@PathVariable Long salonId) {
        return salonService.getSalonByIdWithServicesAndReviews(salonId);
    }

    @GetMapping("/withinRadius")
    public ResponseEntity<List<Salon>> getSalonsWithin4KmRadius(@RequestParam("latitude") Double userLatitude,
                                                                @RequestParam("longitude") Double userLongitude) {
        if (userLatitude == null || userLongitude == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            List<Salon> salonsWithinRadius = salonService.getSalonsWithin4KmRadius(userLatitude, userLongitude);
            return ResponseEntity.ok(salonsWithinRadius);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Salon>> searchSalonsByService(@RequestParam("service") String serviceName) {
        if (serviceName == null || serviceName.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            List<Salon> salons = salonService.searchSalonsByService(serviceName);
            return ResponseEntity.ok(salons);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/upload/{salonId}")
    public ResponseEntity<UploadResponse> uploadImages(
            @PathVariable Long salonId,
            @RequestParam("images") List<MultipartFile> images,
            @RequestParam("licenseImage") MultipartFile licenseImage,
            @RequestParam("electricityBillImage") MultipartFile electricityBillImage,
            @RequestParam("taxReceiptImage") MultipartFile taxReceiptImage) {
        try {
            // Validate the number of images
            int totalImages = images.size() + 3; // Add 3 for license, electricity bill, and tax receipt images
            if (totalImages < 2 || totalImages > 5) {
                return ResponseEntity.badRequest()
                        .body(new UploadResponse("At least 2 salon images and one of each license, electricity bill, and tax receipt images are required.", null));
            }

            // Upload images to Cloudinary and update Salon entity
            List<String> imagePaths = imageUploadService.uploadImages(images, licenseImage, electricityBillImage, taxReceiptImage, salonId);

            // Return the final response
            UploadResponse response = new UploadResponse("Verification complete, salon registered successfully", imagePaths);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new UploadResponse("Failed to upload images: " + e.getMessage(), null));
        }
    }

    @Autowired
    private ExpertEmployeeService expertEmployeeService;

    @PostMapping("/upload/expert")
    public ResponseEntity<?> uploadExpertDetails(
            @RequestParam String expertName,
            @RequestParam String expertSkill,
            @RequestParam MultipartFile expertCertificationImage,
            @RequestParam MultipartFile expertImage,
            @RequestParam int expertExperience,
            @RequestParam String expertSpecialization,
            @RequestParam Long salonId) {

        try {
            // Call the service method to upload expert details
            ExpertEmployeeDTO expertEmployeeDTO = expertEmployeeService.uploadExpertDetails(
                    expertName, expertSkill, expertCertificationImage, expertImage,
                    expertExperience, expertSpecialization, salonId);

            // Return the DTO with uploaded expert details
            return ResponseEntity.status(HttpStatus.CREATED).body(expertEmployeeDTO);
        } catch (IOException e) {
            // Handle any IO exception occurred during file uploading
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload expert details: " + e.getMessage());
        }
    }



}



