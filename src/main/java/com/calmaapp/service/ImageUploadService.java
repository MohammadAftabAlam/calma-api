package com.calmaapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.calmaapp.entity.Salon;
import com.calmaapp.repository.SalonRepository;
import com.cloudinary.Cloudinary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class ImageUploadService {

    @Autowired
    private SalonRepository salonRepository;

    @Autowired
    private Cloudinary cloudinary;

    public List<String> uploadImages(List<MultipartFile> salonImages,
                                     MultipartFile licenseImage,
                                     MultipartFile electricityBillImage,
                                     MultipartFile taxReceiptImage,
                                     Long salonId) throws IOException {
        // Upload salon images to Cloudinary
        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile salonImage : salonImages) {
            Map<?, ?> result = cloudinary.uploader().upload(salonImage.getBytes(), Map.of());
            imageUrls.add((String) result.get("url"));
        }

        // Upload license image to Cloudinary and add its URL to the image URLs list
        Map<?, ?> licenseResult = cloudinary.uploader().upload(licenseImage.getBytes(), Map.of());
        imageUrls.add((String) licenseResult.get("url"));

        // Upload electricity bill image to Cloudinary and add its URL to the image URLs list
        Map<?, ?> electricityBillResult = cloudinary.uploader().upload(electricityBillImage.getBytes(), Map.of());
        imageUrls.add((String) electricityBillResult.get("url"));

        // Upload tax receipt image to Cloudinary and add its URL to the image URLs list
        Map<?, ?> taxReceiptResult = cloudinary.uploader().upload(taxReceiptImage.getBytes(), Map.of());
        imageUrls.add((String) taxReceiptResult.get("url"));

        // Retrieve salon entity and update image URLs
        Salon salon = salonRepository.findById(salonId)
                .orElseThrow(() -> new IllegalArgumentException("Salon with ID " + salonId + " not found"));

        // Update the Salon entity with all the image URLs
        salon.setSalonImageUrls(imageUrls);
        salonRepository.save(salon);

        // Return the list of all image URLs
        return imageUrls;
    }

}
