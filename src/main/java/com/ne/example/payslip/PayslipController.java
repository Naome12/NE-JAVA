package com.ne.example.payslip;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payslips")
public class PayslipController {

    private final PayslipService payslipService;

    public PayslipController(PayslipService payslipService) {
        this.payslipService = payslipService;
    }

    @PostMapping("/generate")
    @PreAuthorize("hasRole('MANAGER')")
    public List<Payslip> generatePayroll(@RequestParam Integer month, @RequestParam Integer year) {
        return payslipService.generatePayslips(month, year); //  updated method name
    }

    @PostMapping("/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public void approvePayroll(@RequestParam Integer month, @RequestParam Integer year) {
        payslipService.approvePayroll(month, year);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public List<Payslip> getAllPayslips(@RequestParam Integer month, @RequestParam Integer year) {
        return payslipService.getPayslipsByMonthAndYear(month, year);
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public List<Payslip> getMyPayslips(@RequestParam Integer month, @RequestParam Integer year) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return payslipService.getPayslipsByEmployeeEmailAndMonthYear(email, month, year);
    }
}
