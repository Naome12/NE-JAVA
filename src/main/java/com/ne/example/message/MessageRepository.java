package com.ne.example.message;

import com.ne.example.employees.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByMonthAndYear(Integer month, Integer year);
    List<Message> findByEmployeeAndMonthAndYear(Employee employee, Integer month, Integer year);
    List<Message> findByEmailSentFalse();

}