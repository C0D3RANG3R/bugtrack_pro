package com.bugtracker.bugtrack_backend.dto;

import lombok.Data;

@Data
public class SubtaskResponseDTO {
    private Long id;
    private String title;
    private String status;
    private Double estimatedTime;
    private Double timeSpent;
    private String assigneeName;
    private Long issueId;
}
