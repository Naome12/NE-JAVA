package com.ne.example.employment;

import com.ne.example.employment.dto.EmploymentRequestDto;
import com.ne.example.employment.dto.EmploymentResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmploymentMapper {
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "employee", ignore = true) // You will set employee manually
    Employment toEntity(EmploymentRequestDto dto);

    @Mapping(source = "employee.id", target = "employeeId")
    EmploymentResponseDto toResponse(Employment employment);
}
