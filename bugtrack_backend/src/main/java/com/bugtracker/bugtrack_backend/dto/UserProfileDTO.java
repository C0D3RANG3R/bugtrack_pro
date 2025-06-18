package com.bugtracker.bugtrack_backend.dto;

import com.bugtracker.bugtrack_backend.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserProfileDTO {
    private Long id;
    private String username;
    private String email;
    private Role role;
    private List<IssueResponseDTO> assignedIssues;
    private List<SubtaskResponseDTO> assignedSubtasks;
}
