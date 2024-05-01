package com.calmaapp.repository;



import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.calmaapp.entity.Salon;
import com.calmaapp.entity.User;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // User findByPhoneNumber(String phoneNumber);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);

    User findByEmail(String email);

    User findByResetToken(String resetToken);

    //Changes made by Aftab
    //There was no any annotation for @NotNull
    @NotNull
    Optional<User> findById(Long userId);


    User findByPhoneNumber(String phoneNumber);

    // Optional<User> findByPhoneNumber(Long phoneNumber);





}


