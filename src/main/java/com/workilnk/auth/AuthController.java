package com.workilnk.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.workilnk.exception.ResourceNotFoundException;
import com.workilnk.user.User;
import com.workilnk.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid credentials"));

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getId());
        return new LoginResponse(token);
    }
}
