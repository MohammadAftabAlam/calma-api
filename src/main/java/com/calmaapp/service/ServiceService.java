package com.calmaapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.calmaapp.entity.ServicesProvided;
import com.calmaapp.repository.ServiceRepository;

import java.util.List;

@Service
public class ServiceService {

    private final ServiceRepository serviceRepository;

    @Autowired
    public ServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public ServicesProvided saveService(ServicesProvided service) {
        // Save the service entity
        return serviceRepository.save(service);
    }

    public List<ServicesProvided> getAllServices() {
        // Retrieve all services from the database
        return serviceRepository.findAll();
    }

    // You can define other methods as needed
}

