package org.example.repository;

import org.example.entity.EmployeeRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRoleRepository extends JpaRepository<EmployeeRole, Long> {
}
