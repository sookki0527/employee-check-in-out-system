package org.example.service;

import org.example.dto.RoleDto;
import org.example.entity.Employee;
import org.example.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private EmployeeRepository employeeRepository;

    public CustomUserDetailService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 예시
        Employee employee = employeeRepository.findByUsernameWithRoles(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<RoleDto> roleDtoList = employee.getRoles().stream().map(role -> new RoleDto( role.getName())).toList();
        return new org.springframework.security.core.userdetails.User(
                employee.getUsername(),
                employee.getPassword(),
               roleDtoList.stream()
                        .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                        .toList()
        );
    }
}
