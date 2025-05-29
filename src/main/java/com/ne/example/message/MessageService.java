package com.ne.example.message;

import com.ne.example.employees.Employee;
import com.ne.example.employees.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final EmployeeRepository employeeRepository;

    public MessageService(MessageRepository messageRepository, EmployeeRepository employeeRepository) {
        this.messageRepository = messageRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<Message> getAllMessages(Integer month, Integer year) {
        return messageRepository.findByMonthAndYear(month, year);
    }

    public List<Message> getMyMessages(String email, Integer month, Integer year) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        return messageRepository.findByEmployeeAndMonthAndYear(employee, month, year);
    }
}