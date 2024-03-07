package com.calmaapp.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;import com.calmaapp.payloads.SalonConfirmationRequestDTO;
import com.calmaapp.service.SalonConfirmationService;

@RestController
@RequestMapping("/api/salon/confirmation")
public class SalonConfirmationController {

    @Autowired
    private SalonConfirmationService salonConfirmationService;

    @PostMapping("/confirm")
    public ResponseEntity<?> confirmBooking(@RequestBody SalonConfirmationRequestDTO requestDTO) {
        salonConfirmationService.confirmBooking(requestDTO.getBookingId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reject")
    public ResponseEntity<?> rejectBooking(@RequestBody SalonConfirmationRequestDTO requestDTO) {
        salonConfirmationService.rejectBooking(requestDTO.getBookingId());
        return ResponseEntity.ok().build();
    }
}
