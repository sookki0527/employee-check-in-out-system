package org.example.dto;


import lombok.*;
import org.example.entity.Role;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeDto {

    private Long id;
    private String username;
    private String password;
    private List<RoleDto> roles;

    public EmployeeDto(String username){
        this.username = username;
    }
    public EmployeeDto(String username, List<RoleDto> roles) {
        this.username = username;
        this.roles = roles;
    }
    public EmployeeDto(Long id, String username, List<RoleDto> roles){
        this.id  = id;
        this.username = username;
        this.roles = roles;
    }
}
