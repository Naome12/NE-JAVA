package com.ne.example.auth.dtos;

import com.ne.example.commons.validation.ValidRwandaId;
import com.ne.example.commons.validation.ValidRwandanPhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record RegisterRequestDto(
        @NotBlank(message = "Code is required")
        String code,

        @NotBlank(message = "First name is required")
        @Size(min = 3, max = 50, message = "First name must be between 2 and 50 characters long")
        String firstName,

        @NotBlank(message = "Last name is required")
        String lastName,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid.")
        String email,

        @NotBlank(message = "Phone number is required.")
        @ValidRwandanPhoneNumber
        String mobile,

        @NotNull(message = "Dob is required")
        LocalDate dob,

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 250, message = "Password must be at least 8 characters long")
        String password
) {
}



