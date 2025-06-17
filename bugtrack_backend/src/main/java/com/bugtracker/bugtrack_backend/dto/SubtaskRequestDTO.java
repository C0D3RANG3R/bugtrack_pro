package com.bugtracker.bugtrack_backend.dto;

import lombok.Data;

@Data
public class SubtaskRequestDTO {
    private String title;
    private Double estimatedTime;
    private Long issueId;
    private Long assigneeId;
}
