package com.calmaapp.payloads;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ServiceDTO {
    private Long id;
    private String serviceName;
    private double cost;
    private Long salonId;
    
    private String imageUrl;
    // Constructors, getters, setters, etc.
}

