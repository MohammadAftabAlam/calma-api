package com.calmaapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.calmaapp.entity.Salon;

@Repository
public interface SalonRepository extends JpaRepository<Salon, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM ServicesProvided s WHERE s.salon.id = :salonId AND s.serviceName = :serviceName")
    void deleteServicesProvidedBySalonIdAndServiceName(@Param("salonId") Long salonId, @Param("serviceName") String serviceName);


	boolean existsByName(String name);
	List<Salon> findSalonsByServicesProvideds_ServiceName(String serviceName);
	Salon findById(long salonId);

}





