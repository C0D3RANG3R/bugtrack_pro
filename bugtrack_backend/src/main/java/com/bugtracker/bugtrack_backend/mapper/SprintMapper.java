package com.bugtracker.bugtrack_backend.mapper;

import com.bugtracker.bugtrack_backend.dto.SprintResponseDTO;
import com.bugtracker.bugtrack_backend.entity.Sprint;

public class SprintMapper {
    public static SprintResponseDTO toDTO(final Sprint sprint) {
        SprintResponseDTO dto = new SprintResponseDTO();
        dto.setId(sprint.getId());
        dto.setName(sprint.getName());
        dto.setStartDate(sprint.getStartDate());
        dto.setEndDate(sprint.getEndDate());
        dto.setStatus(sprint.getStatus());
        dto.setProjectId(
            sprint.getProject() != null ? sprint.getProject().getId() : null
        );
        return dto;
    }
}
