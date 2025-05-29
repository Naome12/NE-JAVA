package com.ne.example.employment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EmploymentRepository extends JpaRepository<Employment,Long> {
    List<Employment> findByStatus(EmploymentStatus status);
    boolean existsByEmployeeId(UUID employeeId);
    Employment findByEmployeeId(UUID employeeId);


}
