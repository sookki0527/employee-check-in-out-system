package org.example.controller;

import org.example.dto.EmployeeDto;
import org.example.dto.LoginRequest;
import org.example.dto.RegisterRequest;
import org.example.entity.Employee;
import org.example.service.AuthService;
import org.example.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final EmployeeService employeeService;
    public AuthController(AuthService authService, EmployeeService employeeService) {

        this.authService = authService;
        this.employeeService = employeeService;
    }


    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody RegisterRequest request) {
        System.out.println("register tried with:  "+ request.getUsername());
        authService.register(request);
        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered successfully");

        return ResponseEntity.ok(response);
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
        String token = authService.login(request.getUsername(), request.getPassword());
        EmployeeDto employeeDto = employeeService.getEmployeeByName(request.getUsername());
        Map<String, String> body = new HashMap<>();
        body.put("token", token);
        body.put("username", request.getUsername());
        body.put("userId", employeeDto.getId().toString());
        body.put("role", employeeDto.getRoles().get(0).getRoleName().toString());
        System.out.println("🔥 role when login" + employeeDto.getRoles().get(0).getRoleName().toString());
        return ResponseEntity.ok(body);
    }

}
