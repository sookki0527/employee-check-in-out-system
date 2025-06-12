package org.example.controller;

import org.example.dto.CheckInRequest;
import org.example.service.CheckInService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/employee-attendance")
public class AttendanceController {
    private final CheckInService checkInService;
    public AttendanceController(CheckInService checkInService) {
        this.checkInService = checkInService;
    }
    @PostMapping("/check-in")
    public ResponseEntity<Map<String, String>> checkIn(@RequestBody CheckInRequest checkInRequest) {


        checkInService.recordCheckIn(checkInRequest);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Employee signed in successfully");
        return ResponseEntity.ok(response);
    }


    @PostMapping("/check-out")
    public ResponseEntity<Map<String, String>> checkOut(@RequestBody CheckInRequest checkInRequest) {
        checkInService.recordCheckIn(checkInRequest);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Employee signed out successfully");
        return ResponseEntity.ok(response);
    }
}
