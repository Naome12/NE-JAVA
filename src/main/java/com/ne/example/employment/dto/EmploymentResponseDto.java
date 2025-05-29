package com.ne.example.employment.dto;


import com.ne.example.employment.EmploymentStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class EmploymentResponseDto {
    private Long code;
    private UUID employeeId;
    private String department;
    private String position;
    private Double baseSalary;
    private EmploymentStatus status;
    private LocalDate joiningDate;
}
