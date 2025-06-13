package org.example.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotifyRequest {
    String username;
    String type;
    String time;

    public NotifyRequest(String username, String type, String timestamp) {
        this.username = username;
        this.type = type;
        this.time = timestamp;
    }
}
