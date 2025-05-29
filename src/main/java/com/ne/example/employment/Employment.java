package com.ne.example.employment;

import com.ne.example.employees.Employee;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "employments")
@Data
public class Employment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long code;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;


    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private String position;

    @Column(nullable = false)
    private Double baseSalary;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmploymentStatus status;

    @Column
    private LocalDate joiningDate;

}