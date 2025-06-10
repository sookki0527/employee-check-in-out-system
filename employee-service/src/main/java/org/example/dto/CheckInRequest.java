package org.example.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class CheckInRequest {
    private Long userId;
    private LocalDateTime timestamp;
    private String type;
}
