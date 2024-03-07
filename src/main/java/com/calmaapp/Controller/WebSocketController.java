package com.calmaapp.Controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/booking")
    @SendTo("/topic/booking")
    public String handleBookingNotification(String message) {
        // Process the booking notification message
        return "New booking request received: " + message;
    }
}

