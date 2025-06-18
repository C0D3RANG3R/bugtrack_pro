package com.bugtracker.bugtrack_backend.mapper;

import com.bugtracker.bugtrack_backend.dto.SubtaskResponseDTO;
import com.bugtracker.bugtrack_backend.entity.Subtask;

public class SubtaskMapper {
    public static SubtaskResponseDTO toDTO(final Subtask subtask) {
        SubtaskResponseDTO dto = new SubtaskResponseDTO();
        dto.setId(subtask.getId());
        dto.setTitle(subtask.getTitle());
        dto.setStatus(subtask.getStatus());
        dto.setEstimatedTime(subtask.getEstimatedTime());
        dto.setTimeSpent(subtask.getTimeSpent());
        dto.setAssigneeName(
            subtask.getAssignee() != null ? subtask.getAssignee().getUsername() : null
        );
        dto.setIssueId(
            subtask.getIssue() != null ? subtask.getIssue().getId() : null
        );
        return dto;
    }
}
