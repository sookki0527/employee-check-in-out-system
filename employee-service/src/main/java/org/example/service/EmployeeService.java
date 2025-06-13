package org.example.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.dto.EmployeeDto;
import org.example.dto.RoleDto;
import org.example.entity.Employee;
import org.example.repository.EmployeeRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public EmployeeDto getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + id));

        List<RoleDto> roles = employee.getRoles().stream()
                .map(role -> new RoleDto(role.getName()))
                .toList();

        return new EmployeeDto(employee.getUsername(), roles);
    }

    public EmployeeDto getEmployeeByName(String username){
        Employee employee = employeeRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<RoleDto> roles = employee.getRoles().stream().map(role -> new RoleDto(role.getName())).toList();
        return new EmployeeDto(employee.getId(), employee.getUsername(), roles);
    }

    public List<Long> getUserIds(){
       return employeeRepository.findAll()
               .stream()
               .map(Employee::getId)
               .toList();
    }

    public Map<Long, EmployeeDto> getEmployeesByIds(List<Long> userIds) {
        try {
            List<Employee> employees = employeeRepository.findAllById(userIds);

            if (employees == null || employees.isEmpty()) {
                throw new RuntimeException("❌ No employees found for given IDs");
            }

            return employees.stream()
                    .filter(emp -> emp.getId() != null && emp.getUsername() != null)
                    .collect(Collectors.toMap(
                            Employee::getId,
                            emp -> new EmployeeDto(emp.getUsername()),
                            (e1, e2) -> e1 // 충돌 시 기존 값 유지
                    ));
        } catch (Exception e) {
            System.out.println("❌ Error in getEmployeesByIds: " + e.getMessage());
            throw new RuntimeException("Failed to fetch employee data", e);
        }
    }

}
