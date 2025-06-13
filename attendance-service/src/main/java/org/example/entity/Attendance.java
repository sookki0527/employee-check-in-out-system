package org.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "employee_checkin")
public class Attendance {
    @Id
    private String id;

    private String username;
    private String checkIn;
    private String time;

    public Attendance(String username, String type, String timestamp) {
        this.username = username;
        this.checkIn = type;
        this.time = timestamp;
    }
}
