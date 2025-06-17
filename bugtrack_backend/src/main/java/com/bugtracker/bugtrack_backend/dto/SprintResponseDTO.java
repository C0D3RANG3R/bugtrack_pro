package com.bugtracker.bugtrack_backend.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class SprintResponseDTO {
    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private Long projectId;
}
