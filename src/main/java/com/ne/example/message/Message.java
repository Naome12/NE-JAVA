package com.ne.example.message;

import com.ne.example.employees.Employee;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "messages")
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(nullable = false)
    private String message;

    private Integer month;
    private Integer year;

    @Column(name = "email_sent", nullable = false)
    private boolean emailSent = false;
}
