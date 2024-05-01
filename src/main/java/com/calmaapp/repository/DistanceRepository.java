package com.calmaapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.calmaapp.entity.Distance;
import com.calmaapp.entity.Salon;
import com.calmaapp.entity.User;

@Repository
public interface DistanceRepository extends JpaRepository<Distance, Long> {
    List<Distance> findByUserIdPhoneNumber(String phoneNumber);

    Distance findByUserIdAndSalon(User user, Salon salon);
}
