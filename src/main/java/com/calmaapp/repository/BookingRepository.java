package com.calmaapp.repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.calmaapp.entity.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByRequestedTime(LocalDateTime localDateTime);
    // Define custom queries or methods as needed

    List<Booking> findByBookingDateTime(LocalDateTime requestedTime);
}

