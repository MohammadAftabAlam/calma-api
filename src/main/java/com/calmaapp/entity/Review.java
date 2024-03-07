package com.calmaapp.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comment;
    private int rating;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "salon_id")
    @JsonIgnore
    private Salon salon;
    
    @ManyToOne
    @JoinColumn(name = "user_id")  // Adjust the column name as needed
    private User user;

    // Other getters and setters...
}

