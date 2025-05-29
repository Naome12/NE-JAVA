package com.ne.example.auth;

import com.ne.example.auth.dtos.*;
import com.ne.example.auth.otp.OtpService;
import com.ne.example.auth.otp.OtpType;
import com.ne.example.commons.exceptions.BadRequestException;
import com.ne.example.email.EmailService;
import com.ne.example.employees.EmployeeService;
import com.ne.example.employees.dtos.EmployeeResponseDto;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final EmployeeService employeeService;
    private final OtpService otpService;
    private final EmailService emailService;

    // 📝 Register new user and send OTP
    @PostMapping("/register")
    @RateLimiter(name = "auth-rate-limiter")
    public ResponseEntity<EmployeeResponseDto> registerUser(
            @Valid @RequestBody RegisterRequestDto user,
            UriComponentsBuilder uriBuilder) {

        var userResponse = employeeService.createUser(user);
        var uri = uriBuilder.path("/users/{id}")
                .buildAndExpand(userResponse.id())
                .toUri();

        String otpCode = otpService.generateOtp(userResponse.email(), OtpType.VERIFY_ACCOUNT);
        emailService.sendAccountVerificationEmail(userResponse.email(), userResponse.firstName(), otpCode);

        return ResponseEntity.created(uri).body(userResponse);
    }

    // Verify account with OTP
    @PatchMapping("/verify-account")
    @RateLimiter(name = "auth-rate-limiter")
    public ResponseEntity<?> verifyAccount(@Valid @RequestBody VerifyAccountDto verifyAccountRequest) {
        boolean isVerified = otpService.verifyOtp(
                verifyAccountRequest.email(),
                verifyAccountRequest.otp(),
                OtpType.VERIFY_ACCOUNT
        );

        if (!isVerified) {
            throw new BadRequestException("Invalid email or OTP");
        }

        employeeService.activateUserAccount(verifyAccountRequest.email());
        return ResponseEntity.ok("Account activated successfully.");
    }

    // 🔒 Initiate password reset with OTP
    @PostMapping("/initiate-password-reset")
    public ResponseEntity<?> initiatePasswordReset(@Valid @RequestBody InitiatePasswordResetDto initiateRequest) {
        var otpCode = otpService.generateOtp(initiateRequest.email(), OtpType.RESET_PASSWORD);

        var user = employeeService.findByEmail(initiateRequest.email());
        emailService.sendResetPasswordOtp(user.getEmail(), user.getFirstName(), otpCode);

        return ResponseEntity.ok(
                "If your email is registered, you will receive an email with instructions to reset your password."
        );
    }

    // 🔁 Reset password using OTP
    @PatchMapping("/reset-password")
    @RateLimiter(name = "auth-rate-limiter")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordDto resetPasswordRequest) {
        boolean isValid = otpService.verifyOtp(
                resetPasswordRequest.email(),
                resetPasswordRequest.otp(),
                OtpType.RESET_PASSWORD
        );

        if (!isValid) {
            throw new BadRequestException("Invalid email or OTP");
        }

        employeeService.changeUserPassword(resetPasswordRequest.email(), resetPasswordRequest.newPassword());
        return ResponseEntity.ok("Password reset successfully. You can now log in with your new password.");
    }

    // 🔓 Login
    @PostMapping("/login")
    @RateLimiter(name = "auth-rate-limiter")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequestDto loginRequestDto,
            HttpServletResponse response) {

        var loginResult = authService.login(loginRequestDto, response);
        return ResponseEntity.ok(new LoginResponse(loginResult.accessToken()));
    }
}
