package com.bugtracker.bugtrack_backend.controller;

import com.bugtracker.bugtrack_backend.dto.AuthRequest;
import com.bugtracker.bugtrack_backend.dto.RegisterRequest;
import com.bugtracker.bugtrack_backend.dto.UserProfileDTO;
import com.bugtracker.bugtrack_backend.entity.User;
import com.bugtracker.bugtrack_backend.security.JwtUtil;
import com.bugtracker.bugtrack_backend.service.AuthService;
import com.bugtracker.bugtrack_backend.service.UserService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        try {
            authService.register(request);
            return ResponseEntity.ok("User registered successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest request, HttpServletResponse response) {
        User user = authService.validateUser(request.getEmail(), request.getPassword());
        String jwt = jwtUtil.generateToken(user);

        ResponseCookie cookie = ResponseCookie.from("token", jwt)
                .httpOnly(true)
                .secure(true) // Set to false for localhost testing if needed
                .path("/")
                .maxAge(Duration.ofHours(1))
                .sameSite("Strict")
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok("Login successful");
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDTO> getProfile() {
        UserProfileDTO profile = userService.getCurrentUserProfile();
        return ResponseEntity.ok(profile);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        ResponseCookie expiredCookie = ResponseCookie.from("token", "")
                .httpOnly(true)
                .secure(true) // Set to false for localhost testing if needed
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, expiredCookie.toString());
        return ResponseEntity.ok("Logout successful");
    }
}
