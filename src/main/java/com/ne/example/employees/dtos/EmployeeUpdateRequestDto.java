package com.ne.example.employees.dtos;

public record EmployeeUpdateRequestDto(
        String firstName,
        String lastName,
        String mobile,
        String email
) {}

