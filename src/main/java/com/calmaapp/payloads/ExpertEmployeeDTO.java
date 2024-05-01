package com.calmaapp.payloads;

import com.calmaapp.entity.ExpertEmployee;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExpertEmployeeDTO {

    private Long id;
    private String name;
    private String skill;
    private String certificationImage;
    private String expertImage;
    private int experience;
    private String specialization;
    private Long salonId; // This field represents the ID of the associated salon

    // Constructors
    public ExpertEmployeeDTO() {
    }

    public ExpertEmployeeDTO(ExpertEmployee expertEmployee) {
        this.id = expertEmployee.getId();
        this.name = expertEmployee.getName();
        this.skill = expertEmployee.getSkill();
        this.certificationImage = expertEmployee.getCertificationImage();
        this.expertImage=expertEmployee.getExpertImage();
        this.experience =  expertEmployee.getExperience();
        this.specialization = expertEmployee.getSpecialization();
        if (expertEmployee.getSalon() != null) {
            this.salonId = expertEmployee.getSalon().getId(); // Set salon ID if salon is not null
        }
    }

    // Getters and setters (if needed)
}

