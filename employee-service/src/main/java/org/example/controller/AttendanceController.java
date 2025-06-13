package org.example.controller;

import org.example.dto.CheckInRequest;
import org.example.dto.NotifyRequest;
import org.example.entity.Employee;
import org.example.repository.EmployeeRepository;
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
    private final EmployeeRepository employeeRepository;

    public AttendanceController(CheckInService checkInService, EmployeeRepository employeeRepository) {
        this.checkInService = checkInService;
        this.employeeRepository = employeeRepository;
    }

    @PostMapping("/check-in")
    public ResponseEntity<Map<String, String>> checkIn(@RequestBody CheckInRequest request) {

        System.out.println("🔥 user check in: " + request.getUserId());
        Employee employee = employeeRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        NotifyRequest notifyRequest = new NotifyRequest(employee.getUsername(), request.getType(), request.getTimestamp());

        checkInService.recordCheckIn(notifyRequest);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Employee signed in successfully");
        return ResponseEntity.ok(response);
    }


    @PostMapping("/check-out")
    public ResponseEntity<Map<String, String>> checkOut(@RequestBody NotifyRequest request) {
        checkInService.recordCheckIn(request);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Employee signed out successfully");
        return ResponseEntity.ok(response);
    }
}
