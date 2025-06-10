package org.example.service;

import org.example.dto.NotificationRequest;
import org.example.dto.NotificationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
@Service
public class NotificationService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

     public NotificationService(SimpMessagingTemplate simpMessagingTemplate){
        this.messagingTemplate = simpMessagingTemplate;
     }

    @KafkaListener(topics = "notification-topic", groupId = "notification-group")
    public void listen(NotificationRequest event){

        NotificationResponse notif = new NotificationResponse(
                "Employee " + event.getUsername() + event.getType(), LocalDateTime.now().toString()
        );
        System.out.println("✅ Consumed Kafka message at admin-dashboard: " + notif);
        messagingTemplate.convertAndSend("/topic/notifications", notif);
    }


}
