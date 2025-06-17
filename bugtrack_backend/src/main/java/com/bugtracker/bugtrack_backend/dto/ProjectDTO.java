package com.bugtracker.bugtrack_backend.dto;

import lombok.Data;

import java.util.Set;

@Data
public class ProjectDTO {
    private String name;
    private String description;
    private Set<Long> userIds;
}
