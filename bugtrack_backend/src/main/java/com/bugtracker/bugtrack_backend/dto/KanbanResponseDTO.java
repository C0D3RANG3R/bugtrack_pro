package com.bugtracker.bugtrack_backend.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KanbanResponseDTO {
    private String status;
    private List<IssueResponseDTO> issues;
}
