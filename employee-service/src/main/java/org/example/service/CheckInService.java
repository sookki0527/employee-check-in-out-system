package org.example.service;

import org.example.dto.CheckInRequest;
import org.example.dto.NotifyRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CheckInService {

    @Autowired
    private KafkaTemplate<String, NotifyRequest> kafkaTemplate;

    public void recordCheckIn(NotifyRequest request) {
        kafkaTemplate.send("attendance-topic", request);
    }

}
