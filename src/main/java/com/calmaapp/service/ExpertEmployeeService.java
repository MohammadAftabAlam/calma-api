package com.calmaapp.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.calmaapp.entity.ExpertEmployee;
import com.calmaapp.entity.Salon;
import com.calmaapp.payloads.ExpertEmployeeDTO;
import com.calmaapp.repository.ExpertEmployeeRepository;
import com.calmaapp.repository.SalonRepository;
import com.cloudinary.Cloudinary;

@Service
public class ExpertEmployeeService {

    @Autowired
    private ExpertEmployeeRepository expertEmployeeRepository;

    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private SalonRepository salonRepository;

    public ExpertEmployeeDTO uploadExpertDetails(
            String expertName,
            String expertSkill,
            MultipartFile expertCertificationImage,
            MultipartFile expertImage,
            int expertExperience,
            String expertSpecialization,
            Long salonId) throws IOException {

        // Upload expert certification image to Cloudinary
        Map<?, ?> certificationResult = cloudinary.uploader().upload(expertCertificationImage.getBytes(), Map.of());
        String certificationImageUrl = (String) certificationResult.get("url");

        // Upload expert image to Cloudinary
        Map<?, ?> expertImageResult = cloudinary.uploader().upload(expertImage.getBytes(), Map.of());
        String expertImageUrl = (String) expertImageResult.get("url");

        // Retrieve the salon from the repository
        Salon salon = salonRepository.findById(salonId)
                .orElseThrow(() -> new IllegalArgumentException("Salon with ID " + salonId + " not found"));

        // Create a new ExpertEmployee entity
        ExpertEmployee expertEmployee = new ExpertEmployee();
        expertEmployee.setName(expertName);
        expertEmployee.setSkill(expertSkill);
        expertEmployee.setCertificationImage(certificationImageUrl);
        expertEmployee.setExpertImage(expertImageUrl);
        expertEmployee.setExperience(expertExperience);
        expertEmployee.setSpecialization(expertSpecialization);
        expertEmployee.setSalon(salon); // Associate the expert with the salon

        // Save the expert employee entity to the database
        expertEmployee = expertEmployeeRepository.save(expertEmployee);

        // Convert the expert employee entity to DTO for response
        ExpertEmployeeDTO expertEmployeeDTO = new ExpertEmployeeDTO(expertEmployee);

        // Return the DTO with uploaded expert details
        return expertEmployeeDTO;
    }
}

