package com.ne.example.employees;


import com.ne.example.auth.dtos.RegisterRequestDto;
import com.ne.example.commons.exceptions.BadRequestException;
import com.ne.example.employees.dtos.EmployeeResponseDto;
import com.ne.example.employees.dtos.EmployeeUpdateRequestDto;
import com.ne.example.employees.mappers.EmployeeMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final PasswordEncoder passwordEncoder;

    public EmployeeResponseDto createUser(RegisterRequestDto user) {
        if(employeeRepository.existsByEmailOrMobile(user.email(), user.mobile()))
            throw new BadRequestException("Employee with this email or nationalId or  phone number already exists.");

        var newUser = employeeMapper.toEntity(user);
        newUser.setCode(user.code());
        newUser.setPassword(passwordEncoder.encode(user.password()));
        newUser.setRole(Role.ROLE_EMPLOYEE);
        newUser.setStatus(EmployeeStatus.DISABLED);
        log.info("user is here, {}", newUser);
        employeeRepository.save(newUser);
        return employeeMapper.toResponseDto(newUser);
    }

    public void changeUserPassword(String userEmail, String newPassword){
        var user = findByEmail(userEmail);
        user.setPassword(passwordEncoder.encode(newPassword));
        employeeRepository.save(user);
    }

    public void activateUserAccount(String userEmail){
        var user = findByEmail(userEmail);
        user.setStatus(EmployeeStatus.ACTIVE);
        employeeRepository.save(user);
    }


    public Employee findByEmail(String email){
        return employeeRepository.findByEmail(email).orElseThrow(() -> new BadRequestException("Employee with that email not found."));
    }

    public List<EmployeeResponseDto> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(employeeMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public EmployeeResponseDto getEmployeeById(UUID id) {
        var employee = employeeRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Employee not found."));
        return employeeMapper.toResponseDto(employee);
    }

    public EmployeeResponseDto updateEmployee(UUID id, EmployeeUpdateRequestDto updateDto) {
        var currentUser = getCurrentUserEmail();
        var employee = employeeRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Employee not found."));

        if (!currentUser.equalsIgnoreCase(employee.getEmail())) {
            throw new BadRequestException("You are not authorized to update this account.");
        }

        employeeMapper.updateEmployeeFromDto(updateDto, employee);
        return employeeMapper.toResponseDto(employeeRepository.save(employee));
    }

    public void deleteEmployee(UUID id) {
        if (!isCurrentUserAdmin()) {
            throw new BadRequestException("Only admin can delete accounts.");
        }

        var employee = employeeRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Employee not found."));
        employeeRepository.delete(employee);
    }

    // --- helper methods ---

    private String getCurrentUserEmail() {
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        } else {
            return principal.toString();
        }
    }

    private boolean isCurrentUserAdmin() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}