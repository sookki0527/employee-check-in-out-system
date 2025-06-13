package org.example.dto;

import lombok.Data;

@Data
public class NotifyRequest {
    String username;
    String type;
    String time;
}
