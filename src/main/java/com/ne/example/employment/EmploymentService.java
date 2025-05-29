package com.ne.example.employment;

import com.ne.example.employees.EmployeeRepository;
import com.ne.example.employment.dto.EmploymentRequestDto;
import com.ne.example.employment.dto.EmploymentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmploymentService {
    private final EmploymentRepository repository;
    private final EmployeeRepository employeeRepository;
    private final EmploymentMapper mapper;

    public EmploymentResponseDto create(EmploymentRequestDto dto) {
        var employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        var employment = mapper.toEntity(dto);
        employment.setEmployee(employee);
        repository.save(employment);
        return mapper.toResponse(employment);
    }

    public List<EmploymentResponseDto> getAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    public EmploymentResponseDto getById(Long code) {
        return repository.findById(code)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Employment not found"));
    }

    public EmploymentResponseDto update(Long code, EmploymentRequestDto dto) {
        var employment = repository.findById(code)
                .orElseThrow(() -> new RuntimeException("Employment not found"));

        var employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        employment.setEmployee(employee);
        employment.setDepartment(dto.getDepartment());
        employment.setPosition(dto.getPosition());
        employment.setBaseSalary(dto.getBaseSalary());
        employment.setJoiningDate(dto.getJoiningDate());
        employment.setStatus(dto.getStatus());

        repository.save(employment);
        return mapper.toResponse(employment);
    }

    public void delete(Long code) {
        if (!repository.existsById(code)) {
            throw new RuntimeException("Employment not found");
        }
        repository.deleteById(code);
    }
}
