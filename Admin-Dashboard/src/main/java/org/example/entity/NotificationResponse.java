package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class NotificationResponse {

    private String message;
    private String time;
}
