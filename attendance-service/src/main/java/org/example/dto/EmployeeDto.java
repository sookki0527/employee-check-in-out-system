package org.example.dto;

import lombok.Data;

import java.util.List;
@Data
public class EmployeeDto {
    private String username;
    private List<RoleDto> roles;


}
