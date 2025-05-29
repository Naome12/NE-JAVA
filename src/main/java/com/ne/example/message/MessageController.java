package com.ne.example.message;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public List<Message> getAllMessages(@RequestParam Integer month, @RequestParam Integer year) {
        return messageService.getAllMessages(month, year);
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public List<Message> getMyMessages(@RequestParam Integer month, @RequestParam Integer year) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return messageService.getMyMessages(email, month, year);
    }
}