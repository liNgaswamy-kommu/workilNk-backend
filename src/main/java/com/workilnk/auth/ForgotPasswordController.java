package com.workilnk.auth;

import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class ForgotPasswordController {

    private final ForgotPasswordService forgotPasswordService;

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestBody ForgotPasswordRequest request) {
        forgotPasswordService.sendOtp(request.getEmail());
        return "OTP sent to email";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody ResetPasswordRequest request) {
        forgotPasswordService.resetPassword(
                request.getEmail(),
                request.getOtp(),
                request.getNewPassword()
        );
        return "Password reset successfully";
    }
}
