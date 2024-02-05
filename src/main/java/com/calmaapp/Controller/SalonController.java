package com.calmaapp.Controller;



import com.calmaapp.UserType;
import com.calmaapp.entity.Salon;
import com.calmaapp.entity.User;
import com.calmaapp.payloads.SalonDTO;
import com.calmaapp.payloads.ServiceDTO;
import com.calmaapp.repository.SalonRepository;
import com.calmaapp.service.SalonService;
import com.calmaapp.service.UserService;
import com.clmaapp.exception.SalonNotFoundException;
import com.mysql.cj.util.StringUtils;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

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




    
     @PostMapping("/{salonId}/services")
    public ResponseEntity<String> addServicesToSalon(
            @PathVariable Long salonId,
            @RequestBody List<ServiceDTO> serviceDTOs) {
        try {
            ResponseEntity<String> response = salonService.addServicesToSalon(salonId, serviceDTOs);
            if (response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok(response.getBody());
            } else {
                return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
            }
        } catch (Exception e) {
            // Log the exception
            logger.error("Failed to add services to salon with ID: " + salonId, e);
            // Return a 500 Internal Server Error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add services to salon");
        }
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

    
   



}
