package com.calmaapp.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "salon")
public class Salon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;
    private String address;
    private String contactInfo;

    private double latitude; 
    private double longitude;

    private String openingTime;
    private String closingTime;
    
    private double rating;

    @OneToMany(mappedBy = "salon", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    @Column
    private Double distanceFromCustomer;

    @ElementCollection
    private List<String> imagePaths; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "salon", fetch = FetchType.EAGER) // Fetch services eagerly
    private List<ServicesProvided> services;
}
