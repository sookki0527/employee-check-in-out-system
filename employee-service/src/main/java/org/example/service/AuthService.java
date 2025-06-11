package org.example.service;

import org.example.dto.RegisterRequest;
import org.example.dto.RoleDto;
import org.example.entity.EmployeeRole;
import org.example.entity.Role;
import org.example.entity.Employee;
import org.example.repository.RoleRepository;
import org.example.repository.EmployeeRepository;
import org.example.util.JwtUtil;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class AuthService {


    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RestTemplate restTemplate;
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    public AuthService(PasswordEncoder passwordEncoder, JwtUtil jwtUtil,
                       RestTemplate restTemplate, EmployeeRepository employeeRepository,
                       RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.restTemplate = restTemplate;
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
    }

    public String login(String username, String password) {
        System.out.println("login with user:" + username);

        Employee employee = employeeRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<RoleDto> roles = employee.getRoles().stream().map(role -> new RoleDto(role.getName())).toList();
        if (!passwordEncoder.matches(password, employee.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        return jwtUtil.generateToken(employee.getUsername());
    }


    public void register(RegisterRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        System.out.println("role ?" + request.getIsAdmin());
        Role role = roleRepository.findByName(
                request.getIsAdmin() ? "ROLE_ADMIN" : "ROLE_EMPLOYEE"
        ).orElseThrow(() -> new RuntimeException("Role not found"));
        EmployeeRole employeeRole = new EmployeeRole(role.getName());

        Employee employee = new Employee(request.getUsername(), encodedPassword, List.of(employeeRole), request.getEmail());
        employeeRole.setEmployee(employee);
        employeeRepository.save(employee);

    }

}
