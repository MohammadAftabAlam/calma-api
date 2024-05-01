package com.calmaapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.calmaapp.entity.ExpertEmployee;

@Repository
public interface ExpertEmployeeRepository extends JpaRepository<ExpertEmployee, Long> {
    // You can define custom query methods here if needed
}

