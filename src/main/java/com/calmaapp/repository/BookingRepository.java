package com.calmaapp.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.calmaapp.entity.Booking;
import com.calmaapp.entity.Salon;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    // List<Booking> findBySalonAndBookingDateTime(Salon salon, LocalDateTime bookingDateTime);
    List<Booking> findBySalonIdAndBookingDateTime(Long salonId, LocalDateTime bookingDateTime);
    List<Booking> findBySalonAndBookingDateTime(Salon salon, LocalDateTime bookingDateTime);


}

