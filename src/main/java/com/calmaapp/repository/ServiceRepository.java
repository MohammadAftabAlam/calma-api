package com.calmaapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.calmaapp.entity.ServicesProvided;

//ServiceRepository.java
@Repository
public interface ServiceRepository extends JpaRepository<ServicesProvided, Long> {
// Define any custom queries or methods if required

}
