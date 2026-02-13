package com.workilnk.auth;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendOtpEmail(String toEmail, String otp) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("WorkiLnk Password Reset OTP");
        message.setText("Your OTP is: " + otp + "\nValid for 10 minutes.");

        mailSender.send(message);
    }
}
