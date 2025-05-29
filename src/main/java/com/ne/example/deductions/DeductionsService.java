package com.ne.example.deductions;

import com.ne.example.commons.exceptions.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Deductions getDeductionById(Long id) {
        return deductionsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Deduction with id " + id + " not found"));
    }

    public Deductions updateDeduction(Long id, Deductions deductionDetails) {
        Deductions deduction = deductionsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Deduction with id " + id + " not found"));

        deduction.setDeductionName(deductionDetails.getDeductionName());
        deduction.setPercentage(deductionDetails.getPercentage());

        return deductionsRepository.save(deduction);
    }

    public void deleteDeduction(Long id) {
        if (!deductionsRepository.existsById(id)) {
            throw new IllegalArgumentException("Cannot delete deduction with id " + id + " — not found");
        }
        deductionsRepository.deleteById(id);
    }
}
