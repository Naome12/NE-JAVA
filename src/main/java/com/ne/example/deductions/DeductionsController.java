package com.ne.example.deductions;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/deductions")
@RequiredArgsConstructor
public class DeductionsController {

    private final DeductionsService deductionsService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public Deductions createDeduction(@RequestBody Deductions deduction) {
        return deductionsService.createDeduction(deduction);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public List<Deductions> getAllDeductions() {
        return deductionsService.getAllDeductions();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public Optional<Deductions> getDeductionById(@PathVariable Long id) {
        return deductionsService.getDeductionById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public Deductions updateDeduction(@PathVariable Long id, @RequestBody Deductions deductionDetails) {
        return deductionsService.updateDeduction(id, deductionDetails);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public void deleteDeduction(@PathVariable Long id) {
        deductionsService.deleteDeduction(id);
    }
}