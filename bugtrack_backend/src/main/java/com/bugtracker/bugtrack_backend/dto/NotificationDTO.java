package com.bugtracker.bugtrack_backend.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationDTO {
    private Long id;
    private String message;
    private boolean seen;
    private LocalDateTime timestamp;
}
