package org.example.repository;

import org.example.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByUsername(String username);
    Optional<Employee> findById(Long id);
    @Query("SELECT e FROM Employee e LEFT JOIN FETCH e.roles WHERE e.username = :username")
    Optional<Employee> findByUsernameWithRoles(@Param("username") String username);
}
