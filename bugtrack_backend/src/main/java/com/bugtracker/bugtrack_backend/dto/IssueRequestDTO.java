package com.bugtracker.bugtrack_backend.dto;

import lombok.Data;

@Data
public class IssueRequestDTO {
    private String title;
    private String description;
    private String type;      // e.g., "BUG", "TASK", etc.
    private String priority;  // e.g., "LOW", "MEDIUM", "HIGH"
    private Long projectId;
    private Long reporterId;
    private Long assigneeId;
}
