package org.example.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class CheckInRequest {
    private Long userId;
    private String timestamp;
    private String type;
}
