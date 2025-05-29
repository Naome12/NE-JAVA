package com.ne.example.auth.otp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpService {
    private final OtpRepository otpRepository;

    public String generateOtp(String email, OtpType type) {
        // Remove any existing OTP for the same email and type
        otpRepository.deleteByEmailAndType(email, type);

        String code = generate6DigitCode();
        Otp otp = Otp.builder()
                .email(email)
                .code(code)
                .type(type)
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .build();

        otpRepository.save(otp);
        log.info("✅ OTP generated and stored for {} [{}]", email, type);
        return code;
    }

    public boolean verifyOtp(String email, String code, OtpType type) {
        return otpRepository.findByEmailAndCodeAndType(email, code, type)
                .filter(otp -> otp.getExpiresAt().isAfter(LocalDateTime.now()))
                .map(otp -> {
                    otpRepository.delete(otp); // Delete after successful verification
                    return true;
                })
                .orElse(false);
    }

    private String generate6DigitCode() {
        return String.valueOf(100_000 + new Random().nextInt(900_000));
    }

    // ⏰ Clean up expired OTPs every hour
    @Scheduled(cron = "0 0 * * * *") // Runs every hour
    public void deleteExpiredOtps() {
        int count = otpRepository.deleteAllByExpiresAtBefore(LocalDateTime.now());
        log.info("🧹 Deleted {} expired OTPs", count);
    }
}
