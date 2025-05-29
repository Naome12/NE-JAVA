package com.ne.example.employees.mappers;


import com.ne.example.auth.dtos.RegisterRequestDto;
import com.ne.example.employees.Employee;
import com.ne.example.employees.dtos.EmployeeResponseDto;
import com.ne.example.employees.dtos.EmployeeUpdateRequestDto;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface EmployeeMapper {
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "status", ignore = true)
    Employee toEntity(RegisterRequestDto userDto);
    EmployeeResponseDto toResponseDto(Employee user);
    void updateEmployeeFromDto(EmployeeUpdateRequestDto dto, @MappingTarget Employee employee);
}
