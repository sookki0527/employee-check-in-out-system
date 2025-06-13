package org.example.controller;

import org.example.dto.AttendanceDto;
import org.example.dto.CheckInRequest;
import org.example.dto.NotificationRequest;
import org.example.dto.NotifyRequest;
import org.example.entity.Attendance;
import org.example.service.AttendanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {
    private final AttendanceService service;

    public AttendanceController(AttendanceService service) {
        this.service = service;
    }

    @PostMapping("/listen")
    public ResponseEntity<Map<String, String>>listenAttendance(@RequestBody NotifyRequest request) {
        service.listen(request);
        Map<String, String> response = new HashMap<>();
        response.put("message", request.getType() + " was successful");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/notify")
    public ResponseEntity<Map<String, String>> notify(@RequestBody NotificationRequest request) {
        service.notifyCheckIn(request);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Notification was sent successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/attendance-list")
    public ResponseEntity<List<AttendanceDto>> getAttendanceList() {
        return ResponseEntity.ok(service.getAttendances());
    }

}
