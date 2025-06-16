package com.bugtracker.bugtrack_backend.dto;

import lombok.Data;
import com.bugtracker.bugtrack_backend.entity.Role;

@Data
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private Role role;
}