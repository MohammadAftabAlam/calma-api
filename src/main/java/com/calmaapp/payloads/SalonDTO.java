package com.calmaapp.payloads;



import com.calmaapp.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SalonDTO {
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
    private List<ReviewDTO> reviews = new ArrayList<>();
    private String buttonText;
    private List<ServiceDTO> services = new ArrayList<>();

    
}

