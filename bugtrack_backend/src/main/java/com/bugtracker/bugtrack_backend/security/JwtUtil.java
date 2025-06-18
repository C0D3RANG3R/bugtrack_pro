package com.bugtracker.bugtrack_backend.security;

import org.springframework.stereotype.Component;
import com.bugtracker.bugtrack_backend.entity.User;

import java.util.Date;
import java.util.List;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
    // Use a secure, random key in production!
    private static final String SECRET_KEY = "q7X9v2Lk5pZ1sW8eR4tU6yB3nM0aJcVh";
    private static final long EXPIRATION_TIME_MS = 1000 * 60 * 60 * 10; // 10 hours

    public String generateToken(User user) {
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("roles", List.of("ROLE_" + user.getRole()))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }

    public String extractEmail(String token) {
        Claims claims = parseClaims(token);
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
