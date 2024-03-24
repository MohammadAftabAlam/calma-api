package com.calmaapp.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Getter
@Setter
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "service_id")
    private Long serviceId;

    @Column(name = "booking_date_time")
    private LocalDateTime bookingDateTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "booking_status")
    private BookingStatus bookingStatus;

    @Column(name = "requested_time")
    private LocalDateTime requestedTime;
    
    @Column(name = "salon_id")
    private Long salonId;

    // Define relationships with Customer, Service, Salon, etc. if needed

    // Constructors, getters, and setters can be added as needed
}

