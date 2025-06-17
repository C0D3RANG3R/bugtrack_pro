package com.bugtracker.bugtrack_backend.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class IssueResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String type;
    private String priority;
    private String status;
    private String reporterUsername;
    private String assigneeUsername;
    private String projectName;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
