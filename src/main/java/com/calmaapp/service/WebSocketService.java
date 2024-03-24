package com.calmaapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.calmaapp.entity.Booking;

@Component
public class WebSocketService {

    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendNotificationToSalonOwner(Booking booking) {
        // Assuming you have a destination for salon owner notifications, for example: "/salon/notifications"
        // You can adjust the destination based on your WebSocket configuration
        String destination = "/salon/notifications";
        
        // Send the booking information to the salon owner
        messagingTemplate.convertAndSend(destination, booking);
    }

    public void sendConfirmationToCustomer(Booking booking) {
        // Assuming you have a destination for customer notifications, for example: "/customer/notifications"
        // You can adjust the destination based on your WebSocket configuration
        String destination = "/customer/notifications";
        
        // Send the confirmation message to the customer
        messagingTemplate.convertAndSend(destination, "Your booking has been confirmed. Thank you!");
    }

    public void sendSlotUnavailableNotificationToCustomer(Booking booking) {
        // Assuming you have a destination for customer notifications, for example: "/customer/notifications"
        // You can adjust the destination based on your WebSocket configuration
        String destination = "/customer/notifications";
        
        // Send the slot unavailable notification to the customer
        messagingTemplate.convertAndSend(destination, "Sorry, the slot is already booked. Please try another time.");
    }
}
