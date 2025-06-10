package org.example.service;

import org.example.dto.*;
import org.example.entity.Attendance;
import org.example.repository.AttendanceRepository;
import org.example.security.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AttendanceService {

    private final RestTemplate restTemplate;
    private final AttendanceRepository attendanceRepository;
    String userServiceUrl = "http://employee-service:8081/employee";
    private final JwtRequestFilter jwtRequestFilter;
    @Autowired
    private KafkaTemplate<String, NotificationRequest> kafkaTemplate;
    public AttendanceService(RestTemplate restTemplate,
                             AttendanceRepository attendanceRepository,
                             KafkaTemplate kafkaTemplate,
                             JwtRequestFilter jwtRequestFilter) {

        this.restTemplate = restTemplate;
        this.attendanceRepository = attendanceRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @KafkaListener(topics = "attendance-topic", groupId = "attendance-group")
    public void listen(CheckInRequest event){

        Long userId = event.getUserId();

        NotificationResponse notif = new NotificationResponse(
            "Employee " + event.getUserId() + " "+ event.getType(), LocalDateTime.now().toString()
        );

        System.out.println("✅ Consumed Kafka message: " + notif);
        saveCheckIn(userId);
    }


    public void saveCheckIn(Long userId){
        Attendance attendance = Attendance.builder()
                .userId(userId).checkIn(true).build();
        attendanceRepository.save(attendance);
    }


    public void notifyCheckIn(NotificationRequest notificationRequest) {
        kafkaTemplate.send("notification-topic", notificationRequest);
    }

   public List<AttendanceDto> getAttendanceList(){
        List<Attendance> attendanceList = attendanceRepository.findAll();
        List<AttendanceDto> attendanceDtos = new ArrayList<>();
        List<Long> userIds = attendanceList.stream()
                .map(Attendance::getUserId)
                .distinct().toList();
       HttpHeaders headers = new HttpHeaders();
       String jwtToken = JwtRequestFilter.getCurrentJwt();
       headers.set("Authorization", "Bearer " + jwtToken);
       headers.setContentType(MediaType.APPLICATION_JSON);
       HttpEntity<List<Long>> request = new HttpEntity<>(userIds, headers);
       ResponseEntity<Map<Long, EmployeeDto>> response =
               restTemplate.exchange(
                       userServiceUrl + "/batch",
                       HttpMethod.POST,
                       request,
                       new ParameterizedTypeReference<>() {}

       );

       Map<Long, EmployeeDto> employeeMap = response.getBody();
       for (Attendance attendance : attendanceList) {
           AttendanceDto dto = new AttendanceDto();
           EmployeeDto emp = employeeMap.get(attendance.getUserId());
           dto.setUsername(emp != null ? emp.getUsername() : "Unknown");
           dto.setCheckIn(attendance.isCheckIn());
           attendanceDtos.add(dto);
       }
        return attendanceDtos;
   }

}

