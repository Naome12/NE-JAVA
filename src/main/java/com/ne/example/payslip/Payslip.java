package com.ne.example.payslip;
import com.ne.example.employees.Employee;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "payslips")
@Data
public class Payslip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    private Double houseAmount;
    private Double transportAmount;
    private Double employeeTaxedAmount;
    private Double pensionAmount;
    private Double medicalInsuranceAmount;
    private Double otherTaxedAmount;
    private Double grossSalary;
    private Double netSalary;
    private String name;

    private Integer month;
    private Integer year;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PayslipStatus status;

}
