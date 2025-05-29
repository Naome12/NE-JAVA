package com.ne.example.employees.dtos;

import java.util.UUID;

public record EmployeeResponseDto(
        UUID id,
        String firstName,
        String lastName,
        String email
) {
}
