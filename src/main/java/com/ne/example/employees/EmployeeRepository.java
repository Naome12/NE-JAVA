package com.ne.example.employees;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    boolean existsByEmailOrMobile(String email, String phoneNumber);
    Optional<Employee> findByEmail(String email);

    @Override
    Optional<Employee> findById(UUID userId);

    List<Employee> findByStatus(EmployeeStatus status);
}
