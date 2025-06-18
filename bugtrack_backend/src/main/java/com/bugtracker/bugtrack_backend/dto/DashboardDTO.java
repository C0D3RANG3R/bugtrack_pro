package com.bugtracker.bugtrack_backend.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardDTO {
    private long totalUsers;
    private long totalProjects;
    private long totalIssues;
    private Map<String, Long> issuesByStatus;
    private Map<String, Long> issuesByPriority;
}
