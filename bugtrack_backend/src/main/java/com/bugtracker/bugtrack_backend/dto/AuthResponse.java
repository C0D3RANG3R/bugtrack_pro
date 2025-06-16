package com.bugtracker.bugtrack_backend.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import com.bugtracker.bugtrack_backend.entity.Role;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String username;
    private Role role;
}