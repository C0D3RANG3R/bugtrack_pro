package com.bugtracker.bugtrack_backend.service;

import com.bugtracker.bugtrack_backend.dto.ProjectDTO;
import com.bugtracker.bugtrack_backend.entity.Project;

import java.util.List;

public interface ProjectService {
    Project createProject(ProjectDTO dto);
    Project updateProject(Long id, ProjectDTO dto);
    void deleteProject(Long id);
    Project getProjectById(Long id);
    List<Project> getAllProjects();
}
