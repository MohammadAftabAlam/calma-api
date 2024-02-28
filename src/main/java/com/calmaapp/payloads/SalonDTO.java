package com.calmaapp.payloads;



import com.calmaapp.entity.Salon;
import com.calmaapp.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class SalonDTO {
    public SalonDTO(Salon salon) {
        this.address=salon.getAddress();
        this.contactInfo=salon.getContactInfo();
        this.name=salon.getName();
        this.openingTime=salon.getOpeningTime();
        this.closingTime=salon.getClosingTime();
        this.rating=salon.getRating();
        
    }
    private Long id;
    private String name;
    private String address;
    private String contactInfo;
    private String openingTime;
    private String closingTime;
    private User owner;
    private double latitude;
    private double longitude;
    private double rating;
    private List<MultipartFile> images;
    private List<ReviewDTO> reviews = new ArrayList<>();
    private String buttonText;
    private List<ServiceDTO> services = new ArrayList<>();

    
}

