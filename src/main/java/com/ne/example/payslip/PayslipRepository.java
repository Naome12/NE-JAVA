package com.ne.example.payslip;

import com.ne.example.employees.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PayslipRepository extends JpaRepository<Payslip, Long> {
    List<Payslip> findByMonthAndYear(Integer month, Integer year);
    List<Payslip> findByEmployeeAndMonthAndYear(Employee employee, Integer month, Integer year);
    boolean existsByMonthAndYear(Integer month, Integer year);
}
