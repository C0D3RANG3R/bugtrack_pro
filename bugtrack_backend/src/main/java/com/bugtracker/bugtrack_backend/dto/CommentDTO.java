package com.bugtracker.bugtrack_backend.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CommentDTO {
    private Long id;
    private String content;
    private String authorUsername;
    private LocalDateTime timestamp;
}
