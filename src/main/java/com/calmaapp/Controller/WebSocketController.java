package com.calmaapp.Controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/notifySalonOwners")
    @SendTo("/topic/bookings")
    public String notifySalonOwners(String message) {
        // Implement logic to notify salon owners about new booking requests
        return "New booking request received";
    }

    @MessageMapping("/sendConfirmation")
    @SendTo("/topic/customerConfirmations")
    public String sendConfirmation(String message) {
        // Implement logic to send confirmation messages to customers
        return "Your booking has been confirmed. Thank you!";
    }
}


