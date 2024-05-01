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
        this.rating=salon.getRating();
        this.owner = salon.getOwner();
        this.latitude = salon.getLatitude();
        this.longitude = salon.getLongitude();
        this.salonImages = new ArrayList<>();


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
    private List<MultipartFile> salonImages;
    private List<ReviewDTO> reviews = new ArrayList<>();
    private List<ServiceDTO> services = new ArrayList<>();
    private MultipartFile licenseImage;
    private MultipartFile electricityBillImage;
    private MultipartFile taxReceiptImage;

    public void setLicenseImage(MultipartFile licenseImage) {
        this.licenseImage = licenseImage;
    }

    public void setElectricityBillImage(MultipartFile electricityBillImage) {
        this.electricityBillImage = electricityBillImage;
    }

    public void setTaxReceiptImage(MultipartFile taxReceiptImage) {
        this.taxReceiptImage = taxReceiptImage;
    }

    // Getters for images
    public MultipartFile getLicenseImage() {
        return licenseImage;
    }

    public MultipartFile getElectricityBillImage() {
        return electricityBillImage;
    }

    public MultipartFile getTaxReceiptImage() {
        return taxReceiptImage;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(CharSequence openingTime) { // Change parameter type to CharSequence
        this.openingTime = openingTime != null ? openingTime.toString() : null;
    }

    // Getter and setter for closingTime
    public String getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(CharSequence closingTime) { // Change parameter type to CharSequence
        this.closingTime = closingTime != null ? closingTime.toString() : null;
    }
}




