package com.ne.example.deductions;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeductionsService {

    private final DeductionsRepository deductionsRepository;

    public DeductionsService(DeductionsRepository deductionsRepository) {
        this.deductionsRepository = deductionsRepository;
    }

    public Deductions createDeduction(Deductions deduction) {
        return deductionsRepository.save(deduction);
    }

    public List<Deductions> getAllDeductions() {
        return deductionsRepository.findAll();
    }

    public Optional<Deductions> getDeductionById(Long id) {
        return deductionsRepository.findById(id);
    }

    public Deductions updateDeduction(Long id, Deductions deductionDetails) {
        Deductions deduction = deductionsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Deduction not found"));
        deduction.setDeductionName(deductionDetails.getDeductionName());
        deduction.setPercentage(deductionDetails.getPercentage());
        return deductionsRepository.save(deduction);
    }

    public void deleteDeduction(Long id) {
        deductionsRepository.deleteById(id);
    }
}