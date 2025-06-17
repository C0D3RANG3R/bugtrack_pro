package com.bugtracker.bugtrack_backend.service;

import com.bugtracker.bugtrack_backend.dto.AuthRequest;
import com.bugtracker.bugtrack_backend.dto.AuthResponse;
import com.bugtracker.bugtrack_backend.dto.RegisterRequest;
import com.bugtracker.bugtrack_backend.entity.User;
import com.bugtracker.bugtrack_backend.repository.UserRepository;
import com.bugtracker.bugtrack_backend.security.JwtUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    // private final EmailService emailService;

    public void register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        User user = new User(null, request.getUsername(), request.getEmail(),
                passwordEncoder.encode(request.getPassword()), request.getRole());
        userRepository.save(user);

        // emailService.sendWelcomeMail(user.getEmail(), user.getUsername());
    }

    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(user);
        return new AuthResponse(token, user.getUsername(), user.getRole());
    }
}
