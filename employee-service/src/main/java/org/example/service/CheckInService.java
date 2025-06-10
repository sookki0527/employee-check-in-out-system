package org.example.service;

import org.example.dto.CheckInRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CheckInService {

    @Autowired
    private KafkaTemplate<String, CheckInRequest> kafkaTemplate;

    public void recordCheckIn(CheckInRequest request) {
        kafkaTemplate.send("attendance-topic", request);
    }

}
