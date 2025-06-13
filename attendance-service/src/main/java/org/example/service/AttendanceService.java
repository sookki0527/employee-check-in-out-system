package org.example.service;

import jakarta.servlet.http.HttpServletRequest;
import org.example.dto.*;
import org.example.entity.Attendance;
import org.example.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class AttendanceService {

    private final RestTemplate restTemplate;
    private final AttendanceRepository attendanceRepository;
    private final HttpServletRequest request;
    String userServiceUrl = "http://employee-service:8081/employee";

    @Autowired
    private KafkaTemplate<String, NotificationRequest> kafkaTemplate;
    public AttendanceService(RestTemplate restTemplate,
                             AttendanceRepository attendanceRepository,
                             KafkaTemplate kafkaTemplate,
                             HttpServletRequest request) {

        this.restTemplate = restTemplate;
        this.attendanceRepository = attendanceRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.request = request;
    }

    @KafkaListener(topics = "attendance-topic", groupId = "attendance-group")
    public void listen(NotifyRequest event){

        NotificationResponse notif = new NotificationResponse(
            "Employee " + event.getUsername() + " "+ event.getType(), LocalDateTime.now().toString()
        );

        System.out.println("✅ Consumed Kafka message: " + notif);
        Attendance attendance = new Attendance(event.getUsername(), event.getType(), event.getTime());
        attendanceRepository.save(attendance);

    }

    public void notifyCheckIn(NotificationRequest notificationRequest) {
        kafkaTemplate.send("notification-topic", notificationRequest);
    }

    public List<AttendanceDto> getAttendances(){
        List<Attendance> attendanceList = attendanceRepository.findAll();
        System.out.println("✅ attendance list size: " + attendanceList.size());
        List<AttendanceDto> attendanceDtoList = new ArrayList<>();
        for (Attendance attendance : attendanceList) {
            AttendanceDto attendanceDto = new AttendanceDto();
            attendanceDto.setUsername(attendance.getUsername());
            attendanceDto.setCheckIn(attendance.getCheckIn());
            attendanceDto.setTime(attendance.getTime());
            System.out.println("✅ attendance : " + attendanceDto.getUsername() + " " + attendanceDto.getCheckIn());
            attendanceDtoList.add(attendanceDto);
        }
        return attendanceDtoList;
    }

//   public Set<AttendanceDto> getAttendanceList(){
//        List<Attendance> attendanceList = attendanceRepository.findAll();
//        Set<AttendanceDto> attendanceDtos = new HashSet<>();
//        List<Long> userIds = attendanceList.stream()
//                .map(Attendance::getUsername)
//                .distinct().toList();
//       HttpHeaders headers = new HttpHeaders();
//
//       String jwtToken = request.getHeader("Authorization");
//       System.out.println("🔥 Sent userIds: " + userIds);
//       System.out.println("🔥 Authorization Header: " + jwtToken);
//       headers.set("Authorization", jwtToken);
//       headers.setContentType(MediaType.APPLICATION_JSON);
//       HttpEntity<List<Long>> request = new HttpEntity<>(userIds, headers);
//       ResponseEntity<Map<Long, EmployeeDto>> response =
//               restTemplate.exchange(
//                       userServiceUrl + "/batch",
//                       HttpMethod.POST,
//                       request,
//                       new ParameterizedTypeReference<>() {}
//
//       );
//
//       Map<Long, EmployeeDto> employeeMap = response.getBody();
//       for (Attendance attendance : attendanceList) {
//           AttendanceDto dto = new AttendanceDto();
//           EmployeeDto emp = employeeMap.get(attendance.getUserId());
//           dto.setUsername(emp != null ? emp.getUsername() : "Unknown");
//           dto.setCheckIn(attendance.getCheckIn());
//           attendanceDtos.add(dto);
//       }
//        return attendanceDtos;
//   }

}

