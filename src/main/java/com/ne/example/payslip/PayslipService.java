package com.ne.example.payslip;

import com.ne.example.deductions.Deductions;
import com.ne.example.deductions.DeductionsRepository;
import com.ne.example.email.EmailService;
import com.ne.example.employees.Employee;
import com.ne.example.employees.EmployeeRepository;
import com.ne.example.employees.EmployeeStatus;
import com.ne.example.employment.Employment;
import com.ne.example.employment.EmploymentRepository;
import com.ne.example.employment.EmploymentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayslipService {

    private final EmployeeRepository employeeRepository;
    private final EmploymentRepository employmentRepository;
    private final DeductionsRepository deductionsRepository;
    private final PayslipRepository payslipRepository;
    private final EmailService emailService;

    @Transactional
    public List<Payslip> generatePayslips(Integer month, Integer year) {
        LocalDate now = LocalDate.now();
        if (year > now.getYear() || (year.equals(now.getYear()) && month > now.getMonthValue())) {
            String error = "Cannot generate payslip for a future month: " + month + "/" + year;
            log.warn(error);
            throw new IllegalArgumentException(error);
        }

        if (payslipRepository.existsByMonthAndYear(month, year)) {
            String error = "Payroll already generated for " + month + "/" + year;
            log.warn(error);
            throw new IllegalStateException(error);
        }

        List<Employee> activeEmployees = employeeRepository.findByStatus(EmployeeStatus.ACTIVE);
        List<Employment> activeEmployments = employmentRepository.findByStatus(EmploymentStatus.ACTIVE);

        Map<String, Employment> employmentMap = activeEmployments.stream()
                .collect(Collectors.toMap(e -> e.getEmployee().getCode(), e -> e));

        Map<String, Double> deductionMap = deductionsRepository.findAll().stream()
                .collect(Collectors.toMap(d -> d.getDeductionName().toLowerCase(), Deductions::getPercentage));

        List<Payslip> payslips = new ArrayList<>();

        for (Employee employee : activeEmployees) {
            Employment employment = employmentMap.get(employee.getCode());
            if (employment == null || employment.getJoiningDate() == null) continue;

            LocalDate joiningDate = employment.getJoiningDate();
            if (joiningDate.getYear() > year || (joiningDate.getYear() == year && joiningDate.getMonthValue() > month)) {
                continue;
            }

            double baseSalary = employment.getBaseSalary();
            double housing = baseSalary * getDeductionValue(deductionMap, "housing") / 100;
            double transport = baseSalary * getDeductionValue(deductionMap, "transport") / 100;
            double grossSalary = baseSalary + housing + transport;

            double tax = baseSalary * getDeductionValue(deductionMap, "employeetax") / 100;
            double pension = baseSalary * getDeductionValue(deductionMap, "pension") / 100;
            double medical = baseSalary * getDeductionValue(deductionMap, "medicalinsurance") / 100;
            double others = baseSalary * getDeductionValue(deductionMap, "others") / 100;
            double netSalary = grossSalary - (tax + pension + medical + others);

            Payslip payslip = new Payslip();
            payslip.setEmployee(employee);
            payslip.setName(employee.getFirstName() + " " + employee.getLastName());
            payslip.setMonth(month);
            payslip.setYear(year);
            payslip.setStatus(PayslipStatus.PENDING);
            payslip.setHouseAmount(housing);
            payslip.setTransportAmount(transport);
            payslip.setEmployeeTaxedAmount(tax);
            payslip.setPensionAmount(pension);
            payslip.setMedicalInsuranceAmount(medical);
            payslip.setOtherTaxedAmount(others);
            payslip.setGrossSalary(grossSalary);
            payslip.setNetSalary(netSalary);

            payslips.add(payslip);
        }

        return payslipRepository.saveAll(payslips);
    }

    @Transactional
    public void approvePayroll(Integer month, Integer year) {
        List<Payslip> payslips = payslipRepository.findByMonthAndYear(month, year);
        if (payslips.isEmpty()) {
            String error = "No payroll found to approve for " + month + "/" + year;
            log.warn(error);
            throw new IllegalStateException(error);
        }

        payslips.forEach(p -> p.setStatus(PayslipStatus.PAID));
        payslipRepository.saveAll(payslips);

        for (Payslip payslip : payslips) {
            Employee employee = payslip.getEmployee();
            if (employee == null || employee.getEmail() == null) {
                log.warn("Email not sent: Employee or email not found for payslip ID {}", payslip.getId());
                continue;
            }

            String message = String.format(
                    "Dear %s, your salary for %02d/%d from RCA amounting to %.2f has been credited to your account %s successfully.",
                    employee.getFirstName(), month, year, payslip.getNetSalary(), employee.getCode());

            try {
                emailService.sendSalaryNotification(employee.getEmail(), employee.getFirstName(), message);
            } catch (Exception e) {
                log.error("Failed to send salary notification to {} for payslip ID {}",
                        employee.getEmail(), payslip.getId(), e);
            }
        }
    }

    public List<Payslip> getPayslipsByMonthAndYear(Integer month, Integer year) {
        return payslipRepository.findByMonthAndYear(month, year);
    }

    public List<Payslip> getPayslipsByEmployeeEmailAndMonthYear(String email, Integer month, Integer year) {
        return employeeRepository.findByEmail(email)
                .map(employee -> payslipRepository.findByEmployeeAndMonthAndYear(employee, month, year))
                .orElseThrow(() -> {
                    String error = "Employee not found with email: " + email;
                    log.warn(error);
                    return new NoSuchElementException(error);
                });
    }

    private double getDeductionValue(Map<String, Double> map, String key) {
        return map.getOrDefault(key.toLowerCase(), 0.0);
    }
}
