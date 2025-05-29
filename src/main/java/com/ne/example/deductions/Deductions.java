package com.ne.example.deductions;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "deductions")
@Data
public class Deductions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long code;

    @Column(nullable = false, unique = true)
    private String deductionName;

    @Column(nullable = false)
    private Double percentage;
}