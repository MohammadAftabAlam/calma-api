package com.calmaapp.service;

import org.springframework.beans.factory.annotation.Autowired;

// import java.io.IOException;
// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.nio.file.Paths;
// import java.nio.file.StandardCopyOption;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
// import org.springframework.util.StringUtils;  // Correct import for StringUtils
// import org.springframework.web.multipart.MultipartFile;

// import com.calmaapp.Config.ImageStorageConfig;

// @Service
// public class ImageUploadService {

    // @Autowired
    // private ImageStorageConfig storageConfig;

//     public String uploadImage(MultipartFile imageFile) throws IOException {
//         String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
//         String uploadDir = storageConfig.getUploadDirectory();

//         Path uploadPath = Paths.get(uploadDir);

//         if (!Files.exists(uploadPath)) {
//             Files.createDirectories(uploadPath);
//         }

//         Path filePath = uploadPath.resolve(fileName);
//         Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

//         return fileName;
//     }
// }
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.calmaapp.entity.Salon;
import com.calmaapp.repository.SalonRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImageUploadService {

    @Autowired
    private SalonRepository salonRepository;

    public List<String> uploadImages(List<MultipartFile> salonImages,
                                     MultipartFile licenseImage,
                                     MultipartFile electricityBillImage,
                                     MultipartFile taxReceiptImage,
                                     Long salonId) throws IOException {
        // Validate the number of salon images
        int numSalonImages = salonImages.size();
        if (numSalonImages < 2 || numSalonImages > 5) {
            throw new IllegalArgumentException("At least 2 and at most 5 salon images are required.");
        }

        // Retrieve an existing Salon entity from the database
        Salon salon = salonRepository.findById(salonId)
                .orElseThrow(() -> new IllegalArgumentException("Salon with ID " + salonId + " not found"));

        // Save the image paths to the database
        List<String> imagePaths = new ArrayList<>();
        for (MultipartFile salonImage : salonImages) {
            imagePaths.add(saveImage(salonImage));
        }

        // Upload and save license image path
        String licenseImagePath = saveImage(licenseImage);
        imagePaths.add(licenseImagePath);

        // Upload and save electricity bill image path
        String electricityBillImagePath = saveImage(electricityBillImage);
        imagePaths.add(electricityBillImagePath);

        // Upload and save tax receipt image path
        String taxReceiptImagePath = saveImage(taxReceiptImage);
        imagePaths.add(taxReceiptImagePath);

        // Update the Salon entity with the new image paths
        salon.setSalonImagePath(imagePaths);
        salonRepository.save(salon);

        // Return the list of saved image paths
        return imagePaths;
    }

    private String saveImage(MultipartFile image) throws IOException {
        String uploadDir = "C:\\Users\\MohdAzam\\Pictures\\imageforapi"; 
        // Logic to save the image to the local directory
        String filename = image.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, filename);
        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Return the relative path of the saved image
        return filePath.toString();
    }
}
