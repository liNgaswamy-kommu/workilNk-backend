package com.workilnk.auth;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.workilnk.exception.BadRequestException;
import com.workilnk.exception.ResourceNotFoundException;
import com.workilnk.user.User;
import com.workilnk.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ForgotPasswordService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    // SEND OTP
    public void sendOtp(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email not registered"));

        String otp = generateOtp();

        user.setResetOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));

        userRepository.save(user);

        emailService.sendOtpEmail(email, otp);
    }

    // RESET PASSWORD
    public void resetPassword(String email, String otp, String newPassword) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email not registered"));

        if (user.getResetOtp() == null ||
            !user.getResetOtp().equals(otp)) {
            throw new BadRequestException("Invalid OTP");
        }

        if (user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("OTP expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetOtp(null);
        user.setOtpExpiry(null);

        userRepository.save(user);
    }

    private String generateOtp() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }
}
