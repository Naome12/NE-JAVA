package com.ne.example.auth.otp;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, Long> {
    Optional<Otp> findByEmailAndCodeAndType(String email, String code, OtpType type);
    void deleteByEmailAndType(String email, OtpType type);
    int deleteAllByExpiresAtBefore(LocalDateTime time);
}
