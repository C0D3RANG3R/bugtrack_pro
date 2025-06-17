package com.bugtracker.bugtrack_backend.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class SprintRequestDTO {
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private Long projectId;
}
