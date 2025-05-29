package com.ne.example.email;

import com.ne.example.message.Message;
import com.ne.example.message.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SalaryEmailScheduler {

    private final MessageRepository messageRepository;
    private final EmailService emailService;

    @Scheduled(fixedRate = 10000) // every 10 seconds
    public void sendSalaryEmails() {
        List<Message> pendingMessages = messageRepository.findByEmailSentFalse();
        log.info("Found {} messages to send", pendingMessages.size());
//
//        for (Message msg : pendingMessages) {
//            var employee = msg.getEmployee();
//
//            if (employee == null || employee.getEmail() == null) {
//                log.warn("No email found for employee with message ID: {}", msg.getId());
//                continue;
//            }
//
//            try {
//                emailService.sendSimpleSalaryNotification(
//                        employee.getEmail(),
//                        employee.getFirstName(),
//                        msg.getMessage()
//                );
//                msg.setEmailSent(true);
//                messageRepository.save(msg);
//                log.info("Sent email to {}", employee.getEmail());
//            } catch (Exception e) {
//                log.error("Failed to send salary message to {}", employee.getEmail(), e);
//            }
//        }
    }
}
