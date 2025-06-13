package org.example.controller;

import org.example.dto.EmployeeDto;
import org.example.entity.Employee;
import org.example.repository.EmployeeRepository;
import org.example.service.EmployeeService;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/{userId}")
    public EmployeeDto getEmployee(@PathVariable Long userId) {
        return employeeService.getEmployeeById(userId);
    }

    @PostMapping("/batch")
    public Map<Long, EmployeeDto> getEmployeesByIds(@RequestBody List<Long> userIds) {
        return employeeService.getEmployeesByIds(userIds);

    }
}

