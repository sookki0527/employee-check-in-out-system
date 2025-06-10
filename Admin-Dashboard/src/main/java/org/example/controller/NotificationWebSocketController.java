package org.example.controller;

import org.example.dto.NotificationRequest;
import org.example.dto.NotificationResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationWebSocketController {
    @MessageMapping("/transfer")
    @SendTo("/topic/notifications")
    public NotificationResponse formatAndSend(NotificationRequest event){
        return new NotificationResponse(
                "Employee " + event.getUsername() + " " + event.getType(),
                "Just now"
        );
    }
}
