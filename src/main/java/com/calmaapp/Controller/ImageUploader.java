package com.calmaapp.Controller;

import com.calmaapp.service.ImageUploadService;
import com.calmaapp.service.UploadResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
@RestController
@RequestMapping("/api/salon")
public class ImageUploader {

    @Autowired
    private ImageUploadService imageUploadService;


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
}
